package driver;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public final class UI {
    public static enum Color {
        RESET("\u001B[0m", "\u001B[0m"),
        BLACK("\u001B[30m", "\033[40m"),
        RED("\u001B[31m", "\u001B[41m"),
        GREEN("\u001B[32m", "\u001B[42m"),
        YELLOW("\u001B[33m", "\u001B[43m"),
        BLUE("\u001B[34m", "\u001B[44m"),
        PURPLE("\u001B[35m", "\u001B[45m"),
        CYAN("\u001B[36m", "\u001B[46m"),
        WHITE("\u001B[37m", "\u001B[47m");

        public final String textColor;
        public final String backgroundColor;

        private Color(String textColor, String backgroundColor) {
            this.textColor = textColor;
            this.backgroundColor = backgroundColor;
        }
    }

    private static int SCREENWIDTH = 52;
    
    public static void showSplashScreen() {
        clearScreen();
        setTextColor(Color.YELLOW);
        String credits = Helper.readTextFile("driver/splashscreen.txt");
        String[] creditsLines = credits.split("\n");
        for (int i = 0; i < creditsLines.length; i++) {
            System.out.println(creditsLines[i]);
            Helper.sleep(0.1);
        }
        resetTextColor();
        System.out.println();
        printFittedLine(" press "+ UI.Color.YELLOW.backgroundColor + UI.Color.BLACK.textColor +"ENTER"+ UI.Color.RESET.textColor + UI.Color.RESET.backgroundColor +" to begin ", '/');
        SafeInput.waitForInput();
    }

    public static void printChoices(String[] choices) {
        for (int i = 0; i < choices.length; i++) {
            System.out.print("[");
            setTextColor(Color.YELLOW);
            System.out.print(i+1);
            resetTextColor();
            System.out.println("] " + choices[i]);
        }
    }

    // Private functions
    
    private static void printHeader() {
        // Print the banner
        setTextColor(Color.CYAN);
        System.out.println("           ____  ____   ___   ___                 ");
        System.out.println("          (  _ \\(  _ \\ / __) / __)                ");
        System.out.println("           ) _ ( ) __/( (_ \\( (__                 ");
        System.out.println("          (____/(__)   \\___/ \\___)                ");
        System.out.println(" __    __  ____  ____  ____  ____  _  _  __    ____ ");
        System.out.println("(  )  (  )(  __)(  __)/ ___)(_  _)( \\/ )(  )  (  __)");
        System.out.println("/ (_/\\ )(  ) _)  ) _) \\___ \\  )(   )  / / (_/\\ ) _) ");
        System.out.println("\\____/(__)(__)  (____)(____/ (__) (__/  \\____/(____)");
        System.out.println();
        resetTextColor();
        
        // Print the commands
        setTextColor(Color.YELLOW);
        printBoxed(" commands: BACK | EXIT ", '#', 1);
        resetTextColor();
    }
    
    private static void clearScreen() {
        System.out.print("\f");
        System.out.print("\033[H\033[2J");
        try {
            String operatingSystem = System.getProperty("os.name"); //Check the current operating system
            
            if(operatingSystem.contains("Windows")) {        
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
            else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                
                startProcess.waitFor();
            } 
        }
        catch(Exception e) {
            System.out.println(e);
        }
        System.out.flush();
    }
    
    // General functions

    public static void setTextColor(Color color) {
        System.out.print(color.textColor);
    }

    public static void resetTextColor() {
        System.out.print(Color.RESET.textColor);
    }
    
    static void resetScreen() {
        clearScreen();
        printHeader();
    }
    
    static void printNavBar(String text) {
        setTextColor(Color.CYAN);
        printFittedLine(" " + text + " ", '<', '>');
        resetTextColor();
        System.out.println();
        System.out.println();
    }
    
    static void showExitScreen() {
        resetScreen();
        System.out.println();
        printBoxed(String.join("\n",
        "Thank you for using BPGC Lifestyle, hope you enjoyed!"
        ), '/', 1);
        SafeInput.waitForInput();

        System.exit(0);
    }
    
    public static void printError(Exception err) {
        // Switch to red color for error
        setTextColor(Color.RED);
        
        // Handle NumberFormatException
        if(err instanceof NumberFormatException) {
            printError("WRONG INPUT FORMAT");
            return;
        }
        
        resetTextColor();
    }
    
    public static void printError(String err) {
        // Switch to red color for error
        setTextColor(Color.RED);
        
        System.out.println();
        String s = String.join(
        "\n",
        err,
        "----------------------",
        "Press [ENTER] to retry"
        );
        printBoxed(s, '-', 1);
        
        SafeInput.waitForInput();
        
        resetTextColor();
    }
    
    public static void printBoxed(File file, char outline_char, int horizontal_padding) {
        printBoxed(Helper.readTextFile(file), outline_char, horizontal_padding);
    }
    
    public static void printBoxed(String text, char outline_char, int horizontal_padding) {
        String[] textLines = text.split("\n");
        for(int i = 0; i < textLines.length; ++i) {
            textLines[i] = textLines[i].replaceAll("\\s+", " ");
        }
        
        // Constraints
        
        int largestWordLength = 0;
        for (int i = 0; i < textLines.length; i++) {
            String line = textLines[i];
            for(String word: line.split("\\s+")) {
                largestWordLength = Math.max(word.length(), largestWordLength);
            }
        }
        int minOccupiedLength = 2 + 2*horizontal_padding + largestWordLength;
        SCREENWIDTH = Math.max(SCREENWIDTH, minOccupiedLength);
        
        int effective_length = SCREENWIDTH - 2 - 2*horizontal_padding;
        LinkedList<String> FINAL = new LinkedList<String>();
        for(String line: textLines) {
            int running_length = 0;
            
            LinkedList<LinkedList<String>> lines = new LinkedList<LinkedList<String>>();
            lines.add(new LinkedList<String>());
            for(String word: line.split("\\s+")) {
                if(running_length + lines.getLast().size() + word.length() > effective_length) {
                    // Push word to new line
                    running_length = word.length();
                    LinkedList<String> newLine = new LinkedList<String>();
                    newLine.add(word);
                    lines.add(newLine);
                }
                else {
                    lines.getLast().add(word);
                    running_length += word.length();
                }
            }
            
            // Add all the lines formed from this line to FINAL
            for(LinkedList<String> lineTemp: lines) {
                String lineString = "";
                for(String word: lineTemp) {
                    lineString += word + " ";
                }
                FINAL.add(lineString.trim());
            }
        }
        
        String output = "";
        
        // First, add the starting line
        output += repeatChar(outline_char, SCREENWIDTH) + "\n";
        
        for (String line : FINAL) {
            int right_spaces = (effective_length / 2) - (line.length() / 2);
            int left_spaces = effective_length - right_spaces - line.length();
            output += (
            outline_char
            + " "
            + repeatChar(' ', left_spaces)
            + line
            + repeatChar(' ', right_spaces)
            + " "
            + outline_char
            ) + "\n";
        }
        
        // Finally, add the ending line
        output += repeatChar(outline_char, SCREENWIDTH);
        
        System.out.println(output);
    }
    
    private static String repeatChar(char c, int count) {
        String output = "";
        for (int i = 0; i < count; i++) {
            output += c;
        }
        return output;
    }

    public static void printFittedLine(String message, char paddingCharacter) {
        printFittedLine(message, paddingCharacter, paddingCharacter);
    }

    /**
     * Returns the given String without any Color Codes
     */

    private static String removeColor(String text) {
        for(Color i : new Color[]{
            Color.RESET, Color.BLACK, Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.PURPLE, Color.CYAN, Color.WHITE
        }) {
            text = text.replace(i.textColor, "");
            text = text.replace(i.backgroundColor, "");
        }
        return text;
    }

    public static void printFittedLine(String message, char leftCharacter, char rightCharacter) {
        int len = removeColor(message).length();
        int halfLength = (SCREENWIDTH - len) / 2;
        String leftString = repeatChar(leftCharacter, halfLength);
        String rightString = repeatChar(rightCharacter, halfLength) + (halfLength*2 != (SCREENWIDTH-len) ? rightCharacter : "");
        System.out.print(leftString + message + rightString);
    }

    public static String alignCenter(String text, int length) {
        int spacesLength = length - removeColor(text).length();
        return repeatChar(' ', spacesLength / 2) + text + repeatChar(' ', spacesLength - (spacesLength / 2));
    }

    public static String alignLeft(String text, int length) {
        int spacesLength = length - removeColor(text).length();
        return text + repeatChar(' ', spacesLength);
    }

    public static String alignRight(String text, int length) {
        int spacesLength = length - removeColor(text).length();
        return repeatChar(' ', spacesLength) + text;
    }

    public static void colorPrint(String text) {
        if(!text.contains("^") && !text.contains("@")) {
            System.out.print(text);
            return;
        };

        HashMap<String, Color> colorcodes = new HashMap<String, Color>();
        colorcodes.put("c", Color.CYAN);
        colorcodes.put("r", Color.RED);
        colorcodes.put("R", Color.RESET);
        colorcodes.put("y", Color.YELLOW);
        colorcodes.put("b", Color.BLACK);
        colorcodes.put("w", Color.WHITE);

        for(String code: colorcodes.keySet()) {
            text = text.replace("^"+code, colorcodes.get(code).textColor);
            text = text.replace("@"+code, colorcodes.get(code).backgroundColor);
        }
        System.out.print(text);
    }

    public static void colorPrintln(String text) {
        colorPrint(text + "\n");
    }
}
