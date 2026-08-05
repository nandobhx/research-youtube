package br.pucminas.researchyoutube.repository;

import br.pucminas.researchyoutube.model.Channel;
import br.pucminas.researchyoutube.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Channel findFirstByChannelIdAndTerm(String channelId, Term term);
}
