package br.pucminas.researchyoutube.repository;

import br.pucminas.researchyoutube.model.Keyword;
import br.pucminas.researchyoutube.model.KeywordVideo;
import br.pucminas.researchyoutube.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordVideoRepository extends JpaRepository<KeywordVideo, Long> {
    int countByKeywordAndVideo(Keyword keyword, Video video);
}
