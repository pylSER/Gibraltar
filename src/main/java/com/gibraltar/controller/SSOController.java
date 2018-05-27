package com.gibraltar.controller;

import com.anubis.sso.*;
import com.gibraltar.grpc.GrpcManager;
import com.gibraltar.grpc.GrpcStubProvider;
import com.gibraltar.ssobean.LoginUserInfo;
import com.gibraltar.ssobean.RegisterResultBean;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class SSOController{
    private static final Logger logger= LoggerFactory.getLogger(SSOController.class);

    @Resource
    GrpcStubProvider grpcStubProvider;

    @Resource
    GrpcManager grpcManager;


    @RequestMapping(path="/login",method= RequestMethod.POST)
    public boolean doLogin(@RequestBody LoginUserInfo userInfo, HttpServletResponse response) {
        //get stub
        AuthGrpc.AuthBlockingStub stub=grpcStubProvider.getStub();

        UserInfo grpcUserinfo=UserInfo.newBuilder().setPassword(userInfo.getPassword()).setUserName(userInfo.getUserName()).build();

        LoginResult result=stub.login(grpcUserinfo);

        if(result.getIsLoginOK()){
            response.addCookie(generateLoginCookie(result.getToken()));
        }


        return result.getIsLoginOK();
    }

    @RequestMapping(path="/register",method= RequestMethod.POST)
    public RegisterResultBean doReg(@RequestBody LoginUserInfo userInfo) {
        //get stub
        AuthGrpc.AuthBlockingStub stub=grpcStubProvider.getStub();
        UserInfo grpcUserinfo=UserInfo.newBuilder().setPassword(userInfo.getPassword()).setUserName(userInfo.getUserName()).build();

        RegResult regResult= stub.register(grpcUserinfo);

        RegisterResultBean bean=new RegisterResultBean();

        bean.setRegOK(regResult.getIsRegOK());
        bean.setPwdTooEasy(regResult.getIsPwdTooEasy());
        bean.setUserExists(regResult.getIsUserExists());

        return bean;
    }


    private Cookie generateLoginCookie(String token){
        Cookie loginCookie=new Cookie("token",token);
        loginCookie.setMaxAge(grpcManager.getExpiredMins()*60*1000);
        loginCookie.setPath("/");

        return loginCookie;
    }







}
