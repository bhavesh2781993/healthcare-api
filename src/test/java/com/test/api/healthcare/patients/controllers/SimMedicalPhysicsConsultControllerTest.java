package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.models.entities.MedicalPhysicsConsult;
import com.test.api.healthcare.patients.mappers.SimMedicalPhysicsConsultMapper;
import com.test.api.healthcare.patients.models.SimMedicalPhysicsConsultModel;
import com.test.api.healthcare.patients.models.entities.SimMedicalPhysicsConsult;
import com.test.api.healthcare.patients.models.entities.SimOrder;
import com.test.api.healthcare.patients.services.SimMedicalPhysicsConsultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class SimMedicalPhysicsConsultControllerTest extends BaseDocumentationTest {

    private static final String URI_SIM_ORDER_MEDICAL_PHYSICS_CONSULT = "/sim-orders/{simOrderId}/sim-medical-physics-consults";
    private static final String URI_SIM_MEDICAL_PHYSICS_CONSULT = "/sim-medical-physics-consults/{simMedicalPhysicsConsultId}";
    private static final Long SIM_ORDER_ID = 1L;
    private static final Long MEDICAL_PHYSICS_CONSULT_ID = 1L;

    @MockBean
    private SimMedicalPhysicsConsultMapper simMedicalPhysicsConsultMapper;

    @MockBean
    private SimMedicalPhysicsConsultService simMedicalPhysicsConsultService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrUpdateSimMedicalPhysicsConsult() throws Exception {
        when(simMedicalPhysicsConsultMapper.toSimMedicalPhysicsConsultModel(any()))
            .thenReturn(getMockedSimMedicalPhysicsConsultModel(SIM_ORDER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
            .put(URI_SIM_ORDER_MEDICAL_PHYSICS_CONSULT, SIM_ORDER_ID)
            .content(objectMapper.writeValueAsString(getMockedSimMedicalPhysicsConsultModel(null)))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("medicalPhysicsConsultId").description("Medical physics consult Id"),
                    fieldWithPath("additionalInstruction").description("Additional instructions"))))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getSimMedicalPhysicsConsult() throws Exception {
        when(simMedicalPhysicsConsultMapper.toSimMedicalPhysicsConsultModel(any()))
            .thenReturn(getMockedSimMedicalPhysicsConsultModel(SIM_ORDER_ID));
        when(simMedicalPhysicsConsultService.getSimMedicalPhysicsConsult(anyLong()))
            .thenReturn(Optional.of(getMockedSimMedicalPhysicsConsult(SIM_ORDER_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_SIM_MEDICAL_PHYSICS_CONSULT, SIM_ORDER_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("simMedicalPhysicsConsultId")
                        .description("SimMedical Physics Consult Id, this is same as Sim Order id"))))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deleteSimMedicalPhysicsConsult() throws Exception {
        when(simMedicalPhysicsConsultMapper.toSimMedicalPhysicsConsultModel(any()))
            .thenReturn(getMockedSimMedicalPhysicsConsultModel(SIM_ORDER_ID));
        when(simMedicalPhysicsConsultService.getSimMedicalPhysicsConsult(anyLong()))
            .thenReturn(Optional.of(getMockedSimMedicalPhysicsConsult(SIM_ORDER_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_SIM_MEDICAL_PHYSICS_CONSULT, SIM_ORDER_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private SimMedicalPhysicsConsultModel getMockedSimMedicalPhysicsConsultModel(final Long simMedicalPhysicsConsultId) {
        final SimMedicalPhysicsConsultModel simMedicalPhysicsConsultModel = new SimMedicalPhysicsConsultModel();
        simMedicalPhysicsConsultModel.setSimMedicalPhysicsConsultId(simMedicalPhysicsConsultId);
        simMedicalPhysicsConsultModel.setSimOrderId(simMedicalPhysicsConsultId);
        simMedicalPhysicsConsultModel.setMedicalPhysicsConsultId(MEDICAL_PHYSICS_CONSULT_ID);
        simMedicalPhysicsConsultModel.setAdditionalInstruction("Test 1");
        return simMedicalPhysicsConsultModel;
    }

    private SimMedicalPhysicsConsult getMockedSimMedicalPhysicsConsult(final Long simMedicalPhysicsConsultId) {
        final SimMedicalPhysicsConsult simMedicalPhysicsConsult = new SimMedicalPhysicsConsult();
        simMedicalPhysicsConsult.setId(simMedicalPhysicsConsultId);
        simMedicalPhysicsConsult.setSimOrder(getMockedSimOrder());
        simMedicalPhysicsConsult.setMedicalPhysicsConsult(getMockedMedicalPhysicsConsult());
        simMedicalPhysicsConsult.setAdditionalInstruction("Test 1");
        return simMedicalPhysicsConsult;
    }

    private SimOrder getMockedSimOrder() {
        final SimOrder simOrder = new SimOrder();
        simOrder.setId(1L);
        return simOrder;
    }

    private MedicalPhysicsConsult getMockedMedicalPhysicsConsult() {
        final MedicalPhysicsConsult medicalPhysicsConsult = new MedicalPhysicsConsult();
        medicalPhysicsConsult.setId(1L);
        return medicalPhysicsConsult;
    }
}
