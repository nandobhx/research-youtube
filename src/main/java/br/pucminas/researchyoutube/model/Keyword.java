package br.pucminas.researchyoutube.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "keywords",
        indexes = {
                @Index(columnList = "keyword,term_id")
        }
)
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private Integer frequency;

    @ManyToOne(optional = false)
    private Term term;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "keyword", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<KeywordChannel> keywordChannels = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "keyword", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<KeywordVideo> keywordVideos = new ArrayList<>();

    public Keyword() {
    }

    public Keyword(String keyword, Term term) {
        this.keyword = keyword;
        this.term = term;
        this.frequency = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public List<KeywordChannel> getKeywordChannels() {
        return keywordChannels;
    }

    public void setKeywordChannels(List<KeywordChannel> keywordChannels) {
        this.keywordChannels = keywordChannels;
    }

    public List<KeywordVideo> getKeywordVideos() {
        return keywordVideos;
    }

    public void setKeywordVideos(List<KeywordVideo> keywordVideos) {
        this.keywordVideos = keywordVideos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Keyword keyword = (Keyword) o;
        return Objects.equals(id, keyword.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
