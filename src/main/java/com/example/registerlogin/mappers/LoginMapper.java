package com.example.registerlogin.mappers;

import com.example.registerlogin.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginMapper implements RowMapper {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return  new User.UserBuilder().
                email(resultSet.getString("email")).
                password(resultSet.getString("password")).
                active(resultSet.getBoolean("active")).
                token(resultSet.getString("token")).
                expiredTime(resultSet.getString("expire_time")).
                dateCreated(resultSet.getString("date_created")).
                build();
    }
}
