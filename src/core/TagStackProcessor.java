package core;

import utils.stack.MyStack;

import java.util.ArrayList;
import java.util.List;

public class TagStackProcessor {

    // Pilha para armazenar as tags abertas aguardando fechamento
    private final MyStack<String> stack = new MyStack<>();

    /**
     * Processa uma tag HTML (abertura, fechamento ou auto-fechamento)
     *
     * @param rawTag Conteúdo bruto da tag (ex: "div" ou "/div")
     * @param tagName Nome normalizado da tag (em minúsculas, sem atributos)
     * @param isSelfClosing Indica se é uma tag auto-fechável (ex: <img/>)
     * @return true se a tag for válida, false se houver erro de estrutura
     */
    public boolean processTag(String rawTag, String tagName, boolean isSelfClosing) {
        // Validação básica do nome da tag
        if (tagName.isEmpty() || containsInvalidCharacters(tagName)) {
            return false;
        }

        boolean isClosing = rawTag.startsWith("/"); // Verifica se é tag de fechamento

        // Tags auto-fecháveis não podem ter versão de fechamento (ex: </img>)
        if (isSelfClosing) {
            return !isClosing; // Válido apenas se não for tag de fechamento
        }

        // Erro se tentar fechar tag quando não há tags abertas
        if (isClosing && stack.isEmpty()) {
            return false;
        }

        // Processamento de tag de fechamento
        if (isClosing) {
            String top = stack.pop(); // Remove a última tag aberta
            System.out.printf("FECHANDO: </%s> | Pilha após pop: %s%n", tagName, stack);
            return tagName.equals(top); // Verifica se corresponde à tag aberta
        }

        // Processamento de tag de abertura
        stack.push(tagName); // Adiciona à pilha
        System.out.printf("ABRINDO: <%s> | Pilha após push: %s%n", tagName, stack);
        return true;
    }

    /**
     * Retorna uma lista de todas as tags não fechadas restantes na pilha
     *
     * @return Lista de nomes de tags não fechadas
     */
    public List<String> getRemainingUnclosedTags() {
        List<String> remainingTags = new ArrayList<>();
        // Esvazia a pilha e coleta todas as tags não fechadas
        while (!stack.isEmpty()) {
            remainingTags.add(stack.pop());
        }
        return remainingTags;
    }

    /**
     * Verifica se a pilha de tags está vazia
     *
     * @return true se a pilha estiver vazia, false caso contrário
     */
    public boolean isStackEmpty() {
        return stack.isEmpty();
    }

    /**
     * Valida se o nome da tag contém caracteres inválidos
     */
    private boolean containsInvalidCharacters(String tagName) {
        return tagName.contains("<") || tagName.contains(">");
    }
}
// Gerencia a lógica central de empilhamento/desempilhamento de tags durante a validação.
// Este processador de pilha é responsável por gerenciar a pilha de tags abertas e fechadas.
// Ele processa as tags HTML, garantindo que as tags de abertura e fechamento estejam corretamente empilhadas e desempilhadas.
// Se uma tag de fechamento não corresponder à última tag aberta, ele retorna false.
// Ele também verifica se as tags são auto-fechadas e garante que não haja caracteres inválidos nas tags.
// A classe usa uma pilha personalizada (MyStack) para armazenar as tags abertas e verificar a correspondência com as tags fechadas.
// Se a pilha estiver vazia no final do processamento, significa que todas as tags foram fechadas corretamente.
// Se a pilha não estiver vazia, significa que há tags abertas sem fechamento correspondente, o que resulta em uma estrutura HTML inválida.
// A classe imprime mensagens de depuração para cada operação de abertura e fechamento de tags, mostrando o estado atual da pilha após cada operação.
// A classe também verifica se as tags contêm caracteres inválidos, como '<' ou '>', que não são permitidos em nomes de tags HTML.