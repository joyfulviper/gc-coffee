package com.prgrms.gccoffee.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocumentController {

	@GetMapping("/api/docs")
    public String redirectSwagger() {
    	return "redirect:/swagger-ui/index.html";
    }
}