package br.pucminas.researchyoutube.service;

import br.pucminas.researchyoutube.dto.VertexDTO;
import br.pucminas.researchyoutube.model.*;
import br.pucminas.researchyoutube.repository.*;
import br.pucminas.researchyoutube.util.Params;
import jakarta.transaction.Transactional;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.BetweennessCentrality;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.graphml.GraphMLExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GraphService {
    private final Logger logger = LoggerFactory.getLogger(GraphService.class);
    private final TermService termService;
    private final KeywordChannelRepository keywordChannelRepository;
    private final KeywordRepository keywordRepository;
    private final TermRepository termRepository;
    private final TopWordRepository topWordRepository;
    private final CooccurrenceWordRepository cooccurrenceWordRepository;

    public GraphService(TermService termService, KeywordChannelRepository keywordChannelRepository, KeywordRepository keywordRepository, TermRepository termRepository, TopWordRepository topWordRepository, CooccurrenceWordRepository cooccurrenceWordRepository) {
        this.termService = termService;
        this.keywordChannelRepository = keywordChannelRepository;
        this.keywordRepository = keywordRepository;
        this.termRepository = termRepository;
        this.topWordRepository = topWordRepository;
        this.cooccurrenceWordRepository = cooccurrenceWordRepository;
    }

    @Transactional
    public void generateWordChannelGraphAndExport(String term) throws Exception {
        var graph = generateWordChannelGraph(term);
        exporter(graph, "graph_word_channel.graphml");
    }

    @Transactional
    public void generateCooccurrenceWordGraphAndExporter(String term) throws Exception {
        var termApp = termRepository.findFirstByTerm(term);
        var topWords = topWords(termApp);
        int total = topWords.size();
        int processed = 0;
        var channels = termApp.getChannels();
        var graph = new SimpleWeightedGraph<VertexDTO, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        // Deleta os registros antigos
        cooccurrenceWordRepository.deleteAllByTerm(termApp.getTerm());

        for (int i = 0; i < topWords.size(); i++) {
            var line = topWords.get(i);

            for (int j = 0; j < i; j++) {
                var column = topWords.get(j);
                var listChannels = new ArrayList<Channel>();

                for (var channel : channels) {
                    var countLine = keywordChannelRepository.countByKeywordAndChannel(line, channel);
                    var countColumn = keywordChannelRepository.countByKeywordAndChannel(column, channel);

                    if (countLine > 0 && countColumn > 0) {
                        listChannels.add(channel);
                    }
                }

                var lineVertex = new VertexDTO("KEYWORD_" + line.getId(), line.getKeyword(), "KEYWORD", Color.GREEN);
                var columnVertex = new VertexDTO("KEYWORD_" + column.getId(), column.getKeyword(), "KEYWORD", Color.GREEN);
                var count = listChannels.size();

                if (count > 1) {

                    for (var channel : listChannels) {
                        cooccurrenceWordRepository.save(
                                new CooccurrenceWord(term, line.getKeyword(), column.getKeyword(), channel.getTitle(), channel.getSubscribers())
                        );
                    }

                    graph.addVertex(lineVertex);
                    graph.addVertex(columnVertex);
                    var edge = graph.addEdge(lineVertex, columnVertex);

                    if (edge != null) {
                        graph.setEdgeWeight(edge, count);
                    }
                }
            }

            processed++;
            logger.info("Progresso: {}%", processed * 100 / total);
        }

        exporter(graph, "graph_coocurrence_word.graphml");
    }

    private List<Keyword> topWords(Term term) {
        var graph = generateWordChannelGraph(term.getTerm());

        var topWords = graph.vertexSet().stream()
                .filter(v -> v.type().equals("KEYWORD"))
                .sorted((v1, v2) -> v2.betweennessCentrality().compareTo(v1.betweennessCentrality()))
                .limit(Params.LIMIT_TOP_WORDS)
                .toList();

        persistWords(term, topWords);

        return topWords.stream()
                .map(word -> keywordRepository.findFirstByKeywordAndTerm(word.label(), term))
                .toList();
    }

    private void persistWords(Term term, List<VertexDTO> topWords) {
        // Deleta os registros antigos
        topWordRepository.deleteAllByTerm(term.getTerm());

        // Grava a lista das top 10 palavras
        topWords.forEach(word -> topWordRepository.save(
                new TopWord(term.getTerm(), word.label(), word.betweennessCentrality()))
        );
    }

    private SimpleGraph<VertexDTO, DefaultEdge> generateWordChannelGraph(String term) {
        var graph = new SimpleGraph<VertexDTO, DefaultEdge>(DefaultEdge.class);

        var appTerm = termService.get(term);
        var keywords = appTerm.getKeywords();

        for (var keyword : keywords) {
            var keywordChannels = keyword.getKeywordChannels();

            if (keywordChannels.size() > 1) {
                var keywordVertex = new VertexDTO("KEYWORD_" + keyword.getId(), keyword.getKeyword(), "KEYWORD", Color.GREEN);
                graph.addVertex(keywordVertex);

                for (var keywordChannel : keywordChannels) {
                    var channel = keywordChannel.getChannel();
                    var channelVertex = new VertexDTO("CHANNEL_" + channel.getId(), channel.getTitle(), "CHANNEL", Color.BLUE);
                    graph.addVertex(channelVertex);
                    graph.addEdge(keywordVertex, channelVertex);
                }
            }
        }

        var betweennessCentrality = new BetweennessCentrality<>(graph, true);
        var scores = betweennessCentrality.getScores();

        for (var vertex : graph.vertexSet()) {
            vertex.setBetweennessCentrality(scores.get(vertex) * 2D);
        }

        return graph;
    }

    private <E> void exporter(Graph<VertexDTO, E> graph, String fileName) throws Exception {
        var exporter = new GraphMLExporter<VertexDTO, E>(
                VertexDTO::id
        );

        exporter.setExportEdgeLabels(true);
        exporter.setExportEdgeWeights(true);
        exporter.setExportVertexLabels(true);

        exporter.registerAttribute("node_type", GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING, "");
        exporter.registerAttribute("r", GraphMLExporter.AttributeCategory.NODE, org.jgrapht.nio.AttributeType.INT, "0");
        exporter.registerAttribute("g", GraphMLExporter.AttributeCategory.NODE, org.jgrapht.nio.AttributeType.INT, "0");
        exporter.registerAttribute("b", GraphMLExporter.AttributeCategory.NODE, org.jgrapht.nio.AttributeType.INT, "0");
        exporter.registerAttribute("betweenness_centrality", GraphMLExporter.AttributeCategory.NODE, org.jgrapht.nio.AttributeType.DOUBLE, "0");

        exporter.setVertexAttributeProvider(v -> {
            var map = new HashMap<String, Attribute>();
            map.put("node_type", DefaultAttribute.createAttribute(v.type()));
            map.put("r", DefaultAttribute.createAttribute(v.color().getRed()));
            map.put("g", DefaultAttribute.createAttribute(v.color().getGreen()));
            map.put("b", DefaultAttribute.createAttribute(v.color().getBlue()));
            map.put("betweenness_centrality", DefaultAttribute.createAttribute(v.betweennessCentrality()));
            return map;
        });

        var file = new File(Params.DEFAULT_PATH + fileName);

        if (file.exists()) {
            if (!file.delete()) {
                throw new Exception(String.format("Erro ao excluir o arquivo %s para gerar um novo arquivo.", file.getName()));
            }
        }

        try (var writer = new FileOutputStream(file)) {
            exporter.exportGraph(graph, writer);
        }
    }
}
