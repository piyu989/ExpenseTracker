package com.expense.tracker.models;

import com.expense.tracker.entities.UserInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
//json parsing is like name_lastname
public class UserInfoDto extends UserInfo {
    private String name;
    private String phoneNo;
    private String lastName;
    @Email(message = "incorrect email format")
    private String email;
    private String password;
}
