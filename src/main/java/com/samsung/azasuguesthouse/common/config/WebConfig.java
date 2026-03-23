package com.samsung.azasuguesthouse.common.config;

import com.samsung.azasuguesthouse.common.auth.AuthInfoResolver;
import com.samsung.azasuguesthouse.common.auth.SessionCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SessionCheckInterceptor sessionCheckInterceptor;
    private final AuthInfoResolver authInfoResolver;

    public WebConfig(SessionCheckInterceptor sessionCheckInterceptor,
                     AuthInfoResolver authInfoResolver) {
        this.sessionCheckInterceptor = sessionCheckInterceptor;
        this.authInfoResolver = authInfoResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionCheckInterceptor)
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/api/v1/admin/rooms", "/api/v1/auth/signup", "/api/v1/auth/login", "/api/v1/auth/my-info", "/api/v1/auth/change-pw");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authInfoResolver);
    }
}