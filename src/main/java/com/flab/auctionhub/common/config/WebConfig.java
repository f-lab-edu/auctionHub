package com.flab.auctionhub.common.config;

import com.flab.auctionhub.common.interceptor.AdminCheckInterceptor;
import com.flab.auctionhub.common.interceptor.LoginCheckInterceptor;
import com.flab.auctionhub.common.interceptor.SellerCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/", "/login", "/logout", "/users", "/users/check-duplication");

        registry.addInterceptor(new SellerCheckInterceptor())
            .order(2)
            .addPathPatterns("/products");

        registry.addInterceptor(new AdminCheckInterceptor())
            .order(3)
            .addPathPatterns("/category");
    }


}
