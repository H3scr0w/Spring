package com.saintgobain.dsi.website4sg.core.web.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Swagger redirection to swagger api documentation
 */
@Controller
@Profile("dev")
public class SwaggerController {

    /**
     * Index string.
     *
     * @return the string
     */
    @RequestMapping(value = "/")
    public String index() {
        return "redirect:swagger-ui.html";
    }
}
