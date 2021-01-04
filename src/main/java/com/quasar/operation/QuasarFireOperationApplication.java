package com.quasar.operation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class QuasarFireOperationApplication {

    public static final String APP_TIMEZONE = "GMT-5:00";

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(APP_TIMEZONE));
    }

    public static void main(String[] args) {
        SpringApplication.run(QuasarFireOperationApplication.class, args);
    }

}
