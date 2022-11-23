package package_pharmacy;
import driver.UI;

class PharmacyRead extends PharmacyApp implements ReadData{

    @Override
    public void readDrugs() {
        // Check if data is null in temp storage 
        if(data == null){
            // read data from excel and store in HashMap data
        }
        resetScreen();

        // Offer user a word input to read data, entering nothing displays all

    }
    
}
