package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class JavaGrepImp implements JavaGrep{

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

    public static void main(String[] args){
        if(args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        //Use default logger config
        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRegex(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try {
            javaGrepImp.process();
        } catch (Exception ex) {
            javaGrepImp.logger.error("Error: Unable to process", ex);
        }
    }

    @Override
    public void process() throws IOException {

        List<String> matchedLines = new ArrayList<>();
        List<File> files = listFiles(this.getRootPath());

        // Read each line in each file and find matched lines.
        for (File file: files) {
            for (String line: readLines(file)) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) throws IOException {
        File rootFile = new File(rootDir);
        File [] temp = rootFile.listFiles();
        List<File> files = new ArrayList<>();

        if(rootFile.isFile()) {
            files.add(rootFile);
            return files;
        }

        if (temp == null) {
            return files;
        }

        for (File file : temp) {
            if (file.isFile())
                files.add(file);
            else {
                files.addAll(listFiles(file.getAbsolutePath()));
            }
        }
        return files;
    }

    @Override
    public List<String> readLines(File inputFile)  throws IOException, FileNotFoundException {
        List<String> lines  = new ArrayList<>();
        try {
            lines = Files.readAllLines(inputFile.toPath());
        } catch (FileNotFoundException message) {
            logger.error("Error! Cannot Find File", message);
        } catch (IOException message) {
            logger.error("Error unable to process",message);
        }

        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        return line.matches(getRegex());
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        FileWriter fileOut = new FileWriter(outFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileOut);

        for(String temp: lines) {
            try {
                bufferedWriter.write(temp);
                bufferedWriter.newLine();
            } catch (IOException message) {
                logger.error("Error! Cannot Write To File.", message);
            }
        } bufferedWriter.close();
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }
}
