package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.log.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Scanner {

    private final String filename;
    private String fileContents;
    private int fileLength;
    private static final char CHARACTER_NEWLINE = '\n';

    private int pointer, line, column;
    private char currentChar;

    public Scanner(String filename) {
        // Store the filename
        this.filename = filename;
    }

    public void scan() {
        if(fileContents != null) {
            // If already scanned, give a warning and stop
            Log.warning("Scanner.scan() called while already scanned");
            return;
        }

        try {
            // Open file for reading
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));

            // Store the entire file in one string
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(CHARACTER_NEWLINE);
            }

            // Create string
            fileContents = sb.toString();
            fileLength = fileContents.length();

            // Close file
            br.close();
        }
        catch(IOException e) {
            // TODO: maybe we must do something here
            e.printStackTrace();
        }
    }

    public String getFilename() {
        // Return the name of the file
        return filename;
    }

    public int getLine() {
        // Return the current line number
        return line;
    }

    public int getColumn() {
        // Return the current column number
        return column;
    }

    public char firstCharacter() {
        if(fileContents == null) {
            // If not yet scanned, give a warning, and then scan
            Log.warning("Scanner.firstCharacter() was called, but there was not yet scanned");
            scan();
        }

        // Set pointer to beginning, and return the first character
        pointer = 0;
        line = 0;
        column = 0;
        currentChar = fileContents.charAt(pointer);
        return currentChar;
    }

    public char nextCharacter() {
        // If we already reached the end of the file, return 0
        if(reachedEnd()) return 0;

        // Update pointers and line numbers
        pointer ++;
        if(reachedEnd()) return 0;
        if(currentChar == CHARACTER_NEWLINE) {
            line = 0;
            column = 0;
        }
        else {
            column ++;
        }

        // Get new character and return it
        currentChar = fileContents.charAt(pointer);
        return currentChar;
    }

    public boolean reachedEnd() {
        return pointer == fileLength;
    }
}