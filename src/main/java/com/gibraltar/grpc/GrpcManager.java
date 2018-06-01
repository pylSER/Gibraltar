package com.gibraltar.grpc;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Data
@Component
@Order(1)
public class GrpcManager implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger= LoggerFactory.getLogger(GrpcManager.class);

    @Value("${grpc.serverIP}")
    private String serverIP;

    @Value("${grpc.serverPort}")
    private String portStr;

    @Value("${grpc.expiredMinutes}")
    private String expiredMinutesStr;


    private int port;

    private int expiredMins;


    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        try {
            this.port=Integer.parseInt(portStr);
            this.expiredMins=Integer.parseInt(expiredMinutesStr);

            logger.info("grpc config:ip:{},port:{}",serverIP,port);
        } catch (Exception e) {
            logger.error("reading grpc config failed");
            e.printStackTrace();
        }
    }
}
