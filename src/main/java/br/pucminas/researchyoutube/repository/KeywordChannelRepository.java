package br.pucminas.researchyoutube.repository;

import br.pucminas.researchyoutube.model.Channel;
import br.pucminas.researchyoutube.model.Keyword;
import br.pucminas.researchyoutube.model.KeywordChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordChannelRepository extends JpaRepository<KeywordChannel, Long> {
    KeywordChannel findFirstByKeywordAndChannel(Keyword keyword, Channel channel);

    int countByKeywordAndChannel(Keyword keyword, Channel channel);
}
