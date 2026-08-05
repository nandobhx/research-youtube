package br.pucminas.researchyoutube.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "terms",
        indexes = {
                @Index(columnList = "term")
        }
)
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String term;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "term", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Video> videos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "term", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Channel> channels = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "term", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Keyword> keywords = new ArrayList<>();

    public Term() {
    }

    public Term(String term) {
        this.term = term;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return Objects.equals(id, term.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
