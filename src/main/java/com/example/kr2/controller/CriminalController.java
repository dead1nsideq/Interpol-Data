package com.example.kr2.controller;

import com.example.kr2.dto.CriminalDTO;
import com.example.kr2.dto.SortDTO;
import com.example.kr2.entety.Criminal;
import com.example.kr2.service.CriminalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@AllArgsConstructor
public class CriminalController {
    private final CriminalService criminalService;

    @GetMapping("/criminals")
    public String showCriminals(@ModelAttribute SortDTO sortDTO,Model model) {
        List<Criminal> criminals = criminalService.findCriminalsByInputParams(sortDTO);
        model.addAttribute("criminals", criminals);
        return "criminals";
    }

    @GetMapping("/addCriminal")
    public String addCriminalMenu() {
        return "addCriminal";
    }

    @PostMapping("/criminals/addCriminal")
    public String addCriminal(@ModelAttribute @Validated CriminalDTO criminal,
                              @RequestParam MultipartFile file) {
        criminalService.addCriminal(criminal,file);
        return "redirect:/criminals";
    }

    @GetMapping("/criminals/{id}")
    public String getCriminal(@PathVariable Long id,Model model) {
        CriminalDTO criminal = criminalService.getCriminalById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("criminal",criminal);
        return "criminal";
    }

    @GetMapping("/criminals/arrest/{id}")
    public String arrestCriminal(@PathVariable Long id) {
        criminalService.arrestCriminal(id);
        return "redirect:/criminals/{id}";
    }
    @GetMapping("/criminals/deleteCriminal/{id}")
    public String deleteCriminal(@PathVariable Long id) {
        if (criminalService.deleteCriminal(id)) {
            return "redirect:/criminals";
        } else {
            throw new NotFoundException();
        }
    }

    @GetMapping("/criminals/editCriminal/{id}")
    public String showEditCriminalForm(@PathVariable Long id, Model model) {
        CriminalDTO criminal = criminalService.getCriminalById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("criminal", criminal);
        model.addAttribute("selectedLanguages",criminal.getSelectedLanguages().split(","));
        model.addAttribute("selectedCriminalRecords",criminal.getSelectedCriminalRecords().split(","));
        return "editCriminal";
    }
    @PostMapping("/criminals/editCriminal/{id}")
    public String editCriminal(@PathVariable Long id, @ModelAttribute @Validated CriminalDTO criminalDto,
                               @RequestParam MultipartFile file) {
        criminalService.updateCriminalById(id,criminalDto,file);
        return "redirect:/criminals/{id}";
    }
}
