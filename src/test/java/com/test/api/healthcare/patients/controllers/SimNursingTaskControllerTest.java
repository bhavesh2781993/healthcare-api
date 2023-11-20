package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.constants.NursingTask;
import com.test.api.healthcare.patients.constants.SimNursingTaskStatus;
import com.test.api.healthcare.patients.mappers.SimNursingTaskMapper;
import com.test.api.healthcare.patients.models.SimNursingTaskModel;
import com.test.api.healthcare.patients.models.entities.SimNursingTask;
import com.test.api.healthcare.patients.services.SimNursingTaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class SimNursingTaskControllerTest extends BaseDocumentationTest {

    private static final String URL_SIM_NURSING_TASK = "/sim-nursing-tasks";
    private static final String URL_SIM_NURSING_TASK_ID = URL_SIM_NURSING_TASK + "/{simNursingTaskId}";

    private static final Long SIM_NURSING_TASK_ID = 1L;
    private static final Long SIM_ORDER_ID = 1L;

    @MockBean
    private SimNursingTaskMapper simNursingTaskMapper;

    @MockBean
    private SimNursingTaskService simNursingTaskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSimNursingTask() throws Exception {
        when(simNursingTaskMapper.toSimNursingTaskModel(any())).thenReturn(getMockedSimNursingTaskModel(SIM_NURSING_TASK_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URL_SIM_NURSING_TASK)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedSimNursingTaskModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("simOrderId").description("Sim Order Id"),
                    fieldWithPath("nursingTask").description("Sim Nursing Task. Allowed values :"
                        + NursingTask.getAllowedValues()),
                    fieldWithPath("nursingTaskDetail").description("Test 1"),
                    fieldWithPath("nursingTaskStatus").description("Sim Nursing Task. Allowed values :"
                        + SimNursingTaskStatus.getAllowedValues())
                )))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listSimNursingTasks() throws Exception {
        when(simNursingTaskMapper.toSimNursingTaskModelList(anyList()))
            .thenReturn(List.of(getMockedSimNursingTaskModel(SIM_NURSING_TASK_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URL_SIM_NURSING_TASK, URL_SIM_NURSING_TASK_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deleteSimNursingTask() throws Exception {
        when(simNursingTaskMapper.toSimNursingTaskModel(any())).thenReturn(getMockedSimNursingTaskModel(SIM_NURSING_TASK_ID));
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URL_SIM_NURSING_TASK_ID, SIM_NURSING_TASK_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void updateSimNursingTask() throws Exception {
        when(simNursingTaskService.getSimNursingTask(SIM_NURSING_TASK_ID))
            .thenReturn(Optional.of(getMockedSimNursingTask(SIM_NURSING_TASK_ID)));
        when(simNursingTaskMapper.toSimNursingTaskModel(any())).thenReturn(getMockedSimNursingTaskModel(SIM_NURSING_TASK_ID));
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URL_SIM_NURSING_TASK_ID, SIM_NURSING_TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedSimNursingTaskModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("simOrderId").description("Sim Order Id"),
                    fieldWithPath("nursingTask").description("Sim Nursing Task. Allowed values :"
                        + NursingTask.getAllowedValues()),
                    fieldWithPath("nursingTaskDetail").description("Test 1"),
                    fieldWithPath("nursingTaskStatus").description("Sim Nursing Task. Allowed values :"
                        + SimNursingTaskStatus.getAllowedValues())
                )))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private SimNursingTaskModel getMockedSimNursingTaskModel(final Long simNursingTaskId) {
        final SimNursingTaskModel simNursingTaskModel = new SimNursingTaskModel();
        simNursingTaskModel.setSimNursingTaskId(simNursingTaskId);
        simNursingTaskModel.setSimOrderId(SIM_ORDER_ID);
        simNursingTaskModel.setNursingTaskStatus(SimNursingTaskStatus.COMPLETED.name());
        simNursingTaskModel.setNursingTask(NursingTask.CREATININE.name());
        simNursingTaskModel.setNursingTaskDetail("Test 1");
        return simNursingTaskModel;
    }

    private SimNursingTask getMockedSimNursingTask(final Long simNursingTaskId) {
        final SimNursingTaskModel simNursingTaskModel = getMockedSimNursingTaskModel(simNursingTaskId);
        final SimNursingTask simNursingTask = new SimNursingTask();
        simNursingTask.setId(simNursingTaskModel.getSimNursingTaskId());
        return simNursingTask;
    }
}

