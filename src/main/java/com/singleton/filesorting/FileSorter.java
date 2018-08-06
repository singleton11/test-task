package com.singleton.filesorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileSorter {

    public static void main(String[] args) throws FileNotFoundException {
        var inputFileName = args[0];
        var outputFileName = args[1];
        var chunkSize = Integer.parseInt(args[2]); // Lines count

        var scanner = new Scanner(new File(inputFileName));

        var strings = new ArrayList<String>();

        var lineNumber = 0;
        var chunkNumber = 0;

        // Sort each chunk and persist in separate file
        while (scanner.hasNextLine()) {
            var string = scanner.next();

            if (lineNumber == chunkSize) {
                lineNumber = 0;

                sortAndPersistStrings(strings, chunkNumber, inputFileName, chunkSize);

                chunkNumber++;

                strings = new ArrayList<>();
            }

            strings.add(string);
            lineNumber++;
        }
        chunkNumber++;
        sortAndPersistStrings(strings, chunkNumber, inputFileName, chunkSize);

        // Remove temporary files
        for (int i = 0; i <= chunkNumber; i++) {
            new File(inputFileName + "." + i).delete();
        }
    }

    private static void sortAndPersistStrings(List<String> strings, int chunkNumber, String inputFileName, int chunkSize) throws FileNotFoundException {
        var stringArray = strings.toArray();
        Arrays.sort(stringArray);
        var writer = new PrintWriter(inputFileName + '.' + chunkNumber);

        for (int i = 0; i < strings.size(); i++) {
            writer.println(stringArray[i]);
        }

        writer.close();
    }
}
