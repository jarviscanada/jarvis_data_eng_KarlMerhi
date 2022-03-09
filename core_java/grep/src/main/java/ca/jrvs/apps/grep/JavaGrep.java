// javaGrep
package ca.jrvs.apps.grep;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {

    /**
     * Top level search workflow.
     * @throws IOException if fails
     */
    void process() throws IOException;

    /**
     * Traverse a given directory and return all files.
     * @param rootDir the input directory
     * @return files under the rootDir
     */
    List<File> listFiles(String rootDir) throws IOException;

    /**
     * Read a file and return all the lines.
     *
     * Explain FileReader, BufferedReader, and character encoding.
     *
     * @param inputFile the file to be read
     * @return lines from the file
     * @throws IllegalArgumentException if a given inputFile is not a file
     */
    List<String> readLines(File inputFile) throws FileNotFoundException, IOException;

    /**
     * Checks if a line contains the regex pattern (passed by the user).
     *
     * @param line the input string
     * @return true if there is a match, false otherwise
     */
    boolean containsPattern(String line);

    /**
     * Write lines to a file.
     *
     * Explore: FileOutputStream, OutputStreamWriter, and BufferedWriter.
     *
     * @param lines the lines to be written
     * @throws IOException if write failed
     */
    void writeToFile(List<String> lines) throws IOException;

    /**
     * Returns the current root path.
     * @return the current root path
     */
    String getRootPath();

    /**
     * Sets the root path.
     * @param rootPath the root path to be set
     */
    void setRootPath(String rootPath);

    /**
     * Returns the current regex pattern.
     * @return the current regex pattern
     */
    String getRegex();

    /**
     * Sets the regex pattern.
     * @param regex the regex pattern to be set
     */
    void setRegex(String regex);

    /**
     * Returns the current output file.
     * @return the current output file
     */
    String getOutFile();

    /**
     * Sets the output file.
     * @param outFile the output file to be set
     */
    void setOutFile(String outFile);
}
