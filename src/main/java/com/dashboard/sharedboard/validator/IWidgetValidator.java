package com.dashboard.sharedboard.validator;

import com.dashboard.sharedboard.dto.WidgetDTO;

public interface IWidgetValidator {
    void validateAdd(WidgetDTO widgetDTO);

    void validateUpdate(WidgetDTO widgetDTO);
}
