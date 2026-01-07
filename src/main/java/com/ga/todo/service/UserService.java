package com.ga.todo.service;

import com.ga.todo.exception.InformationExistException;
import com.ga.todo.model.User;
import com.ga.todo.model.requestpackage.LoginRequest;
import com.ga.todo.model.response.LoginResponse;
import com.ga.todo.repository.UserRepository;
import com.ga.todo.security.JWTUtils;
import com.ga.todo.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private MyUserDetails myUserDetails;

    // @Lazy: won't initialize the instance unless its required.
    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, JWTUtils jwtUtils,
                       @Lazy AuthenticationManager authenticationManager,
                       @Lazy MyUserDetails myUserDetails) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.myUserDetails = myUserDetails;
    }


    // Create User
    public User createUser(User userObject) {
        System.out.println("Service Calling createUser ==>");

        if(!userRepository.existsByEmailAddress(userObject.getEmailAddress())) {
            userObject.setPassword(passwordEncoder.encode(userObject.getPassword()));
            return userRepository.save(userObject);
        } else {
            throw new InformationExistException("User with email address " + userObject.getEmailAddress() + " already exists");
        }

    }

    // Find User by Email Address
    public User findUserByEmailAddress(String email) {
        return userRepository.findUserByEmailAddress(email);
    }

    // Login Service
    public ResponseEntity<?> LoginUser(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        );

        try {

            // Main Login
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword())
            );

            // context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // user details
            myUserDetails = (MyUserDetails) authentication.getPrincipal();

            // generate JWT token from myUserDetails
            final String JWT = jwtUtils.generateJwtToken(myUserDetails);

            // return the response entity ok with the LoginResponse using JWT.
            return ResponseEntity.ok(new LoginResponse(JWT));


        } catch(Exception e){
            return ResponseEntity.ok(new LoginResponse("Error: Username or Password is incorrect."));
        }


    }





}
