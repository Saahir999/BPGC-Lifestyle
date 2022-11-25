package package_pharmacy;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import java.io.File;
import java.io.FileInputStream;
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
    protected HashMap<String,ArrayList<Object>> data = new HashMap<String,ArrayList<Object>>();

    @Override
    public String getAppName(){
        return "PharmacyApp";
    }

    @Override
    public void start() throws BackException{

        int c = 0;
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
                FileOutputStream out = new FileOutputStream(f);
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Drugs");
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

            Iterator<Object> iterator = columnHead.iterator();
            Iterator<ArrayList<Object>> iterator2 = contents.iterator();

            while(iterator.hasNext()){
                data.put((String) iterator.next(),iterator2.next());
            }

            // System.out.println(data);
            // SafeInput.waitForInput();

            workbook.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Unexpected Error");
            Helper.sleep(30);
        }
    }
}