package com.expense.tracker.controller;

import com.expense.tracker.entities.RefreshToken;
import com.expense.tracker.request.AuthRequestDto;
import com.expense.tracker.request.RefreshTokenRequestDto;
import com.expense.tracker.response.JwtResponseDto;
import com.expense.tracker.service.JwtService;
import com.expense.tracker.service.RefreshTokenService;
import com.expense.tracker.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TokenController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth/v1/login")
    public ResponseEntity autheticateAndGetToken(@RequestBody AuthRequestDto authRequestDto){
        try{
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
            if(authenticate.isAuthenticated()) {
                String jwtToken = jwtService.generateToken(authRequestDto.getEmail());
                String refreshToken = refreshTokenService.createRefreshToken(authRequestDto.getEmail()).getToken();
                return new ResponseEntity(JwtResponseDto.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .build(),
                        HttpStatus.OK);
            } else {
                return ResponseEntity.status(401).body("Authentication failed");
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error occurred during authentication: " + e.getMessage());
        }
    }

    @PostMapping("/auth/v1/refreshToken")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto){
        return refreshTokenService.findByToken(refreshTokenRequestDto.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String token = jwtService.generateToken(userInfo.getUsername());
                    return JwtResponseDto.builder()
                            .accessToken(token)
                            .refreshToken(refreshTokenRequestDto.getRefreshToken())
                            .build();
                }).orElseThrow(()-> new RuntimeException("Invalid refresh token: " + refreshTokenRequestDto.getRefreshToken()));

    }

}
