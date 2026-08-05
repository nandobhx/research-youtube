package br.pucminas.researchyoutube.repository;

import br.pucminas.researchyoutube.model.TopWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopWordRepository extends JpaRepository<TopWord, Long> {
    void deleteAllByTerm(String term);
}
