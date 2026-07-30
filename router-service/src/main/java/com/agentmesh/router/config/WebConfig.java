package com.agentmesh.router.config;

import com.agentmesh.router.x402.middleware.X402PaymentInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final X402PaymentInterceptor paymentInterceptor;

    public WebConfig(X402PaymentInterceptor paymentInterceptor) {
        this.paymentInterceptor = paymentInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(paymentInterceptor)
                .addPathPatterns("/api/execution/start", "/api/workflows/execute", "/api/workflows/run");
    }
}
