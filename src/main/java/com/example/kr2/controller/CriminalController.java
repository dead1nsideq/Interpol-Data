package com.example.kr2.controller;

import com.example.kr2.entety.*;
import com.example.kr2.service.CriminalService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class CriminalController {
    private final CriminalService criminalService;

    @GetMapping("/criminals")
    public String showCriminals(@RequestParam(required = false,defaultValue = " ") String inputCriminalFirstName,
                                @RequestParam(required = false,defaultValue = " ") String inputCriminalLastName,
                                @RequestParam(required = false,defaultValue = " ") String inputCriminalNickName,
                                @RequestParam(required = false,defaultValue = " ") String arrestedCriminals,
                                @RequestParam(required = false,defaultValue = " ") String criminalRecord,
                                @RequestParam(required = false,defaultValue = " ") String textInDossier,
                                @RequestParam(required = false,defaultValue = " ") String criminalGroupType,
                                @RequestParam(required = false,defaultValue = " ") String eyeColor,
                                @RequestParam(required = false,defaultValue = " ") String hairColor,
                                @RequestParam(required = false,defaultValue = " ") String sortBy,
                                @RequestParam(required = false, defaultValue = "false") boolean order,
                                Model model) {
        List<Criminal> criminals = criminalService.findCriminalsByInputParams(
                inputCriminalFirstName,inputCriminalLastName,inputCriminalNickName,arrestedCriminals,criminalRecord,
                textInDossier,criminalGroupType,eyeColor,hairColor,sortBy,order);
        model.addAttribute("criminals", criminals);
        return "criminals";
    }

    @GetMapping("/addCriminal")
    public String addCriminalMenu() {
        return "addCriminal";
    }

    @PostMapping("/criminals/addCriminal")
    public String addCriminal(@ModelAttribute @Validated Criminal criminal,
                              @RequestParam MultipartFile file,
                              @ModelAttribute @Validated Dossier textOfDossier,
                              @RequestParam("selectedLanguages") String selectedLanguages,
                              @RequestParam("selectedCriminalRecords") String selectedCriminalRecords,
                              @ModelAttribute @Validated CriminalGroup criminalGroup) {
        criminalService.addCriminal(criminal,textOfDossier,criminalGroup,selectedLanguages,selectedCriminalRecords,file);
        return "redirect:/criminals";
    }

    @GetMapping("/criminals/{id}")
    public String getCriminal(@PathVariable Long id,Model model) {
        Criminal criminal = criminalService.getCriminalById(id).orElseThrow(NotFoundException::new);
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
        Criminal criminal = criminalService.getCriminalById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("criminal", criminal);
        Set<Language> languages = criminal.getLanguages();
        Set<String> selectedLanguagesValue = languages.stream()
                .map(Language::getCriminalLanguage)
                .collect(Collectors.toSet());
        Set<CriminalRecord> criminalRecords = criminal.getCriminalRecords();
        Set<String> selectedCriminalRecords = criminalRecords.stream()
                .map(CriminalRecord::getCriminalRecord)
                .collect(Collectors.toSet());
        model.addAttribute("selectedLanguagesValue", selectedLanguagesValue);
        model.addAttribute("selectedCriminalRecords", selectedCriminalRecords);

        return "editCriminal";
    }
    @PostMapping("/criminals/editCriminal/{id}")
    public String editCriminal(@PathVariable Long id, @ModelAttribute @Validated Criminal criminal,
                               @RequestParam MultipartFile file,
                               @ModelAttribute @Validated Dossier textOfDossier,
                               @RequestParam("selectedLanguages") @NotBlank String selectedLanguages,
                               @RequestParam("selectedCriminalRecords") @NotBlank String selectedCriminalRecords,
                               @ModelAttribute @Validated CriminalGroup criminalGroup) {
        criminalService.updateCriminalById(id,criminal,textOfDossier,criminalGroup,selectedLanguages,selectedCriminalRecords,file);
        return "redirect:/criminals/{id}";
    }
}
