create sequence seq_clinic_departments start with 1 increment by 5;
create sequence seq_clinic_locations start with 1 increment by 5;
create sequence seq_clinic_users start with 1 increment by 5;
create sequence seq_clinic_workflows start with 1 increment by 5;
create sequence seq_clinics start with 1 increment by 5;
create sequence seq_workflow_steps start with 1 increment by 5;
create sequence seq_workflows start with 1 increment by 5;

create table clinic_departments
(
    clinic_department_id bigint not null,
    department_name      varchar(255),
    clinic_location_id   bigint not null,
    created_at           timestamp,
    created_by           varchar(255),
    last_modified_at     timestamp,
    last_modified_by     varchar(255),
    primary key (clinic_department_id)
);

create table clinic_locations
(
    clinic_location_id bigint not null,
    city               varchar(255),
    state              varchar(255),
    street             varchar(255),
    timezone           varchar(255),
    clinic_id          bigint not null,
    created_at         timestamp,
    created_by         varchar(255),
    last_modified_at   timestamp,
    last_modified_by   varchar(255),
    primary key (clinic_location_id)
);

create table clinic_users
(
    clinic_user_id       bigint not null,
    designation          varchar(255),
    name                 varchar(255),
    clinic_department_id bigint not null,
    created_at           timestamp,
    created_by           varchar(255),
    last_modified_at     timestamp,
    last_modified_by     varchar(255),
    primary key (clinic_user_id)
);

create table clinic_workflows
(
    clinic_workflow_id bigint not null,
    status             varchar(255),
    clinic_id          bigint not null,
    workflow_id        bigint not null,
    created_at         timestamp,
    created_by         varchar(255),
    last_modified_at   timestamp,
    last_modified_by   varchar(255),
    primary key (clinic_workflow_id)
);

create table clinics
(
    clinic_id        bigint not null,
    name             varchar(255),
    created_at       timestamp,
    created_by       varchar(255),
    last_modified_at timestamp,
    last_modified_by varchar(255),
    primary key (clinic_id)
);

create table workflow_steps
(
    workflow_step_id bigint not null,
    step             varchar(255),
    seq              integer,
    workflow_id      bigint not null,
    created_at       timestamp,
    created_by       varchar(255),
    last_modified_at timestamp,
    last_modified_by varchar(255),
    primary key (workflow_step_id)
);

create table workflows
(
    workflow_id      bigint not null,
    name             varchar(255),
    type             varchar(255),
    created_at       timestamp,
    created_by       varchar(255),
    last_modified_at timestamp,
    last_modified_by varchar(255),
    primary key (workflow_id)
);

alter table if exists clinic_departments
    add constraint fk_clinic_department_clinic_location_id
        foreign key (clinic_location_id)
            references clinic_locations;

alter table if exists clinic_locations
    add constraint fk_clinic_location_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists clinic_users
    add constraint fk_clinic_user_clinic_department_id
        foreign key (clinic_department_id)
            references clinic_departments;

alter table if exists clinic_workflows
    add constraint fk_clinic_workflow_clinic_id
        foreign key (clinic_id)
            references clinics;

alter table if exists clinic_workflows
    add constraint fk_clinic_workflow_workflow_id
        foreign key (workflow_id)
            references workflows;

alter table if exists clinic_workflows
    add constraint uk_clinic_workflow_clinic_and_workflow unique (clinic_id, workflow_id);

alter table if exists workflow_steps
    add constraint fk_workflow_step_workflow_id
        foreign key (workflow_id)
            references workflows;

alter table if exists workflow_steps
    add constraint uk_workflow_step_workflow_step unique (workflow_id, step);

alter table if exists workflow_steps
    add constraint uk_workflow_step_workflow_seq unique (workflow_id, seq);

alter table if exists workflows
    add constraint uk_workflow_name_type unique (name, type);