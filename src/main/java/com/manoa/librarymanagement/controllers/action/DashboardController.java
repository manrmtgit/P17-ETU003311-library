package com.manoa.librarymanagement.controllers.action;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("username", user.getUsername());
        return "dashboard/admin-dashboard";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("username", user.getUsername());
        return "dashboard/user-dashboard";
    }
}
