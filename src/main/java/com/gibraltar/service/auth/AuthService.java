package com.gibraltar.service.auth;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    public boolean auth(HttpServletRequest request);
}
