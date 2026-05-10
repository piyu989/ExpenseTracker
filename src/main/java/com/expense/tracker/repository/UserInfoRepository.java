package com.expense.tracker.repository;

import com.expense.tracker.entities.RefreshToken;
import com.expense.tracker.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    UserInfo findById(long id);
    UserInfo findByUsername(String username);
}
