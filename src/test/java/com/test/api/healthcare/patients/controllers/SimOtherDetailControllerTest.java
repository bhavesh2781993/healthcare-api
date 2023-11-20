package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.mappers.SimOtherDetailMapper;
import com.test.api.healthcare.patients.models.SimOtherDetailModel;
import com.test.api.healthcare.patients.models.entities.SimOtherDetail;
import com.test.api.healthcare.patients.services.SimOtherDetailService;

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
class SimOtherDetailControllerTest extends BaseDocumentationTest {

    private static final String URI_SIM_OTHER_DETAIL = "/sim-other-details";
    private static final String URI_SIM_OTHER_DETAIL_ID = URI_SIM_OTHER_DETAIL + "/{simOtherDetailId}";
    private static final String URI_SIM_ORDER_SIM_OTHER_DETAIL_ID = "/sim-orders/{simOrderId}" + URI_SIM_OTHER_DETAIL;

    private static final Long SIM_OTHER_DETAIL_ID = 1L;
    private static final Long SIM_ORDER_ID = 1L;

    @MockBean
    private SimOtherDetailMapper simOtherDetailMapper;

    @MockBean
    private SimOtherDetailService simOtherDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSimOtherDetail() throws Exception {
        final SimOtherDetailModel simOtherDetailModel = getMockedSimOtherDetailModel(SIM_OTHER_DETAIL_ID);

        when(simOtherDetailMapper.toSimOtherDetailModel(any())).thenReturn(simOtherDetailModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
            .put(URI_SIM_ORDER_SIM_OTHER_DETAIL_ID, SIM_ORDER_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(getMockedSimOtherDetailModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("simOrderId").description("Sim Order Id")
                ),
                relaxedRequestFields(
                fieldWithPath("otherDetail").description("Other Detail")
            )))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getSimOtherDetail() throws Exception {
        when(simOtherDetailMapper.toSimOtherDetail(any())).thenReturn(getMockedSimOtherDetail(SIM_ORDER_ID));
        when(simOtherDetailService.getSimOtherDetail(any())).thenReturn(Optional.of(getMockedSimOtherDetail(SIM_ORDER_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_SIM_OTHER_DETAIL_ID, SIM_OTHER_DETAIL_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("simOtherDetailId").description("Sim Other Detail Id")
                )))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deleteSimOtherDetail() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_SIM_OTHER_DETAIL_ID, SIM_OTHER_DETAIL_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("simOtherDetailId").description("Sim Other Detail Id")
                )))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private SimOtherDetailModel getMockedSimOtherDetailModel(final Long simOtherDetailId) {
        final SimOtherDetailModel simOtherDetailModel = new SimOtherDetailModel();
        simOtherDetailModel.setSimOtherDetailId(simOtherDetailId);
        simOtherDetailModel.setSimOrderId(SIM_ORDER_ID);
        simOtherDetailModel.setOtherDetail("Other Details");
        return simOtherDetailModel;
    }

    private SimOtherDetail getMockedSimOtherDetail(final Long simOtherDetailId) {
        final SimOtherDetailModel simOtherDetailModel = getMockedSimOtherDetailModel(simOtherDetailId);
        final SimOtherDetail simOtherDetail = new SimOtherDetail();
        simOtherDetail.setId(simOtherDetailModel.getSimOtherDetailId());
        return simOtherDetail;
    }

}
