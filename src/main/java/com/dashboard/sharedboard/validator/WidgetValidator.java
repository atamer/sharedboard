package com.dashboard.sharedboard.validator;

import com.dashboard.sharedboard.dto.WidgetDTO;
import com.dashboard.sharedboard.errors.DashboardException;
import com.dashboard.sharedboard.util.HeaderUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class WidgetValidator implements IWidgetValidator {

    @Override
    public void validateAdd(WidgetDTO widgetDTO) {
        if (widgetDTO.getWidth() < 0)
            throw new DashboardException(HttpStatus.BAD_REQUEST, HeaderUtil.createAlert("Negative Width not allowed", String.valueOf(widgetDTO.getWidth())));

        if (widgetDTO.getHeight() < 0)
            throw new DashboardException(HttpStatus.BAD_REQUEST, HeaderUtil.createAlert("Negative Height not allowed", String.valueOf(widgetDTO.getHeight())));


    }

    @Override
    public void validateUpdate(WidgetDTO widgetDTO) {
        this.validateAdd(widgetDTO);
        if (widgetDTO.getId() == null)
            throw new DashboardException(HttpStatus.BAD_REQUEST, HeaderUtil.createAlert("Id can not be null", widgetDTO.toString()));


    }

}
