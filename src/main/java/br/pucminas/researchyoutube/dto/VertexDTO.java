package br.pucminas.researchyoutube.dto;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.awt.*;
import java.util.Objects;

public final class VertexDTO {
    private final String id;
    private final String label;
    private final String type;
    private final Color color;
    private double betweennessCentrality = 0D;

    public VertexDTO(String id, String label, String type, Color color) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.color = color;
    }

    @Contract(pure = true)
    @Override
    public @NonNull String toString() {
        if (this.label == null) return "";
        return this.label.replaceAll("\\s+", "_");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VertexDTO vertexDTO = (VertexDTO) o;
        return Objects.equals(id, vertexDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String id() {
        return id;
    }

    public String label() {
        return label;
    }

    public String type() {
        return type;
    }

    public Color color() {
        return color;
    }

    public Double betweennessCentrality() {
        return betweennessCentrality;
    }

    public void setBetweennessCentrality(double betweennessCentrality) {
        this.betweennessCentrality = betweennessCentrality;
    }
}
