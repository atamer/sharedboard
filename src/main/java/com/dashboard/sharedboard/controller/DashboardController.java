package com.dashboard.sharedboard.controller;

import com.dashboard.sharedboard.domain.Widget;
import com.dashboard.sharedboard.dto.WidgetDTO;
import com.dashboard.sharedboard.service.DashboardService;
import com.dashboard.sharedboard.util.DTOEntityConverter;
import com.dashboard.sharedboard.util.HeaderUtil;
import com.dashboard.sharedboard.util.PaginationUtil;
import com.dashboard.sharedboard.util.ResponseUtil;
import com.dashboard.sharedboard.validator.IWidgetValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/widgets")
public class DashboardController {

    private static final String ENTITY_WIDGET = "Widget";
    private final DashboardService dashboardService;
    private final IWidgetValidator widgetValidator;
    private final DTOEntityConverter dtoEntityConverter;

    public DashboardController(DashboardService dashboardService, IWidgetValidator widgetValidator, DTOEntityConverter dtoEntityConverter) {
        this.dashboardService = dashboardService;
        this.widgetValidator = widgetValidator;
        this.dtoEntityConverter = dtoEntityConverter;
    }

    @PostMapping
    public ResponseEntity<WidgetDTO> createWidget(@RequestBody WidgetDTO widgetDTO) throws URISyntaxException {
        widgetValidator.validateAdd(widgetDTO);
        WidgetDTO resultWidgetDTO = this.dtoEntityConverter.convertWidgetToDTO(dashboardService.createWidget(this.dtoEntityConverter.convertWidgetToEntity(widgetDTO)));
        return ResponseEntity.created(new URI("/api/widgets" + resultWidgetDTO.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_WIDGET, resultWidgetDTO.getId()))
                .body(resultWidgetDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WidgetDTO> updateWidget(@RequestBody WidgetDTO widgetDTO, @PathVariable(value = "id") String id) {
        widgetValidator.validateUpdate(widgetDTO);
        widgetDTO.setId(id);
        WidgetDTO resultWidgetDTO = this.dtoEntityConverter.convertWidgetToDTO(dashboardService.updateWidget(this.dtoEntityConverter.convertWidgetToEntity(widgetDTO)));
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_WIDGET, resultWidgetDTO.getId()))
                .body(resultWidgetDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WidgetDTO> deleteWidget(@PathVariable(value = "id") String id) {
        dashboardService.deleteWidget(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_WIDGET, id)).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<WidgetDTO> getWidget(@PathVariable(value = "id") String id) {
        Optional<Widget> widget = dashboardService.findWidget(id);
        return ResponseUtil.wrapOrNotFound(widget.map(dtoEntityConverter::convertWidgetToDTO));
    }

    @GetMapping
    public ResponseEntity<List<WidgetDTO>> getAllWidgets(@RequestParam("page") int page, @RequestParam("size") int pageSize) {
        Page<WidgetDTO> widgetPage = dashboardService.findAllWidgets(PageRequest.of(page, pageSize)).map(dtoEntityConverter::convertWidgetToDTO);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), widgetPage);
        return ResponseEntity.ok().headers(headers).body(widgetPage.getContent());
    }
}
