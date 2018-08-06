package com.singleton.filesorting;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Random;

public class FileGenerator {

    public static void main(String[] args) throws FileNotFoundException {
        var fileName = args[0];
        var lineSize = Integer.parseInt(args[1]);
        var lineNumber = Integer.parseInt(args[2]);

        var writer = new PrintWriter(fileName);

        var random = new Random();

        for (int i = 0; i < lineNumber; i++) {
            var byteArray = new byte[random.nextInt(lineSize) + 1];
            random.nextBytes(byteArray);

            writer.println(new String(byteArray, Charset.forName("UTF-8")));
        }

        writer.close();
    }
}
