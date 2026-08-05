package br.pucminas.researchyoutube.model;

import br.pucminas.researchyoutube.util.Convert;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "channels",
        indexes = {
                @Index(columnList = "channelId")
        }
)
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String channelId;

    @Column(nullable = false, length = 1024)
    private String title;

    @Column(nullable = false)
    private Integer subscribers;

    @Column(nullable = false)
    private Integer publishedVideos;

    @Column(nullable = false)
    private Integer views;

    @ManyToOne(optional = false)
    private Term term;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "channel", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Video> videos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "channel", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<KeywordChannel> keywordChannels = new ArrayList<>();

    public Channel() {
    }

    public Channel(com.google.api.services.youtube.model.Channel channel, Term term) {
        this.channelId = channel.getId();
        this.title = channel.getSnippet().getTitle();
        this.subscribers = Convert.toInt(channel.getStatistics().getSubscriberCount());
        this.publishedVideos = Convert.toInt(channel.getStatistics().getVideoCount());
        this.views = Convert.toInt(channel.getStatistics().getViewCount());
        this.term = term;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
    }

    public Integer getPublishedVideos() {
        return publishedVideos;
    }

    public void setPublishedVideos(Integer publishedVideos) {
        this.publishedVideos = publishedVideos;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<KeywordChannel> getKeywordChannels() {
        return keywordChannels;
    }

    public void setKeywordChannels(List<KeywordChannel> keywordChannels) {
        this.keywordChannels = keywordChannels;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(id, channel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
