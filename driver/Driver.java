package driver;

// Import all apps
import package_bmi.BMIApp;
import package_exercise.ExerciseApp;
import package_pharmacy.PharmacyApp;
 
public final class Driver {
    public static void main(String[] args) {
        // Infinite Loop
        MainLoop:
        while(true) {
            UI.resetScreen();

            System.out.println();
            System.out.println("[1] BMI Calculator");
            System.out.println("[2] Exercise App");
            System.out.println("[3] Pharmacy App");
            System.out.println("[4] EXIT");
            System.out.println();
    
            System.out.print("Enter your choice: ");
            
            // Enter user's choice
            String choice = "";
            try {
                choice = SafeInput.inputLine().trim().toLowerCase();
            }
            catch(BackException e) {
                break;
            }

            // Based on user input
            App app = null;
    
            switch (choice) {
                case "1":
                    app = new BMIApp();
                    break;
                case "2":
                    app = new ExerciseApp();
                    break;
                case "3":
                app = new PharmacyApp();
                    break;
                case "4":
                    break MainLoop;
                default:
                    UI.printError("INVALID OPTION");
                    continue MainLoop;
            }
            
            try {
                app.start();
            }
            catch(BackException e) {}
        }
        
        UI.showExitScreen();
    }
}