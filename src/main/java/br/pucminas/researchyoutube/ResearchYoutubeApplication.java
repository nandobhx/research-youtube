package br.pucminas.researchyoutube;

import br.pucminas.researchyoutube.controller.GraphController;
import br.pucminas.researchyoutube.controller.SearchController;
import br.pucminas.researchyoutube.exception.CancelException;
import br.pucminas.researchyoutube.util.Params;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class ResearchYoutubeApplication extends JFrame {

    public static void main(String[] args) {
        var builder = new SpringApplicationBuilder(ResearchYoutubeApplication.class);
        builder.headless(false);
        builder.run(args);
    }

    public ResearchYoutubeApplication(SearchController searchController, GraphController graphController) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("YouTube Research");
        setSize(600, 100);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        var lblOptions = new JLabel("Selecione uma opção:");
        lblOptions.setMaximumSize(new Dimension(this.getWidth(), 20));
        lblOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblOptions.setHorizontalTextPosition(SwingConstants.LEFT);
        add(lblOptions);

        var cmbOptions = new JComboBox<String>();
        cmbOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmbOptions.setMaximumSize(new Dimension(this.getWidth(), 30));
        cmbOptions.addItem("Processar experimento a partir de um termo");
        cmbOptions.addItem("Gerar grafo de palavras e canais");
        cmbOptions.addItem("Gerar grafo de coocorrência de palavras");
        add(cmbOptions);

        add(Box.createRigidArea(new Dimension(0, 10)));

        var btnRun = new JButton("Executar");
        btnRun.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRun.addActionListener(event -> {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            cmbOptions.setEnabled(false);
            btnRun.setEnabled(false);

            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return switch (cmbOptions.getSelectedIndex()) {
                        case 0 -> {
                            searchController.searchAndSave(inputTerm());
                            yield "Experimento finalizado com sucesso!";
                        }
                        case 1 -> {
                            graphController.generateWordChannelGraph(inputTerm());
                            yield "Arquivo do grafo gerado com sucesso!";
                        }
                        case 2 -> {
                            graphController.generateCooccurrenceWordGraph(inputTerm());
                            yield "Arquivo do grafo gerado com sucesso!";
                        }
                        default -> "";
                    };
                }

                @Override
                protected void done() {
                    ResearchYoutubeApplication.this.setCursor(Cursor.getDefaultCursor());
                    cmbOptions.setEnabled(true);
                    btnRun.setEnabled(true);

                    try {
                        var message = get();

                        if (message != null && !message.isEmpty()) {
                            JOptionPane.showMessageDialog(ResearchYoutubeApplication.this, message, "YouTube Research", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (Exception e) {
                        var message = e.getMessage();

                        if (e.getCause() != null) {
                            if (e.getCause() instanceof CancelException) return;
                            message = e.getCause().getMessage();
                        }

                        JOptionPane.showMessageDialog(ResearchYoutubeApplication.this, message, "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        });
        add(btnRun);

        setVisible(true);
    }

    private String inputTerm() throws CancelException {
        var term = JOptionPane.showInputDialog(
                this,
                "Informe o termo de pesquisa do experimento:",
                "YouTube Research",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                Params.TERM
        );

        if (term == null) {
            throw new CancelException();
        }

        return term.toString();
    }
}
