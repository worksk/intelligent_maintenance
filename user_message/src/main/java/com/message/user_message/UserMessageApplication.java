package com.message.user_message;

import com.message.user_message.util.ClassUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class UserMessageApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(UserMessageApplication.class, args);
        ClassUtil.setApplicationContext(applicationContext);
    }

}
