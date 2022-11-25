package package_pharmacy;

class PharmacyUpdate extends PharmacyApp implements ReadData{

    @Override
    public void readDrugs() {
        // Check if data is null in temp storage 
        if(data.isEmpty()){
            // read data from excel and store in HashMap data
        }
        resetScreen();

        // Offer the user data output based on various column values (<,>,==, in case of numbers)



    }

    
    
}
