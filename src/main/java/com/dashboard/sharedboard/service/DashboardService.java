package com.dashboard.sharedboard.service;

import com.dashboard.sharedboard.domain.Widget;
import com.dashboard.sharedboard.domain.WidgetContainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DashboardService {


    private final WidgetContainer widgetContainer;
    private final IdentifierService identifierService;


    public DashboardService(WidgetContainer widgetContainer, IdentifierService identifierService) {
        this.widgetContainer = widgetContainer;
        this.identifierService = identifierService;
    }


    public Widget createWidget(Widget widget) {
        widget.setId(identifierService.createUniqueId());
        return this.widgetContainer.add(widget);
    }


    public Widget updateWidget(Widget widget) {
        return this.widgetContainer.update(widget);
    }

    public void deleteWidget(String id) {
        this.widgetContainer.delete(id);
    }

    public Optional<Widget> findWidget(String id) {
        return this.widgetContainer.findWidgetById(id);
    }

    public Page<Widget> findAllWidgets(Pageable pageable) {
        return this.widgetContainer.findAllWidgets(pageable);
    }

}
