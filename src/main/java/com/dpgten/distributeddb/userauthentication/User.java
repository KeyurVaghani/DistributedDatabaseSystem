package com.dpgten.distributeddb.userauthentication;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class User {
    private String username;

    private String password;

    private String securityQuestion;

    private String securityAnswer;

    private String loggedIn;

}
