package br.pucminas.researchyoutube.service;

import br.pucminas.researchyoutube.model.Term;
import br.pucminas.researchyoutube.model.Video;
import br.pucminas.researchyoutube.repository.VideoRepository;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public boolean hasVideoByTerm(String videoId, Term term) {
        return videoRepository.countByVideoIdAndTerm(videoId, term) > 0;
    }

    public Video create(Video video) {
        return videoRepository.save(video);
    }
}
