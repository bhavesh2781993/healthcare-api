alter table clinic_users
    add column email              varchar(50),
    add column alternate_email    varchar(50),
    add column phone              varchar(20),
    add column alternate_phone    varchar(20),
    add column address            Text,
    add column city               varchar(25),
    add column state_province     varchar(25),
    add column country            varchar(25),
    add column postal_code        varchar(20);


