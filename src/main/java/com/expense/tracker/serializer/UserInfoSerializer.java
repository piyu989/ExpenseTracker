package com.expense.tracker.serializer;

import com.expense.tracker.models.UserInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserInfoSerializer implements Serializer<UserInfoDto> {

    @Override
    public byte[] serialize(String s, UserInfoDto userInfoDto) {
        byte[] bytes = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            bytes = objectMapper.writeValueAsString(userInfoDto).getBytes();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return bytes;
    }
}
