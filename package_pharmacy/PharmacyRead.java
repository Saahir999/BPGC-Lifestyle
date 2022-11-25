package package_pharmacy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import driver.BackException;
import driver.Helper;
import driver.SafeInput;
import driver.UI;


class PharmacyRead extends PharmacyApp implements ReadData{


    /** Offers user a word input to read data, entering nothing displays everything
     * @see package_pharmacy.ReadData#readDrugs()
     **/
    @Override
    public void readDrugs() throws BackException{
        
        HashMap<String,Integer> displayRestrictOrder = new HashMap<String,Integer>(); 
        displayRestrictOrder.put("Name",-1);
        displayRestrictOrder.put("Cost",-1);


        String name = "";
        int traverse = 0;
        ListIterator<Object> listIterator;
        Iterator<String> iterator;
        Iterator<Integer> dispIterator;
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

            listIterator = data.get("0Name").listIterator();

            if(name.isBlank()){
                while(listIterator.hasNext()){
                    listIterator.next();
                    index.add(listIterator.previousIndex());     
                }
            }

            while(listIterator.hasNext()){
                if(name.equals(((String)listIterator.next()).toLowerCase())){
                    index.add(listIterator.previousIndex());
                    break;
                }
            }

            resetScreen();
            System.out.println("The result(s) are");

            iterator = data.keySet().iterator();
            while(iterator.hasNext()){
                System.out.print(iterator.next().substring(1)+"\t");               
            }

            
            dispIterator = index.iterator();
            while(dispIterator.hasNext()){
                int i = dispIterator.next();
                iterator = data.keySet().iterator();
                System.out.println();
                while(iterator.hasNext()){
                    String t = iterator.next();
                    System.out.print(data.get(t).get(i)+"\t");               
                }

                System.out.println();
            }
            
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
