package br.pucminas.researchyoutube.controller;

import br.pucminas.researchyoutube.service.SearchService;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    public void searchAndSave(String term) throws IOException {
        searchService.searchAndSave(term);
    }
}
