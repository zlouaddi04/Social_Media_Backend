package org.one.corporatesocialmediaapp_backend.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/welcome")
public class UserController {
    @GetMapping("")
    public String welcome() {
        return "welcome";
    }
}
