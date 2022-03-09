package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp {

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        // creating JavaGrepLambdaImp instead of JavaGrepImp
        // JavaGrepImp inherits all methods except two override methods
        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try {
            // calling parent method
            // but it will call override method (in this class)
            javaGrepLambdaImp.process();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * implement using Lambda and Stream APIs
     *
     */
    @Override
    public List<File> listFiles(String rootDir) throws IOException {
        Stream<Path> path = Files.walk(Paths.get(rootDir));
        return path.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
    }

    /**
     * implement using Lambda and Stream APIs
     *
     */
    @Override
    public List<String> readLines(File inputFile) throws IOException {
        List<String> fileLines = new ArrayList<>();
        Stream<String> stream = Files.lines(Paths.get(inputFile.getAbsolutePath()));
        stream.forEach(fileLines::add);
        stream.close();
        return fileLines;
    }
}

