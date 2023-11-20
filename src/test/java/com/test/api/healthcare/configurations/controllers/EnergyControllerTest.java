package com.test.api.healthcare.configurations.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.mappers.EnergyMapper;
import com.test.api.healthcare.configurations.models.EnergyModel;
import com.test.api.healthcare.configurations.services.EnergyService;

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
class EnergyControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_ENERGY = "/configurations/energies";
    private static final String URI_ENERGY_ID = URI_ENERGY + "/{energyId}";
    private static final Long ENERGY_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @MockBean
    private EnergyMapper energyMapper;

    @MockBean
    private EnergyService energyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createEnergy() throws Exception {
        final EnergyModel energyModel = getMockedEnergyModel(ENERGY_ID, CLINIC_ID);

        when(energyMapper.toEnergyModel(any())).thenReturn(energyModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_ENERGY)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedEnergyModel(null, null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                requestFields(
                    fieldWithPath("energy").description("Energy 1")
                )
            ))
            .andReturn();

        Assertions.assertNotNull(mvcResult);

    }

    @Test
    void listEnergy() throws Exception {
        final List<EnergyModel> energyModels = List.of(getMockedEnergyModel(ENERGY_ID, CLINIC_ID));
        when(energyMapper.toEnergyModelList(any())).thenReturn(energyModels);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_ENERGY)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet()
            ))
            .andReturn();

        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deleteEnergy() throws Exception {

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_ENERGY_ID, ENERGY_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                pathParameters(
                    parameterWithName("energyId").description("Energy Id")
                )
            ))
            .andReturn();

        Assertions.assertNotNull(mvcResult);
    }

    private RequestHeadersSnippet getRequestHeaderSnippet() {
        return requestHeaders(
            headerWithName(HEADER_CLINIC_ID_KEY).description("Clinic Id")
        );
    }

    private EnergyModel getMockedEnergyModel(final Long energyId, final Long clinicId) {
        final EnergyModel energyModel = new EnergyModel();
        energyModel.setEnergyId(energyId);
        energyModel.setClinicId(clinicId);
        energyModel.setEnergy("e1");
        return energyModel;
    }
}
