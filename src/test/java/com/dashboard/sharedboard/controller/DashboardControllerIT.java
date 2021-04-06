package com.dashboard.sharedboard.controller;

import com.dashboard.sharedboard.dto.WidgetDTO;
import com.dashboard.sharedboard.service.DashboardService;
import com.dashboard.sharedboard.util.DTOEntityConverter;
import com.dashboard.sharedboard.util.TestUtil;
import com.dashboard.sharedboard.validator.IWidgetValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class DashboardControllerIT {

    @Autowired
    private DashboardController dashboardController;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private IWidgetValidator widgetValidator;

    @Autowired
    private DTOEntityConverter dtoEntityConverter;

    private MockMvc dashboardMockService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.dashboardMockService = MockMvcBuilders.standaloneSetup(new DashboardController(dashboardService, widgetValidator, dtoEntityConverter))
                .build();
    }

    @Test
    void createWidget() throws Exception {
        ResultActions resultActions = createWidgetCommon();
        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.height", is(10)))
                .andExpect(jsonPath("$.width", is(10)))
                .andExpect(jsonPath("$.x", is(1)))
                .andExpect(jsonPath("$.y", is(1)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()));
    }

    private ResultActions createWidgetCommon() throws Exception {
        WidgetDTO widgetDTO = WidgetDTO.builder().height(10).width(10).x(1).y(1).z(1).build();
        return dashboardMockService.perform(post("/api/widgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(widgetDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.height", is(10)))
                .andExpect(jsonPath("$.width", is(10)))
                .andExpect(jsonPath("$.x", is(1)))
                .andExpect(jsonPath("$.y", is(1)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()));

    }

    @Test
    void updateWidget() throws Exception {
        ResultActions resultActions = createWidgetCommon();
        WidgetDTO widgetDTO = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), WidgetDTO.class);

        widgetDTO.setHeight(20);

        dashboardMockService.perform(put("/api/widgets/" + widgetDTO.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(widgetDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.height", is(20)));
    }

    @Test
    void deleteWidget() throws Exception {

        ResultActions resultActions = createWidgetCommon();
        WidgetDTO widgetDTO = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), WidgetDTO.class);

        dashboardMockService.perform(delete("/api/widgets/" + widgetDTO.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    void getWidgetById() throws Exception {
        ResultActions resultActions = createWidgetCommon();
        WidgetDTO widgetDTO = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), WidgetDTO.class);

        dashboardMockService.perform(get("/api/widgets/" + widgetDTO.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(widgetDTO.getId())));

    }

    @Test
    void getAllWidgets() throws Exception {
        ResultActions resultActions = createWidgetCommon();
        WidgetDTO widgetDTO = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), WidgetDTO.class);

        MvcResult result = dashboardMockService.perform(get("/api/widgets")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8)).andReturn();

        List<WidgetDTO> widgetDTOList = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), WidgetDTO[].class));
        Assertions.assertEquals(1, widgetDTOList.size());
        Assertions.assertEquals(widgetDTO.getId(), widgetDTOList.get(0).getId());

    }


}
