## Introduction
The purpose of this project was to create a program that has similar features to the `grep` command in the linux bash terminal, it is able to search for regex patterns recursively within a directory, and output the matching lines to a seperate file. 

The application takes three arguments:
```
1. The regex pattern to match within the file
2. The root directory to search the files for the regex pattern
3. The path to where the output file will be written (can be a pre-existing file or not)
```

This grep application was designed using Java with the Mavin framework, git version control to keep track of the changes on the feature/CoreJavaGrep branch, IntelliJ Ultimate as the IDE, can be launched from a Jar file or from inside a docker container.

<img height="32" width="32" src="https://raw.githubusercontent.com/github/explore/5b3600551e122a3277c2c5368af2ad5725ffa9a1/topics/java/java.png" 
     />
<img height="32" width="32" src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/git/git.png" />
<img height="32" width="32" src="https://cdn.jsdelivr.net/npm/simple-icons@v5/icons/github.svg" />
<img height="32" width="32" src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/maven/maven.png" />
<img height="32" width="32" src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/docker/docker.png" />

## Quick Start
- Compile and Package the Java code with Maven:
```
mvn clean compile package
```
- Launch the JVM implementation and run the app using JAR:
```
java -cp [PATH_TO_JAR_FILE] [regex] [rootPath] [outputFile]
```
- Can also run the application within the Docker container:
```
docker run --rm {docker_user}/grep [regex pattern] [rootPath] [outFile]
```

## Implemenation
### Pseudocode  
process( ) method pseudocode:
```
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

### Performance Issue
The performance issue that I ran into when designing the first version of the grep application was when the program tried to process a large amount of data it would give the error message `outOfMemory`, this is caused when there is insufficient space in the heap and it overflows. This issue was resolved by updating the grep interface by replacing the List with Buffer/Stream instead.

## Test
How did you test your application manually? (e.g. prepare sample data, run some test cases manually, compare result)
The testing of the application was done manually by comparing the output file of my program to the sample output using the same sample text files.

## Deployment
1. Maven was used to package the project `mvn clean compile package` with all the dependencies in the pom.xml file.
2. A docker file was created locally on my machine in order to create a docker image.
3. The Dockerfile was deployed on DockerHub.

## Improvement
The application is still missing a lot of functionality that is available with the original `grep` command in bash. This program can therefore be improved upon by adding further features.
