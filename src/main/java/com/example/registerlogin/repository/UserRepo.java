package com.example.registerlogin.repository;

import com.example.registerlogin.dao.EmailDao;
import com.example.registerlogin.dao.UserDao;
import com.example.registerlogin.helpers.GetDateTime;
import com.example.registerlogin.mappers.LoginMapper;
import com.example.registerlogin.mappers.UserLoginMapper;
import com.example.registerlogin.model.User;
import com.example.registerlogin.model.UserLogin;
import com.example.registerlogin.mappers.UserMapper;
import com.example.registerlogin.model.UserRegistration;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Repository("online")
public class UserRepo implements UserDao {



    private JdbcTemplate jdbcTemplate;

    private EmailDao emailDao;

    @Autowired
    public UserRepo(JdbcTemplate jdbcTemplate, EmailDao emailDao) {

        this.jdbcTemplate = jdbcTemplate;

        this.emailDao = emailDao;
    }

    @Override
    public User confirmLogin(UserLogin user) {

        if(isLoginValid(user.getEmail(), user.getPassword())){
            return getUser(user.getEmail());
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        final String sql = "SELECT * FROM user_tbl";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public String createUser(User user) {
        final String token = getRandId();

        if(!isEmailExists(user.getEmail())){

            final String sql_user = "INSERT INTO user_tbl (id, first_name, last_name, phone_number, gender, date)" +
                    " VALUES (?,?,?,?,?::gender,?)";
            jdbcTemplate.update(sql_user,
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPhoneNumber(),
                    user.getGender().name().toUpperCase(),
                    LocalDateTime.now()
            );

            final String sql_login = "INSERT INTO login_tbl (" +
                    "email, password, active, role, expire_time, date_created, token) " +
                    "VALUES (?,?,?,?,?,?,?)";

            jdbcTemplate.update(sql_login,
                    user.getEmail(),
                    user.getPassword(),
                    false,
                    "ROLE_USER",
                    new GetDateTime().getDateTime(15),
                    LocalDateTime.now(),
                    token
            );
            return token;
        }
        else{
            final String sql = "SELECT * FROM login_tbl WHERE email = ?";
            List<User> users = jdbcTemplate.query(sql,
                    preparedStatement -> preparedStatement.setString(1, user.getEmail()),
                    new LoginMapper());

            if(!users.isEmpty()){
                if(!users.get(0).getActive()){
                    final String sql_update = "UPDATE login_tbl SET expire_time = ?, token = ? WHERE email = ?";

                    jdbcTemplate.update(sql_update,
                            new GetDateTime().getDateTime(15),
                            token,
                            user.getEmail());

                    return token;
                }
            }
        }
        return "exist";
    }

    private String getRandId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public int updateUser(User user) {
        final String sql = "UPDATE user_tbl SET first_name = ?," + "last_name = ?, " +
                "occupation = ?, phone_number = ?, address = ?, age = ?, gender = ?::gender " +
                "WHERE id = ?";
        jdbcTemplate.update(sql,
                user.getFirstName(),
                user.getLastName(),
                user.getOccupation(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getAge(),
                user.getGender().name().toUpperCase(),
                user.getEmail()
        );
        return 1;
    }

    @Override
    public int updateUserRole(User user) {
        final String sql = "UPDATE login_tbl SET role = ? WHERE email = ?";
        return jdbcTemplate.update(sql,
                user.getRole(),
                user.getEmail()
        );

    }

    @Override
    public User getUser(String email) {
        final String sql = "SELECT * FROM user_tbl WHERE id = ? LIMIT 1";
        List<User> users = jdbcTemplate.query(sql,
                preparedStatement -> preparedStatement.setString(1, email),
                new UserMapper());
        System.out.println(users);

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public String verifyAccount(String token) {
        boolean active = true;
        final String sql = "SELECT * FROM login_tbl WHERE token = ?";
        List<User> users = jdbcTemplate.query(sql,
                preparedStatement -> preparedStatement.setString(1, token),
                new LoginMapper());

        if(users.isEmpty())
            return "Sorry, was unable to complete the email verification";

        String timed = users.get(0).getExpiredTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime expiring = LocalDateTime.parse(timed, formatter);
        if(expiring.isBefore(LocalDateTime.now())){
            return "sorry, token has expired, please register again";
        }
        final String sql_update = "UPDATE login_tbl SET active = ? WHERE email = ?";
        jdbcTemplate.update(sql_update,
                true,
                users.get(0).getEmail());
        return "Thank you for registering, your account has been activated! you can now login";
    }

    @Override
    public int updateImage(User user) {
        if(isEmailExists(user.getEmail())){
            final String sql = "UPDATE user_tbl SET pic = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    user.getPic(),
                    user.getEmail()
            );
            return 1;
        }
        return 0;
    }

    @Override
    public int getNewPassword(String email, String newPassword) {
        if(isEmailExists(email)){
            final String sql = "UPDATE login_tbl SET password = ? WHERE email = ?";
            jdbcTemplate.update(sql,
                    newPassword,
                    email
            );
            return 1;
        }
        return 0;
    }

    private boolean isEmailExists(String email){
        String sql = "SELECT EXISTS (SELECT 1 FROM user_tbl WHERE id = ?)";
        return jdbcTemplate.query(sql,
                preparedStatement -> preparedStatement.setString(1, email),
                (resultSet, i) -> resultSet.getBoolean(1)).get(0);
    }

    private boolean isLoginValid(String email, String password){
        String sql = "SELECT EXISTS (SELECT 1 FROM login_tbl WHERE email = ? AND password = ? AND active = ?)";
        return jdbcTemplate.query(sql,
                preparedStatement -> {
                    preparedStatement.setString(1, email);
                    preparedStatement.setString(2, password);
                    preparedStatement.setBoolean(3, true);
                },
                (resultSet, i) -> resultSet.getBoolean(1)).get(0);
    }

    @Override
    public Optional<UserLogin> getUserLogin(String email) {
        final String sql = "SELECT * FROM login_tbl WHERE email = ? AND active = ?";
        List<UserLogin> users = jdbcTemplate.query(sql,
                preparedStatement -> {
                    preparedStatement.setString(1, email);
                    preparedStatement.setBoolean(2, true);
                },
                new UserLoginMapper());

        return users.isEmpty() ? Optional.ofNullable(null) : Optional.ofNullable(users.get(0));
    }


}
