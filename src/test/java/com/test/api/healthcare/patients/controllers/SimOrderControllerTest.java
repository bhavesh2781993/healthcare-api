package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.constants.SimOrderStatus;
import com.test.api.healthcare.patients.mappers.SimOrderMapper;
import com.test.api.healthcare.patients.models.CtScanInstruction;
import com.test.api.healthcare.patients.models.SimOrderDeviceModel;
import com.test.api.healthcare.patients.models.SimOrderInstructionModel;
import com.test.api.healthcare.patients.models.SimOrderModel;
import com.test.api.healthcare.patients.models.entities.SimOrder;
import com.test.api.healthcare.patients.services.SimOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


@SpringBootTest
class SimOrderControllerTest extends BaseDocumentationTest {

    private static final String URI_SIM_ORDER = "/sim-orders";
    private static final String URI_SIM_ORDER_ID = URI_SIM_ORDER + "/{simOrderId}";
    private static final Long SIM_ORDER_ID = 1L;
    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long PATIENT_POSITION_ID = 1L;
    private static final Long ARM_POSITION_ID = 1L;
    private static final Long LEG_POSITION_ID = 1L;
    private static final Long IMMOBILIZATION_DEVICE_ID = 1L;
    private static final Long SIM_ORDER_DEVICE_ID = 1L;
    private static final Long SIM_ORDER_INSTRUCTION_ID = 1L;
    private static final Long INSTRUCTION_LOOKUP_ID = 1L;

    @MockBean
    private SimOrderMapper simOrderMapper;

    @MockBean
    private SimOrderService simOrderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrUpdateSimOrder() throws Exception {
        when(simOrderMapper.toSimOrderModel(any())).thenReturn(getMockedSimOrderModel(SIM_ORDER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_SIM_ORDER)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedSimOrderModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("isocenterLocation").description("In isocenterLocation"),
                    fieldWithPath("simOrderStatus").description("Sim Order. Allowed values :" + SimOrderStatus.getAllowedValues()),
                    fieldWithPath("ctScanInstruction").description("head"),
                    fieldWithPath("patientPositionId").description("patient Position Id"),
                    fieldWithPath("armPositionId").description("arm Position Id"),
                    fieldWithPath("legPositionId").description("leg Position Id"),
                    fieldWithPath("immobilizationDevices").description("immobilization Devices Id"),
                    fieldWithPath("additionalInstructions").description("additional Instructions Id")
                )))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getSimOrder() throws Exception {
        when(simOrderService.getSimOrder(anyLong())).thenReturn(Optional.of(getMockedSimOrder()));
        when(simOrderMapper.toSimOrderModel(any())).thenReturn(getMockedSimOrderModel(SIM_ORDER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_SIM_ORDER_ID, SIM_ORDER_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listSimOrders() throws Exception {

        when(simOrderService.listSimOrders(any())).thenReturn(paginatedMockedSimOrders());

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_SIM_ORDER)
                .queryParams(getMockedPaginationRequest())
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
    void deleteSimOrder() throws Exception {
        when(simOrderService.getSimOrder(anyLong())).thenReturn(Optional.of(getMockedSimOrder()));
        when(simOrderMapper.toSimOrderModel(any())).thenReturn(getMockedSimOrderModel(SIM_ORDER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_SIM_ORDER_ID, SIM_ORDER_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private SimOrder getMockedSimOrder() {
        final SimOrderModel simOrderModel = getMockedSimOrderModel(SIM_ORDER_ID);
        final SimOrder simOrder = new SimOrder();
        simOrder.setId(simOrderModel.getSimOrderId());
        return simOrder;
    }

    private List<SimOrder> getMockedSimOrderList() {
        return List.of(getMockedSimOrder());
    }

    private Page<SimOrder> paginatedMockedSimOrders() {
        return new PageImpl<>(getMockedSimOrderList());
    }

    private SimOrderModel getMockedSimOrderModel(final Long simOrderId) {
        final SimOrderModel simOrderModel = new SimOrderModel();
        final CtScanInstruction ctScanInstruction = new CtScanInstruction();
        ctScanInstruction.setFrom("head");
        ctScanInstruction.setTo("head");
        simOrderModel.setSimOrderId(simOrderId);
        simOrderModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        simOrderModel.setIsocenterLocation("surat");
        simOrderModel.setCtScanInstruction(ctScanInstruction);
        simOrderModel.setSimOrderStatus(SimOrderStatus.COMPLETED.name());
        simOrderModel.setPatientPositionId(PATIENT_POSITION_ID);
        simOrderModel.setArmPositionId(ARM_POSITION_ID);
        simOrderModel.setLegPositionId(LEG_POSITION_ID);
        simOrderModel.setImmobilizationDevices(getMockedSimOrderDeviceModel());
        simOrderModel.setAdditionalInstructions(getMockedSimOrderInstructionModel());
        return simOrderModel;
    }

    private MultiValueMap<String, String> getMockedPaginationRequest() {
        final LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
        linkedMultiValueMap.add("pageNo", "0");
        linkedMultiValueMap.add("pageSize", "1");
        linkedMultiValueMap.add("sortField", "simOrderStatus");
        linkedMultiValueMap.add("sortDirection", "asc");
        return linkedMultiValueMap;
    }

    private List<SimOrderDeviceModel> getMockedSimOrderDeviceModel() {
        final SimOrderDeviceModel simOrderDeviceModel = new SimOrderDeviceModel();
        simOrderDeviceModel.setSimOrderDeviceId(SIM_ORDER_DEVICE_ID);
        simOrderDeviceModel.setSimOrder(SIM_ORDER_ID);
        simOrderDeviceModel.setImmobilizationDeviceId(IMMOBILIZATION_DEVICE_ID);

        final List<SimOrderDeviceModel> simOrderDeviceModels = new ArrayList<>();
        simOrderDeviceModels.add(simOrderDeviceModel);
        return simOrderDeviceModels;
    }

    private List<SimOrderInstructionModel> getMockedSimOrderInstructionModel() {
        final SimOrderInstructionModel simOrderInstructionModel = new SimOrderInstructionModel();
        simOrderInstructionModel.setSimOrderInstructionId(SIM_ORDER_INSTRUCTION_ID);
        simOrderInstructionModel.setSimOrder(SIM_ORDER_ID);
        simOrderInstructionModel.setInstructionLookupId(INSTRUCTION_LOOKUP_ID);

        final List<SimOrderInstructionModel> simOrderInstructionModels = new ArrayList<>();
        simOrderInstructionModels.add(simOrderInstructionModel);
        return simOrderInstructionModels;
    }
}

