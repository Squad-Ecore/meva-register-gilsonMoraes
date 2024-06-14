package com.meva.finance.configTest;

import com.meva.finance.config.SwaggerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springfox.documentation.spring.web.plugins.Docket;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SwaggerConfigTest {

    @Autowired
    private SwaggerConfig swaggerConfig;

    @Test
    public void testSwaggerConfig() {
        // Verifica se o bean Docket está sendo criado corretamente
        Docket docket = swaggerConfig.api();
        assertNotNull(docket);
    }
}
