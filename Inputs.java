import java.io.*;

/*
 * This class is to control inputs
 */
public class Inputs {
    public static BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    
    /* 
     * If input value can't convert to integer, 
     * show an error message and request it again.
     */
    public static int int_input(String text) throws IOException {
        while (true) {
            try{
                System.out.print(text);
                return Integer.parseInt(r.readLine());
            } catch (Exception e){
                System.out.println("Incorrect value");
            }
        }
    }
    
    /* 
     * The same of int_input method and control the input is in the range
     */
    public static int int_input_range(String text, int range1, int range2) throws IOException {
        int num = range1 - 1;
        do {
            try{
                num = int_input(text);
            } catch (Exception e){
                System.out.println("Incorrect value");
                continue;
            }
            if (num < range1 || num > range2) {
                System.out.println("Incorrect value");
            }
        } while (num < range1 || num > range2);
        return num;
    }
    
    /*
     * If input value can't convert to float, 
     * show an error message and request it again.
     */
    public static float float_input(String text) throws IOException {
        while (true){
            try{
                System.out.print(text);
                return Float.parseFloat(r.readLine());
            } catch (Exception e){
                System.out.println("Incorrect value");
            }
        }
    }


    /* 
     * The same of flot_input method and control the input is in the range
     */
    public static float float_input_range(String text, float range1, float range2) throws IOException {
        float num = range1 - 1;
        do {
            try{
                num = float_input(text);
            } catch (Exception e){
                System.out.println("Incorrect value");
                continue;
            }
            if (num < range1 || num > range2) {
                System.out.println("Incorrect value");
            }
        } while (num < range1 || num > range2);
        return num;
    }

    /*
     * String input control, and simplifys the syntax of BufferedReader --> InputStreamReader
     */
    public static String str_input(String text) throws IOException {
        while (true){
            try{
                System.out.print(text);
                return r.readLine();
            } catch (Exception e){
                System.out.println("Incorrect value");
            }
        }
    }
}

