package gestion.cours.salles.controller;

import gestion.cours.salles.entities.Cours;
import gestion.cours.salles.entities.SalleDeClasse;
import gestion.cours.salles.repository.CoursRepository;
import gestion.cours.salles.repository.SalleDeClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CoursController {
    boolean isSearched = false;

    @Autowired
    private SalleDeClasseRepository salleDeClasseRepository;

    @Autowired
    private CoursRepository coursRepository;

    @GetMapping("/")
    public String home(@RequestParam(required = false) String query,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Pageable pageable = PageRequest.of(page, 7);
        Page<Cours> coursPage;

        if (query != null && !query.isEmpty()) {
            coursPage = coursRepository.findByNomContaining(query, pageable);
        } else {
            coursPage = coursRepository.findAll(pageable);
        }
        model.addAttribute("coursList", coursPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursPage.getTotalPages());
        model.addAttribute("query", query);
        model.addAttribute("isSearched", query != null && !query.isEmpty());
        return "index-cours";
    }


    @GetMapping("/ajoutcours")
    public String showSignUpForm(Cours cours, Model model) {
        List<SalleDeClasse> salleList = (List<SalleDeClasse>) salleDeClasseRepository.findAll();
        model.addAttribute("salleList", salleList);
        model.addAttribute("cours", cours);
        return "add-cours";
    }

    @PostMapping("/addcours")
    public String addCours(@Validated Cours cours, BindingResult result, Model model, @RequestParam Long salleId) {
        if (result.hasErrors()) {
            return "add-cours";
        }
        Optional<SalleDeClasse> salle = salleDeClasseRepository.findById(salleId);
        cours.setSalleDeClasse(salle.orElse(null));
        coursRepository.save(cours);
        return "redirect:/";
    }

    @GetMapping("/cours/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        List<SalleDeClasse> salleList = (List<SalleDeClasse>) salleDeClasseRepository.findAll();
        model.addAttribute("salleList", salleList);
        model.addAttribute("cours", cours);
        return "update-cours";
    }

    @PostMapping("/cours/update/{id}")
    public String updateCours(@PathVariable("id") long id, @Validated Cours cours, BindingResult result, @RequestParam Long salleId) {
        if (result.hasErrors()) {
            cours.setId(id);
            return "update-cours";
        }
        Optional<SalleDeClasse> salle = salleDeClasseRepository.findById(salleId);
        cours.setSalleDeClasse(salle.orElse(null));
        coursRepository.save(cours);
        return "redirect:/";
    }

    @GetMapping("/cours/delete/{id}")
    public String deleteCours(@PathVariable("id") long id) {
        Cours cours = (Cours) coursRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        coursRepository.delete(cours);
        return "redirect:/";
    }
}