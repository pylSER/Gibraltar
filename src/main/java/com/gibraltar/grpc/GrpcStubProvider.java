package com.gibraltar.grpc;


import com.anubis.sso.AuthGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Order(2)
public class GrpcStubProvider implements ApplicationListener<ContextRefreshedEvent> {
    private static AuthGrpc.AuthBlockingStub stub;

    @Resource
    private GrpcManager grpcManager;



    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        String serverIpAndPort=grpcManager.getServerIP()+":"+grpcManager.getPort();
        final ManagedChannel channel= ManagedChannelBuilder.forTarget(serverIpAndPort).usePlaintext().build();
        stub=AuthGrpc.newBlockingStub(channel);
    }


    public AuthGrpc.AuthBlockingStub getStub(){
        return stub;
    }
}
