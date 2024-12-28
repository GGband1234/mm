package org.example.config;


import feign.Logger;
import feign.RequestInterceptor;

import org.example.utils.ThreadLocalUtil;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return template -> {
            // 获取登录用户
            String username = ThreadLocalUtil.get();
            if(username == null) {
                // 如果为空则直接跳过
                return;
            }
            // 如果不为空则放入请求头中，传递给下游微服务
            template.header("user-info", username);
        };
    }
}