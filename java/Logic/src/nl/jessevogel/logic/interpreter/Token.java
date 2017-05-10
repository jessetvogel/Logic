package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.basic.Sense;

public class Token {

    private int line;
    private int column;

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public Token setLine(int line) {
        this.line = line;
        return this;
    }

    public Token setColumn(int column) {
        this.column = column;
        return this;
    }

    public static class CharToken extends Token {

        public final char c;

        public CharToken(char c) {
            // Store char
            this.c = c;
        }
    }

    public static class StringToken extends Token {

        public final String str;

        public StringToken(String str) {
            // Store string
            this.str = str;
        }
    }

    public static class SenseToken extends Token {

        public final Sense sense;

        public SenseToken(Sense sense) {
            // Store sense
            this.sense = sense;
        }
    }
}
