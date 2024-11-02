package gestion.cours.salles.controller;

import gestion.cours.salles.entities.Cours;
import gestion.cours.salles.entities.SalleDeClasse;
import gestion.cours.salles.repository.CoursRepository;
import gestion.cours.salles.repository.SalleDeClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@Controller
public class SalleDeClasseController {
    boolean isSearched = false;
    @Autowired
    private CoursRepository coursRepository;
    @Autowired
    private SalleDeClasseRepository salleDeClasseRepository;
    @GetMapping("/s")
    public String home(@RequestParam(required = false) String query,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Pageable pageable = PageRequest.of(page, 3);
        Page<SalleDeClasse> salleDeClassePage;

        if (query != null && !query.isEmpty()) {
            salleDeClassePage = salleDeClasseRepository.findByNomContaining(query, pageable);
        } else {
            salleDeClassePage = salleDeClasseRepository.findAll(pageable);
        }

        List<SalleDeClasse> salleList = salleDeClassePage.getContent();
        for (SalleDeClasse salle : salleList) {
            List<Cours> coursForSalle = coursRepository.findBySalleDeClasse(salle);
            salle.setCoursList(coursForSalle);
        }
        model.addAttribute("salleList", salleList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", salleDeClassePage.getTotalPages());
        model.addAttribute("query", query);
        model.addAttribute("isSearched", query != null && !query.isEmpty());

        return "index-salles";
    }


    @GetMapping("/ajoutsalles")
    public String showSignUpForm(SalleDeClasse salleDeClasse) {
        return"add-salles";
    }

    @PostMapping("/addsalles")
    public String addUser(@Validated SalleDeClasse salleDeClasse, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return"add-salles";
        }

        salleDeClasseRepository.save(salleDeClasse);
        model.addAttribute("salleList", salleDeClasseRepository.findAll());
        return "redirect:/s";
    }

    @GetMapping("/salles/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        SalleDeClasse salleDeClasse = salleDeClasseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid salle Id:" + id));
        model.addAttribute("salleDeClasse", salleDeClasse);
        return "update-salles";
    }

    @PostMapping("/salles/update/{id}")
    public String updateSalle(@PathVariable("id") long id, @Validated SalleDeClasse salleDeClasse, BindingResult result, Model model) {
        if (result.hasErrors()) {
            salleDeClasse.setId(id);
            return "update-salles";
        }

        salleDeClasseRepository.save(salleDeClasse);
        model.addAttribute("salleList", salleDeClasseRepository.findAll());
        return "redirect:/s";
    }

    @GetMapping("/salles/delete/{id}")
    public String deleteSalle(@PathVariable("id") long id, Model model) {
        SalleDeClasse salleDeClasse = salleDeClasseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid salle Id:" + id));
        salleDeClasseRepository.delete(salleDeClasse);
        model.addAttribute("salleList", salleDeClasseRepository.findAll());
        return "redirect:/s";
    }
}