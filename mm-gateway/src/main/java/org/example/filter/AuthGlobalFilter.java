package org.example.filter;

import lombok.RequiredArgsConstructor;
import org.example.config.AuthProperties;
import org.example.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class AuthGlobalFilter implements GlobalFilter, Ordered {


    private final AuthProperties authProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Autowired
    private StringRedisTemplate stringRedisTemplate ;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取Request
        ServerHttpRequest request = exchange.getRequest();
        // 2.判断是否不需要拦截
        if(isExclude(request.getPath().toString())){
            // 无需拦截，直接放行
            return chain.filter(exchange);
        }
        // 3.获取请求头中的token
        String token = null;
        List<String> headers = request.getHeaders().get("Authorization");

        if (headers != null && !headers.isEmpty()) {
            token = headers.get(0);
        }
        // 4.校验并解析token

        String username = null;
        try {
            if (token == null)
                throw new RuntimeException();
            ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
            String redistoken = stringStringValueOperations.get(token);
            if (redistoken == null)
                throw new RuntimeException("用户未登录");
            Map<String,Object> claims = JwtUtils.parseToken(token);
            username = (String) claims.get("username");
        } catch (Exception e) {
            // 如果无效，拦截
            ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(401);
            return response.setComplete();
        }


        String finalUsername = username;
        ServerWebExchange build = exchange.mutate()
                .request(builder -> builder.header("user-info", finalUsername))
                .build();
        // 6.放行
        return chain.filter(build);
    }

    private boolean isExclude(String antPath) {
        for (String pathPattern : authProperties.getExcludePaths()) {
            if(antPathMatcher.match(pathPattern, antPath)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
