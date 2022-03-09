package ca.jrvs.apps.practice;

public interface RegexExec {
    /**
     * return true if filename extensions is jpg ot jpeg (case insensitive)
     * @param filename
     * @return
     */
    public boolean matchJpeg(String filename);

    /**
     * return true if ip is valid
     * to simplify the problem, IP address range is from 0.0.0.0 to 999.999.999.999
     * @param ip
     * @return
     */
    public boolean match(String ip);

    /**
     * return true if line is empty (e.g. empty, white space, tabs, etc...)
     * @param line
     * @return
     */
    public boolean isEmptyLine(String line);
}
