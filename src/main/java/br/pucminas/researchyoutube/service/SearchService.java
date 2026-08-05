package br.pucminas.researchyoutube.service;

import br.pucminas.researchyoutube.model.*;
import br.pucminas.researchyoutube.util.Convert;
import br.pucminas.researchyoutube.util.Params;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchService {
    private final Logger logger = LoggerFactory.getLogger(SearchService.class);
    private final TermService termService;
    private final ChannelService channelService;
    private final YouTubeService youtubeService;
    private final VideoService videoService;
    private final KeywordService keywordService;

    public SearchService(YouTubeService youtubeService, TermService termService, ChannelService channelService, VideoService videoService, KeywordService keywordService) {
        this.youtubeService = youtubeService;
        this.termService = termService;
        this.channelService = channelService;
        this.videoService = videoService;
        this.keywordService = keywordService;
    }

    @Transactional
    public void searchAndSave(String term) throws IOException {
        logger.info("Iniciando experimento.");

        // Cria ou recupera um termo
        logger.info("Criando ou recuperando o termo {}.", term);
        var appTerm = termService.createOrGet(term);

        // Busca os vídeos
        logger.info("Buscando os vídeos mais relevantes sobre o termo {} limitados em {}.", term, Params.LIMIT_VIDEOS);
        var videos = youtubeService.getListBySearch(term, Params.LIMIT_VIDEOS);
        var totalVideos = videos.size();

        logger.info("Persistindo os dados dos {} vídeos encontrados.", totalVideos);
        var processedVideos = 0;
        var progress = 0;
        var printProgress = 0;
        var persistedVideos = 0;

        for (var video : videos) {
            // Progresso do experimento
            processedVideos++;
            progress = processedVideos * 100 / totalVideos;
            if (progress >= printProgress) {
                logger.info("Progresso: {}%", progress);
                printProgress += 10;
            }

            // Somente processa vídeos que retornaram id
            if (video.getId().getVideoId() == null || video.getId().getVideoId().isBlank()) continue;

            // Recupera todos os detalhes necessários do vídeo
            var detailVideo = youtubeService.getVideo(video.getId().getVideoId());

            // Somente vídeos com duração maior que zero segundos serão considerados
            if (Convert.durationToSeconds(detailVideo.getContentDetails().getDuration()) == 0) continue;

            // Somente vídeos de língua portuguesa como principal
            var language = detailVideo.getSnippet().getDefaultLanguage();
            if (language == null || !language.contains("pt")) continue;

            // Somente vídeos não processados para o termo
            if (videoService.hasVideoByTerm(detailVideo.getId(), appTerm)) continue;

            // Cria ou recupera um canal
            var channel = youtubeService.getChannel(video.getSnippet().getChannelId());
            var appChanel = new Channel(channel, appTerm);
            appChanel = channelService.createOrGet(appChanel, appTerm);

            // Cria um vídeo
            var appVideo = new Video(detailVideo, appTerm, appChanel);
            appVideo = videoService.create(appVideo);

            // Extrai as palavras-chave
            var keywords = keywordService.extract(
                    appVideo.getTitleDescription(),
                    Params.LIMIT_PERCENT
            );

            for (var keyword : keywords) {
                var appKeyword = new Keyword(keyword.keyword(), appTerm);
                appKeyword = keywordService.createOrGet(appKeyword, appTerm);

                var keywordVideo = new KeywordVideo(appVideo, appKeyword, keyword.frequency());
                var keywordChannel = new KeywordChannel(appChanel, appKeyword);
                keywordService.mapKeywordToVideoAndChannel(keywordVideo, keywordChannel);
            }

            persistedVideos++;
        }

        logger.info("Total de vídeos persistidos: {}", persistedVideos);

        // Atualizo as datas de extração
        termService.finish(appTerm);

        logger.info("Finalizando experimento.");
    }
}
