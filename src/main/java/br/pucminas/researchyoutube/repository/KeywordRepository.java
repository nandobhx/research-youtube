package br.pucminas.researchyoutube.repository;

import br.pucminas.researchyoutube.model.Keyword;
import br.pucminas.researchyoutube.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Keyword findFirstByKeywordAndTerm(String keyword, Term term);
}
