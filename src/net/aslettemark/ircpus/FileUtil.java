/*
 *  Copyright (C) 2015-2018 Aksel H. Slettemark http://aslettemark.net/
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.aslettemark.ircpus;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static ArrayList<String> readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static ArrayList<String> readFile(File file) throws IOException {
        final ArrayList<String> lines = new ArrayList<>();
        String line = "";
        try (
                final FileReader reader = new FileReader(file);
                final BufferedReader buffer = new BufferedReader(reader);
        ) {
            while ((line = buffer.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static String toSingleLine(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String s : lines) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static void append(String text, File file) throws IOException {
        FileWriter writer = new FileWriter(file, true);
        writer.write(text);
        writer.flush();
        writer.close();
    }
}