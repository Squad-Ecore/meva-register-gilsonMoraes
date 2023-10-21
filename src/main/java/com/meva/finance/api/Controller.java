
package com.meva.finance.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Controller {

    @GetMapping("/teste")
    private String teste() throws Exception {
        log.info("OK");
        return "ESTÁ TUDO FUNCIONANDO";


    }

}
