package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlFileReader {

    public String readContent(String filePath) {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
// Faz apenas I/O (leitura de arquivos), sem lógica de negócio.
// Não possui dependências externas, exceto as da biblioteca padrão do Java.