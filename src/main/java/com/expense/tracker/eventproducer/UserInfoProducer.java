package com.expense.tracker.eventproducer;

import com.expense.tracker.models.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoProducer {
    private final KafkaTemplate<String, UserInfoDto> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String KAFAK_TOPIC;

    @Autowired
    public UserInfoProducer(KafkaTemplate<String, UserInfoDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafka(UserInfoDto userInfoDto){
        Message<UserInfoDto> userInfoDtoMessage = MessageBuilder.withPayload(userInfoDto)
                .setHeader(KafkaHeaders.TOPIC, KAFAK_TOPIC).build();
        kafkaTemplate.send(userInfoDtoMessage);
        System.out.println("topis is "+KAFAK_TOPIC+" and user info is "+userInfoDto.getUsername());
    }
}
