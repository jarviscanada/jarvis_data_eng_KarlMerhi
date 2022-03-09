package ca.jrvs.apps.practice;

public class RegexExecImpl implements RegexExec{
    @Override
    public boolean matchJpeg(String filename) {
        String pattern = ".*\\.jpe?g$";
        String normFilename = filename.toLowerCase(); // Normalize filename.

        return normFilename.matches(pattern);
    }

    @Override
    public boolean match(String ip) {
        String pattern = "^(\\d{1,3}\\.){3}(\\d{1,3})$";

        return ip.matches(pattern);
    }

    @Override
    public boolean isEmptyLine(String line) {
        String pattern = "\\s*";

        return line.matches(pattern);
    }
}
