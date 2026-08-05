package br.pucminas.researchyoutube.service;

import br.pucminas.researchyoutube.model.Term;
import br.pucminas.researchyoutube.repository.TermRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TermService {
    private final TermRepository termRepository;

    public TermService(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    public Term createOrGet(String term) {
        var newTerm = termRepository.findFirstByTerm(term);

        if (newTerm == null) {
            newTerm = termRepository.save(new Term(term));
        }

        return newTerm;
    }

    public Term get(String term) {
        return termRepository.findFirstByTerm(term);
    }

    public void finish(Term term) {
        var now = LocalDateTime.now();
        if (term.getStartDate() == null) {
            term.setStartDate(now);
        }
        term.setEndDate(now);
        termRepository.save(term);
    }
}
