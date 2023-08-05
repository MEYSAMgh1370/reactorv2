/*
package com.example.reactorv2.service;
//repository
public Flux<User> findUsersByRole(Role role) {
        return databaseClient.execute("SELECT u.* FROM user u JOIN user_role_mapping urm ON u.id = urm.user_id WHERE urm.role_id = :roleId")
        .bind("roleId", role.getId())
        .map(User::from)
        .all();
        }

public Flux<Role> findRolesByUser(User user) {
        return databaseClient.execute("SELECT r.* FROM role r JOIN user_role_mapping urm ON r.id = urm.role_id WHERE urm.user_id = :userId")
        .bind("userId", user.getId())
        .map(Role::from)
        .all();
        }
*/

