import java.io.*;

/*
 * Only simplfys the syntax for open files with BufferedReader and BufferedWriter
 */
public class OpenFile {
    
    /*
     * Open a file in read mode.
     * Syntax: 'BufferedReader file = OpenFile.r("path/file.ext")'
     */
    public static BufferedReader r (String file_path) throws IOException {
        return new BufferedReader( new FileReader( new File(file_path)));
    }
    
    /*
     * Open a file in write mode.
     * Syntax: 'BufferedWriter file = OpenFile.w("path/file.ext")'
     */
    public static BufferedWriter w (String file_path) throws IOException {
        return new BufferedWriter( new FileWriter( new File(file_path)));
    }

    /*
     * Open a file in apend mode. 
     * Syntax: 'BufferedWriter file = OpenFile.a("path/file.ext")'
     */
    public static BufferedWriter a (String file_path) throws IOException {
        return new BufferedWriter( new FileWriter( new File(file_path), true));
    }
}
