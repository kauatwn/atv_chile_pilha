package ui;

import core.HtmlFileValidator;
import core.HtmlStructureValidator;
import core.HtmlTagParser;
import utils.HtmlFileReader;
import utils.HtmlTagExtractor;

import javax.swing.*;
import java.io.File;

public class HtmlValidatorUI {

    public void show() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        String filePath = selectedFile.getAbsolutePath();

        HtmlStructureValidator validator = new HtmlStructureValidator(
                new HtmlFileValidator(),
                new HtmlFileReader(),
                new HtmlTagExtractor(),
                new HtmlTagParser());

        try {
            boolean isValid = validator.isValidHtml(filePath);

            if (isValid) {
                JOptionPane.showMessageDialog(null, "HTML válido!", "Resultado",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "HTML inválido!", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (RuntimeException e) {
            // Aqui captura a exceção lançada no fileValidator.validate(filePath)
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
