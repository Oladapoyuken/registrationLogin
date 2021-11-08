package com.example.registerlogin.dao;

import com.example.registerlogin.model.User;
import com.example.registerlogin.model.UserLogin;
import com.example.registerlogin.model.UserRegistration;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User confirmLogin(UserLogin user);

    List<User> getUsers();

    String createUser(User user);

    int updateUser(User user);

    int updateUserRole(User user);

    User getUser(String email);

    Optional<UserLogin> getUserLogin(String email);

    String verifyAccount(String token);

    int updateImage(User user);

    int getNewPassword(String email, String newPassword);


}
