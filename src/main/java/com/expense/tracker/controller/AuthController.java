package com.expense.tracker.controller;

import com.expense.tracker.entities.RefreshToken;
import com.expense.tracker.entities.UserInfo;
import com.expense.tracker.models.UserInfoDto;
import com.expense.tracker.response.JwtResponseDto;
import com.expense.tracker.service.JwtService;
import com.expense.tracker.service.RefreshTokenService;
import com.expense.tracker.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @PostMapping("/api/v1/signup")
    public ResponseEntity<?> signup(@RequestBody UserInfoDto userInfoDto){
        try{
            System.out.println("Received signup request for username: " + userInfoDto.getUsername());
            boolean flag = userDetailService.signUp(userInfoDto);
            if(!flag){
                return ResponseEntity.ok("User signed up successfully");
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.generateToken(userInfoDto.getUsername());

            return new ResponseEntity<>(JwtResponseDto.builder().accessToken(jwtToken)
                    .refreshToken(refreshToken.getToken())
                    .build(), HttpStatus.OK);


        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error occurred during signup: " + e.getMessage());
        }
    }
}
