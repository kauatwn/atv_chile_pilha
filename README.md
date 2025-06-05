# Validador de Estrutura HTML

Um validador que verifica se um arquivo HTML possui estrutura correta (tags balanceadas e bem formadas).

1. `HtmlFileReader`
    - **Responsabilidade:** Leitura de arquivos HTML.
    - **Função principal:** `readContent(String filePath)` lê o conteúdo completo de um arquivo.
    - **Observações:**
        - Opera apenas com I/O.
        - Sem lógica de negócio ou validação.
        - Usa apenas biblioteca padrão (`java.nio.file`).

2. `HtmlFileValidator`
    - **Responsabilidade:** Validação inicial do arquivo.
    - **Validações:**
        - Verifica se o caminho termina com `.html` ou `.htm`.
        - **Função principal:** `validate(String filePath)` lança exceção se a extensão for inválida.
    - **Observações:**
        - Responsável por garantir que o arquivo tem uma extensão HTML antes de qualquer leitura ou parsing.

3. `HtmlStructureValidator` (Classe principal)
    - **Responsabilidade:** Orquestra toda a validação da estrutura HTML.
    - **Dependências:**
        - `HtmlFileValidator` (validação inicial)
        - `HtmlFileReader` (leitura do conteúdo)
        - `HtmlTagExtractor` (extração de tags)
        - `HtmlTagParser` (análise de tags)
    - **Fluxo de validação:**
        1. Valida o arquivo (extensão)
        2. Lê o conteúdo do arquivo
        3. Percorre caractere a caractere identificando tags
        4. Processa cada tag conforme as regras:
            - Ignora tags especiais (comentários, DOCTYPE)
            - Trata tags auto-fecháveis
            - Valida aninhamento e fechamento das tags
    - **Método principal:** `isValidHtml(String filePath)` retorna true/false

4. `HtmlTagExtractor`
    - **Responsabilidade:** Extração e análise de tags HTML.
    - **Principais funções:**
        - `extractTagName(String tagContent)` – extrai o nome da tag, ignorando atributos.
        - `shouldIgnoreTag(String tagContent)` – detecta comentários, DOCTYPEs e instruções de processamento.
        - `isSelfClosingTag(String tagName)` – reconhece tags auto-fecháveis (`<img>`,`<br>`, etc.).
        - `isSpecialTag(String tagName)` – identifica tags cujo conteúdo deve ser ignorado (`<script>`,`<style>`).
    - **Observações:**
        - Trabalha com análise semântica das tags.

5. `HtmlTagParser`
    - **Responsabilidade:** Análise detalhada de conteúdo HTML.
    - **Principais funções:**
        - `findTagClosingBracket(...)` – localiza o caractere `>` de fechamento da tag, respeitando aspas dentro da tag.
        - `skipSpecialTagContent(...)` – avança o índice após o fechamento de `<script>` ou `<style>`.
    - **Observações:**
        - Cuida de aspectos técnicos do parsing, como aspas aninhadas e blocos especiais.

6. `TagStackProcessor`
    - **Responsabilidade:** Controle da pilha de tags.
    - **Estrutura:** Usa uma pilha customizada `MyStack<String>`.
    - **Função principal:** `processTag(...)`:
        - Empilha tags de abertura
        - Desempilha e verifica correspondência com tags de fechamento
        - Trata tags auto-fecháveis
        - Verifica se todas as tags foram fechadas corretamente
    - **Validação extra:** Garante que não há caracteres inválidos como `<` e `>` no nome da tag.
    - **Observações:**
        - Responsável por validar o **aninhamento** e **fechamento correto** das tags.

7. `HtmlValidatorUI`
    - **Responsabilidade:** Interface gráfica para seleção de arquivo e exibição do resultado.
    - **Funcionalidade principal:**
        - Abre um seletor de arquivos (`JFileChooser`).
        - Valida o arquivo selecionado com `HtmlStructureValidator`.
        - Exibe o resultado com `JOptionPane` (mensagem de sucesso ou erro).
    - **Observações:**
        - Não possui lógica de negócio — apenas interação com o usuário.

8. `MyStack<T>`
    - **Responsabilidade:** Estrutura de dados de pilha genérica.
    - **Características principais:**
        - Implementada com nós encadeados (`Node<T>`).
        - Permite empilhar (`push`), desempilhar (`pop`) e inspecionar o topo (`peek`).
        - Inclui método `toString()` para visualização da pilha.
    - **Observações:**
        - Não usa bibliotecas prontas como `java.util.Stack`.
        - Projetada para aprendizado, com controle total sobre a estrutura.
        - Lança exceções para operações inválidas (ex.: `pop` em pilha vazia).
        - Genérica, podendo ser reutilizada em outros contextos, não apenas HTML.

9. `Node<T>`
    - **Responsabilidade:** Representar um nó da pilha.
    - **Atributos:**
        - `value`: o valor armazenado no nó.
        - `next`: referência para o próximo nó.
    - **Funções principais:**
        - Getters e setters padrão.
        - `toString()` retorna a representação do valor armazenado.
    - **Observações:**
        - Simples, porém essencial para o funcionamento encadeado da `MyStack`.
        - Imutabilidade parcial: `value` é constante, mas `next` pode ser alterado.

10. `Main`
    - **Responsabilidade:** Ponto de entrada da aplicação.
    - **Função principal:**
        - Inicializa `HtmlValidatorUI` e chama `show()`.
    - **Observações:**
        - Classe mínima.
        - Permite executar o projeto diretamente.

## Fluxo Principal

1. Usuário seleciona um arquivo através da interface
2. O sistema valida a extensão do arquivo
3. O conteúdo é lido e analisado
4. Cada tag é processada verificando:
    - Formatação correta
    - Aninhamento adequado
    - Fechamento correspondente
5. O resultado é exibido ao usuário (válido/inválido)