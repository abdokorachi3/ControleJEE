package gestion.cours.salles.repository;

import gestion.cours.salles.entities.Cours;
import gestion.cours.salles.entities.SalleDeClasse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {

    Page<Cours> findByNomContaining(String nom, Pageable pageable);
    List<Cours> findByNomContaining(String nom);
    List<Cours> findBySalleDeClasse(SalleDeClasse salleDeClasse);


}
