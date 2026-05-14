package com.expense.tracker.service;

import com.expense.tracker.entities.UserInfo;
import com.expense.tracker.eventproducer.UserInfoProducer;
import com.expense.tracker.models.UserInfoDto;
import com.expense.tracker.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoProducer userInfoProducer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByUsername(username);
        if(userInfo == null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new CustomeUserDetail(userInfo);
    }

    public UserInfo checkIfUserAlreadyExist(UserInfo userInfo){
        return userInfoRepository.findByUsername(userInfo.getUsername());
    }

    public boolean signUp(UserInfoDto userInfo){
        try {
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            if (checkIfUserAlreadyExist(userInfo) != null) {
                throw new RuntimeException("User already exist with username: " + userInfo.getUsername());
            }
            UserInfo user = UserInfo.builder()
                    .password(userInfo.getPassword())
                    .username(userInfo.getUsername())
                    .roles(new HashSet<>())
                    .build();
            userInfoRepository.save(user);
            System.out.println("User signed up successfully: " + user.getUsername());
            userInfoProducer.sendEventToKafka(userInfo);
//            System.out.println("User signed up successfully: );
            return true;
        }catch (Exception e) {
            System.out.println("Error while signing up: " + e.getMessage());
            return false;
        }
     }
}
