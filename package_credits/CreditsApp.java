package package_credits;


import driver.App;
import driver.BackException;
import driver.Helper;
import driver.SafeInput;
import driver.UI;

public class CreditsApp extends App {
    @Override
    public void start() {
        resetScreen();
        showCredits();
    }

    @Override
    public String getAppName() {
        return "Credits";
    }

    private static void showCredits() {
        UI.setTextColor(UI.Color.GREEN);
        String credits = Helper.readTextFile("driver/credits.txt");
        UI.printBoxed(credits, '#', 1);
        UI.resetTextColor();
        SafeInput.waitForInput(1);
    }
}
