package de.etgramlich.antlr.parser.listener.type.terminal;

import de.etgramlich.antlr.parser.listener.type.BnfType;
import de.etgramlich.antlr.util.visitor.BnfElement;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public abstract class AbstractId implements BnfType, BnfElement {
    private static final String HOST_LANG_KEYWORD_FILENAME = "src/main/resources/keywords-java.txt";
    private static List<String> keywords = Collections.emptyList();
    static {
        try {
            keywords = Files.readAllLines(Path.of(HOST_LANG_KEYWORD_FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final String id;

    @Contract(pure = true)
    AbstractId(final String id) {
        if (keywords.contains(id)) {
            throw new IllegalArgumentException("ID is a keyword of the host language: " + id);
        }
        this.id = id;
    }

    public String getText() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        visitor.visit(this);
    }
}
