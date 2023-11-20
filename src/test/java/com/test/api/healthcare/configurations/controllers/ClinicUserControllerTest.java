package com.test.api.healthcare.configurations.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.constants.UserDesignation;
import com.test.api.healthcare.configurations.mappers.ClinicUserMapper;
import com.test.api.healthcare.configurations.models.ClinicUserImageModel;
import com.test.api.healthcare.configurations.models.ClinicUserModel;
import com.test.api.healthcare.configurations.models.entities.ClinicUser;
import com.test.api.healthcare.configurations.services.ClinicUserService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
@SpringBootTest
class ClinicUserControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_USERS = "/users";
    private static final String URI_CLINIC_USERS_ID = URI_USERS + "/{clinicUserId}";
    private static final Long CLINIC_USER_ID = 1L;
    private static final String CLINIC_USER_NAME = "Test Clinic User";
    private static final Long CLINIC_DEPARTMENT_ID = 1L;

    final MockMultipartFile image = new MockMultipartFile(
        "image",
        "image.jpg",
        MULTIPART_FORM_DATA_VALUE,
        new byte[1]
    );

    @MockBean
    private ClinicUserMapper clinicUserMapper;
    @MockBean
    private ClinicUserService clinicUserService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createClinicUser() throws Exception {
        final ClinicUserModel mockedClinicUserModel = getMockedClinicUserModel(CLINIC_USER_ID);

        final byte[] requestBytes = objectMapper.writeValueAsString(mockedClinicUserModel).getBytes(Charset.defaultCharset());
        final MockPart clinicUser = new MockPart("clinicUser", requestBytes);

        clinicUser.getHeaders().setContentType(APPLICATION_JSON);

        when(clinicUserMapper.toClinicUserModel(any())).thenReturn(mockedClinicUserModel);
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .multipart(URI_USERS)
                .part(clinicUser)
                .file(image)
                .content(objectMapper.writeValueAsString(getMockedClinicUserModel(null))))
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("name").description("User name"),
                    fieldWithPath("designation").description("User Designation"),
                    fieldWithPath("clinicDepartmentId").description("Clinic department Id"),
                    fieldWithPath("phone").description("User Phone"),
                    fieldWithPath("alternatePhone").description("User Alternate Phone"),
                    fieldWithPath("email").description("User Email"),
                    fieldWithPath("alternateEmail").description("User Alternate Email"),
                    fieldWithPath("address").description("User Address"),
                    fieldWithPath("city").description("User City"),
                    fieldWithPath("stateProvince").description("State Province"),
                    fieldWithPath("country").description("User Country"),
                    fieldWithPath("clinicUserImage").description("Clinic User Image")
                )
            ))
            .andExpect(status().isCreated())
            .andReturn();
        assertNotNull(mvcResult);
    }


    @Test
    void getClinicUser() throws Exception {
        when(clinicUserMapper.toClinicUserModel(any())).thenReturn(getMockedClinicUserModel(CLINIC_USER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CLINIC_USERS_ID, CLINIC_USER_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("clinicUserId").description("Clinic User Id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void listClinicUsers() throws Exception {
        when(clinicUserService.listClinicUsers(any())).thenReturn(mockedClinicUsers(CLINIC_USER_ID));
        when(clinicUserMapper.toClinicUserModelList(any())).thenReturn(List.of(getMockedClinicUserModel(CLINIC_USER_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_USERS)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .queryParams(getMockedPaginationRequest())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                queryParameters(
                    parameterWithName("pageNo").description("Page no for which resources are requested. pageNo starts at: 0"),
                    parameterWithName("pageSize").description("No of elements in a page. Max pageSize limit: 100"),
                    parameterWithName("sortField").description("Field on which sorting is requested"),
                    parameterWithName("sortDirection").description("Direction of sort field. Allowed values: [ASC, DESC]")
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void deleteClinicUser() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_CLINIC_USERS_ID, CLINIC_USER_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("clinicUserId").description("Clinic User Id")
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

    @Test
    void updateClinicUser() throws Exception {
        final ClinicUserModel mockedClinicUserModel = getMockedClinicUserModel(CLINIC_USER_ID);
        final byte[] requestBytes = objectMapper.writeValueAsString(mockedClinicUserModel).getBytes(Charset.defaultCharset());
        final MockPart clinicUser = new MockPart("clinicUser", requestBytes);

        clinicUser.getHeaders().setContentType(APPLICATION_JSON);

        when(clinicUserMapper.toClinicUserModel(any())).thenReturn(mockedClinicUserModel);

        final MvcResult mvcResult = mockMvc.perform(
            MockMvcRequestBuilders
                .multipart(HttpMethod.PUT, URI_CLINIC_USERS_ID, CLINIC_USER_ID)
                .part(clinicUser)
                .file(image)
                .content(objectMapper.writeValueAsString(getMockedClinicUserModel(null))))
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("name").description("User name"),
                    fieldWithPath("designation").description("User Designation"),
                    fieldWithPath("clinicDepartmentId").description("Clinic department Id"),
                    fieldWithPath("phone").description("User Phone"),
                    fieldWithPath("alternatePhone").description("User Alternate Phone"),
                    fieldWithPath("email").description("User Email"),
                    fieldWithPath("alternateEmail").description("User Alternate Email"),
                    fieldWithPath("address").description("User Address"),
                    fieldWithPath("city").description("User City"),
                    fieldWithPath("stateProvince").description("State Province"),
                    fieldWithPath("country").description("User Country"),
                    fieldWithPath("clinicUserImage").description("Clinic User Image")
                )
            ))
            .andExpect(status().isOk())
            .andReturn();
        assertNotNull(mvcResult);

    }

    private ClinicUser getMockedClinicUser(final Long clinicUserId) {
        final ClinicUserModel mockedClinicUserModel = getMockedClinicUserModel(clinicUserId);
        final ClinicUser clinicUser = new ClinicUser();
        clinicUser.setId(mockedClinicUserModel.getClinicUserId());
        return clinicUser;
    }

    private ClinicUserModel getMockedClinicUserModel(final Long clinicUserId) {
        final ClinicUserModel clinicUserModel = new ClinicUserModel();
        clinicUserModel.setClinicUserId(clinicUserId);
        clinicUserModel.setName(CLINIC_USER_NAME);
        clinicUserModel.setDesignation(UserDesignation.DOCTOR.name());
        clinicUserModel.setClinicDepartmentId(CLINIC_DEPARTMENT_ID);
        clinicUserModel.setPhone("1234567");
        clinicUserModel.setAlternatePhone("36334");
        clinicUserModel.setEmail("Test@123");
        clinicUserModel.setAlternateEmail("TEST1@123");
        clinicUserModel.setAddress("Test");
        clinicUserModel.setCity("Test 1");
        clinicUserModel.setStateProvince("Test 2");
        clinicUserModel.setCountry("Test 3");
        clinicUserModel.setPostalCode("123454");
        clinicUserModel.setClinicUserImage(getMockedClinicUserImageModel(image));
        return clinicUserModel;
    }

    private ClinicUserImageModel getMockedClinicUserImageModel(final MultipartFile image) {
        final ClinicUserImageModel clinicUserImageModel = new ClinicUserImageModel();
        clinicUserImageModel.setImageName(image.getOriginalFilename());
        clinicUserImageModel.setImageContentType(image.getContentType());
        clinicUserImageModel.setIsActive(true);
        try {
            clinicUserImageModel.setImage(image.getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return clinicUserImageModel;
    }

    private List<ClinicUser> mockedClinicUsers(final Long clinicUserId) {
        return List.of(getMockedClinicUser(clinicUserId));
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
