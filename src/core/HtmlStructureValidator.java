package core;

import utils.HtmlFileReader;
import utils.HtmlTagExtractor;

public class HtmlStructureValidator {

    private final HtmlFileValidator fileValidator;
    private final HtmlFileReader fileReader;
    private final HtmlTagExtractor tagExtractor;
    private final HtmlTagParser tagParser;

    public HtmlStructureValidator(
            HtmlFileValidator fileValidator,
            HtmlFileReader fileReader,
            HtmlTagExtractor tagExtractor,
            HtmlTagParser tagParser) {
        this.fileValidator = fileValidator;
        this.fileReader = fileReader;
        this.tagExtractor = tagExtractor;
        this.tagParser = tagParser;
    }

    /**
     * Valida se o arquivo especificado contém HTML estruturalmente válido
     * @param filePath Caminho do arquivo HTML
     * @return true se o HTML for válido, false caso contrário
     */
    public boolean isValidHtml(String filePath) {
        // 1. Validação inicial do arquivo
        fileValidator.validate(filePath);

        // 2. Leitura do conteúdo
        String content = fileReader.readContent(filePath);

        // 3. Validação da estrutura
        return validateHtmlStructure(content);
    }

    /**
     * Realiza a validação estrutural do conteúdo HTML
     * @param content Conteúdo HTML a ser validado
     * @return true se a estrutura for válida
     */
    private boolean validateHtmlStructure(String content) {
        // Verificação de conteúdo vazio
        if (content == null || content.trim().isEmpty()) {
            System.err.println("Arquivo HTML vazio.");
            return false;
        }

        // Processador de pilha para controle de aninhamento
        TagStackProcessor stackProcessor = new TagStackProcessor();
        int length = content.length();

        // Varredura caractere por caractere
        for (int i = 0; i < length; i++) {
            // Ignora caracteres que não iniciam tags
            if (!isTagStart(content.charAt(i))) {
                continue;
            }

            // Encontra o fechamento da tag atual
            int closingIndex = getTagClosingIndex(content, i);
            if (closingIndex == -1) { // Tag mal formada
                return false;
            }

            // Extrai o conteúdo interno da tag (sem '<' e '>')
            String rawTag = extractTagContent(content, i, closingIndex);
            i = closingIndex; // Avança o índice

            // Ignora tags especiais (comentários, doctype)
            if (shouldIgnoreTag(rawTag)) {
                continue;
            }

            // Normaliza o nome da tag (minúsculas, sem atributos)
            String normalizedTagName = getNormalizedTagName(rawTag);

            // Tratamento especial para tags como script/style
            if (needsSpecialHandling(rawTag, normalizedTagName)) {
                i = handleSpecialTagContent(content, i, normalizedTagName);
                if (i == -1) { // Fechamento não encontrado
                    return false;
                }
                continue;
            }

            // Verifica se é tag auto-fechável
            boolean selfClosing = isSelfClosing(rawTag, normalizedTagName);

            // Processa a tag (empilha/desempilha)
            if (!processTag(stackProcessor, rawTag, normalizedTagName, selfClosing)) {
                return false; // Erro no processamento
            }
        }

        // Verifica tags não fechadas
        if (!stackProcessor.isStackEmpty()) {
            System.err.println("Tags não fechadas corretamente:");
            for (String unclosedTag : stackProcessor.getRemainingUnclosedTags()) {
                System.err.printf("<%s> não foi fechada.%n", unclosedTag);
            }
            return false;
        }

        return true; // Todas as validações passaram
    }

    /* Métodos auxiliares */

    /**
     * Verifica se o caractere inicia uma tag HTML.
     *
     * @param character Caractere a ser verificado.
     * @return true se o caractere for '<', false caso contrário.
     */
    private boolean isTagStart(char character) {
        return character == '<';
    }

    /**
     * Encontra o índice do fechamento '>' da tag atual.
     *
     * @param content Conteúdo HTML completo.
     * @param startIndex Índice inicial onde a tag começa (aponta para '<').
     * @return Índice do caractere '>' correspondente, ou -1 se não encontrado.
     */
    private int getTagClosingIndex(String content, int startIndex) {
        return tagParser.findTagClosingBracket(content, startIndex + 1);
    }

    /**
     * Extrai o conteúdo interno de uma tag (entre os caracteres '<' e '>').
     *
     * @param content Conteúdo HTML completo.
     * @param startIndex Índice do caractere '<' da tag.
     * @param endIndex Índice do caractere '>' da tag.
     * @return Conteúdo da tag (ex: "div", "/div", "img src='x'").
     */
    private String extractTagContent(String content, int startIndex, int endIndex) {
        return content.substring(startIndex + 1, endIndex);
    }

    /**
     * Determina se a tag deve ser ignorada (ex: comentários, doctype).
     *
     * @param rawTag Conteúdo cru da tag (sem '<' e '>').
     * @return true se a tag for do tipo a ser ignorado, false caso contrário.
     */
    private boolean shouldIgnoreTag(String rawTag) {
        return tagExtractor.shouldIgnoreTag(rawTag);
    }

    /**
     * Normaliza o nome da tag, removendo a barra de fechamento (se houver)
     * e convertendo para minúsculas.
     *
     * @param rawTag Conteúdo cru da tag.
     * @return Nome da tag normalizado.
     */
    private String getNormalizedTagName(String rawTag) {
        String normalizedTagName;
        if (rawTag.startsWith("/")) {
            normalizedTagName = rawTag.substring(1);
        } else {
            normalizedTagName = rawTag;
        }
        return tagExtractor.extractTagName(normalizedTagName).toLowerCase();
    }

    /**
     * Verifica se a tag requer tratamento especial (como script ou style).
     *
     * @param rawTag Conteúdo cru da tag.
     * @param tagName Nome normalizado da tag.
     * @return true se a tag exigir tratamento especial, false caso contrário.
     */
    private boolean needsSpecialHandling(String rawTag, String tagName) {
        return !rawTag.startsWith("/") && tagExtractor.isSpecialTag(tagName);
    }

    /**
     * Avança no conteúdo HTML até o fechamento da tag especial (ex: </script>).
     *
     * @param content Conteúdo HTML completo.
     * @param currentIndex Índice atual no conteúdo (posição do '>').
     * @param tagName Nome da tag especial.
     * @return Novo índice após o fechamento da tag especial, ou -1 se não encontrado.
     */
    private int handleSpecialTagContent(String content, int currentIndex, String tagName) {
        return tagParser.skipSpecialTagContent(content, currentIndex, tagName);
    }

    /**
     * Verifica se a tag é auto-fechável (ex: <img/>, <br>).
     *
     * @param rawTag Conteúdo cru da tag.
     * @param tagName Nome normalizado da tag.
     * @return true se a tag for auto-fechável, false caso contrário.
     */
    private boolean isSelfClosing(String rawTag, String tagName) {
        return tagExtractor.isSelfClosingTag(tagName) || rawTag.endsWith("/");
    }

    /**
     * Processa a tag usando o processador de pilha.
     *
     * @param processor Instância de TagStackProcessor.
     * @param rawTag Conteúdo cru da tag.
     * @param tagName Nome normalizado da tag.
     * @param selfClosing Indica se a tag é auto-fechável.
     * @return true se a tag for processada com sucesso, false em caso de erro estrutural.
     */
    private boolean processTag(TagStackProcessor processor, String rawTag, String tagName, boolean selfClosing) {
        return processor.processTag(rawTag, tagName, selfClosing);
    }
}
