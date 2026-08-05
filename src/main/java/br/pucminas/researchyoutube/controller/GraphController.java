package br.pucminas.researchyoutube.controller;

import br.pucminas.researchyoutube.service.GraphService;
import org.springframework.stereotype.Component;

@Component
public class GraphController {
    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    public void generateWordChannelGraph(String term) throws Exception {
        graphService.generateWordChannelGraphAndExport(term);
    }
    
    public void generateCooccurrenceWordGraph(String term) throws Exception {
        graphService.generateCooccurrenceWordGraphAndExporter(term);
    }
}
