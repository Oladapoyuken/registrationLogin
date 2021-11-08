package com.example.registerlogin.mappers;

import com.example.registerlogin.model.User;
import com.example.registerlogin.model.UserLogin;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLoginMapper implements RowMapper {
    @Override
    public UserLogin mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserLogin(
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("role")
        );
    }
}
