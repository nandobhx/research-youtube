package br.pucminas.researchyoutube.repository;

import br.pucminas.researchyoutube.model.CooccurrenceWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CooccurrenceWordRepository extends JpaRepository<CooccurrenceWord, Long> {
    void deleteAllByTerm(String term);
}
