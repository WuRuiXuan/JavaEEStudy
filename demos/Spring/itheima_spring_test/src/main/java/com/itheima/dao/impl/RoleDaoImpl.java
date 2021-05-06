package com.itheima.dao.impl;

import com.itheima.dao.RoleDao;
import com.itheima.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("RoleDao")
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Role> findAll() {
        List<Role> roleList = jdbcTemplate.query("select * from sys_role", new BeanPropertyRowMapper<Role>(Role.class));
        return roleList;
    }

    public void save(Role role) {
        jdbcTemplate.update("insert into sys_role values(?, ?, ?)", null, role.getName(), role.getDesc());
    }

    public List<Role> findRolesByUserId(Long id) {
        List<Role> roleList = jdbcTemplate.query(
                "select * from sys_user_role ur, sys_role r where ur.roleId=r.id and ur.id=?",
                new BeanPropertyRowMapper<Role>(Role.class), id);
        return roleList;
    }
}
