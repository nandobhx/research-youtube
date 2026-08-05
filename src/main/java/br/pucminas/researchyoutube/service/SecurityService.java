package br.pucminas.researchyoutube.service;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Service
public class SecurityService {
    private final String apiKey;

    public SecurityService() {
        try (var fis = new FileInputStream("security.properties")) {
            Properties properties = new Properties();
            properties.load(fis);
            apiKey = Objects.requireNonNullElse(properties.getProperty("API_KEY"), "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getApiKey() {
        return apiKey;
    }
}
