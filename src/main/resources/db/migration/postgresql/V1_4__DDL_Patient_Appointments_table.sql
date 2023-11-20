create sequence seq_patient_appointment start with 1 increment by 5;

create table patient_appointments
(
    patient_appointment_id        bigint not null,
    appointment_type              varchar(255),
    appointment_status            varchar(255),
    scheduled_datetime            timestamp(6),
    clinic_location_id            bigint,
    parent_patient_appointment_id bigint,
    patient_id                    bigint not null,
    patient_tracker_id            bigint,
    patient_tracker_step          bigint,
    scheduled_by                  bigint,
    scheduled_with                bigint,
    created_at                    timestamp,
    created_by                    varchar(255),
    last_modified_at              timestamp,
    last_modified_by              varchar(255),
    primary key (patient_appointment_id)
);

alter table if exists patient_appointments
    add constraint fk_patient_appointment_clinic_location_id
        foreign key (clinic_location_id)
            references clinic_locations;

alter table if exists patient_appointments
    add constraint fk_patient_appointment_parent_patient_appointment_id
        foreign key (parent_patient_appointment_id)
            references patient_appointments;

alter table if exists patient_appointments
    add constraint fk_patient_appointment_patient_id
        foreign key (patient_id)
            references patients;

alter table if exists patient_appointments
    add constraint fk_patient_appointment_patient_tracker_id
        foreign key (patient_tracker_id)
            references patient_trackers;

alter table if exists patient_appointments
    add constraint fk_patient_appointment_patient_tracker_step
        foreign key (patient_tracker_step)
            references patient_tracker_steps;

alter table if exists patient_appointments
    add constraint fk_patient_appointment_scheduled_by
        foreign key (scheduled_by)
            references clinic_users;

alter table if exists patient_appointments
    add constraint fk_patient_appointment_scheduled_with
        foreign key (scheduled_with)
            references clinic_users;