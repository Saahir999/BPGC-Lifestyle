package package_pharmacy;

class PharmacyUpdate extends PharmacyApp implements ReadData{

    @Override
    public void readDrugs() {
        // Check if data is null in temp storage 
        if(data.isEmpty()){
            initialise();
        }
        resetScreen();

        



    }

    
    
}
