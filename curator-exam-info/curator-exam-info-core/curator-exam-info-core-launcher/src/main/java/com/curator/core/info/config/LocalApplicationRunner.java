package com.curator.core.info.config;

import com.curator.common.util.Help;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Jun
 * @date 2021/4/16
 */
@Component
public class LocalApplicationRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext context;
    private final Environment environment;

    public LocalApplicationRunner(ConfigurableApplicationContext context, Environment environment) {
        this.context = context;
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (context.isActive()) {
            String banner = "-----------------------------------------\n" +
                    "服务启动成功，时间：" + Help.currentTimeToTxt() + "\n" +
                    "服务名称：" + environment.getProperty("spring.application.name") + "\n" +
                    "端口号：" + environment.getProperty("server.port") + "\n" +
                    "-----------------------------------------";
            System.out.println(banner);
        }
    }
}

