package com.example.registerlogin.service;


import com.example.registerlogin.dao.EmailDao;
import com.example.registerlogin.dao.UserDao;
import com.example.registerlogin.helpers.Global;
import com.example.registerlogin.model.*;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class UserService implements UserDetailsService {

    private final UserDao userDao;

    private final EmailDao emailDao;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(@Qualifier("online") UserDao userDao, EmailDao emailDao, BCryptPasswordEncoder passwordEncoder)
    {
        this.userDao = userDao;
        this.emailDao = emailDao;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserLogin> user = userDao.getUserLogin(email);
        if(user.isPresent()){
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.get().getRole()));

            return new org.springframework.security.core.userdetails.User(
                    user.get().getEmail(),
                    user.get().getPassword(),
                    authorities
            );
        }
        else{
            throw new UsernameNotFoundException("Sorry, invalid credentials");
        }

    }



    public User confirmLogin(UserLogin user){

        return userDao.confirmLogin(user);
    }

    public List<User> getUsers(){

        return userDao.getUsers();
    }

    public User getUser(String email){

        return userDao.getUser(email);

    }

    public Response createUser(User user) throws MessagingException {
        User newUser = new User.UserBuilder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
//                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber()).build();

        String token = userDao.createUser(newUser);

        if(!token.equals("exist")) {

            String subject = "Registration confirmation";
            String body = "http://localhost:8080/api/confirm?token=" + token;
            Mail mail = new Mail(user.getEmail(), subject, body);
            emailDao.sendMail(mail);

            return new Response("success", "Please check your mail box and follow the link provided to verify your account");

        }
        return new Response("error", "Sorry, a user with inserted email already existed");
    }

    public Response updateUser(User user){
        if(userDao.updateUser(user) == 1)
            return new Response("success", "User profile updated successfully");

        return new Response("error", "Sorry, an error occurred while updating profile");
    }

    public Response updateUserRole(User user){
        if(userDao.updateUserRole(user) == 1)
            return new Response("success", "User profile updated successfully");

        return new Response("error", "Sorry, an error occurred while updating profile");
    }

    public String confirmEmail(String token){
        return userDao.verifyAccount(token);

    }

    public Response updateImage(MultipartFile file, String email) throws IOException {

        if(file.isEmpty()){
            return new Response("error", "Sorry, cannot upload an empty file [" +file.getSize()+ "]");
        }

        if(!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType()).contains(file.getContentType())){
            return new Response("error", "Sorry, File must be an image");
        }
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'));

        RandomStringGenerator generator = new RandomStringGenerator.Builder().
                withinRange('a', 'z').build();
        String imageId = generator.generate(25) + ext;

        File convertFile = new File(Global.PROFILE_DIR + imageId);
        convertFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convertFile);
        fos.write(file.getBytes());
        fos.close();

        User oldUser = userDao.getUser(email);
        String oldProfile = oldUser == null ? null : oldUser.getPic();

        User user = new User.UserBuilder().
                email(email).
                pic(imageId).build();

        if(userDao.updateImage(user) == 1){
            if(oldProfile != null){
                var tempFile = new File(oldProfile);
                if(tempFile.exists()) {
                    if (!tempFile.delete())
                        throw new IOException("Failed to delete file in " + oldProfile);
                }
            }
            return new Response("success", "Image uploaded successfully");
        }
        return new Response("error", "Sorry an error occurred while saving image");
    }

    public String getImage(String email){
        User user = userDao.getUser(email);
        if (user != null) {
            if (user.getPic() != null) {
                return user.getPic();
            }
        }
        return Global.PROFILE_DIR + "avater.jpg";
    }

    public Response forgetPassword(String email){
        String newPassword = generatePassword();
        int res = userDao.getNewPassword(email, passwordEncoder.encode(newPassword));
        if(res == 1){
            String subject = "Password Reset";
            String msg = "Hello, your new password is given as " + newPassword;
            emailDao.sendMail(new Mail(email, subject, msg));
            return new Response("success", "Your new password has been sent to your email");
        }
        return new Response("failure", "Sorry, could not reset your password");
    }

    private String generatePassword(){
        RandomStringGenerator generator = new RandomStringGenerator.Builder().
                withinRange('0', '9').build();
        return generator.generate(4);
    }



}
