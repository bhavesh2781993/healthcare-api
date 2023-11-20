alter table patients
    add column masked_dob         varchar(15),
    add column masked_phone       varchar(20),
    add column masked_ssn         varchar(25),
    add column masked_email       varchar(50),
    add column masked_address     varchar(255);
