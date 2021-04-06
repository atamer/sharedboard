package com.dashboard.sharedboard.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class WidgetLinkListTest {

    private WidgetLinkList widgetLinkList;

    @BeforeEach
    void init() {
        widgetLinkList = new WidgetLinkList();
    }

    @Test
    void test_addToTail() {
        Widget widget = Widget.builder().id("id").height(10).width(10).x(10).y(10).z(10).build();
        widgetLinkList.addToTail(widget);
        List<Widget> widgetList = widgetLinkList.list();
        assertEquals(1, widgetList.size());
        assertEquals(widget.getId(), widgetList.get(0).getId());

    }

    @Test
    void test_add() {
        Widget widget = Widget.builder().id("id").height(10).width(10).x(10).y(10).z(10).build();
        widgetLinkList.add(widget);
        List<Widget> widgetList = widgetLinkList.list();

        assertEquals(1, widgetList.size());
        assertEquals(widget.getId(), widgetList.get(0).getId());
    }

    @Test
    void test_shiftZIndex() {
        Widget widget1 = Widget.builder().id("id1").height(10).width(10).x(10).y(10).z(10).build();
        Widget widget2 = Widget.builder().id("id2").height(10).width(10).x(10).y(10).z(10).build();
        widgetLinkList.add(widget1);
        widgetLinkList.add(widget2);

        List<Widget> widgetList = widgetLinkList.list();

        assertEquals(2, widgetList.size());
        assertEquals(widget2.getId(), widgetList.get(0).getId());
        assertEquals(widget1.getId(), widgetList.get(1).getId());
    }

    @Test
    void test_update() {
        Widget widget1 = Widget.builder().id("id1").height(10).width(10).x(10).y(10).z(9).build();
        Widget widget2 = Widget.builder().id("id2").height(10).width(10).x(10).y(10).z(10).build();
        widgetLinkList.add(widget1);
        WidgetLinkItem widgetLinkItem = widgetLinkList.add(widget2);

        // update z index
        widgetLinkItem.getWidget().setZ(8);
        widgetLinkList.update(widgetLinkItem);
        List<Widget> widgetList = widgetLinkList.list();

        assertEquals(2, widgetList.size());
        assertEquals(widget2.getId(), widgetList.get(0).getId());
        assertEquals(widget1.getId(), widgetList.get(1).getId());
    }

    @Test
    void test_delete() {
        Widget widget = Widget.builder().id("id").height(10).width(10).x(10).y(10).z(9).build();
        WidgetLinkItem widgetLinkItem = widgetLinkList.add(widget);
        widgetLinkList.delete(widgetLinkItem);

        List<Widget> widgetList = widgetLinkList.list();
        assertEquals(0, widgetList.size());

    }

}
