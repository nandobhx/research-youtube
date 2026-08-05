package br.pucminas.researchyoutube.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.Video;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
public class YouTubeService {
    private final SecurityService securityService;
    private final YouTube youtube;

    public YouTubeService(SecurityService securityService) throws GeneralSecurityException, IOException {
        this.securityService = securityService;

        youtube = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                null
        ).setApplicationName("research-youtube").build();
    }

    public List<SearchResult> getListBySearch(String term, int limit) throws IOException {
        var videos = new ArrayList<SearchResult>();
        String pageToken = null;
        var request = youtube.search()
                .list("snippet")
                .setQ(term)
                .setType("video")
                .setRegionCode("BR")
                .setRelevanceLanguage("pt")
                .setMaxResults(50L)
                .setKey(securityService.getApiKey());
        var countVideos = 0;

        do {
            if (pageToken != null) {
                request.setPageToken(pageToken);
            }

            var response = request.execute();
            for (var item : response.getItems()) {
                videos.add(item);
                countVideos++;

                if (limit != 0 && countVideos == limit) {
                    break;
                }
            }
            pageToken = response.getNextPageToken();
        } while (pageToken != null && (limit == 0 || countVideos < limit));

        return videos;
    }

    public Video getVideo(String id) throws IOException {
        var request = youtube.videos()
                .list("snippet,statistics,contentDetails")
                .setId(id)
                .setKey(securityService.getApiKey());
        var response = request.execute();
        return response.getItems().get(0);
    }

    public Channel getChannel(String id) throws IOException {
        var request = youtube.channels()
                .list("snippet,statistics,contentDetails,topicDetails,status,brandingSettings,contentOwnerDetails")
                .setId(id)
                .setKey(securityService.getApiKey());
        var response = request.execute();
        return response.getItems().get(0);
    }

    public List<Subscription> getSubscriptions(String channelId) throws IOException {
        var request = youtube.subscriptions()
                .list("snippet")
                .setChannelId(channelId)
                .setKey(securityService.getApiKey());
        var response = request.execute();
        return response.getItems();
    }
}
