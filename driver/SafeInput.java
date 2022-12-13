package driver;

import java.util.Scanner;

import driver.UI.Color;

public class SafeInput {
    private static Scanner sc;

    static {
        sc = new Scanner(System.in);
    }

    private static final void validateInput(String input) throws BackException {
        if(input.equals("BACK")) {
            throw new BackException();
        }
        if(input.equals("EXIT")) {
            UI.showExitScreen();
        }
    }

    /**
     * Returns the user input as a String
     * @return The user input
     * @throws BackException
     */
    
    public static final String inputLine() throws BackException {
        UI.setTextColor(Color.YELLOW);

        String input = sc.nextLine();
        validateInput(input);

        UI.resetTextColor();

        return input;
    }

    /**
     * Returns the user input as an int
     * @return The user input
     * @throws BackException
     * @throws NumberFormatException
     */
    
    public static final int inputInteger(int fallback) throws BackException, NumberFormatException {
        try {
            if(fallback == -1) return Integer.parseInt(inputLine());
            
            UI.setTextColor(Color.YELLOW);
            System.out.println(fallback);
            UI.resetTextColor();
            return fallback;
        }
        catch(NumberFormatException err) {
            UI.printError(err);

            throw err;
        }
    }
    
    public static final int inputInteger() throws BackException, NumberFormatException {
        try {
            return Integer.parseInt(inputLine());
        }
        catch(NumberFormatException err) {
            UI.printError(err);

            throw err;
        }
    }

    /**
     * Returns the user input as a double
     * @return The user input
     * @throws BackException
     * @throws NumberFormatException
     */
    
    public static final double inputDouble(double fallback) throws BackException, NumberFormatException {
        try {
            if(fallback == -1) return Double.parseDouble(inputLine());
            
            UI.setTextColor(Color.YELLOW);
            System.out.println(fallback);
            UI.resetTextColor();
            return fallback;
        }
        catch(NumberFormatException err) {
            UI.printError(err);

            throw err;
        }
    }
    
    public static final double inputDouble() throws BackException, NumberFormatException {
        try {
            return Double.parseDouble(inputLine());
        }
        catch(NumberFormatException err) {
            UI.printError(err);

            throw err;
        }
    }
    
    /**
     * Pauses execution until the user enters some value
     */

    public static final void waitForInput() {
        sc.nextLine();
    }
    
    /**
     * Pauses execution until the user enters some value
     */

    public static final void waitForInput(int x) {
        UI.printFittedLine(" press "+ UI.Color.YELLOW.backgroundColor + UI.Color.BLACK.textColor +"ENTER"+ UI.Color.RESET.textColor + UI.Color.RESET.backgroundColor +" to continue ", '/');
        System.out.println();
        sc.nextLine();
    }
}