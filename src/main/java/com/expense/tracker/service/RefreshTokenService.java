package com.expense.tracker.service;

import com.expense.tracker.entities.RefreshToken;
import com.expense.tracker.entities.UserInfo;
import com.expense.tracker.repository.RefreshTokenRepository;
import com.expense.tracker.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;


    public RefreshToken createRefreshToken(String username){
        UserInfo user = userInfoRepository.findByUsername(username);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(user)
                .expireDate(Instant.now().plusSeconds(343434) )
                .token(UUID.randomUUID().toString())
                .build();
        return  refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpireDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            System.out.println("token expired for this user "+token.getUserInfo().getUsername());
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> findByUserName(String username) {
        return refreshTokenRepository.findByUserInfoUsername(username);
    }

     public void deleteByUserId(long userId){
        refreshTokenRepository.delete(refreshTokenRepository.findById(userId).orElseThrow((
                ()-> new RuntimeException("User not found with id: " + userId))));
     }
}
