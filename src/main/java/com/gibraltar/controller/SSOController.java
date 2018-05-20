package com.gibraltar.controller;

import com.anubis.sso.AuthGrpc;
import com.anubis.sso.AuthOuterClass;
import com.anubis.sso.LoginResult;
import com.anubis.sso.UserInfo;
import com.gibraltar.grpc.GrpcManager;
import com.gibraltar.ssobean.LoginUserInfo;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SSOController{
    private static final Logger logger= LoggerFactory.getLogger(SSOController.class);

    @Resource
    private GrpcManager grpcManager;

    @RequestMapping(path="/login",method= RequestMethod.POST)
    public boolean doLogin(@RequestBody LoginUserInfo userInfo) {
        String serverIpAndPort=grpcManager.getServerIP()+":"+grpcManager.getPort();
        final ManagedChannel channel= ManagedChannelBuilder.forTarget(serverIpAndPort).usePlaintext().build();

        AuthGrpc.AuthBlockingStub stub=AuthGrpc.newBlockingStub(channel);
        UserInfo grpcUserinfo=UserInfo.newBuilder().setPassword(userInfo.getPassword()).setUserName(userInfo.getUserName()).build();

        LoginResult result=stub.login(grpcUserinfo);

        return result.getIsLoginOK();
    }






}
