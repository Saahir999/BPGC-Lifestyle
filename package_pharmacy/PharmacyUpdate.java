package package_pharmacy;

import java.util.ArrayList;

import driver.BackException;
import driver.Helper;
import driver.SafeInput;

class PharmacyUpdate extends PharmacyApp implements ReadData{

    @Override
    public void readDrugs() throws BackException{

        String name = "";
        ArrayList<Integer> index = new ArrayList<Integer>();


        // Check if data is null in temp storage 
        if(data.isEmpty()){
            initialise();
        }
        resetScreen();

        while(true){
            resetScreen();
            try {
                SafeInput.inputLine();
                // choice for column header then proceed as previously or apply some other way

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

    void updateData(){
        // Check if data is null in temp storage 
        if(data.isEmpty()){
            initialise();
        }
        resetScreen();
        
        // System.out.println(data);
        // updateDatabase();
        // System.out.println(data);

        while(true){
            try{
                SafeInput.inputLine();


            }catch(BackException e){
                return;
            }catch(Exception e){

            }
        }

    }

    void addDrugs(){
        ArrayList<Object> columnHead = new ArrayList<Object>();
        ArrayList<ArrayList<Object>> contents = new ArrayList<ArrayList<Object>>();
        // Check if data is null in temp storage 
        if(data.isEmpty()){
            initialise();
        }
        resetScreen();

        while(true){
            try{
                SafeInput.inputLine();

                // accept inputs for all column headers
                // Check for type mismatch using try catch for each input
                
                setData(columnHead, contents);


            }catch(BackException e){

            }catch(Exception e){

            }
        }
    }
    
}
