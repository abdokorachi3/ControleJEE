package gestion.cours.salles.repository;

import gestion.cours.salles.entities.SalleDeClasse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalleDeClasseRepository extends JpaRepository<SalleDeClasse, Long> {
    List<SalleDeClasse> findByNomContaining(String nom);
    Page<SalleDeClasse> findByNomContaining(String nom, Pageable pageable);
}
