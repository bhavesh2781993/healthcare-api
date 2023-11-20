package com.test.api.healthcare.common.constants;

import static com.test.api.healthcare.common.constants.ApplicationConstant.PAGE_SIZE;
import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ErrorMessage {

    public static final String MALFORMED_JSON_REQUEST = "Malformed JSON request";
    public static final String SERVICE_UNAVAILABLE = "Service unavailable";
    public static final String INVALID_DATA = "Invalid data";
    public static final String INVALID_FIELD_VALUE = "Invalid value %s";
    public static final String INVALID_SORT_FILTER_PARAM = "Invalid filter or sort param: %s";
    public static final String ERR_MSG_FIELD_CAN_NOT_BE_BLANK = "Field cannot be blank";
    public static final String ERR_MSG_FIELD_DATA_ALREADY_PRESENT = "Data already present %s";
    public static final String ERR_MSG_FILE_SIZE_EXCEEDED = "Actual file size %s exceeds allowed limit of %s MB";
    public static final String ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT = "Page size exceeded allowed limit of " + PAGE_SIZE;

    //Configuration
    public static final String ERR_MSG_TREATMENT_META_NOT_FOUND = "Treatment meta not found for id: %s and clinic id: %s";
    public static final String ERR_MSG_TREATMENT_STAGE_NOT_FOUND = "Treatment stage not found for id: %s and clinic id: %s";
    public static final String ERR_MSG_IMMOBILIZATION_DEVICE_NOT_FOUND = "Immobilization Device not found for id: %s and clinic id: %s";
    public static final String ERR_MSG_INSTRUCTION_NOT_FOUND = "Instruction not found for id: %s and clinic id: %s";
    public static final String ERR_MSG_FUSION_NOT_FOUND = "Fusion not found for id: %s and clinic id: %s";
    public static final String ERR_MSG_IMAGING_TYPE_NOT_FOUND = "Imaging Type not found for id: %s and clinic id: %s";
    public static final String ERR_MSG_IMAGING_ALIGNMENT_NOT_FOUND = "Imaging Alignment not found for : %s and clinic id: %s";
    public static final String ERR_MSG_TREATMENT_PROCEDURE_NOT_FOUND = "Treatment Procedure not found for id: %s and clinic id: %s";
    public static final String ERR_MSG_TREATMENT_PROCEDURE_NOT_FOUND_FOR_ID = "Treatment Procedure not found for id: %s";
    public static final String ERR_MSG_MEDICAL_PHYSICS_CONSULT_NOT_FOUND = "Medical Physics consult not found for id: %s and clinic id: %s";
    public static final String ERR_MSG_MEDICAL_PHYSICS_CONSULT_NOT_FOUND_FOR_ID = "Medical Physics consult not found for id: %s";
    public static final String ERR_MSG_POSITION_NOT_FOUND = "Position not found for id : %s";
    public static final String ERR_MSG_FREQUENCY_NOT_FOUND = "Frequency not found for : %s";
    public static final String ERR_MSG_ENERGY_NOT_FOUND = "Energy not found for : %s";
    public static final String ERR_MSG_CONCURRENT_CHEMOTHERAPY_NOT_FOUND = "Concurrent Chemotherapy not found for id : %s";

    //Clinic & Workflow
    public static final String ERR_MSG_DUPLICATE_CLINIC_WORKFLOW = "Clinic Workflow already exist";
    public static final String ERR_MSG_CLINIC_NOT_FOUND = "No clinic found with id: %s";
    public static final String ERR_MSG_WORKFLOW_NOT_FOUND = "No workflow found with id: %s";
    public static final String ERR_MSG_WORKFLOW_EXIST = "Workflow Exist with similar name and type";
    public static final String ERR_MSG_WORKFLOW_STEP_EXIST = "Workflow Step Exist with similar name";
    public static final String ERR_MSG_WORKFLOW_STEP_NOT_FOUND = "No Workflow Step found with name %s";
    public static final String ERR_MSG_WORKFLOW_SEQ_EXIST = "Workflow Sequence Exist with similar name";
    public static final String ERR_MSG_CLINIC_USER_NOT_FOUND = "No Clinic User found with id: %s";
    public static final String ERR_MSG_CLINIC_LOCATION_NOT_FOUND = "No Clinic Location found with id: %s";
    public static final String ERR_MSG_CLINIC_DEPARTMENT_NOT_FOUND = "No Clinic Department found with id: %s";
    public static final String ERR_MSG_CLINIC_WORKFLOW_NOT_FOUND = "No Clinic Workflow found with id: %s";
    public static final String ERR_MSG_CLINIC_USER_TASK_NOT_FOUND = "No Clinic User Task found with id: %s";

    //Patient & Treatment
    public static final String ERR_MSG_DUPLICATE_PATIENT_TRACKER_STEP = "Patient Tracker Step already exist";
    public static final String ERR_MSG_PATIENT_NOT_FOUND = "No Patient found with id: %s";
    public static final String ERR_MSG_PATIENT_TRACKER_NOT_FOUND = "No Patient Tracker found with id: %s";
    public static final String ERR_MSG_TRACKER_INVALID_CLINIC_WORKFLOW =
        "Clinic workflow id: %s is not valid for Boost and Adaptation Tracker";
    public static final String ERR_MSG_PATIENT_NOTE_NOT_FOUND = "No Patient Note found with id: %s";
    public static final String ERR_MSG_TREATMENT_TEAM_NOT_FOUND = "No Treatment Team found with id: %s";
    public static final String ERR_MSG_PATIENT_TRACKER_WORKFLOW_STEP_NOT_FOUND = "No Patient-Tracker-Workflow-Step found with id: %s";
    public static final String ERR_MSG_TREATMENT_APPROVAL_NOT_FOUND = "No Treatment Approval found with id: %s";
    public static final String ERR_MSG_PATIENT_VITALS_SIGN_NOT_FOUND = "No Patient-Vitals-Sign found with id: %s";
    public static final String ERR_MSG_PATIENT_ANCILLARY_CARE_NOT_FOUND = "No Patient-Ancillary-Care found with id: %s";
    public static final String ERR_MSG_PATIENT_ANCILLARY_CARE_NOT_FOUND_WITH_PATIENT_TRACKER_ID =
        "No Patient-Ancillary-Care found with Patient-Tracker-Id: %s";
    public static final String ERR_MSG_ON_TREATMENT_ISSUE_NOT_FOUND = "No On-Treatment-Issue found with id: %s";
    public static final String ERR_MSG_FOLLOWUP_STUDY_NOT_FOUND = "No Followup Study found with id: %s";
    public static final String ERR_MSG_FOLLOWUP_STUDY_NOT_FOUND_WITH_PATIENT_TRACKER_ID =
        "No Followup Study found with Patient-Tracker-Id: %s";
    public static final String ERR_MSG_FOLLOWUP_PLAN_NOT_FOUND = "No Followup Plan found with id: %s";
    public static final String ERR_MSG_FOLLOWUP_PLAN_NOT_FOUND_WITH_PATIENT_TRACKER_ID =
        "No Followup Plan found with Patient-Tracker-Id: %s";
    public static final String ERR_MSG_EARLY_EOT_REASON_NOT_FOUND = "No Early Eot Reason found with id: %s";
    public static final String ERR_MSG_PATIENT_ONCOLOGY_SUMMARY_NOT_FOUND = "No Patient Oncology Summary found with id: %s";
    public static final String ERR_MSG_OTV_NOTE_NOT_FOUND = "No Otv Note found with id: %s";
    public static final String ERR_MSG_PATIENT_LAB_NOT_FOUND = "No Patient Lab found with id: %s";
    public static final String ERR_MSG_PATIENT_CANCER_STAGE_NOT_FOUND = "No Patient Cancer Stage found with id: %s";
    public static final String ERR_MSG_DUPLICATE_PATIENT_CANCER_STAGE = "Duplicate Patient Cancer Stage found in request";
    public static final String ERR_MSG_MISSING_PATIENT_CANCER_STAGE = "Missing Patient Cancer Stages in request";
    public static final String ERR_MSG_CANCER_TREATMENT_STAGE_EXIST = "Treatment Stage already exist with id: %s";
    public static final String ERR_MSG_PATIENT_TREATMENT_INFO_NOT_FOUND = "No Patient Treatment Info found with id : %s";
    public static final String ERR_MSG_PATIENT_TREATMENT_GOAL_NOT_FOUND = "No Patient Treatment Goal found with id: %s";
    public static final String ERR_MSG_PATIENT_PRESCRIPTION_NOT_FOUND = "No Patient Prescription found with id: %s";
    public static final String ERR_MSG_PATIENT_APPOINTMENT_NOT_FOUND = "No Patient Appointment found with id %s";
    public static final String ERR_MSG_PATIENT_CHART_NOTE_NOT_FOUND = "No Patient Chart Note found with id : %s";

    public static final String ERR_MSG_KEYCLOAK_USER_NOT_FOUND = "No User found with email: %s";
    public static final String ERR_MSG_KEYCLOAK_CLIENT_NOT_FOUND = "No Client data found";

    //Simulation Request
    public static final String ERR_MSG_SIM_ORDER_NOT_FOUND = "No sim order found with id: %s";
    public static final String ERR_MSG_POSITION_FIELD_CAN_NOT_BE_PRESENT = "No sim position field is present: %s ";
    public static final String ERR_MSG_SIM_FUSION_NOT_FOUND = "No sim fusion found with id: %s";
    public static final String ERR_MSG_SIM_FUSION_NOT_FOUND_FOR_LIST = "No sim fusion found with ids: %s";
    public static final String ERR_MSG_PATIENT_IMAGING_INFO_FIELD_NOT_FOUND = "No patient imaging info field is present: %s";
    public static final String ERR_MSG_PATIENT_IMAGING_INFO_NOT_FOUND_FOR_LIST = "No patient imaging info found with ids: %s";
    public static final String ERR_MSG_SIM_NURSING_TASK_NOT_FOUND = "No sim nursing task field is present: %s";
    public static final String ERR_MSG_SIM_TREATMENT_PROCEDURE_NOT_FOUND = "No sim treatment procedure found with id: %s";
    public static final String ERR_MSG_SIM_MEDICAL_PHYSICS_CONSULT_NOT_FOUND = "No sim medical physics consult found with id: %s";
    public static final String ERR_MSG_MEDICAL_NECESSITY_NOT_FOUND = "No such medical necessity present with id : %s";

    //Simulation Template
    public static final String ERR_MSG_SIM_TEMPLATE_NOT_FOUND = "Sim template not found for id: %s and clinic id : %s";
    public static final String ERR_MSG_SIM_TEMPLATE_FIELD_CAN_NOT_BE_PRESENT = "No sim template field is present : %s";
}
