create sequence seq_concurrent_chemotherapy_lookup start with 1 increment by 5;
create sequence seq_energies_lookup start with 1 increment by 5;
create sequence seq_frequencies_lookup start with 1 increment by 5;
create sequence seq_fusions_lookup start with 1 increment by 5;
create sequence seq_imaging_info_lookup start with 1 increment by 5;
create sequence seq_imaging_type_lookup start with 1 increment by 5;
create sequence seq_immobilization_devices_lookup start with 1 increment by 5;
create sequence seq_instructions_lookup start with 1 increment by 5;
create sequence seq_medical_physics_consults_lookup start with 1 increment by 5;
create sequence seq_positions_lookup start with 1 increment by 5;
create sequence seq_tnm_stages_lookup start with 1 increment by 5;
create sequence seq_treatment_meta_lookup start with 1 increment by 5;
create sequence seq_treatment_procedures_lookup start with 1 increment by 5;

create table concurrent_chemotherapy_lookup
(
    concurrent_chemotherapy_lookup_id bigint not null,
    concurrent_chemotherapy           varchar(255),
    clinic_id                         bigint,
    created_at                        timestamp,
    created_by                        varchar(255),
    last_modified_at                  timestamp,
    last_modified_by                  varchar(255),
    primary key (concurrent_chemotherapy_lookup_id)
);

create table energies_lookup
(
    energy_id        bigint not null,
    energy           varchar(255),
    clinic_id        bigint not null,
    created_at       timestamp,
    created_by       varchar(255),
    last_modified_at timestamp,
    last_modified_by varchar(255),
    primary key (energy_id)
);

create table frequencies_lookup
(
    frequency_id     bigint not null,
    frequency        varchar(255),
    clinic_id        bigint not null,
    created_at       timestamp,
    created_by       varchar(255),
    last_modified_at timestamp,
    last_modified_by varchar(255),
    primary key (frequency_id)
);

create table fusions_lookup
(
    fusion_lookup_id bigint not null,
    fusions          varchar(255),
    clinic_id        bigint not null,
    created_at       timestamp,
    created_by       varchar(255),
    last_modified_at timestamp,
    last_modified_by varchar(255),
    primary key (fusion_lookup_id)
);

create table imaging_alignments_lookup
(
    imaging_alignment_lookup_id bigint not null,
    align_to                    varchar(255),
    clinic_id                   bigint not null,
    created_at                  timestamp,
    created_by                  varchar(255),
    last_modified_at            timestamp,
    last_modified_by            varchar(255),
    primary key (imaging_alignment_lookup_id)
);

create table imaging_types_lookup
(
    imaging_type_lookup_id bigint not null,
    imaging_type           varchar(255),
    clinic_id              bigint not null,
    created_at             timestamp,
    created_by             varchar(255),
    last_modified_at       timestamp,
    last_modified_by       varchar(255),
    primary key (imaging_type_lookup_id)
);

create table immobilization_devices_lookup
(
    immobilization_device_lookup_id bigint not null,
    device                          varchar(255),
    clinic_id                       bigint not null,
    created_at                      timestamp,
    created_by                      varchar(255),
    last_modified_at                timestamp,
    last_modified_by                varchar(255),
    primary key (immobilization_device_lookup_id)
);

create table instructions_lookup
(
    instruction_lookup_id bigint not null,
    instruction           varchar(255),
    clinic_id             bigint not null,
    created_at            timestamp,
    created_by            varchar(255),
    last_modified_at      timestamp,
    last_modified_by      varchar(255),
    primary key (instruction_lookup_id)
);

create table medical_physics_consults_lookup
(
    medical_physics_consult_lookup_id bigint not null,
    consult                           varchar(255),
    clinic_id                         bigint not null,
    created_at                        timestamp,
    created_by                        varchar(255),
    last_modified_at                  timestamp,
    last_modified_by                  varchar(255),
    primary key (medical_physics_consult_lookup_id)
);

create table positions_lookup
(
    position_lookup_id bigint not null,
    position           varchar(255),
    position_type      varchar(255),
    clinic_id          bigint,
    created_at         timestamp,
    created_by         varchar(255),
    last_modified_at   timestamp,
    last_modified_by   varchar(255),
    primary key (position_lookup_id)
);

create table tnm_stages_lookup
(
    tnm_stage_lookup_id bigint not null,
    stage               varchar(255),
    stage_type          varchar(255),
    spread_type         varchar(255),
    clinic_id           bigint not null,
    created_at          timestamp,
    created_by          varchar(255),
    last_modified_at    timestamp,
    last_modified_by    varchar(255),
    primary key (tnm_stage_lookup_id)
);

create table treatment_procedures_lookup
(
    treatment_procedure_lookup_id bigint not null,
    procedure                     varchar(255),
    clinic_id                     bigint not null,
    created_at                    timestamp,
    created_by                    varchar(255),
    last_modified_at              timestamp,
    last_modified_by              varchar(255),
    primary key (treatment_procedure_lookup_id)
);

alter table if exists concurrent_chemotherapy_lookup
    add constraint fk_concurrent_chemotherapy_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists energies_lookup
    add constraint fk_energy_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists frequencies_lookup
    add constraint fk_frequency_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists fusions_lookup
    add constraint fk_fusion_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists imaging_alignments_lookup
    add constraint fk_imaging_alignment_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists imaging_types_lookup
    add constraint fk_imaging_type_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists immobilization_devices_lookup
    add constraint fk_immobilization_device_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists instructions_lookup
    add constraint fk_instruction_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists medical_physics_consults_lookup
    add constraint fk_medical_physics_consult_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists positions_lookup
    add constraint fk_position_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists tnm_stages_lookup
    add constraint fk_tnm_stage_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists treatment_meta_lookup
    add constraint fk_treatment_meta_lookup_clinic_id
        foreign key (clinic_id)
            references clinics;