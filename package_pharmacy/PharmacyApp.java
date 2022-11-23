package package_pharmacy;

import java.util.HashMap;
import java.util.ArrayList;

import driver.App;
import driver.BackException;
import driver.Helper;
import driver.SafeInput;


public class PharmacyApp extends App{
    HashMap<String,ArrayList> data = new HashMap<String,ArrayList>();

    @Override
    public String getAppName(){
        return "PharmacyApp";
    }

    @Override
    public void start() throws BackException{

        int c;
        PharmacyApp pharmacy;

        // Usage of super class is to maintain the "cache" the 
        //temporarily stored HashMap of read Data for access

        while(true){
            try {
            c = SafeInput.inputInteger();
            System.out.println("[1] Customer mode");
            System.out.println("[2] Admin mode");
            System.out.println("[3] EXIT");

            switch(c){
                case 1:
                    pharmacy = new PharmacyRead();
                    ((PharmacyRead)pharmacy).readDrugs();
                    
                break;
                case 2:
                    pharmacy = new PharmacyUpdate();
                break;
                case 3:
                break;
                default:
                    continue;
            }
            }catch (NumberFormatException e) {
                System.out.println("Please enter a integer number");
                Helper.sleep(2);
                continue;
            }catch (BackException e){
                return;
            } 
    }
}
}