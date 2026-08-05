package br.pucminas.researchyoutube.model;

import br.pucminas.researchyoutube.util.Convert;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "videos",
        indexes = {
                @Index(columnList = "videoId,term_id")
        }
)
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String videoId;

    @Column(nullable = false, length = 1024)
    private String title;

    @Column(nullable = false, length = 16384)
    private String description;

    @Column(nullable = false)
    private Integer comments;

    @Column(nullable = false)
    private Integer likes;

    @Column(nullable = false)
    private Integer views;

    @Column(nullable = false)
    private Long duration;

    @ManyToOne(optional = false)
    private Term term;

    @ManyToOne(optional = false)
    private Channel channel;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "video", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<KeywordVideo> keywordVideos = new ArrayList<>();

    public Video() {
    }

    public Video(com.google.api.services.youtube.model.Video video, Term term, Channel channel) {
        this.videoId = video.getId();
        this.title = video.getSnippet().getTitle();
        this.description = video.getSnippet().getDescription();
        this.comments = Convert.toInt(video.getStatistics().getCommentCount());
        this.likes = Convert.toInt(video.getStatistics().getLikeCount());
        this.views = Convert.toInt(video.getStatistics().getViewCount());
        this.duration = Convert.durationToSeconds(video.getContentDetails().getDuration());
        this.term = term;
        this.channel = channel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String youtubeId) {
        this.videoId = youtubeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public List<KeywordVideo> getKeywordVideos() {
        return keywordVideos;
    }

    public void setKeywordVideos(List<KeywordVideo> keywordVideos) {
        this.keywordVideos = keywordVideos;
    }

    public String getTitleDescription() {
        var titleDescription = "";

        if (title != null) {
            titleDescription += title + "\n";
        }

        if (description != null) {
            titleDescription += description;
        }

        return titleDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(id, video.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
