package com.agentmesh.router.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI agentMeshOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AgentMesh Router Service API")
                        .description("AI Multi-Agent Service Router with Algorand Atomic Payments Foundation API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("AgentMesh Team")
                                .url("https://github.com/anuragpatil1729/algorand-smart-contract"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
