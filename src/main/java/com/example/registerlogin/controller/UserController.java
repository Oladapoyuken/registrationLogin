package com.example.registerlogin.controller;

import com.example.registerlogin.helpers.Global;
import com.example.registerlogin.model.Response;
import com.example.registerlogin.model.User;
import com.example.registerlogin.security.JwtTokenProcessor;
import com.example.registerlogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.header.Header;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


@RestController
@RequestMapping(path = "/api")
public class UserController {

    private final UserService userService;
//    private final AuthenticationManager authenticationManager;
//    private final JwtTokenProcessor jwtTokenProcessor;

    @Autowired
    public UserController(UserService userService){//, AuthenticationManager authenticationManager, JwtTokenProcessor jwtTokenProcessor) {

        this.userService = userService;
//        this.authenticationManager = authenticationManager;
//        this.jwtTokenProcessor = jwtTokenProcessor;
    }

    @PostMapping(value = "/register")
    private ResponseEntity<Object> register(@RequestBody User user) throws MessagingException {
        Response response = userService.createUser(user);
        if(response.getStatus().equals("success"))
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    private ResponseEntity<Object> updateUser(@RequestBody User user){
        Response response = userService.updateUser(user);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping(value = "/admin/userole")
    private ResponseEntity<Object> addRoleToUser(@RequestBody User user){
        Response response = userService.updateUserRole(user);

        if(response.equals("success")){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping(value = "/admin/users")
    private ResponseEntity<Object> getUsers(){

        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/user")
    private ResponseEntity<Object> getUser(@RequestParam("email") String email){
        var getUser = userService.getUser(email);
        if(getUser != null)
            return new ResponseEntity<>(getUser, HttpStatus.OK);
        return new ResponseEntity<>(new Response("failure", "User not found"), HttpStatus.OK);
    }


    @GetMapping(value = "/confirm")
    private ResponseEntity<Object> confirmEmail(@RequestParam("token") String token){

        return new ResponseEntity<>(userService.confirmEmail(token), HttpStatus.OK);
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file,
                                             @RequestParam("email") String email) throws IOException {
        Response response = userService.updateImage(file, email);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping(value = "/profile", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProfileImage(@RequestParam("email") String email) throws IOException{

        var profileImage = new File(userService.getImage(email));

        if(!profileImage.exists())
            profileImage = new File(Global.PROFILE_DIR + "avater.jpg");

        var imgFile = new FileInputStream(profileImage);
        byte[] bytes = StreamUtils.copyToByteArray(imgFile);
        imgFile.close();

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }

//    @PostMapping(value = "/login")
//    public ResponseEntity<Object> test(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
//        try{
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//        }catch (BadCredentialsException bce){
//            return ResponseEntity.ok(new Response("failure", "Sorry, invalid login credentials"));
//        }
//        final org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) userService.loadUserByUsername(username);
//
//        final var token = jwtTokenProcessor.encryptToken(user,request);
//
//        return ResponseEntity.ok(new Response("success", token.get("access_token")));
//    }

    @GetMapping(value = "forget")
    public ResponseEntity<Object> forget(@RequestParam("email") String email){
        Response response = userService.forgetPassword(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
