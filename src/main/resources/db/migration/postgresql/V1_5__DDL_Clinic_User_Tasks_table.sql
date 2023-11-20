create sequence seq_clinic_user_tasks start with 1 increment by 5;

create table clinic_user_tasks
(
    clinic_user_task_id bigint not null,
    task_type           varchar(255),
    task_note           varchar(255),
    task_status         varchar(255),
    task_due_date       timestamp(6),
    clinic_id           bigint not null,
    patient_id          bigint not null,
    task_assigned_to    bigint,
    task_ref_id         bigint,
    created_at          timestamp,
    created_by          varchar(255),
    last_modified_at    timestamp,
    last_modified_by    varchar(255),
    primary key (clinic_user_task_id)
);

alter table if exists clinic_user_tasks
    add constraint fk_clinic_user_task_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists clinic_user_tasks
    add constraint fk_clinic_user_task_patient_id
        foreign key (patient_id)
            references patients;

alter table if exists clinic_user_tasks
    add constraint fk_clinic_user_task_task_assigned_to_id
        foreign key (task_assigned_to)
            references clinic_users;