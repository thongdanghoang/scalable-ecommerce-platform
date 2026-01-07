create table user_authentication_providers
(
    version            integer      not null,
    created_date       timestamp(6) not null,
    last_modified_date timestamp(6) not null,
    id                 uuid         not null,
    user_id            uuid,
    provider_id        varchar(255) not null unique,
    provider_name      varchar(255) not null,
    primary key (id)
);
create table user_roles
(
    version            integer      not null,
    created_date       timestamp(6) not null,
    last_modified_date timestamp(6) not null,
    id                 uuid         not null,
    name               varchar(255) not null unique,
    primary key (id)
);
create table users
(
    version            integer      not null,
    created_date       timestamp(6) not null,
    last_modified_date timestamp(6) not null,
    id                 uuid         not null,
    primary key (id)
);
create table users_roles
(
    role_id uuid not null,
    user_id uuid not null,
    primary key (role_id, user_id)
);
alter table if exists user_authentication_providers
    add constraint fk_UserAuthenticationProviders_Users foreign key (user_id) references users;
alter table if exists users_roles
    add constraint fk_UserRoles_UsersRoles foreign key (role_id) references user_roles;
alter table if exists users_roles
    add constraint fk_UsersRoles_Users foreign key (user_id) references users;