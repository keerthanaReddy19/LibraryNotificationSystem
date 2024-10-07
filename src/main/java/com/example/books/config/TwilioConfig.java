package com.example.books.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String sid;
    @Value("${twilio.phone.number}")
    private String phone_number;

    public String getPhone_number() {
        return phone_number;
    }

    @Value("${twilio.auth.token}")
    private String auth_token;

    @PostConstruct
    public void twilioInit() {
        Twilio.init(sid, auth_token);
    }

}
