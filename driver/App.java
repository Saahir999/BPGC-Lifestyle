package driver;

public abstract class App {
    public abstract void start() throws BackException;

    public final void resetScreen() {
        UI.resetScreen();
        System.out.println();
        UI.printNavBar(this.getAppName());
    }
    
    public abstract String getAppName();
}