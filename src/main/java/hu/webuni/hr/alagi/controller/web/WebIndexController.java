package hu.webuni.hr.alagi.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebIndexController {
   @GetMapping("/")
   public String getIndexPage() {
      return "index";
   }
}
