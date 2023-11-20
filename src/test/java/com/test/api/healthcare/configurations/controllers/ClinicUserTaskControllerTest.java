package com.test.api.healthcare.configurations.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.constants.UserTaskStatus;
import com.test.api.healthcare.configurations.constants.UserTaskType;
import com.test.api.healthcare.configurations.mappers.ClinicUserTaskMapper;
import com.test.api.healthcare.configurations.models.ClinicUserTaskModel;
import com.test.api.healthcare.configurations.models.entities.ClinicUserTask;
import com.test.api.healthcare.configurations.services.ClinicUserTaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class ClinicUserTaskControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";

    private static final String URI_CLINIC_USER_TASK = "/clinic-user-tasks";
    private static final String URI_CLINIC_USER_TASK_ID = URI_CLINIC_USER_TASK + "/{clinicUserTaskId}";
    private static final Long CLINIC_USER_TASK_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final Long PATIENT_ID = 1L;
    private static final Long TASK_REF_ID = 1L;


    @MockBean
    private ClinicUserTaskMapper clinicUserTaskMapper;

    @MockBean
    private ClinicUserTaskService clinicUserTaskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createClinicUserTask() throws Exception {
        final ClinicUserTaskModel clinicUserTaskModel = getMockedClinicUserTaskModel(CLINIC_USER_TASK_ID, CLINIC_ID);
        when(clinicUserTaskMapper.toClinicUserTaskModel(any())).thenReturn(clinicUserTaskModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_CLINIC_USER_TASK)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedClinicUserTaskModel(null, CLINIC_ID))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("taskType").description("Task Type. Allowed values :" + UserTaskType.getAllowedValues()),
                    fieldWithPath("patientId").description("Patient_Id"),
                    fieldWithPath("patientName").description("Patient Name"),
                    fieldWithPath("patientMrn").description("Patient Mrn"),
                    fieldWithPath("taskDueDate").description("Task Due Date"),
                    fieldWithPath("taskAssignedTo").description("Task Assigned To"),
                    fieldWithPath("taskStatus").description("Task Status. Allowed values :" + UserTaskStatus.getAllowedValues()),
                    fieldWithPath("taskNote").description("Task Note"),
                    fieldWithPath("taskRefId").description("Task Ref Id")
                )))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void updateClinicUserTask() throws Exception {
        final ClinicUserTaskModel clinicUserTaskModel = getMockedClinicUserTaskModel(CLINIC_USER_TASK_ID, CLINIC_ID);
        when(clinicUserTaskService.getClinicUserTask(CLINIC_USER_TASK_ID))
            .thenReturn(Optional.of(getMockedClinicUserTask(CLINIC_USER_TASK_ID)));
        when(clinicUserTaskMapper.toClinicUserTaskModel(any())).thenReturn(clinicUserTaskModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_CLINIC_USER_TASK_ID, CLINIC_USER_TASK_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedClinicUserTaskModel(null, CLINIC_ID))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("taskType").description("Task Type. Allowed values :" + UserTaskType.getAllowedValues()),
                    fieldWithPath("patientId").description("Patient_Id"),
                    fieldWithPath("patientName").description("Patient Name"),
                    fieldWithPath("patientMrn").description("Patient Mrn"),
                    fieldWithPath("taskDueDate").description("Task Due Date"),
                    fieldWithPath("taskAssignedTo").description("Task Assigned To"),
                    fieldWithPath("taskStatus").description("Task Status. Allowed values :" + UserTaskStatus.getAllowedValues()),
                    fieldWithPath("taskNote").description("Task Note"),
                    fieldWithPath("taskRefId").description("Task Ref Id")
                )))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getClinicUserTask() throws Exception {
        when(clinicUserTaskService.getClinicUserTask(anyLong())).thenReturn(Optional.of(getMockedClinicUserTask(CLINIC_USER_TASK_ID)));
        when(clinicUserTaskMapper.toClinicUserTask(any())).thenReturn(getMockedClinicUserTask(CLINIC_USER_TASK_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CLINIC_USER_TASK_ID, CLINIC_USER_TASK_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("clinicUserTaskId").description("Clinic User Task Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listClinicUserTasks() throws Exception {
        when(clinicUserTaskService.listClinicUserTasks(any())).thenReturn(paginatedMockedSimOrders());
        when(clinicUserTaskMapper.toClinicUserTaskModelList(any()))
            .thenReturn(List.of(getMockedClinicUserTaskModel(CLINIC_USER_TASK_ID, CLINIC_ID)));
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CLINIC_USER_TASK)
                .queryParams(getMockedPaginationRequest())
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                queryParameters(
                    parameterWithName("pageNo").description("Page no for which resources are requested. pageNo starts at: 0"),
                    parameterWithName("pageSize").description("No of elements in a page. Max pageSize limit: 100"),
                    parameterWithName("sortField").description("Field on which sorting is requested"),
                    parameterWithName("sortDirection").description("Direction of sort field. Allowed values: [ASC, DESC]")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deleteClinicUserTask() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_CLINIC_USER_TASK_ID, CLINIC_USER_TASK_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("clinicUserTaskId").description("Clinic User Task Id")
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    private RequestHeadersSnippet getRequestHeaderSnippet() {
        return requestHeaders(
            headerWithName(HEADER_CLINIC_ID_KEY).description("Clinic id")
        );
    }

    private ClinicUserTaskModel getMockedClinicUserTaskModel(final Long clinicUserTaskId, final Long clinicId) {
        final ClinicUserTaskModel clinicUserTaskModel = new ClinicUserTaskModel();
        clinicUserTaskModel.setClinicUserTaskId(clinicUserTaskId);
        clinicUserTaskModel.setTaskType(UserTaskType.NURSING_TASK.name());
        clinicUserTaskModel.setPatientId(PATIENT_ID);
        clinicUserTaskModel.setPatientName("ekZero");
        clinicUserTaskModel.setPatientMrn("abc");
        clinicUserTaskModel.setTaskDueDate(LocalDateTime.now());
        clinicUserTaskModel.setTaskAssignedTo(1L);
        clinicUserTaskModel.setTaskStatus(UserTaskStatus.COMPLETE.name());
        clinicUserTaskModel.setTaskNote("Task 1");
        clinicUserTaskModel.setTaskRefId(TASK_REF_ID);
        return clinicUserTaskModel;
    }

    private ClinicUserTask getMockedClinicUserTask(final Long clinicUserTaskId) {
        final ClinicUserTaskModel clinicUserTaskModel = getMockedClinicUserTaskModel(clinicUserTaskId, CLINIC_ID);
        final ClinicUserTask clinicUserTask = new ClinicUserTask();
        clinicUserTask.setId(clinicUserTaskModel.getClinicUserTaskId());
        return clinicUserTask;
    }

    private List<ClinicUserTask> mockedClinicUserTaskList(final Long clinicUserTaskId) {
        return List.of(getMockedClinicUserTask(clinicUserTaskId));
    }

    private Page<ClinicUserTask> paginatedMockedSimOrders() {
        return new PageImpl<>(mockedClinicUserTaskList(CLINIC_USER_TASK_ID));
    }

    private MultiValueMap<String, String> getMockedPaginationRequest() {
        final LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
        linkedMultiValueMap.add("pageNo", "0");
        linkedMultiValueMap.add("pageSize", "1");
        linkedMultiValueMap.add("sortField", "name");
        linkedMultiValueMap.add("sortDirection", "asc");
        return linkedMultiValueMap;
    }
}

