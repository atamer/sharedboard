package com.dashboard.sharedboard.domain;

class WidgetLinkItem {
    private Widget widget;
    private WidgetLinkItem next;
    private WidgetLinkItem previous;

    WidgetLinkItem(Widget widget, WidgetLinkItem next, WidgetLinkItem previous) {
        this.widget = widget;
        this.next = next;
        this.previous = previous;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public WidgetLinkItem getNext() {
        return next;
    }

    public WidgetLinkItem getPrevious() {
        return previous;
    }

    public void setPrevious(WidgetLinkItem previous) {
        this.previous = previous;
    }

    public void setNext(WidgetLinkItem next) {
        this.next = next;
    }
}