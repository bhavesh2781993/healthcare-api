package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.mappers.SimFusionMapper;
import com.test.api.healthcare.patients.models.SimFusionModel;
import com.test.api.healthcare.patients.models.entities.SimFusion;
import com.test.api.healthcare.patients.services.SimFusionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class SimFusionControllerTest extends BaseDocumentationTest {

    private static final String URI_SIM_FUSION = "/sim-fusions";
    private static final String URI_SIM_FUSION_ID = URI_SIM_FUSION + "/{simFusionId}";
    private static final String URI_SIM_ORDER_SIM_FUSION_ID = "/sim-orders/{simOrderId}" + URI_SIM_FUSION;

    private static final Long SIM_FUSION_ID = 1L;
    private static final Long SIM_ORDER_ID = 1L;
    private static final Long SIM_SCAN_ID = 1L;
    private static final LocalDate SIM_SCAN_DATE = LocalDate.of(2022, 01, 07);

    @MockBean
    private SimFusionMapper simFusionMapper;
    @MockBean
    private SimFusionService simFusionService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSimFusion() throws Exception {
        when(simFusionMapper.toSimFusionModel(any())).thenReturn(getMockedSimFusionModel(SIM_FUSION_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_SIM_FUSION)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedSimFusionModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("simOrderId").description("Sim Order Id"),
                    fieldWithPath("scan").description("Scan"),
                    fieldWithPath("scanDate").description("scan Date"),
                    fieldWithPath("imageSeq").description("image Seq")
                )))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listSimFusions() throws Exception {
        when(simFusionMapper.toSimFusionModelList(any())).thenReturn(List.of(getMockedSimFusionModel(SIM_FUSION_ID)));
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_SIM_ORDER_SIM_FUSION_ID, SIM_ORDER_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deleteSimFusion() throws Exception {
        when(simFusionMapper.toSimFusionModel(any())).thenReturn(getMockedSimFusionModel(SIM_FUSION_ID));
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_SIM_FUSION_ID, SIM_FUSION_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(parameterWithName("simFusionId").description("sim Fusion Id"))
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void updateSimFusion() throws Exception {
        when(simFusionService.getSimFusion(SIM_FUSION_ID)).thenReturn(Optional.of(getMockedSimFusion(SIM_FUSION_ID)));
        when(simFusionMapper.toSimFusionModel(any())).thenReturn(getMockedSimFusionModel(SIM_FUSION_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_SIM_FUSION_ID, SIM_FUSION_ID)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedSimFusionModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("simOrderId").description("Sim Order Id"),
                    fieldWithPath("scan").description("Scan"),
                    fieldWithPath("scanDate").description("scan Date"),
                    fieldWithPath("imageSeq").description("image Seq")
                )))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void bulkCreateOrUpdateSimFusion() throws Exception {
        final List<SimFusionModel> simFusionModels = List.of(getMockedSimFusionModel(SIM_FUSION_ID));
        final List<SimFusion> simFusionList = List.of(getMockedSimFusion(SIM_FUSION_ID));

        when(simFusionMapper.toSimFusionModelList(any())).thenReturn(simFusionModels);
        when(simFusionService.bulkCreateOrUpdateSimFusion(any())).thenReturn(simFusionList);

        final SimFusionModel mockedSimFusionModel = getMockedSimFusionModel(null);
        mockedSimFusionModel.setSimOrderId(null);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_SIM_ORDER_SIM_FUSION_ID, SIM_ORDER_ID)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(mockedSimFusionModel))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(parameterWithName("simOrderId").description("sim Order Id")),
                relaxedRequestFields(
                    fieldWithPath("[]").description("Sim Fusion Array"),
                    fieldWithPath("[].simFusionId").description("Sim Fusion ID").type(Long.class).optional(),
                    fieldWithPath("[].scan").description("Scan"),
                    fieldWithPath("[].scanDate").description("Scan Date"),
                    fieldWithPath("[].imageSeq").description("Image Seq")
                ))
            )
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private SimFusionModel getMockedSimFusionModel(final Long simFusionId) {
        final SimFusionModel simFusionModel = new SimFusionModel();
        simFusionModel.setSimFusionId(simFusionId);
        simFusionModel.setSimOrderId(SIM_ORDER_ID);
        simFusionModel.setScan(SIM_SCAN_ID);
        simFusionModel.setScanDate(SIM_SCAN_DATE);
        simFusionModel.setImageSeq("T1");
        return simFusionModel;
    }

    private SimFusion getMockedSimFusion(final Long simFusionId) {
        final SimFusionModel simFusionModel = getMockedSimFusionModel(simFusionId);
        final SimFusion simFusion = new SimFusion();
        simFusion.setId(simFusionModel.getSimFusionId());
        return simFusion;
    }
}
