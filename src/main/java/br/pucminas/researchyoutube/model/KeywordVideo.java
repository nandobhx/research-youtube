package br.pucminas.researchyoutube.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
        name = "keyword_videos",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"video_id", "keyword_id"}
        )
)
public class KeywordVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Video video;

    @ManyToOne(optional = false)
    private Keyword keyword;

    @Column(nullable = false)
    private Integer frequency;

    public KeywordVideo() {
    }

    public KeywordVideo(Video video, Keyword keyword, Integer frequency) {
        this.video = video;
        this.keyword = keyword;
        this.frequency = frequency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        KeywordVideo that = (KeywordVideo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
