package com.example.authserver.repository;

import com.example.authserver.entity.Endpoint;
import com.example.authserver.entity.Role;
import com.example.authserver.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String SELECT_ALL_BY_NAME =
            "select\n" +
                    "\t-- users\n" +
                    "\tu.id as user_id,\n" +
                    "\tu.name as user_name,\n" +
                    "\tu.\"password\" as user_password,\n" +
                    "\tu.creation_date as user_creation_date,\n" +
                    "\tu.update_date as user_update_date,\n" +
                    "\tu.\"locked\" as user_locked,\n" +
                    "\tu.access_token_ttl as user_access_token_ttl,\n" +
                    "\tu.refresh_token_ttl as user_refresh_token_ttl,\n" +
                    "\t\n" +
                    "\t-- roles\n" +
                    "\tr.id as role_id,\n" +
                    "\tr.name as role_name,\n" +
                    "\tr.creation_date as role_creation_date,\n" +
                    "\tr.update_date as role_update_date,\n" +
                    "\tr.\"locked\" as role_locked,\n" +
                    "\t\n" +
                    "\t-- endpoints\n" +
                    "\te.id as endpoint_id,\n" +
                    "\te.\"method\" as endpoint_method,\n" +
                    "\te.\"path\" as endpoint_path,\n" +
                    "\te.service as endpoint_service\n" +
                    "from auth_server.users u\n" +
                    "join auth_server.users_roles ur on ur.user_id = u.id\n" +
                    "join auth_server.roles r on r.id = ur.role_id\n" +
                    "join auth_server.roles_endpoints re on re.role_id = r.id\n" +
                    "join auth_server.endpoints e on e.id = re.endpoint_id\n" +
                    "where u.name = ?";

    private final JdbcTemplate jdbcTemplate;

    public User findByName(String name) {
        return jdbcTemplate.query(
                SELECT_ALL_BY_NAME,
                this::extractData,
                name
        );
    }

    private User extractData(ResultSet rs) throws SQLException {
        User user = null;
        Map<Integer, Role> roles = new HashMap<>();
        while (rs.next()) {
            if (user == null) {
                user = User.builder()
                        .id(rs.getInt("user_id"))
                        .name(rs.getString("user_name"))
                        .password(rs.getString("user_password"))
                        .creationDate(rs.getDate("user_creation_date"))
                        .updateDate(rs.getDate("user_update_date"))
                        .locked(rs.getBoolean("user_locked"))
                        .accessTokenTtl(rs.getObject("user_access_token_ttl", Integer.class))
                        .refreshTokenTtl(rs.getObject("user_refresh_token_ttl", Integer.class))
                        .build();
            }

            int roleId = rs.getInt("role_id");
            Role role;
            if (!roles.containsKey(roleId)) {
                HashSet<Endpoint> endpoints = new HashSet<>();
                role = Role.builder()
                        .id(roleId)
                        .name(rs.getString("role_name"))
                        .creationDate(rs.getDate("role_creation_date"))
                        .updateDate(rs.getDate("role_update_date"))
                        .locked(rs.getBoolean("role_locked"))
                        .endpoints(endpoints)
                        .build();
                roles.put(roleId, role);
            } else {
                role = roles.get(roleId);
            }

            Endpoint endpoint = Endpoint.builder()
                    .id(rs.getInt("endpoint_id"))
                    .method(rs.getString("endpoint_method"))
                    .path(rs.getString("endpoint_path"))
                    .service(rs.getString("endpoint_service"))
                    .build();
            role.getEndpoints().add(endpoint);
        }

        user.setRoles(new HashSet<>(roles.values()));

        return user;
    }

}
