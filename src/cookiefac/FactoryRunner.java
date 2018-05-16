package cookiefac;

/**
 *
 * @author Sachini
 */

import java.util.*;
import java.io.*;
import java.text.*;

class Final{
    private Formatter FinalizedReport;
    public void openFinalizedReport(){
        try{
            FinalizedReport = new Formatter("processReport.txt");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    void FinalizedReport() throws FileNotFoundException, IOException{
        openFinalizedReport();
        String[] fileName = new String[8];
        fileName[0]="processReport0.txt";
        fileName[1]="processReport1.txt";
        fileName[2]="processReport2.txt";
        fileName[3]="processReport3.txt";
        fileName[4]="processReport4.txt";
        fileName[5]="processReport5.txt";
        fileName[6]="processReport6.txt";
        for(int i=0;i<=6;i++){
            String line = null;
            FileReader fileReader = new FileReader(fileName[i]);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {                 
                    System.out.println(line);
                    FinalizedReport.format("%s", line);
                    FinalizedReport.format("\n");
            }
            FinalizedReport.format("\n");
            System.out.println();
            bufferedReader.close();
        }
        FinalizedReport.close();
    }
}

public class FactoryRunner {
    private Formatter x;
    
    public void openFile(){
        try{
            x = new Formatter("processReport0.txt");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
        FactoryRunner fr = new FactoryRunner();
        InventoryManager im = new InventoryManager();
        FactoryManager fm = new FactoryManager();
        im.cookieFactory();
        fr.openFile();
        fr.x.format("%s\n", "+------------------------------------------------------+","\n");
        fr.x.format("%s\n", "|          JAVA COOKIE FACTORY OPERATION REPORT        |","\n");
        fr.x.format("%s\n", "+------------------------------------------------------+","\n");
        fr.x.format("%s%s\n", "Start time: ",dateFormat.format(date) ,"\n");
        fr.x.format("%s%s\n", "Production line: ",im.productionLine,"\n");
        fr.x.format("%s%s%s\n", "Run time: ",im.runTime,"ms","\n");
        fr.x.format("%s%s\n", "Warehouse storage capacity: ",im.warehouseStorageCapacity,"\n");
        fr.x.format("%s%s\n", "factoryStorageCapacity: ",im.factoryStorageCapacity,"\n");
        new Thread(im.ml).start();
        fm.start();
        fr.x.close();
    }
    
}
