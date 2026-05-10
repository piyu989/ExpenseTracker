package com.expense.tracker.service;

import com.expense.tracker.entities.UserInfo;
import com.expense.tracker.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//@Service
public class CustomeUserDetail extends UserInfo implements UserDetails {
    private String username;
    private String password;
    Collection<? extends  GrantedAuthority> authorities;

//    @Autowired
    public CustomeUserDetail(UserInfo userInfo){
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for(UserRole role : userInfo.getRoles()){
            grantedAuthorityList.add((GrantedAuthority) role::getRole);
        }
        this.authorities = grantedAuthorityList;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}
