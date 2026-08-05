package br.pucminas.researchyoutube.repository;

import br.pucminas.researchyoutube.model.Term;
import br.pucminas.researchyoutube.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findFirstByVideoIdAndTerm(String videoId, Term term);

    int countByVideoIdAndTerm(String videoId, Term term);
}
