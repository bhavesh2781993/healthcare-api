create sequence seq_email_notifications start with 1 increment by 10;

create table email_notifications
(
    email_notification_id bigint not null,
    "from"                varchar(255),
    receiver              jsonb  not null,
    provider              varchar(255),
    status                varchar(255),
    failure_count         integer,
    failure_reason        varchar(255),
    created_at            timestamp,
    created_by            varchar(255),
    last_modified_at      timestamp,
    last_modified_by      varchar(255),
    primary key (email_notification_id)
);