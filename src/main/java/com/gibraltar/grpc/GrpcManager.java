package com.gibraltar.grpc;


import com.amihaiemil.camel.Yaml;
import com.amihaiemil.camel.YamlMapping;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Data
@Component
public class GrpcManager implements ApplicationListener<ContextRefreshedEvent> {
    private static final String configFilePath="grpcConfig.yml";

    private static final Logger logger= LoggerFactory.getLogger(GrpcManager.class);

    private int port;
    private String serverIP;

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        File configFile=new File(GrpcManager.class.getResource("../../../").getPath()+configFilePath);
        YamlMapping yamlMapping = null;
        try {
            yamlMapping = Yaml.createYamlInput(configFile).readYamlMapping();
            String portStr=yamlMapping.string("serverPort");


            String serverIP=yamlMapping.string("serverIP");
            int port=Integer.parseInt(portStr);

            this.serverIP=serverIP;
            this.port=port;

            logger.info("grpc config:ip:{},port:{}",serverIP,port);
        } catch (IOException e) {
            logger.error("reading grpc config failed");
            e.printStackTrace();
        }
    }
}
