create table users
(
    id                 uuid         not null,
    version            integer      not null,
    created_date       timestamp(6) not null,
    last_modified_date timestamp(6) not null,
    provider_id        varchar(255) not null unique,
    provider_name      varchar(255) not null,
    disabled           boolean      not null,
    primary key (id)
);
create table user_profiles
(
    id                 uuid         not null,
    version            integer      not null,
    created_date       timestamp(6) not null,
    last_modified_date timestamp(6) not null,
    user_id            uuid         not null,
    first_name         varchar(255),
    last_name          varchar(255),
    phone_number       varchar(255),
    address            varchar(255),
    email              varchar(255),
    primary key (user_id)
);
alter table if exists user_profiles
    add constraint fk_users_user_profile foreign key (user_id) references users;

create table authorities
(
    id                 uuid         not null,
    version            integer      not null,
    created_date       timestamp(6) not null,
    last_modified_date timestamp(6) not null,
    name               varchar(255) not null unique,
    primary key (id)
);

create table users_authorities
(
    authority_id uuid not null,
    user_id      uuid not null,
    primary key (authority_id, user_id)
);
alter table if exists users_authorities
    add constraint fk_users_authorities_authority foreign key (authority_id) references authorities;
alter table if exists users_authorities
    add constraint fk_users_authorities_user foreign key (user_id) references users;