--liquibase formatted sql

--changeset rynffoll:02-data
insert into auth_server.users
(
    id,
    name,
    password,
    creation_date,
    update_date,
    locked,
    access_token_ttl,
    refresh_token_ttl
)
values
(
    0,
    'client',
    '{bcrypt}$2a$04$/0F6hIVlb9vgUj/roXE5N.Qhk7cD9deBZZ1TcCT3gxYqzVgY9dQLu', -- secret
    default,
    default,
    default,
    null,
    null
);

insert into auth_server.roles
(
    id,
    name,
    creation_date,
    update_date,
    locked
)
values
(
    0,
    'ROLE_USER',
    now(),
    now(),
    default
);

insert into auth_server.endpoints
(
    id,
    method,
    path,
    service
)
values
(
    0,
    '*',
    '/api/v1/test',
    'api-server'
);

insert into auth_server.users_roles (user_id, role_id) values (0, 0);
insert into auth_server.roles_endpoints (role_id, endpoint_id) values (0, 0);
