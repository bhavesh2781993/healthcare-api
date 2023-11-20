package com.test.api.healthcare.configurations.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.mappers.InstructionMapper;
import com.test.api.healthcare.configurations.models.InstructionModel;
import com.test.api.healthcare.configurations.services.InstructionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class InstructionControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_INSTRUCTION = "/configurations/instructions";
    private static final String URI_INSTRUCTION_ID = URI_INSTRUCTION + "/{instructionId}";
    private static final Long INSTRUCTION_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @MockBean
    private InstructionMapper instructionMapper;

    @MockBean
    private InstructionService instructionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createInstruction() throws Exception {
        when(instructionMapper.toInstructionModel(any()))
            .thenReturn(getMockedInstruction(INSTRUCTION_ID, CLINIC_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_INSTRUCTION)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedInstruction(null, null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet(),
                relaxedRequestFields(
                    fieldWithPath("instruction").description("Instruction")
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void listInstructions() throws Exception {
        when(instructionMapper
            .toInstructionModelList(any()))
            .thenReturn(List.of(getMockedInstruction(INSTRUCTION_ID, CLINIC_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_INSTRUCTION)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet()
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void deleteInstruction() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
            .delete(URI_INSTRUCTION_ID, INSTRUCTION_ID)
            .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet(),
                pathParameters(parameterWithName("instructionId").description("Instruction Id"))
                ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    private RequestHeadersSnippet getRequestHeadersSnippet() {
        return requestHeaders(
            headerWithName(HEADER_CLINIC_ID_KEY).description("Clinic Id")
        );
    }

    private InstructionModel getMockedInstruction(final Long instructionId, final Long clinicId) {
        final InstructionModel instructionModel = new InstructionModel();
        instructionModel.setInstructionsId(instructionId);
        instructionModel.setClinicId(clinicId);
        instructionModel.setInstruction("Instruction 1");
        return instructionModel;
    }
}
