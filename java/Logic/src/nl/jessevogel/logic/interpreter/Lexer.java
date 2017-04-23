package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;

public class Lexer {

    private Scanner scanner;
    private ArrayList<Token> tokens;
    private char currentChar;

    private static final Token TOKEN_IGNORE = new Token();
    private static final String specialCharacters = "(){}[]<>._-+*/\\^";
    private static final String wordCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private int amountOfTokens;
    private int position;
    private Token currentToken;

    public Lexer(String filename) {
        // Setup for analyzing
        scanner = new Scanner(filename);
    }

    public void analyze() {
        if(tokens != null) {
            // If already scanned, give a warning and stop
            Log.warning("Lexer.analyze() called while already analyzed");
            return;
        }

        // Scan with the scanner!
        scanner.scan();

        // Group characters to tokens
        tokens = new ArrayList<>();
        currentChar = scanner.firstCharacter();
        Token token;
        while((token = readToken()) != null) {
            // If this token should be ignored, continue
            if(token == TOKEN_IGNORE) continue;

            // Otherwise, add it to the list of tokens
            tokens.add(token);
        }

        // Count how many tokens there are
        amountOfTokens = tokens.size();
    }

    private Token readToken() {
        // If we reached the end of the file, stop and return null
        if(scanner.reachedEnd()) return null;

        // Check for whitespace, if any was found, ignore it
        if (java.lang.Character.isWhitespace(currentChar)) {
            currentChar = scanner.nextCharacter();
            while (java.lang.Character.isWhitespace(currentChar)) {
                currentChar = scanner.nextCharacter();
            }
            return TOKEN_IGNORE;
        }

        // Check for comments, if any was found, ignore it
        if (currentChar == '#' && scanner.getColumn() == 0) {
            int currentLine = scanner.getLine();
            currentChar = scanner.nextCharacter();
            while (scanner.getLine() == currentLine)
                currentChar = scanner.nextCharacter();
            Log.debug("Comment found"); // TODO: remove this line
            return TOKEN_IGNORE;
        }

        // Check for special characters
        if (specialCharacters.indexOf(currentChar) != -1) {
            Token token = new Token.CharToken(currentChar);
            currentChar = scanner.nextCharacter();
            return token;
        }

        // Check for words
        if (wordCharacters.indexOf(currentChar) != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append(currentChar);
            currentChar = scanner.nextCharacter();
            while (wordCharacters.indexOf(currentChar) != -1) {
                sb.append(currentChar);
                currentChar = scanner.nextCharacter();
            }
            return new Token.StringToken(sb.toString());
        }

        // If none of the above was the case, give a warning
        Log.warning("Unexpected symbol '" + currentChar + "' in " + scanner.getFilename() + " at line " + scanner.getLine() + " at position " + scanner.getColumn());
        return null;
    }

    public Token firstToken() {
        if(tokens == null) {
            // If not yet analyzed, give a warning, and then analyze
            Log.warning("Scanner.firstCharacter() was called, but there was not yet scanned");
            analyze();
        }

        // Set position to beginning, and return the first character
        position = 0;
        currentToken = tokens.get(0);
        return currentToken;
    }

    public Token nextToken() {
        // If we already reached the end of the list of tokens, return null
        if(reachedEnd()) return null;

        // Update position
        position ++;
        if(reachedEnd()) return null;

        // Get new character and return it
        currentToken = tokens.get(position);
        return currentToken;
    }

    public int getPosition() {
        // Return the current position
        return position;
    }

    public boolean reachedEnd() {
        return position == amountOfTokens;
    }

    public String createString(int startPosition, int endPosition) {
        // Create a string by concatenating the contents of the tokens between start and end position
        StringBuilder sb = new StringBuilder();
        for(int i = startPosition;i < endPosition;i ++) {
            Token token = tokens.get(i);
            if(token instanceof Token.CharToken)
                sb.append(((Token.CharToken) token).c);

            if(token instanceof Token.StringToken)
                sb.append(((Token.StringToken) token).str);
        }
        return sb.toString();
    }
}