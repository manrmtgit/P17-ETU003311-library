package com.manoa.librarymanagement.controllers.init;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalMessageAdvice {

    @ModelAttribute
    public void addGlobalMessages(Model model) {
        model.addAttribute("message_global",
                "Bienvenue sur le portail de la bibliothèque");
    }
}