package com.gibraltar.grpc;


import com.amihaiemil.camel.Yaml;
import com.amihaiemil.camel.YamlMapping;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Data
@Component
@Order(1)
public class GrpcManager implements ApplicationListener<ContextRefreshedEvent> {
    private static final String configFilePath="grpcConfig.yml";

    private static final Logger logger= LoggerFactory.getLogger(GrpcManager.class);

    private int port;
    private String serverIP;

    private int expiredMins;


    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        File configFile=new File(GrpcManager.class.getResource("../../../").getPath()+configFilePath);
        YamlMapping yamlMapping = null;
        try {
            yamlMapping = Yaml.createYamlInput(configFile).readYamlMapping();
            String portStr=yamlMapping.string("serverPort");
            int port=Integer.parseInt(portStr);

            String serverIP=yamlMapping.string("serverIP");

            String expiredMinutesStr=yamlMapping.string("expiredMinutes");
            int expiredMinutes=Integer.parseInt(expiredMinutesStr);

            this.serverIP=serverIP;
            this.port=port;
            this.expiredMins=expiredMinutes;

            logger.info("grpc config:ip:{},port:{}",serverIP,port);
        } catch (IOException e) {
            logger.error("reading grpc config failed");
            e.printStackTrace();
        }
    }
}
