package com.singleton.filesorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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

                sortAndPersistStrings(strings, chunkNumber, inputFileName);

                chunkNumber++;

                strings = new ArrayList<>();
            }

            strings.add(string);
            lineNumber++;
        }
        sortAndPersistStrings(strings, chunkNumber, inputFileName);

        // Merge stage
        var scanners = new Scanner[chunkNumber + 1];
        var iterators = new String[chunkNumber + 1];
        for (int i = 0; i <= chunkNumber; i++) {
            scanners[i] = new Scanner(new File(inputFileName + '.' + i));
        }
        var activeIteratorCount = 0;
        var writer = new PrintWriter(outputFileName);
        do {
            // If one of string iterator is null, update it
            for (int i = 0; i <= chunkNumber; i++) {
                if (iterators[i] == null) {
                    if (scanners[i].hasNextLine()) {
                        try {
                            iterators[i] = scanners[i].next();
                            activeIteratorCount++;
                        } catch (NoSuchElementException e) {

                        }
                    }
                }
            }

            // Find a minimum string and write it to result file
            int min = 0;
            for (int i = 0; i <= chunkNumber; i++) {
                if (iterators[min] == null) {
                    if (iterators[i] != null) {
                        min = i;
                    }
                    continue;
                }
                if (iterators[i] == null) {
                    continue;
                }
                if (iterators[i].compareTo(iterators[min]) < 0) {
                    min = i;
                }
            }

            writer.println(iterators[min]);
            iterators[min] = null;
            activeIteratorCount--;

        } while (activeIteratorCount > 0);

        // Remove temporary files
        for (int i = 0; i <= chunkNumber; i++) {
            new File(inputFileName + "." + i).delete();
        }

        writer.close();
    }

    private static void sortAndPersistStrings(List<String> strings, int chunkNumber, String inputFileName) throws FileNotFoundException {
        var writer = new PrintWriter(inputFileName + '.' + chunkNumber);
        strings.stream().sorted().forEach(writer::println);
        writer.close();
    }
}
