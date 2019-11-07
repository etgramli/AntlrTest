package de.etgramlich.antlr.parser.listener.bnf.type.terminal;

import de.etgramlich.antlr.parser.listener.bnf.type.BnfType;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.RhsType;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public abstract class AbstractId implements BnfType, RhsType {
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
    public boolean isTerminal() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public List<RhsType> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return id;
    }
}
