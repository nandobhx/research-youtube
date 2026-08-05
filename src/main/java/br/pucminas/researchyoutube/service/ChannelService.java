package br.pucminas.researchyoutube.service;

import br.pucminas.researchyoutube.model.Channel;
import br.pucminas.researchyoutube.model.Term;
import br.pucminas.researchyoutube.repository.ChannelRepository;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {
    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public Channel createOrGet(Channel channel, Term term) {
        var newChannel = channelRepository.findFirstByChannelIdAndTerm(channel.getChannelId(), term);
        if (newChannel == null) {
            newChannel = channelRepository.save(channel);
        }
        return newChannel;
    }
}
