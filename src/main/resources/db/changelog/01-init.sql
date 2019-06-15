--liquibase formatted sql

--changeset rynffoll:01-init--create-auth_server-schema
create schema auth_server;

comment on schema auth_server is 'Auth Server';

--changeset rynffoll:01-init--create-users-table
create table auth_server.users
(
    id                serial primary key,
    name              varchar unique not null,
    password          varchar        not null,
    creation_date     timestamp      not null default now(),
    update_date       timestamp      not null default now(),
    locked            boolean        not null default false,
    access_token_ttl  integer,
    refresh_token_ttl integer
);

comment on table auth_server.users is 'Users';
comment on column auth_server.users.id is 'ID';
comment on column auth_server.users.name is 'Name';
comment on column auth_server.users.password is 'Password hash';
comment on column auth_server.users.creation_date is 'Creation date';
comment on column auth_server.users.update_date is 'Update date';
comment on column auth_server.users.locked is 'Locked';
comment on column auth_server.users.access_token_ttl is 'Access token TTL';
comment on column auth_server.users.refresh_token_ttl is 'Refresh token TTL';

--changeset rynffoll:01-init--create-roles-table
create table auth_server.roles
(
    id            serial primary key,
    name          varchar unique not null,
    creation_date timestamp      not null default now(),
    update_date   timestamp      not null default now(),
    locked        boolean        not null default false
);

comment on table auth_server.roles is 'Roles';
comment on column auth_server.roles.id is 'ID';
comment on column auth_server.roles.name is 'Name';
comment on column auth_server.roles.creation_date is 'Creation date';
comment on column auth_server.roles.update_date is 'Update date';
comment on column auth_server.roles.locked is 'Locked';

--changeset rynffoll:01-init--create-endpoints-table
create table auth_server.endpoints
(
    id      serial primary key,
    method  varchar not null,
    path    varchar not null,
    service varchar not null
);

comment on table auth_server.endpoints is 'Endpoints';
comment on column auth_server.endpoints.id is 'ID';
comment on column auth_server.endpoints.method is 'HTTP Method (regexp)';
comment on column auth_server.endpoints.path is 'Path (regexp)';
comment on column auth_server.endpoints.service is 'Service Name in Service Discovery';

--changeset rynffoll:01-init--create-users_roles-table
create table auth_server.users_roles
(
    user_id integer references auth_server.users (id),
    role_id integer references auth_server.roles (id)
);

comment on table auth_server.users_roles is 'Users - Roles';
comment on column auth_server.users_roles.user_id is 'User ID';
comment on column auth_server.users_roles.role_id is 'Role ID';

--changeset rynffoll:01-init--create-roles_endpoint-table
create table auth_server.roles_endpoints
(
    role_id     integer references auth_server.roles (id),
    endpoint_id integer references auth_server.endpoints (id)
);

comment on table auth_server.roles_endpoints is 'Roles - Endpoints';
comment on column auth_server.roles_endpoints.role_id is 'Role ID';
comment on column auth_server.roles_endpoints.endpoint_id is 'Endpoint ID';
