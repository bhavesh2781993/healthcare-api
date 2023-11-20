create table patient_charts
(
    patient_chart_id bigint not null,
    note             varchar(255),
    is_reviewed      boolean,
    created_at       timestamp,
    created_by       varchar(255),
    last_modified_at timestamp,
    last_modified_by varchar(255),
    primary key (patient_chart_id)
);

alter table if exists patient_charts
    add constraint fk_patient_chart_note_patient_tracker_id
        foreign key (patient_chart_id)
            references patient_trackers;
