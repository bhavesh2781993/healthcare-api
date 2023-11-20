create sequence seq_clinic_user_images start with 1 increment by 5;

create table clinic_user_images
(
    clinic_user_image_id bigint not null,
    image_name           varchar(255),
    image_content_type   varchar(255),
    image                bytea,
    is_active            boolean,
    clinic_user_id       bigint,
    created_at           timestamp,
    created_by           varchar(255),
    last_modified_at     timestamp,
    last_modified_by     varchar(255),
    primary key (clinic_user_image_id)
);

alter table if exists clinic_user_images
    add constraint fk_clinic_user_image_clinic_user_id
    foreign key (clinic_user_id)
    references clinic_users;
