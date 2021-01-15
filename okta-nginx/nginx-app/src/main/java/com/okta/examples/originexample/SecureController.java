package com.okta.examples.originexample;

import com.okta.examples.originexample.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Scope("request")
public class SecureController {

    private User user;

    public SecureController(User user) {
        this.user = user;
    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("user", user);
        return "authenticated";
    }

    @RequestMapping("/club_golfers")
    @PreAuthorize("hasAuthority('club_golfers')")
    public String club_golfers(Model model) {
        model.addAttribute("user", user);
        return "roles";
    }

    @RequestMapping("/major_winners")
    @PreAuthorize("hasAuthority('major_winners')")
    public String major_winners(Model model) {
        model.addAttribute("user", user);
        return "roles";
    }

    @RequestMapping("/403")
    public String error403() {
        return "403";
    }
}