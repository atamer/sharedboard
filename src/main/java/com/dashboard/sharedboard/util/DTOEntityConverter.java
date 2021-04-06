package com.dashboard.sharedboard.util;

import com.dashboard.sharedboard.domain.Widget;
import com.dashboard.sharedboard.dto.WidgetDTO;
import org.springframework.stereotype.Component;

@Component
public class DTOEntityConverter {

    public WidgetDTO convertWidgetToDTO(Widget widget) {
        return WidgetDTO.builder().id(widget.getId()).x(widget.getX()).y(widget.getY()).z(widget.getZ()).height(widget.getHeight()).width(widget.getWidth()).build();
    }

    public Widget convertWidgetToEntity(WidgetDTO widgetDTO) {
        return Widget.builder().id(widgetDTO.getId()).x(widgetDTO.getX()).y(widgetDTO.getY()).z(widgetDTO.getZ()).height(widgetDTO.getHeight()).width(widgetDTO.getWidth()).build();
    }

}
