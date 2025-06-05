package core;

public class HtmlFileValidator {

    // Constantes com as extensões válidas
    private static final String HTML_EXTENSION = ".html";
    private static final String HTM_EXTENSION = ".htm";

    /**
     * Valida se o caminho do arquivo possui uma extensão HTML válida.
     *
     * @param filePath Caminho completo do arquivo a ser validado
     * @throws RuntimeException Se o arquivo não possuir uma extensão válida
     */
    public void validate(String filePath) {
        // Verifica se a extensão é válida
        if (!hasValidExtension(filePath)) {
            System.err.println("Extensão inválida. Use .html ou .htm");
            throw new RuntimeException("Extensão inválida.");
        }
    }

    /**
     * Método auxiliar que verifica se a extensão do arquivo é válida.
     *
     * @param filePath Caminho do arquivo a ser verificado
     * @return true se a extensão for .html ou .htm, false caso contrário
     */
    private boolean hasValidExtension(String filePath) {
        if (filePath == null) {
            return false;
        }

        String lowerPath = filePath.toLowerCase();

        // Verifica se termina com uma das extensões válidas
        return lowerPath.endsWith(HTML_EXTENSION) || lowerPath.endsWith(HTM_EXTENSION);
    }
}