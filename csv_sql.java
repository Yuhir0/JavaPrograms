/*
 * Program to convert csv (or other) to sql (MySQL syntax)
 */

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class csv_sql {

    // Tabulation
    public static final String TAB="\t";

    // End of line
    public static final String ENDL="\n";

    // End of qwery
    public static final String ENDQ=";" + ENDL;

    // The extension that the program will reed.
    public static final String EXTENSION=".tsv";

    // You can decide which separator to use.
    public static final String SEPARATOR="\t";

    // SQL sintax used
    public static final String NEW_DATABASE="CREATE DATABASE IF NOT EXISTS ";
    public static final String USE="USE ";
    public static final String NEW_TABLE="CREATE TABLE ";
    public static final String DROP_TABLE="DROP TABLE IF EXISTS ";
    public static final String ID=TAB + "`ID` INT NOT NULL AUTO_INCREMENT," + ENDL;
    public static final String INSERT="INSERT INTO ";
    public static final String VALUES=" VALUES (";

    // A filter of 'toxic' characters, these characters will be removed.
    public static final char[] FILTER={':', ' ', '`', '"', '\''};


    /* Main of the program, open the files and call all functions */
    public static void csv_to_sql (String csv_file_path, String sql_file_path, String dbname, String table_name) throws IOException {
        BufferedReader csv_file = OpenFile.r(csv_file_path + EXTENSION);
        BufferedWriter sql_file = OpenFile.w(sql_file_path + ".sql");

        log("START conversion " + csv_file_path + EXTENSION + " to " + sql_file_path + ".sql");
        long time_start = System.currentTimeMillis();

        // CREATE DATABASE
        sql_file.write(NEW_DATABASE + "`" + dbname + "`" + ENDQ + USE + "`" + dbname + "`" + ENDL + ENDL);
        log("Successfully CREATE DATABASE");

        // CREATE TABLE
        sql_file.write(DROP_TABLE + "`" + table_name + "`" + ENDQ);
        String[] table_fields = csv_file.readLine().split(SEPARATOR);
        create_table(sql_file, table_name, table_fields);
        log("Successfully CREATE TABLE");

        // INSERTS
        make_inserts(csv_file, sql_file, table_name, table_fields.length);
        sql_file.write("ALTER TABLE `" + table_name + "` ADD COLUMN `ID` INT AUTO_INCREMENT PRIMARY KEY;");

        csv_file.close();
        sql_file.close();
        long time_end = System.currentTimeMillis();
        log("END conversion " + csv_file_path + EXTENSION + " to " + sql_file_path + ".sql (" + ((time_end - time_start) / 1000.0) + "s)");
        System.out.println("Ok. (" + ((time_end - time_start) / 1000.0) + "s)");
    }

    /* Make transform the first line of csv file to a syntax of CRATE TABLE */
    public static void create_table (BufferedWriter sql_file, String table_name, String[] table_fields) throws IOException {
        sql_file.write(NEW_TABLE + "`" + table_name + "` (" + ENDL);
        for (int i = 0; i < table_fields.length; i++) {
            if (i + 1 != table_fields.length){
                sql_file.write(TAB + check_value(table_fields[i], '`') + " VARCHAR(100)," + ENDL);
            } else {
                sql_file.write(TAB + check_value(table_fields[i], '`') + " VARCHAR(100))" + ENDQ + ENDL);
            }
        }
    }

    public static void make_inserts (BufferedReader csv_file, BufferedWriter sql_file, String table_name, int num_fields) throws IOException {
        String csv_line = null;
        int errors = 0;
        int lines = 0;
        while ((csv_line = csv_file.readLine()) != null){
            lines++;
            String[] fields = csv_line.split(SEPARATOR);
            if (fields.length == num_fields){
                sql_file.write(INSERT + "`" + table_name + "`" + VALUES);
                for (int i = 0; i < fields.length; i++){
                    if (i + 1 != fields.length){
                        sql_file.write(check_value(fields[i], '\'') + ",");
                    } else {
                        sql_file.write(check_value(fields[i], '\'') + ")" + ENDQ);
                    }
                }
            } else {
                log("W: excelty " + num_fields + " fields, " + fields.length + " fields given");
                System.out.println(ENDL + csv_line + ENDL + "W: excelty " + num_fields + " fields, " + fields.length + " fields given");
                errors++;
            }
        }
        log("Successfully " + (lines - errors) + " INSERTS of " + lines);
    }

    public static String check_value (String value, char quote){
        String new_value = "";
        for (int i = 0; i < value.length(); i++){
            if (!char_in(value.charAt(i), FILTER)){
                new_value += value.charAt(i) + "";
            }
        }
        if (new_value.equals("")){
            return "NULL";
        }
        return quote + new_value + quote;
    }

    public static boolean char_in (char value_in, char[] char_list){
        for (char c: char_list){
            if (value_in == c){
                return true;
            }
        }
        return false;
    }

    public static boolean char_in (char value_in, String text){
        for (int i = 0; i < text.length(); i++){
            if (value_in == text.charAt(i)){
                return true;
            }
        }
        return false;
    }

    /* Log register, write in a log file */
    public static void log (String log_text) throws IOException {
        try{
            BufferedWriter log_file = OpenFile.a("csv_sql.log");
            DateFormat log_format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            Date date = new Date();
            log_file.write(log_format.format(date) + " > " + log_text + ENDL);
            log_file.close();
        } catch (IOException e) {
            System.out.println("E: " + e.getMessage());
            log("E: " + e.getMessage());
        }
    }

    /* Control input for avoid errors on the path */
    private static String input_control (String text) throws IOException {
        while (true) {
            String result = Inputs.str_input(text);
            if (result.length() == 0 || char_in(' ', result)){
                log("E: Empty path or invalid path");
                System.out.println("E: Enter any path or delete spaces");
            } else {
                String[] splited_unix = result.split("/");
                if (char_in('.',splited_unix[splited_unix.length - 1])){
                    log("E: Entred path with extension");
                    System.out.println("E: Enter a path without extension");
                } else {
                    return result;
                }
            }
        }
    }

    public static void main (String args[]) throws IOException {
        String csv_file_path = "";
        String sql_file_path = "";
        try{
            csv_file_path = input_control("Enter " + EXTENSION.substring(1) + " file path: ");
            sql_file_path = input_control("Enter sql file path: ");
            String dbname = input_control("Enter name of Database: ");
            String table_name =  input_control("Enter name of Table: ");
            csv_to_sql(csv_file_path, sql_file_path, dbname, table_name);
        } catch (IOException e) {
            System.out.println("E: " + e.getMessage());
            log("E: " + e.getMessage());
            log("END " + csv_file_path + EXTENSION + " to " + sql_file_path + ".sql");
        } catch (Exception e) {
            System.out.println("E: " + e.getMessage());
            log("E: " + e.getMessage());
        }
    }
}
