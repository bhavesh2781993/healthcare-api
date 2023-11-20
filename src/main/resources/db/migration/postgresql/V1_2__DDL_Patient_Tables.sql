create sequence seq_otv_notes start with 1 increment by 5;
create sequence seq_patient_treatment_stage start with 1 increment by 5;
create sequence seq_patient_imaging_info start with 1 increment by 5;
create sequence seq_patient_labs start with 1 increment by 5;
create sequence seq_patient_notes start with 1 increment by 5;
create sequence seq_patient_prescription start with 1 increment by 5;
create sequence seq_patient_tracker_steps start with 1 increment by 5;
create sequence seq_patient_treatment_goals start with 1 increment by 5;
create sequence seq_patient_treatment_info start with 1 increment by 5;
create sequence seq_patient_vitals_signs start with 1 increment by 5;
create sequence seq_patients start with 1 increment by 5;
create sequence seq_patients_tracker start with 1 increment by 5;
create sequence seq_sim_fusions start with 1 increment by 5;
create sequence seq_sim_nursing_tasks_requests start with 1 increment by 5;
create sequence seq_sim_order_devices start with 1 increment by 5;
create sequence seq_sim_order_instructions start with 1 increment by 5;
create sequence seq_treatment_approval start with 1 increment by 5;
create sequence seq_treatment_team start with 1 increment by 5;

create table patients
(
    patient_id         bigint       not null,
    address            varchar(255),
    dob                date,
    email              varchar(255) not null,
    first_consult_date timestamp(6),
    first_name         varchar(255) not null,
    last_name          varchar(255),
    middle_name        varchar(255),
    mrn                varchar(255) not null,
    phone              varchar(255),
    ssn                varchar(255) not null,
    clinic_id          bigint       not null,
    clinic_user_id     bigint,
    created_at         timestamp,
    created_by         varchar(255),
    last_modified_at   timestamp,
    last_modified_by   varchar(255),
    primary key (patient_id)
);

create table patient_trackers
(
    patient_tracker_id        bigint not null,
    patient_id                bigint not null,
    clinic_workflow_id        bigint not null,
    status                    varchar(255),
    parent_patient_tracker_id bigint,
    created_at                timestamp,
    created_by                varchar(255),
    last_modified_at          timestamp,
    last_modified_by          varchar(255),
    primary key (patient_tracker_id)
);

create table patient_tracker_steps
(
    patient_tracker_step_id bigint not null,
    patient_tracker_id      bigint not null,
    workflow_step_id        bigint not null,
    status                  varchar(255),
    created_at              timestamp,
    created_by              varchar(255),
    last_modified_at        timestamp,
    last_modified_by        varchar(255),
    primary key (patient_tracker_step_id)
);

create table early_eot_reasons
(
    early_eot_reason_id bigint not null,
    reason              varchar(255),
    created_at          timestamp,
    created_by          varchar(255),
    last_modified_at    timestamp,
    last_modified_by    varchar(255),
    primary key (early_eot_reason_id)
);

create table eot_notes
(
    eot_note_id      bigint not null,
    note             varchar(255),
    is_early_eot     boolean,
    status           varchar(255),
    created_at       timestamp,
    created_by       varchar(255),
    last_modified_at timestamp,
    last_modified_by varchar(255),
    primary key (eot_note_id)
);


create table followup_plans
(
    followup_plan_id bigint not null,
    followup_in      varchar(255),
    plan             varchar(255),
    plan_detail      varchar(255),
    created_at       timestamp,
    created_by       varchar(255),
    last_modified_at timestamp,
    last_modified_by varchar(255),
    primary key (followup_plan_id)
);

create table followup_studies
(
    followup_study_id        bigint not null,
    has_ct_scan              boolean,
    has_mri                  boolean,
    others                   varchar(255),
    types_of_studies_ct_scan varchar(255),
    types_of_studies_mri     varchar(255),
    created_at               timestamp,
    created_by               varchar(255),
    last_modified_at         timestamp,
    last_modified_by         varchar(255),
    primary key (followup_study_id)
);

create table on_treatment_issues
(
    on_treatment_issue_id bigint not null,
    treatment_issue       varchar(255),
    created_at            timestamp,
    created_by            varchar(255),
    last_modified_at      timestamp,
    last_modified_by      varchar(255),
    primary key (on_treatment_issue_id)
);

create table otv_notes
(
    otv_note_id               bigint not null,
    otv_note_date             timestamp(6),
    assessment                varchar(255),
    objective                 varchar(255),
    plan                      varchar(255),
    subjective                varchar(255),
    total_dose_delivered      integer,
    total_fractions_delivered integer,
    patient_tracker_id        bigint not null,
    created_at                timestamp,
    created_by                varchar(255),
    last_modified_at          timestamp,
    last_modified_by          varchar(255),
    primary key (otv_note_id)
);

create table patient_ancillary_care
(
    patient_ancillary_care_id bigint not null,
    has_colostomy             boolean,
    has_peg_tube              boolean,
    has_tracheotomy           boolean,
    is_on_pain_medication     boolean,
    is_on_steroid             boolean,
    pain_medication           varchar(255),
    steroid                   varchar(255),
    created_at                timestamp,
    created_by                varchar(255),
    last_modified_at          timestamp,
    last_modified_by          varchar(255),
    primary key (patient_ancillary_care_id)
);

create table patient_treatment_stages
(
    patient_treatment_stage_id bigint not null,
    tnm_stage_lookup_id        bigint not null,
    patient_tracker_id         bigint not null,
    created_at                 timestamp,
    created_by                 varchar(255),
    last_modified_at           timestamp,
    last_modified_by           varchar(255),
    primary key (patient_treatment_stage_id)
);

create table patient_imaging_info
(
    patient_imaging_info_id bigint not null,
    notes                   varchar(255),
    align_to                bigint,
    imaging_type            bigint,
    patient_tracker_id      bigint,
    created_at              timestamp,
    created_by              varchar(255),
    last_modified_at        timestamp,
    last_modified_by        varchar(255),
    primary key (patient_imaging_info_id)
);

create table patient_labs
(
    patient_lab_id     bigint not null,
    lab_date           timestamp(6),
    lab_name           varchar(255),
    lab_url            varchar(255),
    patient_tracker_id bigint,
    created_at         timestamp,
    created_by         varchar(255),
    last_modified_at   timestamp,
    last_modified_by   varchar(255),
    primary key (patient_lab_id)
);

create table patient_notes
(
    patient_note_id    bigint not null,
    patient_note       varchar(255),
    clinic_user_id     bigint not null,
    patient_tracker_id bigint not null,
    created_at         timestamp,
    created_by         varchar(255),
    last_modified_at   timestamp,
    last_modified_by   varchar(255),
    primary key (patient_note_id)
);

create table patient_oncology_summary
(
    patient_oncology_summary_id bigint not null,
    brief_plan                  varchar(255),
    diagnosis                   varchar(255),
    summary                     varchar(255),
    created_at                  timestamp,
    created_by                  varchar(255),
    last_modified_at            timestamp,
    last_modified_by            varchar(255),
    primary key (patient_oncology_summary_id)
);

create table patient_prescriptions
(
    patient_prescription_id     bigint not null,
    course_name                 varchar(255),
    dose_per_fraction_in_cgy    integer,
    sequence                    integer,
    target_volume               varchar(255),
    total_dose_delivered_in_cgy integer,
    total_dose_in_cgy           integer,
    total_fractions             integer,
    total_fractions_delivered   integer,
    energy_id                   bigint not null,
    frequency_id                bigint not null,
    patient_treatment_info_id   bigint not null,
    technique_id                bigint not null,
    created_at                  timestamp,
    created_by                  varchar(255),
    last_modified_at            timestamp,
    last_modified_by            varchar(255),
    primary key (patient_prescription_id)
);

create table patient_treatment_goals
(
    patient_treatment_goal_id    bigint not null,
    primary_goal_dose            integer,
    primary_goal_dose_comparator varchar(255),
    primary_goal_dose_limit      integer,
    primary_goal_dose_limit_unit varchar(255),
    primary_goal_dose_unit       varchar(255),
    priority                     integer,
    structure                    varchar(255),
    structure_measure            varchar(255),
    variation_dose               integer,
    variation_dose_comparator    varchar(255),
    variation_dose_limit         integer,
    variation_dose_limit_unit    varchar(255),
    variation_dose_unit          varchar(255),
    is_objective_met             boolean,
    patient_tracker_id           bigint,
    created_at                   timestamp,
    created_by                   varchar(255),
    last_modified_at             timestamp,
    last_modified_by             varchar(255),
    primary key (patient_treatment_goal_id)
);

create table patient_treatment_info
(
    patient_treatment_info_id   bigint not null,
    treatment_intent_id         bigint not null,
    treatment_location_id       bigint not null,
    treatment_machine_id        bigint not null,
    treatment_modality_id       bigint not null,
    treatment_site_id           bigint not null,
    treatment_start_date        timestamp(6),
    has_concurrent_chemotherapy boolean,
    imrt_medical_necessity_id   bigint,
    patient_tracker_id          bigint not null,
    created_at                  timestamp,
    created_by                  varchar(255),
    last_modified_at            timestamp,
    last_modified_by            varchar(255),
    primary key (patient_treatment_info_id)
);

create table patient_vitals_signs
(
    patient_vitals_signs_id bigint not null,
    bp                      varchar(255),
    o2_stats_in_pct         varchar(255),
    patient_vitals_date     timestamp(6),
    pulse                   varchar(255),
    resp                    varchar(255),
    temp_in_celcius         varchar(255),
    weight_in_kg            varchar(255),
    patient_tracker_id      bigint not null,
    created_at              timestamp,
    created_by              varchar(255),
    last_modified_at        timestamp,
    last_modified_by        varchar(255),
    primary key (patient_vitals_signs_id)
);

create table sim_fusions
(
    sim_fusion_id    bigint not null,
    image_seq        varchar(255),
    scan_date        date,
    scan             bigint,
    sim_order_id     bigint,
    created_at       timestamp,
    created_by       varchar(255),
    last_modified_at timestamp,
    last_modified_by varchar(255),
    primary key (sim_fusion_id)
);

create table sim_nursing_tasks_requests
(
    nursing_task_request_id bigint not null,
    nursing_task            varchar(255),
    nursing_task_status     varchar(255),
    nursing_task_detail     varchar(255),
    sim_order_id            bigint,
    created_at              timestamp,
    created_by              varchar(255),
    last_modified_at        timestamp,
    last_modified_by        varchar(255),
    primary key (nursing_task_request_id)
);

create table sim_order_devices
(
    sim_order_device_id      bigint not null,
    immobilization_device_id bigint,
    sim_order_id             bigint,
    created_at               timestamp,
    created_by               varchar(255),
    last_modified_at         timestamp,
    last_modified_by         varchar(255),
    primary key (sim_order_device_id)
);

create table sim_order_instructions
(
    sim_order_instruction_id bigint not null,
    instruction_id           bigint,
    sim_order_id             bigint,
    created_at               timestamp,
    created_by               varchar(255),
    last_modified_at         timestamp,
    last_modified_by         varchar(255),
    primary key (sim_order_instruction_id)
);

create table sim_orders
(
    sim_order_id        bigint not null,
    ct_scan_instruction jsonb  not null,
    isocenter_location  varchar(255),
    sim_order_status    varchar(255),
    arm_position        bigint,
    leg_position        bigint,
    patient_position    bigint,
    created_at          timestamp,
    created_by          varchar(255),
    last_modified_at    timestamp,
    last_modified_by    varchar(255),
    primary key (sim_order_id)
);

create table sim_other_details
(
    sim_other_detail_id bigint not null,
    other_detail        varchar(255),
    created_at          timestamp,
    created_by          varchar(255),
    last_modified_at    timestamp,
    last_modified_by    varchar(255),
    primary key (sim_other_detail_id)
);

create table sim_special_medical_physics_consults
(
    sim_special_medical_physics_consult_id bigint not null,
    additional_instruction                 varchar(255),
    medical_physics_consult_id             bigint not null,
    created_at                             timestamp,
    created_by                             varchar(255),
    last_modified_at                       timestamp,
    last_modified_by                       varchar(255),
    primary key (sim_special_medical_physics_consult_id)
);

create table sim_special_treatment_procedures
(
    sim_special_treatment_procedure_id bigint not null,
    additional_instruction             varchar(255),
    treatment_procedure_id             bigint not null,
    created_at                         timestamp,
    created_by                         varchar(255),
    last_modified_at                   timestamp,
    last_modified_by                   varchar(255),
    primary key (sim_special_treatment_procedure_id)
);

create table treatment_approvals
(
    treatment_approval_id bigint       not null,
    approval_for          varchar(255) not null,
    approval_status       varchar(255) not null,
    approved_on           timestamp(6) not null,
    approved_by           bigint       not null,
    patient_tracker_id    bigint       not null,
    created_at            timestamp,
    created_by            varchar(255),
    last_modified_at      timestamp,
    last_modified_by      varchar(255),
    primary key (treatment_approval_id)
);

create table treatment_meta_lookup
(
    treatment_meta_lookup_id bigint not null,
    meta_type                varchar(255),
    meta_value               varchar(255),
    clinic_id                bigint not null,
    created_at               timestamp,
    created_by               varchar(255),
    last_modified_at         timestamp,
    last_modified_by         varchar(255),
    primary key (treatment_meta_lookup_id)
);

create table treatment_teams
(
    treatment_team_id    bigint not null,
    clinic_department_id bigint not null,
    clinic_user_id       bigint not null,
    patient_tracker_id   bigint not null,
    created_at           timestamp,
    created_by           varchar(255),
    last_modified_at     timestamp,
    last_modified_by     varchar(255),
    primary key (treatment_team_id)
);

alter table if exists patients
    add constraint uk_patient_mrn unique (mrn);

alter table if exists patients
    add constraint uk_patient_ssn unique (ssn);

alter table if exists eot_notes
    add constraint fk_eot_note_patient_tracker_id
        foreign key (eot_note_id)
            references patient_trackers;

alter table if exists followup_plans
    add constraint fk_followup_plans_patient_tracker_id
        foreign key (followup_plan_id)
            references patient_trackers;

alter table if exists followup_studies
    add constraint fk_followup_studies_patient_tracker_id
        foreign key (followup_study_id)
            references patient_trackers;

alter table if exists on_treatment_issues
    add constraint fk_on_treatment_issue_patient_tracker_id
        foreign key (on_treatment_issue_id)
            references patient_trackers;

alter table if exists otv_notes
    add constraint fk_otv_notes_patient_tracker_id
        foreign key (patient_tracker_id)
            references patient_trackers;

alter table if exists patient_ancillary_care
    add constraint fk_patient_ancillary_care_patient_tracker_id
    foreign key (patient_ancillary_care_id)
    references patient_trackers;

alter table if exists patient_treatment_stages
    add constraint fk_patient_treatment_stage_patient_tracker_id
        foreign key (patient_tracker_id)
            references patient_trackers;

alter table if exists patient_treatment_stages
    add constraint fk_patient_treatment_stage_treatment_stage_id
        foreign key (tnm_stage_lookup_id)
            references tnm_stages_lookup;

alter table if exists patient_imaging_info
    add constraint fk_patient_imaging_info_align_to
        foreign key (align_to)
            references imaging_alignments_lookup;

alter table if exists patient_imaging_info
    add constraint fk_patient_imaging_info_imaging_type
        foreign key (imaging_type)
            references imaging_types_lookup;

alter table if exists patient_imaging_info
    add constraint fk_patient_imaging_info_patient_tracker_id
        foreign key (patient_tracker_id)
            references patient_trackers;

alter table if exists patient_labs
    add constraint fk_patient_lab_patient_tracker_id
        foreign key (patient_tracker_id)
            references patient_trackers;

alter table if exists patient_notes
    add constraint fk_patient_note_user_id
        foreign key (clinic_user_id)
            references clinic_users;

alter table if exists patient_notes
    add constraint fk_patient_note_patient_tracker_id
        foreign key (patient_tracker_id)
            references patient_trackers;

alter table if exists patient_oncology_summary
    add constraint fk_patient_onc_summary_patient_tracker_id
        foreign key (patient_oncology_summary_id)
            references patient_trackers;

alter table if exists patient_prescriptions
    add constraint fk_patient_prescription_energy_id
        foreign key (energy_id)
            references energies_lookup;

alter table if exists patient_prescriptions
    add constraint fk_patient_prescription_frequency_id
        foreign key (frequency_id)
            references frequencies_lookup;

alter table if exists patient_prescriptions
    add constraint fk_patient_prescription_treatment_info_id
        foreign key (patient_treatment_info_id)
            references patient_treatment_info;

alter table if exists patient_prescriptions
    add constraint fk_patient_prescription_technique_id
        foreign key (technique_id)
            references treatment_meta_lookup;

alter table if exists patient_tracker_steps
    add constraint uk_patient_tracker_step_tracker_and_workflow_step unique (patient_tracker_id, workflow_step_id);

alter table if exists patient_tracker_steps
    add constraint fk_patient_tracker_step_patient_tracker_id
        foreign key (patient_tracker_id)
            references patient_trackers;

alter table if exists patient_tracker_steps
    add constraint fk_patient_tracker_step_wf_step_id
        foreign key (workflow_step_id)
            references workflow_steps;

alter table if exists patient_trackers
    add constraint fk_patient_tracker_clinic_workflow_id
        foreign key (clinic_workflow_id)
            references clinic_workflows;

alter table if exists patient_trackers
    add constraint fk_patient_tracker_patient_id
        foreign key (patient_id)
            references patients;

alter table if exists patient_treatment_goals
    add constraint fk_patient_treatment_goal_patient_tracker_id
        foreign key (patient_tracker_id)
            references patient_trackers;

alter table if exists patient_treatment_info
    add constraint fk_patient_treatment_info_medical_necessity_id
        foreign key (imrt_medical_necessity_id)
            references treatment_meta_lookup;

alter table if exists patient_treatment_info
    add constraint fk_patient_treatment_info_patient_tracker_id
        foreign key (patient_tracker_id)
            references patient_trackers;

alter table if exists patient_treatment_info
    add constraint fk_patient_treatment_info_treatment_intent_id
        foreign key (treatment_intent_id)
            references treatment_meta_lookup;

alter table if exists patient_treatment_info
    add constraint fk_patient_treatment_info_treatment_location_id
        foreign key (treatment_location_id)
            references treatment_meta_lookup;

alter table if exists patient_treatment_info
    add constraint fk_patient_treatment_info_treatment_machine_id
        foreign key (treatment_machine_id)
            references treatment_meta_lookup;

alter table if exists patient_treatment_info
    add constraint fk_patient_treatment_info_treatment_modality_id
        foreign key (treatment_modality_id)
            references treatment_meta_lookup;

alter table if exists patient_treatment_info
    add constraint fk_patient_treatment_info_treatment_site_id
        foreign key (treatment_site_id)
            references treatment_meta_lookup;

alter table if exists patient_vitals_signs
    add constraint fk_patient_vitals_signs_patient_tracker_id
        foreign key (patient_tracker_id)
            references patient_trackers;

alter table if exists patients
    add constraint fk_patient_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists patients
    add constraint fk_patient_clinic_user_id
        foreign key (clinic_user_id)
            references clinic_users;

alter table if exists sim_fusions
    add constraint fk_sim_fusion_scan_id
        foreign key (scan)
            references fusions_lookup;

alter table if exists sim_fusions
    add constraint fk_sim_fusion_sim_order_id
        foreign key (sim_order_id)
            references sim_orders;

alter table if exists sim_nursing_tasks_requests
    add constraint fk_sim_nursing_task_request_sim_order_id
        foreign key (sim_order_id)
            references sim_orders;

alter table if exists sim_order_devices
    add constraint fk_sim_order_device_immobilization_device_lookup_id
        foreign key (immobilization_device_id)
            references immobilization_devices_lookup;

alter table if exists sim_order_devices
    add constraint fk_sim_order_device_sim_order_id
        foreign key (sim_order_id)
            references sim_orders;

alter table if exists sim_order_instructions
    add constraint fk_sim_order_instruction_instruction_lookup_id
        foreign key (instruction_id)
            references instructions_lookup;

alter table if exists sim_order_instructions
    add constraint fk_sim_order_instruction_sim_order_id
        foreign key (sim_order_id)
            references sim_orders;

alter table if exists sim_orders
    add constraint fk_sim_order_arm_position
        foreign key (arm_position)
            references positions_lookup;

alter table if exists sim_orders
    add constraint fk_sim_order_leg_position
        foreign key (leg_position)
            references positions_lookup;

alter table if exists sim_orders
    add constraint fk_sim_order_patient_position
        foreign key (patient_position)
            references positions_lookup;

alter table if exists sim_orders
    add constraint fk_sim_order_patient_tracker_id
    foreign key (sim_order_id)
    references patient_trackers;

alter table if exists sim_other_details
    add constraint fk_sim_other_detail_sim_order_id
        foreign key (sim_other_detail_id)
            references sim_orders;

alter table if exists sim_special_medical_physics_consults
    add constraint fk_sim_special_medical_physics_consult_id
        foreign key (medical_physics_consult_id)
            references medical_physics_consults_lookup;

alter table if exists sim_special_medical_physics_consults
    add constraint fk_sim_special_medical_physics_consult_sim_order_id
        foreign key (sim_special_medical_physics_consult_id)
            references sim_orders;

alter table if exists sim_special_treatment_procedures
    add constraint fk_sim_special_treatment_procedure_treatment_procedure_id
        foreign key (treatment_procedure_id)
            references treatment_procedures_lookup;

alter table if exists sim_special_treatment_procedures
    add constraint fk_sim_special_treatment_procedure_sim_order_id
        foreign key (sim_special_treatment_procedure_id)
            references sim_orders;

alter table if exists treatment_approvals
    add constraint fk_treatment_approval_clinic_user_id
        foreign key (approved_by)
            references clinic_users;

alter table if exists treatment_approvals
    add constraint fk_treatment_approval_patient_tracker_id
        foreign key (patient_tracker_id)
            references patient_trackers;

alter table if exists treatment_procedures_lookup
    add constraint fk_treatment_procedure_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists treatment_teams
    add constraint fk_treatment_team_clinic_department_id
        foreign key (clinic_department_id)
            references clinic_departments;

alter table if exists treatment_teams
    add constraint fk_treatment_team_clinic_user_id
        foreign key (clinic_user_id)
            references clinic_users;

alter table if exists treatment_teams
    add constraint fk_treatment_team_patient_tracker_id
        foreign key (patient_tracker_id)
            references patient_trackers;