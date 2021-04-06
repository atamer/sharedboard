package com.dashboard.sharedboard.domain;

import com.dashboard.sharedboard.errors.DashboardException;
import com.dashboard.sharedboard.service.DashboardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.dashboard.sharedboard.util.TestUtil.randomInt;
import static com.dashboard.sharedboard.util.TestUtil.randomStr;

@SpringBootTest
public class WidgetContainerConcurrencyTest {

    private final static Logger logger = Logger.getLogger(WidgetContainerConcurrencyTest.class.getName());

    private final int NUMBER_OF_REQUEST = 10000;
    private final int NUMBER_OF_WRITE_REQUEST = NUMBER_OF_REQUEST / 10;
    private final int NUMBER_OF_READ_REQUEST = NUMBER_OF_REQUEST - NUMBER_OF_WRITE_REQUEST;
    // write ratio
    private final float updateRatio = 0.4f;
    private final float createRatio = 0.4f;
    private final float deleteRatio = 0.2f;

    private final int zRange = 10;
    private ExecutorService executor;
    @Autowired
    private DashboardService dashboardService;

    @BeforeEach
    void init() {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Test
    void test_multipleReadWriteConcurrent_checkZIndexOrdered() throws InterruptedException {
        List<Callable<Object>> requests = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_READ_REQUEST; i++) {
            requests.add(new ReadRequest());
        }

        for (int i = 0; i < NUMBER_OF_WRITE_REQUEST * createRatio; i++) {
            requests.add(new CreateRequest());
        }

        for (int i = 0; i < NUMBER_OF_WRITE_REQUEST * updateRatio; i++) {
            requests.add(new UpdateRequest((int) (NUMBER_OF_REQUEST * createRatio)));
        }

        for (int i = 0; i < NUMBER_OF_WRITE_REQUEST * deleteRatio; i++) {
            requests.add(new DeleteRequest((int) (NUMBER_OF_REQUEST * createRatio)));
        }

        Collections.shuffle(requests);
        executor.invokeAll(requests);
        long oneMinute = 1000L * 60;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(oneMinute, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }


    class UpdateRequest implements Callable<Object> {

        private final int widgetNumber;

        UpdateRequest(int widgetNumber) {
            this.widgetNumber = widgetNumber;
        }

        @Override
        public Object call() {
            Widget updateWidget = Widget.builder().id(randomStr(this.widgetNumber))
                    .height(10).width(10).x(1).y(1).z(randomInt(zRange)).build();
            try {
                dashboardService.updateWidget(updateWidget);
                logger.info("updated id " + updateWidget.getId() + " zIndex " + updateWidget.getZ());
            } catch (DashboardException exception) {
                //logger.info("update id " + updateWidget.getId() + " not found");
            }
            return updateWidget;
        }
    }

    class CreateRequest implements Callable<Object> {

        @Override
        public Object call() {
            Widget.WidgetBuilder newWidget = Widget.builder().height(10).width(10).x(1).y(1);

            StringBuilder builder = new StringBuilder();
            if (randomInt(2) == 1) {
                int zIndex = randomInt(zRange);
                newWidget.z(zIndex);
                builder.append("with ").append(zIndex).append(" zIndex");
            } else {
                builder.append("without zIndex");
            }

            Widget e = dashboardService.createWidget(newWidget.build());
            logger.info("created " + e.getId() + " " + builder.toString());
            return e;
        }
    }

    class DeleteRequest implements Callable<Object> {
        private final int widgetNumber;

        DeleteRequest(int widgetNumber) {
            this.widgetNumber = widgetNumber;
        }

        @Override
        public Object call() {
            String widgetId = randomStr(this.widgetNumber);
            try {
                dashboardService.deleteWidget(widgetId);
                logger.info("deleted id " + widgetId);
            } catch (DashboardException exception) {
                // ignore
            }
            return widgetId;
        }
    }

    class ReadRequest implements Callable<Object> {
        @Override
        public Object call() {
            Page<Widget> page = dashboardService.findAllWidgets(PageRequest.of(0, NUMBER_OF_WRITE_REQUEST));
            StringBuilder builder = new StringBuilder();
            if (!page.getContent().isEmpty()) {
                int lastZIndex = page.getContent().get(0).getZ();
                for (Widget widget : page.getContent()) {
                    if (widget.getZ() < lastZIndex) {
                        Assertions.fail("Widget is in wrong place " + widget);
                    } else {
                        lastZIndex = widget.getZ();
                    }
                    builder.append(widget.getId()).append("-").append(widget.getZ()).append("|");
                }
            }
            logger.info(builder.toString());
            return null;
        }
    }


}
