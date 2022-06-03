package com.boyalong.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@SpringBootApplication
//使用@ServletComponentScan 注解后，
// 使用@WebServlet、@WebFilter、@WebListener标记的 Servlet、Filter、Listener 就可以自动注册到Servlet容器中，
// 无需其他代码。
@ServletComponentScan
@EnableCaching //开启spring cache注解缓存功能
public class ReggieTakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieTakeOutApplication.class, args);
        log.info("项目启动成功。。。");
    }

}
