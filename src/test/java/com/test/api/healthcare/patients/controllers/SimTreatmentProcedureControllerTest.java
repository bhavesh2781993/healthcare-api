package com.test.api.healthcare.patients.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.models.entities.TreatmentProcedure;
import com.test.api.healthcare.patients.mappers.SimTreatmentProcedureMapper;
import com.test.api.healthcare.patients.models.SimTreatmentProcedureModel;
import com.test.api.healthcare.patients.models.entities.SimOrder;
import com.test.api.healthcare.patients.models.entities.SimTreatmentProcedure;
import com.test.api.healthcare.patients.services.SimTreatmentProcedureService;

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
import org.mockito.ArgumentMatchers;

@SpringBootTest
class SimTreatmentProcedureControllerTest extends BaseDocumentationTest {

    private static final String URI_SIM_ORDER_TREATMENT_PROCEDURE = "/sim-orders/{simOrderId}/sim-treatment-procedures";
    private static final String URI_SIM_TREATMENT_PROCEDURE = "/sim-treatment-procedures/{simTreatmentProcedureId}";
    private static final Long SIM_ORDER_ID = 1L;
    private static final Long TREATMENT_PROCEDURE_ID = 1L;

    @MockBean
    private SimTreatmentProcedureService simTreatmentProcedureService;
    @MockBean
    private SimTreatmentProcedureMapper simTreatmentProcedureMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrUpdateSimTreatmentProcedure() throws Exception {
        when(simTreatmentProcedureMapper.toSimTreatmentProcedureModel(ArgumentMatchers.any()))
            .thenReturn(getMockedSimTreatmentProcedureModel(SIM_ORDER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_SIM_ORDER_TREATMENT_PROCEDURE, SIM_ORDER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedSimTreatmentProcedureModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("simOrderId").description("Sim Order Id, for which treatment procedure is requested")
                ),
                relaxedRequestFields(
                    fieldWithPath("treatmentProcedureId").description("Treatment procedure Id"),
                    fieldWithPath("additionalInstruction").description("Additional instructions")
                )
            ))
            .andReturn();

        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getSimTreatmentProcedure() throws Exception {
        when(simTreatmentProcedureService.getSimTreatmentProcedure(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.of(getMockedSimTreatmentProcedure(SIM_ORDER_ID)));
        when(simTreatmentProcedureMapper.toSimTreatmentProcedureModel(ArgumentMatchers.any()))
            .thenReturn(getMockedSimTreatmentProcedureModel(SIM_ORDER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_SIM_TREATMENT_PROCEDURE, SIM_ORDER_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("simTreatmentProcedureId").description("Sim treatment procedure id, this is same as Sim Order id")
                )
            ))
            .andReturn();

        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deleteSimTreatmentProcedure() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_SIM_TREATMENT_PROCEDURE, SIM_ORDER_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("simTreatmentProcedureId").description("Sim treatment procedure id, this is same as Sim Order id")
                )
            ))
            .andReturn();

        Assertions.assertNotNull(mvcResult);
    }

    private SimTreatmentProcedureModel getMockedSimTreatmentProcedureModel(final Long simTreatmentProcedureId) {
        final SimTreatmentProcedureModel simTreatmentProcedureModel = new SimTreatmentProcedureModel();
        simTreatmentProcedureModel.setSimTreatmentProcedureId(simTreatmentProcedureId);
        simTreatmentProcedureModel.setSimOrderId(simTreatmentProcedureId);
        simTreatmentProcedureModel.setTreatmentProcedureId(TREATMENT_PROCEDURE_ID);
        simTreatmentProcedureModel.setAdditionalInstruction("Test Instruction");
        return simTreatmentProcedureModel;
    }

    private SimTreatmentProcedure getMockedSimTreatmentProcedure(final Long simTreatmentProcedureId) {
        final SimTreatmentProcedure simTreatmentProcedureModel = new SimTreatmentProcedure();
        final SimOrder simOrder = new SimOrder();
        simOrder.setId(simTreatmentProcedureId);
        simTreatmentProcedureModel.setSimOrder(simOrder);

        final TreatmentProcedure treatmentProcedure = new TreatmentProcedure();
        treatmentProcedure.setId(TREATMENT_PROCEDURE_ID);
        simTreatmentProcedureModel.setTreatmentProcedure(treatmentProcedure);

        simTreatmentProcedureModel.setAdditionalInstruction("Test Instruction");
        return simTreatmentProcedureModel;
    }

}
