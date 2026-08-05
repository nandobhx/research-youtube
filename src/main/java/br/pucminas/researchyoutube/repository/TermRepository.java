package br.pucminas.researchyoutube.repository;

import br.pucminas.researchyoutube.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
    Term findFirstByTerm(String term);
}
