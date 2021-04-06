package com.dashboard.sharedboard.domain;


import com.dashboard.sharedboard.errors.DashboardException;
import com.dashboard.sharedboard.util.HeaderUtil;
import com.dashboard.sharedboard.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class WidgetContainer {

    final Map<String, WidgetLinkItem> widgetMap;
    final WidgetLinkList widgetLinkList;

    final ReentrantReadWriteLock.ReadLock readLock;
    final ReentrantReadWriteLock.WriteLock writeLock;


    public WidgetContainer() {
        this.widgetLinkList = new WidgetLinkList();
        this.widgetMap = new HashMap<>();

        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }


    public Widget add(Widget widget) {
        // copy widget
        Widget newWidget = new Widget(widget);
        // lock write + read
        this.writeLock.lock();
        try {
            WidgetLinkItem widgetLinkItem;
            if (widget.getZ() == null) {
                widgetLinkItem = this.widgetLinkList.addToTail(newWidget);
            } else {
                widgetLinkItem = this.widgetLinkList.add(newWidget);
            }
            this.widgetMap.put(newWidget.getId(), widgetLinkItem);
        } finally {
            // un-lock write + read
            this.writeLock.unlock();
        }
        return new Widget(newWidget);
    }

    public Widget update(Widget widget) {
        // lock write + read
        this.writeLock.lock();
        Widget newWidget;
        try {
            if (!this.widgetMap.containsKey(widget.getId()))
                throw new DashboardException(HttpStatus.NOT_FOUND, HeaderUtil.createAlert("Widget Id Not Found ", widget.getId()));
            // copy widget
            newWidget = new Widget(widget);
            WidgetLinkItem widgetLinkItem = this.widgetMap.get(newWidget.getId());
            Widget originalWidget = widgetLinkItem.getWidget();

            widgetLinkItem.setWidget(newWidget);

            if (!originalWidget.getZ().equals(newWidget.getZ())) {
                // need to update linked list
                this.widgetLinkList.update(widgetLinkItem);
            }
        } finally {
            // un-lock write + read
            this.writeLock.unlock();
        }
        return new Widget(newWidget);
    }


    public void delete(String id) {
        // lock write + read
        this.writeLock.lock();
        try {
            if (!this.widgetMap.containsKey(id))
                throw new DashboardException(HttpStatus.NOT_FOUND, HeaderUtil.createAlert("Widget Id Not Found ", id));
            this.widgetLinkList.delete(this.widgetMap.remove(id));
        } finally {
            // un-lock write + read
            this.writeLock.unlock();
        }
    }

    public Optional<Widget> findWidgetById(String id) {
        // lock read
        Optional<Widget> result ;
        this.readLock.lock();
        try {
            if (widgetMap.containsKey(id)) {
                result = Optional.of(widgetMap.get(id).getWidget());
            } else {
                result = Optional.empty();
            }
        } finally {
            this.readLock.unlock();
        }
        return result;
    }

    public Page<Widget> findAllWidgets(Pageable pageable) {
        Page<Widget> page ;
        // lock read
        this.readLock.lock();
        try {
            page = PaginationUtil.createPageFromList(new ArrayList<>(this.widgetLinkList.list()), pageable);
        } finally {
            // un-lock read
            this.readLock.unlock();
        }
        return page;
    }
}
