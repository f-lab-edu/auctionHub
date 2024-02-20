package com.flab.auctionhub.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // 스프링 애플리케이션 컨텍스트에 빈(Bean) 구성 정보를 제공하는 데 사용되는 어노테이션
public class SecurityConfig {

    @Bean // Spring 프레임워크에서 IoC 컨테이너에게 해당 메서드가 반환하는 객체를 Bean으로 등록하도록 지시하는 어노테이션
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
