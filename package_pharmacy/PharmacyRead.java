package package_pharmacy;
import java.util.ArrayList;

import driver.BackException;
import driver.Helper;
import driver.SafeInput;


class PharmacyRead extends PharmacyApp implements ReadData{


    /** Offers user a word input to read data, entering nothing displays everything
     * @see package_pharmacy.ReadData#readDrugs()
     **/
    @Override
    public void readDrugs() throws BackException{

        String name = "";
        ArrayList<Integer> index = new ArrayList<Integer>();

        // Check if data is null in temp storage 
        if(data.isEmpty()){
           initialise();
        }

        while(true){
            resetScreen();
            try {
            System.out.println("Enter a Drug name for info");
            System.out.println("Enter nothing for all info");
            System.out.println();

            name = SafeInput.inputLine().trim().toLowerCase();
            
            try{
                Integer.parseInt(name);
                System.out.println("Please enter a name");
                Helper.sleep(3);
                continue;
            }catch(NumberFormatException e){};

            index = searchMap(name, "0Name");

            resetScreen();
            System.out.println("The result(s) are");

           display(index);

            System.out.println("Enter any key to go back");
            SafeInput.waitForInput();
            return;
            
            }catch (BackException e){
                return;
            }catch(Exception e){
                System.out.println(e);
                e.printStackTrace();
                Helper.sleep(30);
            }
        }
    }   
}
