package gestion.cours.salles.entities;

import jakarta.persistence.*;

@Entity
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    private int duree;
    @ManyToOne // علاقة متعددة إلى واحدة
    @JoinColumn(name = "salle_id") // عمود المفاتيح الأجنبية
    private SalleDeClasse salleDeClasse;
    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public SalleDeClasse getSalleDeClasse() {
        return salleDeClasse;
    }

    public void setSalleDeClasse(SalleDeClasse salleDeClasse) {
        this.salleDeClasse = salleDeClasse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }
}