package com.example.registerlogin.mappers;

import com.example.registerlogin.helpers.Global;
import com.example.registerlogin.model.Gender;
import com.example.registerlogin.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {

        String profileImage = resultSet.getString("pic") == null ?
                null : Global.PROFILE_DIR + resultSet.getString("pic");

        User user = new User.UserBuilder().
                firstName(resultSet.getString("first_name")).
                lastName(resultSet.getString("last_name")).
                phoneNumber(resultSet.getString("phone_number")).
                id(resultSet.getString("id")).
                email(resultSet.getString("id")).
                occupation(resultSet.getString("occupation")).
                address(resultSet.getString("address")).
                age(resultSet.getInt("age")).
                gender(Gender.valueOf(resultSet.getString("gender").toUpperCase())).
//                role(resultSet.getString("role")).
                pic(profileImage).build();

        return user;
    }
}
