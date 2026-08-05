package br.pucminas.researchyoutube.service;

import br.pucminas.researchyoutube.dto.KeywordFrequency;
import br.pucminas.researchyoutube.model.Keyword;
import br.pucminas.researchyoutube.model.KeywordChannel;
import br.pucminas.researchyoutube.model.KeywordVideo;
import br.pucminas.researchyoutube.model.Term;
import br.pucminas.researchyoutube.repository.KeywordChannelRepository;
import br.pucminas.researchyoutube.repository.KeywordRepository;
import br.pucminas.researchyoutube.repository.KeywordVideoRepository;
import br.pucminas.researchyoutube.util.Params;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final KeywordVideoRepository keywordVideoRepository;
    private final KeywordChannelRepository keywordChannelRepository;

    public KeywordService(KeywordRepository keywordRepository, KeywordVideoRepository keywordVideoRepository, KeywordChannelRepository keywordChannelRepository) {
        this.keywordRepository = keywordRepository;
        this.keywordVideoRepository = keywordVideoRepository;
        this.keywordChannelRepository = keywordChannelRepository;
    }

    public List<KeywordFrequency> extract(String text, double percent) throws IOException {
        var fileTokens = new ClassPathResource("opennlp-pt-ud-gsd-tokens-1.3-2.5.4.bin");
        var filePos = new ClassPathResource("opennlp-pt-ud-gsd-pos-1.3-2.5.4.bin");
        var fileLemmas = new ClassPathResource("opennlp-pt-ud-gsd-lemmas-1.3-2.5.4.bin");

        var tokenModel = new TokenizerModel(fileTokens.getInputStream());
        var tokenizer = new TokenizerME(tokenModel);
        var tokens = tokenizer.tokenize(sanitize(text.toLowerCase()));

        var posModel = new POSModel(filePos.getInputStream());
        var tagger = new POSTaggerME(posModel);
        var tags = tagger.tag(tokens);

        var lemmatizerModel = new LemmatizerModel(fileLemmas.getInputStream());
        var lemmatizer = new LemmatizerME(lemmatizerModel);
        var lemmas = lemmatizer.lemmatize(tokens, tags);

        var keywordFrequency = new HashMap<String, Integer>();

        for (int i = 0; i < tokens.length; i++) {
            String word = lemmas[i];
            String tag = tags[i];

            if (tag.startsWith("NOUN") && word.length() >= 2 && !isStopWord(word)) {
                keywordFrequency.put(word, keywordFrequency.getOrDefault(word, 0) + 1);
            }
        }

        var keywords = keywordFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(kf -> new KeywordFrequency(kf.getKey(), kf.getValue()))
                .toList();

        if (percent == 0) return keywords;
        return keywords.stream().limit((long) (keywords.size() * percent / 100D)).toList();
    }

    private String sanitize(String word) {
        return word
                .replaceAll("\\b(?:https?://|ftp://|www\\.)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "")
                .replaceAll("[-a-zA-Z0-9@:%_+.~#?&/=]{2,256}\\.[a-z]{2,4}\\b(/[-a-zA-Z0-9@:%_+.~#?&/=]*)?", "")
                .replace("“", "")
                .replace("|", "")
                .replace("#", "")
                .replace("*", "")
                .replace("...", "");
    }

    private boolean isStopWord(String word) {
        return Arrays.asList(Params.STOP_WORDS).contains(word);
    }

    public Keyword createOrGet(Keyword keyword, Term term) {
        var newKeyword = keywordRepository.findFirstByKeywordAndTerm(keyword.getKeyword(), term);
        if (newKeyword == null) {
            newKeyword = keywordRepository.save(keyword);
        }
        return newKeyword;
    }

    public void mapKeywordToVideoAndChannel(KeywordVideo keywordVideo, KeywordChannel keywordChannel) {
        var keywordVideoCount = keywordVideoRepository.countByKeywordAndVideo(
                keywordVideo.getKeyword(),
                keywordVideo.getVideo()
        );

        // Se já possui a palavra-chave mapeada para o vídeo, ignoro
        if (keywordVideoCount > 0) return;

        // Mapeio a palavra-chave para o vídeo
        keywordVideoRepository.save(keywordVideo);

        // Mapeio a palavra-chave para o canal
        var keywordChannelCurrent = keywordChannelRepository.findFirstByKeywordAndChannel(
                keywordChannel.getKeyword(),
                keywordChannel.getChannel()
        );

        // Atualizo a frequência da palavra-chave para o canal
        if (keywordChannelCurrent != null) {
            keywordChannelCurrent.setFrequency(keywordChannelCurrent.getFrequency() + keywordVideo.getFrequency());
            keywordChannelRepository.save(keywordChannelCurrent);
        } else {
            keywordChannel.setFrequency(keywordVideo.getFrequency());
            keywordChannelRepository.save(keywordChannel);
        }

        // Atualizo a frequência da palavra-chave
        var keyword = keywordVideo.getKeyword();
        keyword.setFrequency(keyword.getFrequency() + keywordVideo.getFrequency());
        keywordRepository.save(keyword);
    }
}
