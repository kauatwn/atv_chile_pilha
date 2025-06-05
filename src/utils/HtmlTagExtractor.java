package utils;

import java.util.Set;

public class HtmlTagExtractor {
    // Conjunto de tags HTML que são auto-fecháveis (não precisam de tag de fechamento)
    private static final Set<String> SELF_CLOSING_TAGS = Set.of(
            "area", "base", "br", "col", "embed", "hr", "img", "input",
            "link", "meta", "param", "source", "track", "wbr");

    // Prefixos para identificação de tipos especiais de conteúdo em tags HTML
    public static final String COMMENT_PREFIX = "!--";
    public static final String DOCTYPE_PREFIX = "!DOCTYPE";
    public static final String PROCESSING_INSTRUCTION_PREFIX = "?";

    // Tags especiais que podem requerer tratamento diferenciado
    public static final String SCRIPT_TAG = "script";
    public static final String STYLE_TAG = "style";

    /**
     * Extrai o nome da tag HTML do conteúdo fornecido.
     * <p>
     * O método procura pelo primeiro delimitador (espaço em branco, '>' ou '/')
     * para determinar o fim do nome da tag.
     *
     * @param tagContent o conteúdo completo da tag HTML (incluindo '<' e '>')
     * @return o nome da tag em letras minúsculas, ou string vazia se o conteúdo for inválido
     */
    public String extractTagName(String tagContent) {
        // Verifica se a string é nula ou vazia para evitar NullPointerException
        if (tagContent == null || tagContent.isEmpty()) {
            return "";
        }

        // Percorre cada caractere do conteúdo da tag para encontrar o nome
        for (int i = 0; i < tagContent.length(); i++) {
            char c = tagContent.charAt(i);

            // Verifica se o caractere atual é um delimitador que indica fim do nome da tag
            if (Character.isWhitespace(c) || c == '>' || c == '/') {
                // Retorna a substring do início até o caractere anterior ao delimitador
                return tagContent.substring(0, i);
            }
        }

        // Caso nenhum delimitador seja encontrado, assume todo o conteúdo como nome da tag
        return tagContent;
    }

    /**
     * Verifica se a tag deve ser ignorada com base em seu conteúdo.
     * <p>
     * Tags como comentários, declarações DOCTYPE e instruções de processamento
     * são normalmente ignoradas durante a análise HTML.
     *
     * @param tagContent o conteúdo da tag a ser verificado
     * @return true se a tag deve ser ignorada, false caso contrário
     */
    public boolean shouldIgnoreTag(String tagContent) {
        return tagContent.startsWith(COMMENT_PREFIX) ||
                tagContent.toUpperCase().startsWith(DOCTYPE_PREFIX) ||
                tagContent.startsWith(PROCESSING_INSTRUCTION_PREFIX);
    }

    /**
     * Verifica se a tag é do tipo auto-fechável (self-closing).
     * <p>
     * Tags auto-fecháveis como &lt;img/&gt; ou &lt;br/&gt; não requerem
     * uma tag de fechamento separada.
     *
     * @param tagName o nome da tag a ser verificada
     * @return true se a tag é auto-fechável, false caso contrário
     */
    public boolean isSelfClosingTag(String tagName) {
        return SELF_CLOSING_TAGS.contains(tagName.toLowerCase());
    }

    /**
     * Verifica se a tag é especial (como script ou style).
     * <p>
     * Tags especiais geralmente contêm conteúdo que não deve ser interpretado
     * como HTML padrão.
     *
     * @param tagName o nome da tag a ser verificada
     * @return true se a tag é especial, false caso contrário
     */
    public boolean isSpecialTag(String tagName) {
        return SCRIPT_TAG.equalsIgnoreCase(tagName) ||
                STYLE_TAG.equalsIgnoreCase(tagName);
    }
}
// Esta classe é responsável por extrair o nome de uma tag HTML a partir de seu conteúdo.
// Ela também verifica se a tag deve ser ignorada (como comentários ou instruções de processamento),
// se é uma tag de fechamento automático (self-closing) ou se é uma tag especial (como <script> ou <style>).