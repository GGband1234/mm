package org.example.interceptor;


import com.github.xiaoymin.knife4j.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.utils.ThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的用户信息
        String userInfo =request.getHeader("user-info");

        // 2.判断是否为空
        if (StrUtil.isNotBlank(userInfo) ){
            // 不为空，保存到ThreadLocal
            ThreadLocalUtil.set(userInfo);
        }
        // 3.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        ThreadLocalUtil.remove();
    }
}
