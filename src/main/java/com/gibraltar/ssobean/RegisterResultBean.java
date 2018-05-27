package com.gibraltar.ssobean;

import lombok.Data;

@Data
public class RegisterResultBean {
    private boolean isRegOK;
    private boolean isUserExists;
    private boolean isPwdTooEasy;
}
