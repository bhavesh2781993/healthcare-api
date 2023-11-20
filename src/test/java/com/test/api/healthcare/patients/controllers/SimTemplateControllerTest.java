package com.test.api.healthcare.patients.controllers;

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
import com.test.api.healthcare.patients.constants.StructureMeasure;
import com.test.api.healthcare.patients.mappers.SimTemplateMapper;
import com.test.api.healthcare.patients.models.CtScanInstruction;
import com.test.api.healthcare.patients.models.SimTemplateDeviceModel;
import com.test.api.healthcare.patients.models.SimTemplateInstructionModel;
import com.test.api.healthcare.patients.models.SimTemplateModel;
import com.test.api.healthcare.patients.models.SimTemplateTreatmentGoalModel;
import com.test.api.healthcare.patients.models.SimTemplateTreatmentInfoModel;
import com.test.api.healthcare.patients.models.entities.SimTemplate;
import com.test.api.healthcare.patients.services.SimTemplateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class SimTemplateControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_SIM_TEMPLATE = "/sim-templates";
    private static final String URI_SIM_TEMPLATE_ID = URI_SIM_TEMPLATE + "/{simTemplateId}";

    private static final Long SIM_TEMPLATE_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @MockBean
    private SimTemplateMapper simTemplateMapper;

    @MockBean
    private SimTemplateService simTemplateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSimTemplate() throws Exception {
        final SimTemplateModel simTemplateModel = getMockedSimTemplateModel(SIM_TEMPLATE_ID);

        when(simTemplateMapper.toSimTemplateModel(any())).thenReturn(simTemplateModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_SIM_TEMPLATE)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedSimTemplateModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet(),
                relaxedRequestFields(
                    fieldWithPath("clinicId").description("Clinic Id"),
                    fieldWithPath("templateName").description("Template Name"),
                    fieldWithPath("patientPosition").description("Patient Position"),
                    fieldWithPath("armPosition").description("Arm Position"),
                    fieldWithPath("legPosition").description("Leg Position"),
                    fieldWithPath("isocenterLocation").description("ISO Center Location"),
                    fieldWithPath("ctScanInstructions").description("Ct Scan Instructions"),
                    fieldWithPath("simTemplateTreatmentInfo").description("Sim Template Treatment Info"),
                    fieldWithPath("simTemplateTreatmentGoal").description("Sim Template Treatment Goal"),
                    fieldWithPath("simTemplateDevices").description("Sim Template Devices"),
                    fieldWithPath("simTemplateInstructions").description("Sim Template Instructions")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listSimTemplates() throws Exception {
        final List<SimTemplateModel> simTemplateModels = List.of(getMockedSimTemplateModel(SIM_TEMPLATE_ID));
        when(simTemplateMapper.toSimTemplateModelList(any())).thenReturn(simTemplateModels);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_SIM_TEMPLATE)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet()
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deleteSimTemplate() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_SIM_TEMPLATE_ID, SIM_TEMPLATE_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet(),
                pathParameters(
                    parameterWithName("simTemplateId").description("sim Template Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void updateSimTemplate() throws Exception {
        final SimTemplateModel simTemplateModel = getMockedSimTemplateModel(SIM_TEMPLATE_ID);

        when(simTemplateMapper.toSimTemplateModel(any())).thenReturn(simTemplateModel);
        when(simTemplateService.updateSimTemplate(any())).thenReturn(getMockedSimTemplate(SIM_TEMPLATE_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_SIM_TEMPLATE_ID, SIM_TEMPLATE_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedSimTemplateModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet(),
                pathParameters(
                    parameterWithName("simTemplateId").description("sim Template Id")
                ),
                relaxedRequestFields(
                    fieldWithPath("clinicId").description("Clinic Id"),
                    fieldWithPath("templateName").description("Template Name"),
                    fieldWithPath("patientPosition").description("Patient Position"),
                    fieldWithPath("armPosition").description("Arm Position"),
                    fieldWithPath("legPosition").description("Leg Position"),
                    fieldWithPath("isocenterLocation").description("ISO Center Location"),
                    fieldWithPath("ctScanInstructions").description("Ct Scan Instructions"),
                    fieldWithPath("simTemplateTreatmentInfo").description("Sim Template Treatment Info"),
                    fieldWithPath("simTemplateTreatmentGoal").description("Sim Template Treatment Goal"),
                    fieldWithPath("simTemplateDevices").description("Sim Template Devices"),
                    fieldWithPath("simTemplateInstructions").description("Sim Template Instructions")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private RequestHeadersSnippet getRequestHeadersSnippet() {
        return requestHeaders(
            headerWithName(HEADER_CLINIC_ID_KEY).description("Clinic Id")
        );
    }

    private SimTemplate getMockedSimTemplate(final Long simTemplateId) {
        final SimTemplateModel simTemplateModel = getMockedSimTemplateModel(simTemplateId);
        final SimTemplate simTemplate = new SimTemplate();
        simTemplate.setId(simTemplateModel.getSimTemplateId());
        return simTemplate;
    }

    private SimTemplateModel getMockedSimTemplateModel(final Long simTemplateId) {
        final SimTemplateModel simTemplateModel = new SimTemplateModel();
        simTemplateModel.setSimTemplateId(simTemplateId);
        simTemplateModel.setClinicId(CLINIC_ID);
        simTemplateModel.setTemplateName("Template 1");
        simTemplateModel.setPatientPosition(1L);
        simTemplateModel.setArmPosition(1L);
        simTemplateModel.setLegPosition(1L);
        simTemplateModel.setIsocenterLocation("Vadodara");
        simTemplateModel.setCtScanInstructions(getMockedCtScanInstruction());
        simTemplateModel.setSimTemplateTreatmentInfo(getMockedSimTemplateTreatmentInfoModel());
        simTemplateModel.setSimTemplateTreatmentGoal(getMockedSimTemplateTreatmentGoalModel());
        simTemplateModel.setSimTemplateDevices(List.of(getMockedSimTemplateDeviceModel()));
        simTemplateModel.setSimTemplateInstructions(List.of(getMockedSimTemplateInstructionModel()));
        return simTemplateModel;
    }

    private CtScanInstruction getMockedCtScanInstruction() {
        final CtScanInstruction ctScanInstruction = new CtScanInstruction();
        ctScanInstruction.setFrom("head");
        ctScanInstruction.setTo("leg");
        return ctScanInstruction;
    }

    private SimTemplateTreatmentInfoModel getMockedSimTemplateTreatmentInfoModel() {
        final SimTemplateTreatmentInfoModel simTemplateTreatmentInfoModel = new SimTemplateTreatmentInfoModel();
        simTemplateTreatmentInfoModel.setSimTemplateTreatmentInfoId(1L);
        simTemplateTreatmentInfoModel.setSimTemplateId(1L);
        simTemplateTreatmentInfoModel.setTreatmentSiteId(1L);
        simTemplateTreatmentInfoModel.setTreatmentIntentId(1L);
        simTemplateTreatmentInfoModel.setTreatmentModalityId(1L);
        simTemplateTreatmentInfoModel.setImrtMedicalNecessityId(1L);
        simTemplateTreatmentInfoModel.setTreatmentLocationId(1L);
        simTemplateTreatmentInfoModel.setTreatmentMachineId(1L);
        return simTemplateTreatmentInfoModel;
    }

    private SimTemplateTreatmentGoalModel getMockedSimTemplateTreatmentGoalModel() {
        final SimTemplateTreatmentGoalModel simTemplateTreatmentGoalModel = new SimTemplateTreatmentGoalModel();
        simTemplateTreatmentGoalModel.setSimTemplateTreatmentGoalId(1L);
        simTemplateTreatmentGoalModel.setSimTemplateId(1L);
        simTemplateTreatmentGoalModel.setStructure("Structure");
        simTemplateTreatmentGoalModel.setStructureMeasure(StructureMeasure.MAX_LESS_THAN.name());
        simTemplateTreatmentGoalModel.setPrimaryGoal("Primary Goal");
        simTemplateTreatmentGoalModel.setPrimaryGoalUnit("Primary Goal Unit");
        simTemplateTreatmentGoalModel.setPrimaryGoalComparison("Primary Goal Comparison");
        simTemplateTreatmentGoalModel.setVariationAcceptable("Variation Acceptable");
        simTemplateTreatmentGoalModel.setPriority(1);
        return simTemplateTreatmentGoalModel;
    }

    private SimTemplateDeviceModel getMockedSimTemplateDeviceModel() {
        final SimTemplateDeviceModel simTemplateDeviceModel = new SimTemplateDeviceModel();
        simTemplateDeviceModel.setSimTemplateDeviceId(1L);
        simTemplateDeviceModel.setSimTemplateId(1L);
        simTemplateDeviceModel.setImmobilizationDevicesId(1L);
        return simTemplateDeviceModel;
    }

    private SimTemplateInstructionModel getMockedSimTemplateInstructionModel() {
        final SimTemplateInstructionModel simTemplateInstructionModel = new SimTemplateInstructionModel();
        simTemplateInstructionModel.setSimTemplateInstructionId(1L);
        simTemplateInstructionModel.setSimTemplateId(1L);
        simTemplateInstructionModel.setSimTemplateInstructionId(1L);
        return simTemplateInstructionModel;
    }
}
