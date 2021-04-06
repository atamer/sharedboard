package com.dashboard.sharedboard.domain;

import com.dashboard.sharedboard.errors.DashboardException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class WidgetContainerTest {

    private WidgetContainer widgetContainer;

    @BeforeEach
    void init() {
        widgetContainer = new WidgetContainer();
    }

    @Test
    void test_addWithoutZIndex_checkOrder() {
        Widget widgetWithoutZIndex1 = Widget.builder().id("id1").height(10).width(10).x(10).y(10).build();
        Widget widgetWithoutZIndex2 = Widget.builder().id("id2").height(10).width(10).x(10).y(10).build();
        widgetContainer.add(widgetWithoutZIndex1);
        widgetContainer.add(widgetWithoutZIndex2);

        Page<Widget> widgetList = widgetContainer.findAllWidgets(PageRequest.of(0, 10));
        assertEquals(2, widgetList.getContent().size());
        assertEquals(widgetWithoutZIndex1.getId(), widgetList.getContent().get(0).getId());
        assertEquals(widgetWithoutZIndex2.getId(), widgetList.getContent().get(1).getId());
        Assertions.assertNotNull(widgetList.getContent().get(0).getZ());
        Assertions.assertNotNull(widgetList.getContent().get(1).getZ());

    }

    @Test
    void test_addWithZIndex_checkOrder() {
        Widget widgetWithZIndex1 = Widget.builder().id("id1").height(10).width(10).x(10).y(10).z(11).build();
        Widget widgetWithZIndex2 = Widget.builder().id("id2").height(10).width(10).x(10).y(10).z(9).build();

        widgetContainer.add(widgetWithZIndex1);
        widgetContainer.add(widgetWithZIndex2);

        Page<Widget> widgetList = widgetContainer.findAllWidgets(PageRequest.of(0, 10));
        assertEquals(2, widgetList.getContent().size());
        assertEquals(widgetWithZIndex2.getId(), widgetList.getContent().get(0).getId());
        assertEquals(widgetWithZIndex1.getId(), widgetList.getContent().get(1).getId());

    }

    @Test
    void test_update_itemNotFound() {
        Widget widget = Widget.builder().id("id").height(10).width(10).x(10).y(10).z(11).build();
        assertThrows(DashboardException.class, () -> widgetContainer.update(widget));
    }

    @Test
    void test_update_changeZOrderUpdate_checkNewOrder() {

        Widget widget1 = Widget.builder().id("id").height(10).width(10).x(10).y(10).z(11).build();
        Widget widget2 = Widget.builder().id("id1").height(10).width(10).x(10).y(10).z(12).build();
        Widget widget3 = Widget.builder().id("id2").height(10).width(10).x(10).y(10).z(13).build();

        widgetContainer.add(widget1);
        widgetContainer.add(widget2);
        widgetContainer.add(widget3);

        widget3.setZ(11);
        widgetContainer.update(widget3);

        Page<Widget> widgetPage = widgetContainer.findAllWidgets(PageRequest.of(0, 10));
        assertEquals("id2", widgetPage.getContent().get(0).getId());
        assertEquals(11, widgetPage.getContent().get(0).getZ());

        assertEquals("id", widgetPage.getContent().get(1).getId());
        assertEquals(12, widgetPage.getContent().get(1).getZ());

        assertEquals("id1", widgetPage.getContent().get(2).getId());
        assertEquals(13, widgetPage.getContent().get(2).getZ());
    }

    @Test
    void test_delete_itemNotFound() {
        Widget widget = Widget.builder().id("id").height(10).width(10).x(10).y(10).z(11).build();
        widgetContainer.add(widget);

        widgetContainer.delete(widget.getId());
        assertThrows(DashboardException.class, () -> widgetContainer.delete(widget.getId()));
    }

    @Test
    void test_findWidgetById_itemNotFound() {
        Widget widget = Widget.builder().id("id").height(10).width(10).x(10).y(10).z(11).build();
        widgetContainer.add(widget);
        widgetContainer.delete(widget.getId());
        Assertions.assertTrue(widgetContainer.findWidgetById(widget.getId()).isEmpty());
    }

    @Test
    void test_findAllWidgets() {
        for (int i = 0; i < 100; i++) {
            widgetContainer.add(Widget.builder().id("id_" + i).height(10).width(10).x(10).y(10).z(11).build());
        }
        widgetContainer.add(Widget.builder().id("id").height(10).width(10).x(10).y(10).z(11).build());

        for (int i = 0; i < 10; i++) {
            Page<Widget> page = widgetContainer.findAllWidgets(PageRequest.of(i, 10));
            assertEquals(10, page.getContent().size());
        }
        assertEquals(1, widgetContainer.findAllWidgets(PageRequest.of(10, 10)).getContent().size());
    }
}
