package com.gibraltar.serviceImpl.auth;

import com.anubis.sso.AuthGrpc;
import com.anubis.sso.AuthResult;
import com.anubis.sso.TokenInfo;
import com.gibraltar.grpc.GrpcStubProvider;
import com.gibraltar.service.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service("auth")
public class AuthServiceImpl implements AuthService {

    private static final Logger logger= LoggerFactory.getLogger(AuthServiceImpl.class);
    @Resource
    GrpcStubProvider grpcStubProvider;


    public boolean auth(HttpServletRequest request) {
        //check if there has token
        String token=getTokenFromCookie(request);

        logger.info("the token is {}",token);

        if(token.equals("")){
            return false;
        }else{
            //get stub
            TokenInfo tokenInfo=TokenInfo.newBuilder().setToken(token).build();

            AuthGrpc.AuthBlockingStub stub=grpcStubProvider.getStub();

            AuthResult authResult = stub.auth(tokenInfo);

            return authResult.getIsAuthOK();
        }
    }



    private String getTokenFromCookie(HttpServletRequest request){
        Cookie[] cookies=request.getCookies();

        if(cookies==null){
            return "";
        }

        for (Cookie c:cookies) {
            if(c.getName().equals("token")){
                return c.getValue();
            }
        }

        return "";
    }
}
