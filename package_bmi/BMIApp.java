package package_bmi;

import driver.App;
import driver.BackException;
import driver.Helper;
import driver.SafeInput;
import driver.UI;
import driver.UI.Color;

public class BMIApp extends App {
    // Constants
    private static final double UNDERWEIGHT_LT = 18.5;
    private static final double HEALTHY_LT = 25;
    private static final double OVERWEIGHT_LT = 30;
    private static final double OBESE_LT = 35;
    // Severely obese is greater than 35 BMI

    private int weightUnitChoice = -1; // 1 for kg, 2 for lb
    private double weight = -1;
    private int heightUnitChoice = -1; // 1 for cm, 2 for ft'in
    private double height = -1;
    private int ft = -1;
    private int in = -1;
    private double bmi = -1;

    private void setTestVariables() {
        weightUnitChoice = 1;
        weight = -1;
        heightUnitChoice = 1;
        height = -1;
    }

    @Override
    public void start() {
        try {
            // setTestVariables();
            getWeightUnits();
        }
        catch(BackException e) {
            return;
        }
        catch(Exception e) {
            start();
        }
    }

    @Override
    public String getAppName() {
        return "BMI Calculator";
    }

    private void getWeightUnits() throws BackException {
        resetScreen();

        System.out.println("Choose a unit of weight:");
        UI.printChoices(new String[]{"Metric System (kg)", "Imperial System (lb)"});

        System.out.print("\nEnter your choice: ");

        weightUnitChoice = SafeInput.inputInteger(weightUnitChoice);

        if(weightUnitChoice != 1 && weightUnitChoice != 2) {
            weightUnitChoice = -1;
            UI.printError(new NumberFormatException());
            throw new NumberFormatException();
        }
        
        try {
            takeWeightInput();
        }
        catch(BackException e) {
            weightUnitChoice = -1;
            start();
        }
    }

    private void takeWeightInput() throws BackException {
        resetScreen();

        System.out.print("Enter your weight ("+(weightUnitChoice==1?"kg":"lb")+"): ");

        weight = SafeInput.inputDouble(weight);
        
        if(weight <= 0) {
            weight = -1;
            UI.printError(new NumberFormatException());
            throw new NumberFormatException();
        }

        try {
            getHeightUnits();
        }
        catch(BackException e) {
            weight = -1;
            start();
        }
    }

    private void getHeightUnits() throws BackException {
        // Bypass choice if already done
        if(heightUnitChoice != -1) {
            try{
                takeHeightInput();
            }
            catch(BackException e) {
                heightUnitChoice = -1;
                start();
            }
            return;
        }

        System.out.println();
        System.out.println("Choose a unit of length:");
        UI.printChoices(new String[]{"Metric System (cm)", "Imperial System (ft'in)"});

        System.out.print("\nEnter your choice: ");

        heightUnitChoice = SafeInput.inputInteger(heightUnitChoice);

        if(heightUnitChoice != 1 && heightUnitChoice != 2) {
            heightUnitChoice = -1;
            UI.printError(new NumberFormatException());
            throw new NumberFormatException();
        }

        start();
    }

    private void takeHeightInput() throws BackException {
        if(heightUnitChoice == 1) {
            // Normal, in cm
            System.out.print("Enter your height (cm): ");
            height = SafeInput.inputDouble(height);
            
            if(height <= 0) {
                height = -1;
                UI.printError(new NumberFormatException());
                throw new NumberFormatException();
            }

            displayBMI();
        }
        else {
            // In ft/in
            takeFtInput();
        }
    }

    private void takeFtInput() throws BackException {
        System.out.print("Enter your height (just the 'ft' part): ");

        ft = SafeInput.inputInteger(ft);

        if(ft <= 0) {
            ft = -1;
            UI.printError(new NumberFormatException());
            throw new NumberFormatException();
        }

        try {
            takeInInput();
        }
        catch(BackException e) {
            ft = -1;
            start();
        }
    }

    private void takeInInput() throws BackException {
        System.out.print("Enter your height (just the 'in' part): ");

        in = SafeInput.inputInteger(in);

        if(in < 0) {
            in = -1;
            UI.printError(new NumberFormatException());
            throw new NumberFormatException();
        }

        displayBMI();
    }

    /**
     * Calculates the weight from the units chosen and stores in the weight attribute
     */

    private void calculateWeight() {
        if(weightUnitChoice == 1) {
            // Calculate in kg
            // Do nothing
        }
        else {
            // Convert lb to kg
            weight = weight * 0.453592;
        }
    }

    /**
     * Calculates the height from the units chosen and stores in the height attribute
     */

    private void calculateHeight() {
        if(heightUnitChoice == 1) {
            // Calculate in cm
            // Do nothing
        }
        else {
            // Convert ft'in to cm
            height = (ft * 12 + in) * 2.54;
        }
    }

    private void displayBMI() {
        calculateWeight();
        calculateHeight();

        bmi = weight / ((height/100) * (height/100));

        printBMICategories();
        
        System.out.println();
        SafeInput.waitForInput(1);
    }

    private String doubleTo2DP(double val) {
        String output = val + ".00";
        return output.substring(0, output.indexOf(".")+3);
    }

    private void printBMICategories() {
        String bmiString = doubleTo2DP(bmi);

        double[] w = {
            reverseCalculateWeight(35, height),
            reverseCalculateWeight(30, height),
            reverseCalculateWeight(25, height),
            reverseCalculateWeight(18.5, height)
        };

        if(weightUnitChoice == 2) {
            for (int i = 0; i < w.length; i++) {
                w[i] = kgToLb(w[i]);
            }
        }

        System.out.println();
        System.out.println("+----------------+-----------+--------------+");
        System.out.println("|    category    | bmi range | weight range |");
        System.out.println("+----------------+-----------+--------------+");
        UI.colorPrintln("| "+checkBMI("SEVERELY OBESE", 0)+" |   > 35    |"+UI.alignCenter("> " + doubleTo2DP(w[0]), 14)+"|");
        UI.colorPrintln("|      "+checkBMI("OBESE", 1)+"     |   30-35   |"+UI.alignCenter("< " + doubleTo2DP(w[0]), 14)+"|");
        UI.colorPrintln("|    "+checkBMI("OVERWEIGHT", 2)+"  |   25-30   |"+UI.alignCenter("< " + doubleTo2DP(w[1]), 14)+"|");
        UI.colorPrintln("|     "+checkBMI("HEALTHY", 3)+"    |  18.5-25  |"+UI.alignCenter("< " + doubleTo2DP(w[2]), 14)+"|");
        UI.colorPrintln("|   "+checkBMI("UNDERWEIGHT", 4)+"  |  < 18.5   |"+UI.alignCenter("< " + doubleTo2DP(w[3]), 14)+"|");
        System.out.println("+----------------+-----------+--------------+");

        System.out.println();
        UI.colorPrintln("Your BMI is ^b@w"+bmiString+"^R@R");
        UI.colorPrintln("Your healthy weight range is ^b@w" + doubleTo2DP(w[3]) + (weightUnitChoice==1?"kg":"lb") + "^R@R to ^b@w" + doubleTo2DP(w[2]) + (weightUnitChoice==1?"kg":"lb") + "^R@R");
    }

    private String checkBMI(String text, int bmiCategory) {
        boolean isTheOne = false;

        switch(bmiCategory) {
            case 0: isTheOne = bmi > 35;
                break;
            case 1:
                isTheOne = bmi <= 35 && bmi > 30;
                break;
            case 2:
                isTheOne = bmi <= 30 && bmi > 25;
                break;
            case 3:
                isTheOne = bmi <= 25 && bmi > 18.5;
                break;
            case 4:
                isTheOne = bmi <= 18.5;
                break;
        }

        if(isTheOne) return "^b@w" + text + "^R@R";

        return text;
    }

    private double kgToLb(double kg) {
        return kg / 0.453592;
    }

    private double reverseCalculateWeight(double bmi, double height) {
        return (height / 100) * (height / 100) * bmi;
    }
}
