package com.samsung.azasuguesthouse.common.config;

import com.samsung.azasuguesthouse.common.auth.AdminCheckInterceptor;
import com.samsung.azasuguesthouse.common.auth.AuthInfoResolver;
import com.samsung.azasuguesthouse.common.auth.SessionCheckInterceptor;
import com.samsung.azasuguesthouse.common.log.RequestLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestLogInterceptor requestLogInterceptor;
    private final SessionCheckInterceptor sessionCheckInterceptor;
    private final AdminCheckInterceptor adminCheckInterceptor;
    private final AuthInfoResolver authInfoResolver;

    public WebConfig(RequestLogInterceptor requestLogInterceptor,
                     SessionCheckInterceptor sessionCheckInterceptor,
                     AdminCheckInterceptor adminCheckInterceptor,
                     AuthInfoResolver authInfoResolver) {
        this.requestLogInterceptor = requestLogInterceptor;
        this.sessionCheckInterceptor = sessionCheckInterceptor;
        this.adminCheckInterceptor = adminCheckInterceptor;
        this.authInfoResolver = authInfoResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLogInterceptor)
                .addPathPatterns("/**")
                .order(1);
        registry.addInterceptor(sessionCheckInterceptor)
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/api/v1/auth/signup", "/api/v1/auth/login", "/api/v1/auth/my-info", "/api/v1/auth/change-pw")
                .order(2);
        registry.addInterceptor(adminCheckInterceptor)
                .addPathPatterns("/api/v1/admin/**")
                .order(3);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authInfoResolver);
    }
}