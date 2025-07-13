package com.manoa.librarymanagement.controllers.action;


import com.manoa.librarymanagement.models.action.Pret;
import com.manoa.librarymanagement.repositories.action.PretRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/prets")
@RequiredArgsConstructor
public class PretController {

    private final PretRepository pretRepository;

    @GetMapping
    public String listerPrets(Model model) {
        List<Pret> prets = pretRepository.findAll();
        model.addAttribute("prets", prets);
        return "admin/prets";
    }
}
