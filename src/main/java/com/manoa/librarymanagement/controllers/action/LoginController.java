package com.manoa.librarymanagement.controllers.action;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/home")
    public String homeRedirect(Authentication auth, HttpSession session, Model model) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_BIBLIOTHECAIRE"))) {
            return "redirect:/admin/dashboard";
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADHERENT"))) {
            return "redirect:/user/dashboard";
        }
        return "redirect:/login";
    }
}
