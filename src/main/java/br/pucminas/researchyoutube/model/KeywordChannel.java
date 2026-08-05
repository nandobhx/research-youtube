package br.pucminas.researchyoutube.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
        name = "keyword_channels",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"channel_id", "keyword_id"}
        )
)
public class KeywordChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Channel channel;

    @ManyToOne(optional = false)
    private Keyword keyword;

    @Column(nullable = false)
    private Integer frequency;

    public KeywordChannel() {
    }

    public KeywordChannel(Channel channel, Keyword keyword) {
        this.channel = channel;
        this.keyword = keyword;
        this.frequency = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
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
        KeywordChannel that = (KeywordChannel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
