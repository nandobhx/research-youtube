package br.pucminas.researchyoutube.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "top_words")
public class TopWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String term;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private Double betweennessCentrality;

    public TopWord() {
    }

    public TopWord(String term, String word, Double betweennessCentrality) {
        this.term = term;
        this.word = word;
        this.betweennessCentrality = betweennessCentrality;
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Double getBetweennessCentrality() {
        return betweennessCentrality;
    }

    public void setBetweennessCentrality(Double betweennessCentrality) {
        this.betweennessCentrality = betweennessCentrality;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TopWord topWord = (TopWord) o;
        return Objects.equals(id, topWord.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
