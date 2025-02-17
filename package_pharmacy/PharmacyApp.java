package package_pharmacy;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import driver.App;
import driver.BackException;
import driver.SafeInput;
import driver.Helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class PharmacyApp extends App{

    /**
     * It stores column headers as String keys with a integer as the 
     * first character to indicate their order in the database
     * Example :- Name column in database will be registered as 0Name here, the second
     * column if named - say Cost will be stored as 1Cost and so on.
     */
    protected HashMap<String,ArrayList<Object>> data = new HashMap<String,ArrayList<Object>>();

    @Override
    public String getAppName(){
        return "PharmacyApp";
    }

    @Override
    public void start() throws BackException{

        int c = 0;
        int c2 = 0;
        PharmacyApp pharmacy;

        // Usage of super class is to maintain the "cache" the 
        //temporarily stored HashMap of read Data for access

        while(true){
            resetScreen();
            try {
            System.out.println("[1] Customer mode");
            System.out.println("[2] Admin mode");
            System.out.println("[3] EXIT");
            System.out.println();
    
            System.out.print("Enter your choice: ");
            c = SafeInput.inputInteger();

            switch(c){
                case 1:
                    pharmacy = new PharmacyRead();
                    ((PharmacyRead)pharmacy).readDrugs();
                    
                break;
                case 2:
                    pharmacy = new PharmacyUpdate();
                    UpdateLoop:while(true){
                        resetScreen();
                        try{
                            System.out.println("[1] Scour drugs");
                            System.out.println("[2] Add drugs");
                            System.out.println("[3] Update drug");
                            System.out.println("[4] EXIT");
                            c2 = SafeInput.inputInteger();
                            switch(c2){
                                case 1:
                                    ((PharmacyUpdate)pharmacy).readDrugs();
                                break;
                                case 2:
                                    ((PharmacyUpdate)pharmacy).addDrugs();
                                break;
                                case 3:
                                    ((PharmacyUpdate)pharmacy).updateData();
                                break;
                                case 4:
                                break UpdateLoop;
                                default :
                                    continue;
                            }
                        }catch (BackException e){
                            break;
                        } 
                    }
                break;
                case 3:
                    return;
                default:
                    continue;
            }
            
            // catch (NumberFormatException e) {
            //     System.out.println("Please enter a integer number");
            //     Helper.sleep(2);
            //     continue;
            }catch (BackException e){
                return;
            } 
        }
    }

    /**
     * 
     * @param name Column header names in Excel database
     * @param searchFor Column header names as present in data, refer to data
     *  variable storage approach
     * @return indexes of elements which match the search logic
     */
    protected ArrayList<Integer> searchMap(String name,String searchFor){

        ListIterator<Object> listIterator = data.get(searchFor).listIterator();
        ArrayList<Integer> index = new ArrayList<Integer>();

            if(name.isBlank()){
                while(listIterator.hasNext()){
                    listIterator.next();
                    index.add(listIterator.previousIndex());     
                }
            }

            // else not needed as lisIterator is exhausted

            while(listIterator.hasNext()){
                if(name.equals(((String)listIterator.next()).toLowerCase())){
                    index.add(listIterator.previousIndex());
                    break;
                }
            }

        return index;
    }

    /**
     * @param index ArrayList<Integer>
     * Displays the required data based on indexes passed
     */
    protected void display(final ArrayList<Integer> index){
        Iterator<String> iterator;
        Iterator<Integer> dispIterator;

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
    }

    /**
     * Initialises the empty temporary copy of database in variable data
     */
    protected void initialise(){
        try {
            FileInputStream file;
            XSSFWorkbook workbook;
            XSSFSheet sheet;
            boolean header = true;
            ArrayList<Object> columnHead = new ArrayList<Object>();
            ArrayList<ArrayList<Object>> contents = new ArrayList<ArrayList<Object>>();
            File f = new File("db/pharmacy/Read.xlsx");
            int orderIndex = 0;

            if(!f.exists()){
                String[] sample = new String[]{"Name","Cost"};
                FileOutputStream out = new FileOutputStream(f);
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Drugs");
                for(Row r:sheet){
                    int index = 0;
                    for(Cell c:r){
                        c.setCellValue(sample[index++]);
                    }
                }
                workbook.write(out);
                out.close();
            }

            // Reading file from local directory
            file = new FileInputStream(f);           

            // Create Workbook instance holding reference to
            // .xlsx file
            workbook = new XSSFWorkbook(file);
  
            // Get first/desired sheet from the workbook
            sheet = workbook.getSheetAt(0);
  
            Iterator<Row> rowIterator = sheet.iterator();

            while(rowIterator.hasNext()){
                int index = 0;
                Row r = rowIterator.next();
                if(header == true){
                    for(Cell c: r){
                        contents.add(new ArrayList<Object>());
                        try{
                            if(c.getCellType().equals(CellType.NUMERIC)){
                                columnHead.add(orderIndex+""+c.getNumericCellValue());
                            }else if(c.getCellType().equals(CellType.BOOLEAN)){
                                columnHead.add(orderIndex+""+c.getBooleanCellValue());
                            }else{
                                columnHead.add(orderIndex+""+c.getStringCellValue());
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            System.out.println("Cell HEADER Exception");
                        }
                        orderIndex++;
                    }
                    header = false;
                }else{
                    for(Cell c: r){
                        try{
                            double t;
                            String temp;
                            if(c.getCellType().equals(CellType.NUMERIC)){
                                t = c.getNumericCellValue();
                                contents.get(index).add(t);
                            }else{
                                temp = c.getStringCellValue();
                                contents.get(index).add(temp);
                            }
                        }catch(Exception e){
                            System.out.println(e);
                            System.out.println(contents.get(0));
                            System.out.println("Cell STYLE Exception");
                        }
                        index++;
                    }
                }
            }

            setData(columnHead, contents);

            // System.out.println(data);
            // SafeInput.waitForInput();

            workbook.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Unexpected Error");
            Helper.sleep(30);
        }
    }

    /**
     * It will set the value of the temporary data variable
     * @param columnHead Names of column heads with order of appearance 
     * recorded by placing a index before name of the header ex. Name is 
     * stored as 0Name if it is the first column and so on.
     * @param contents the ArrayList of ArrayList of values present in a column
     */
    protected void setData(ArrayList<Object> columnHead,
    ArrayList<ArrayList<Object>> contents){
        Iterator<Object> iterator = columnHead.iterator();
            Iterator<ArrayList<Object>> iterator2 = contents.iterator();

            while(iterator.hasNext()){
                data.put((String) iterator.next(),iterator2.next());
            }
    }

    /**
     * clears the contents of the database
     */
    protected void clearDatabase(){
        try {
            FileOutputStream out = new FileOutputStream(new
             File("db/pharmacy/Read.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook();
            workbook.createSheet("Drugs");
            workbook.write(out);
            workbook.close();
            out.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Set the Excel database to the contents of data
     * This will be a complete override of all data which overlaps with 
     * temporary storage so do not forget 
     * to use {@code intialise() } function
     */
    protected void updateDatabase(){
        try{
            FileInputStream fileInputStream = new FileInputStream(new
             File("db/pharmacy/Read.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            boolean header = true;

            Iterator<String> columnIterator = data.keySet().iterator();
            int k = sheet.getLastRowNum();
            while(k>0){
                for(Row r:sheet){
                    int index = 0;
                    if(header == true){
                        for(Cell c:r){
                            if(columnIterator.hasNext()){
                                c.setCellValue(columnIterator.next().substring(1));
                            }
                            else{
                                c.setBlank();
                            }
                        }
                        header = false;
                    }
                    else{
                        k--;
                        Iterator<String> contentIterator = data.keySet().iterator();
                        for(Cell c:r){
                            if(contentIterator.hasNext()){
                                String temp = contentIterator.next();
                                if(data.get(temp).size()-index>=1){
                                    Object datacell = data.get(temp).get(index++);
                                    if(datacell.getClass().getSimpleName() == "Double"){
                                        c.setCellValue((double)datacell);
                                    }
                                    else if(datacell.getClass().getSimpleName() == "Boolean"){
                                        c.setCellValue((boolean)datacell);
                                    }
                                    else{
                                        c.setCellValue((String)datacell);
                                    }
                                }
                            }
                            else{
                                c.setBlank();
                            }
                        }
                    }
                    index = 0;
                }
            }

            fileInputStream.close();

            // Writing issue here
            FileOutputStream out = new FileOutputStream(new File("db/pharmacy/Read.xlsx"));
            workbook.write(out);
            workbook.close();
            out.close();
        }catch(Exception e){
            System.out.println("Error in Writing to database");
            e.printStackTrace();
            Helper.sleep(10);
        }
    }

}