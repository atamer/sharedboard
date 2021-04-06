package com.dashboard.sharedboard.domain;


import java.util.ArrayList;
import java.util.List;

class WidgetLinkList {

    private final WidgetLinkItem head;
    private WidgetLinkItem tail;

    public WidgetLinkList() {
        this.head = new WidgetLinkItem(null, null, null);
        this.tail = null;
    }

    protected WidgetLinkItem addToTail(Widget widget) {
        WidgetLinkItem widgetLinkItem = new WidgetLinkItem(widget, null, null);

        int nextZIndex = 1;
        if (this.tail == null) {
            this.tail = widgetLinkItem;
            this.head.setNext(widgetLinkItem);
        } else {
            nextZIndex = this.tail.getWidget().getZ() + 1;
            widgetLinkItem.setPrevious(this.tail);
            this.tail.setNext(widgetLinkItem);
            this.tail = widgetLinkItem;
        }
        widget.setZ(nextZIndex);
        return widgetLinkItem;
    }

    protected WidgetLinkItem add(Widget widget) {
        WidgetLinkItem iterator = head;
        while (iterator.getNext() != null) {
            if (iterator.getNext().getWidget().getZ() >= widget.getZ()) {
                break;
            }
            iterator = iterator.getNext();
        }
        WidgetLinkItem widgetLinkItem = new WidgetLinkItem(widget, iterator.getNext(), iterator);
        iterator.setNext(widgetLinkItem);
        shiftZIndex(widgetLinkItem);
        return widgetLinkItem;
    }

    private void shiftZIndex(WidgetLinkItem iterator) {
        int lastZIndex = iterator.getWidget().getZ();
        while ((iterator = iterator.getNext()) != null) {
            if (iterator.getWidget().getZ() != lastZIndex) {
                break;
            } else {
                lastZIndex = iterator.getWidget().getZ() + 1;
                iterator.getWidget().setZ(lastZIndex);
            }
        }
    }

    public void update(WidgetLinkItem widgetLinkItem) {
        this.delete(widgetLinkItem);
        this.add(widgetLinkItem.getWidget());
    }

    public void delete(WidgetLinkItem widgetLinkItem) {
        widgetLinkItem.getPrevious().setNext(widgetLinkItem.getNext());
        if (widgetLinkItem.getPrevious() == this.head) {
            this.tail = null;
        } else if (widgetLinkItem.getNext() == null) {
            this.tail = widgetLinkItem.getPrevious();
        }
    }

    public List<Widget> list() {
        List<Widget> widgetList = new ArrayList<>();
        WidgetLinkItem iterator = head;
        while ((iterator = iterator.getNext()) != null) {
            widgetList.add(new Widget(iterator.getWidget()));
        }
        return widgetList;
    }
}
