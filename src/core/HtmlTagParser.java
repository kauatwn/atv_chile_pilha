package core;

public class HtmlTagParser {

    /**
     * Encontra a posição do caractere '>' que fecha uma tag HTML, ignorando os que
     * aparecem dentro de valores de atributos entre aspas.
     *
     * @param content O conteúdo HTML completo a ser analisado
     * @param startIndex A posição inicial para começar a busca (após o '<' da tag)
     * @return A posição do '>' de fechamento ou -1 se não encontrar
     */
    public int findTagClosingBracket(String content, int startIndex) {
        // Flags para controlar quando estamos dentro de aspas
        boolean insideDoubleQuotes = false;
        boolean insideSingleQuotes = false;

        // Percorre o conteúdo a partir da posição inicial
        for (int i = startIndex; i < content.length(); i++) {
            char c = content.charAt(i);

            // Verifica aspas duplas (ignora se estiver dentro de aspas simples)
            if (c == '"' && !insideSingleQuotes) {
                insideDoubleQuotes = !insideDoubleQuotes;  // Alterna o estado
                continue;  // Pula para o próximo caractere
            }

            // Verifica aspas simples (ignora se estiver dentro de aspas duplas)
            if (c == '\'' && !insideDoubleQuotes) {
                insideSingleQuotes = !insideSingleQuotes;  // Alterna o estado
                continue;  // Pula para o próximo caractere
            }

            // Quando encontrar um '>' fora de aspas, retorna sua posição
            if (c == '>' && !insideDoubleQuotes && !insideSingleQuotes) {
                return i;
            }
        }

        // Se chegou ao final sem encontrar o fechamento
        return -1;
    }

    /**
     * Avança o índice de análise para pular o conteúdo de tags especiais como
     * <script> e <style> que podem conter caracteres que se parecem com tags HTML.
     *
     * @param content O conteúdo HTML completo
     * @param currentIndex Posição atual de análise (após a abertura da tag especial)
     * @param tagName Nome da tag especial (ex: "script" ou "style")
     * @return A nova posição após o fechamento da tag especial ou -1 se não encontrar
     */
    public int skipSpecialTagContent(String content, int currentIndex, String tagName) {
        // Constrói a tag de fechamento esperada (ex: </script>)
        String endTag = String.format("</%s>", tagName);

        // Procura a tag de fechamento a partir da posição atual
        int endIndex = content.indexOf(endTag, currentIndex);

        // Se não encontrar a tag de fechamento
        if (endIndex == -1) {
            return -1;  // Indica HTML mal formado
        }

        // Retorna a posição após o fechamento da tag especial
        return endIndex + endTag.length();
    }
}
// Implementa algoritmos específicos de parsing de HTML (encontrar fechamento de tags, pular conteúdo especial).
// Este parser é usado para analisar o conteúdo HTML e encontrar as tags, além de pular o conteúdo interno de tags especiais como <script> e <style>.
// Ele fornece métodos para encontrar o fechamento de uma tag e pular o conteúdo de tags especiais, garantindo que a estrutura HTML seja validada corretamente.