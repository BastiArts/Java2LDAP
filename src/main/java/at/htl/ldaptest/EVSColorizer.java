/*
 * ************************************************
 *            -= Sebastian Schiefermayr =-       
 *                     4AHITM   
 *  > EVSColorizer
 *  > 12:31:09
 * 
 *  E-Mail: basti@bastiarts.com 
 *  Web: https://bastiarts.com
 *  Github: https://github.com/BastiArts
 * ************************************************
 */
package evs.ldapconnection;

/**
 *
 * @author Basti
 */
public class EVSColorizer {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static String red(){
        return RED;
    }

    public static String black(){
        return BLACK;
    }

    public static String green(){
        return GREEN;
    }
    public static String yellow(){
        return YELLOW;
    }
    public static String blue(){
        return BLUE;
    }

    public static String purple(){
        return PURPLE;
    }
    
    public static String cyan(){
        return CYAN;
    }
    
    public static String white(){
        return WHITE;
    }
    
    public static String reset(){
        return RESET;
    }
}
