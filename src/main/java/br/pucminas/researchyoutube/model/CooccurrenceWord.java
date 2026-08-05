package br.pucminas.researchyoutube.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "cooccurence_words")
public class CooccurrenceWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String term;

    @Column(nullable = false)
    private String wordX;

    @Column(nullable = false)
    private String wordY;

    @Column(nullable = false)
    private String channel;

    @Column(nullable = false)
    private Integer channelSubscribers;

    public CooccurrenceWord() {
    }

    public CooccurrenceWord(String term, String wordX, String wordY, String channel, Integer channelSubscribers) {
        this.term = term;
        this.wordX = wordX;
        this.wordY = wordY;
        this.channel = channel;
        this.channelSubscribers = channelSubscribers;
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

    public String getWordX() {
        return wordX;
    }

    public void setWordX(String wordX) {
        this.wordX = wordX;
    }

    public String getWordY() {
        return wordY;
    }

    public void setWordY(String wordY) {
        this.wordY = wordY;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getChannelSubscribers() {
        return channelSubscribers;
    }

    public void setChannelSubscribers(Integer channelSubscribers) {
        this.channelSubscribers = channelSubscribers;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CooccurrenceWord that = (CooccurrenceWord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
