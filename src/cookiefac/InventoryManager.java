package cookiefac;

/**
 *
 * @author Sachini
 */

import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InventoryManager{
    private Formatter Warehouse;
    private Formatter Inventorypopulated;
    private Formatter RMArrival;
    
    public void openFile(){
        try{
            Warehouse = new Formatter("processReport1.txt");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void openFileInventorypopulated(){
        try{
            Inventorypopulated = new Formatter("processReport3.txt");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void openFileRMArrival(){
        try{
            RMArrival = new Formatter("processReport5.txt");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    FactoryRunner myy =new FactoryRunner();
    static ExecutorService executor = Executors.newFixedThreadPool(1);
    public final Runnable ml;
    public final Runnable il;
    static String name = null;//#base data
    static int productionLine = 0;
    static int warehouseStorageCapacity = 0;
    static int factoryStorageCapacity = 0;
    static int runTime = 0;
    static double FL = 0;//warehouse inventory
    static double BT = 0;
    static double CH = 0;
    static double MK = 0;
    static double SG = 0;
    static double GN = 0;
    static int BB = 0, startBB = 0;//Merchandise inventory
    static int BC = 0, startBC = 0;
    static int CC = 0, startCC = 0;
    static int GC = 0, startGC = 0;
    static double BBfl =0, BBbt =0, BBsg =0, BBmk =0;//#recipe line
    static double BCfl =0, BCbt =0, BCsg =0, BCmk =0;
    static double CCfl =0, CCbt =0, CCsg =0, CCmk =0, CCch = 0;
    static double GCfl =0, GCbt =0, GCsg =0, GCmk =0, GCgn = 0;
    static int BBtime =0, BCtime =0, CCtime =0, GCtime =0;
    static int bbCreat = 0,bcCreat = 0,ccCreat = 0,gcCreat = 0;
    int oldArrivalTime = 0;
    static int  recipe =0;
    static String f1 =null; 
    static int manufactured =0,manufacturedBB =0,manufacturedBC =0,manufacturedCC =0,manufacturedGC =0;
    public InventoryManager(){
        FactoryManager _fm = new FactoryManager();
        ml = new Runnable() {
            public void run() {
                openFileRMArrival();
                String fileName = "rmarrival.dat";// The file to read
                String line = null;
                try {
                    FileReader fileReader = new FileReader(fileName);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    while((line = bufferedReader.readLine()) != null) {
                        String array1[]= line.split(" ");
                        int arrivalTime = Integer.parseInt(array1[2]);
                        int quantity = Integer.parseInt(array1[1]);
                        try {
                            Thread.sleep(arrivalTime-oldArrivalTime);
                            oldArrivalTime = arrivalTime;
                        } catch (InterruptedException ex) {
                            Logger.getLogger(InventoryManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        switch(array1[0]){
                            case "SG":
                                SG += quantity;
                                RMArrival.format("%s%s%s%s%s%s%s%s%s\n", "RM Arrival ","Sugar ",arrivalTime,"ms ",quantity,"gs ","[SG ",SG,"]");
                                break;
                            case "FL":
                                FL += quantity;
                                RMArrival.format("%s%s%s%s%s%s%s%s%s\n", "RM Arrival ","Flour ",arrivalTime,"ms ",quantity,"gs ","[FL ",FL,"]");
                                break;
                            case "BT":
                                BT += quantity;
                                RMArrival.format("%s%s%s%s%s%s%s%s%s\n", "RM Arrival ","Butter ",arrivalTime,"ms ",quantity,"gs ","[BT ",BT,"]");
                                break;
                            case "MK":
                                MK += quantity;
                                RMArrival.format("%s%s%s%s%s%s%s%s%s\n", "RM Arrival ","Milk, ",arrivalTime,"ms ",quantity,"gs ","[MK ",MK,"]");
                                break;
                            case "GN":
                                GN += quantity;
                                RMArrival.format("%s%s%s%s%s%s%s%s%s\n", "RM Arrival ","Ginger ",arrivalTime,"ms ",quantity,"gs ","[GN ",GN,"]");
                                break;
                            case "CH":
                                CH += quantity;
                                RMArrival.format("%s%s%s%s%s%s%s%s%s\n", "RM Arrival ","Chips , ",arrivalTime,"ms ",quantity,"gs ","[CH ",CH,"]");
                                break;
                        }
                    }               
                    bufferedReader.close();
                }
                catch(FileNotFoundException ex) {
                    System.out.println("Unable to open file '" + fileName + "'");                
                }
                catch(IOException ex) {
                    System.out.println("Error reading file '" + fileName + "'");                  
                }
                RMArrival.close();
            }
        };
        il = new Runnable() {
            public void run() {
                switch(f1){
                    case "BB":
                        FL = FL - (BBfl/100)*bbCreat;
                        BT = BT - (BBbt/100)*bbCreat;
                        SG = SG - (BBsg/100)*bbCreat;
                        MK = MK - (BBmk/100)*bbCreat;
                        try {
                            Thread.sleep(((BBtime*bbCreat)/100));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(InventoryManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "BC":
                        FL = FL - (BCfl/100)*bcCreat;
                        BT = BT - (BCbt/100)*bcCreat;
                        SG = SG - (BCsg/100)*bcCreat;
                        MK = MK - (BCmk/100)*bcCreat;
                        try {
                            Thread.sleep(((BCtime*bcCreat)/100));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(InventoryManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "CC":
                        FL = FL - (CCfl/100)*ccCreat;
                        BT = BT - (CCbt/100)*ccCreat;
                        SG = SG - (CCsg/100)*ccCreat;
                        CH = CH - (CCch/100)*ccCreat;
                        MK = MK - (CCmk/100)*ccCreat;
                        try {
                            Thread.sleep(((CCtime*ccCreat)/100));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(InventoryManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "GC":
                        FL = FL - (GCfl/100)*gcCreat;
                        BT = BT - (GCbt/100)*gcCreat;
                        SG = SG - (GCsg/100)*gcCreat;
                        GN = GN - (GCgn/100)*gcCreat;
                        MK = MK - (CCmk/100)*gcCreat;
                        try {
                            Thread.sleep(((GCtime*gcCreat)/100));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(InventoryManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                            
                }
            }
        };
    }
    void cookieFactory(){
        openFileInventorypopulated();
        String fileName = "cookieFactory.info";
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                switch(line){
                    case "#base data":
                        String _line; 
                        _line = bufferedReader.readLine().replace("Name:","");
                        _line = _line.replace(" ","");
                        name = _line;
                        _line = bufferedReader.readLine().replace("Production line:","");
                        _line = _line.replace(" ","");
                        productionLine = Integer.parseInt(_line);
                        _line = bufferedReader.readLine().replace("Warehouse storage capacity:","");
                        _line = _line.replace(" ","");
                        warehouseStorageCapacity = Integer.parseInt(_line);
                        _line = bufferedReader.readLine().replace("Factory storage capacity:","");
                        _line = _line.replace(" ","");
                        factoryStorageCapacity = Integer.parseInt(_line);
                        _line = bufferedReader.readLine().replace("Run time:","");
                        _line = _line.replace(" ","");
                        _line = _line.replace("ms","");
                        runTime = Integer.parseInt(_line);
                    break;
                    case "#recipe line":
                        for(int i=0;i<4;i++){
                            line = bufferedReader.readLine();
                            String[] parts = line.split(" ", 2);
                            parts[1] = parts[1].replace("[","");
                            parts[1] = parts[1].replace("]","");
                            parts[1] = parts[1].replace(":"," ");
                            StringTokenizer token = new StringTokenizer(parts[1], " ");
                            switch(parts[0]){
                                case "BB":
                                    for(int j = 1; j<=5;j++){
                                        switch(token.nextToken()){
                                            case "FL":
                                                BBfl = Integer.parseInt(token.nextToken());
                                                break;
                                            case "BT":
                                                BBbt = Integer.parseInt(token.nextToken());
                                                break;
                                            case "SG":
                                                BBsg = Integer.parseInt(token.nextToken());
                                                break;
                                            case "MK":
                                                BBmk = Integer.parseInt(token.nextToken());
                                                break;
                                            case "|":
                                                BBtime = Integer.parseInt(token.nextToken());
                                                break;
                                        }
                                    }
                                    break;
                                case "BC":
                                    for(int j = 1; j<=5;j++){
                                        switch(token.nextToken()){
                                            case "FL":
                                                BCfl = Integer.parseInt(token.nextToken());
                                                break;
                                            case "BT":
                                                BCbt = Integer.parseInt(token.nextToken());
                                                break;
                                            case "SG":
                                                BCsg = Integer.parseInt(token.nextToken());
                                                break;
                                            case "MK":
                                                BCmk = Integer.parseInt(token.nextToken());
                                                break;
                                            case "|":
                                                BCtime = Integer.parseInt(token.nextToken());
                                                break;
                                        }
                                    }
                                    break;
                                case "CC":
                                    for(int j = 1; j<=6;j++){
                                        switch(token.nextToken()){
                                            case "FL":
                                                 CCfl = Integer.parseInt(token.nextToken());
                                                 break;
                                            case "BT":
                                                 CCbt = Integer.parseInt(token.nextToken());
                                                 break;
                                            case "SG":
                                                 CCsg = Integer.parseInt(token.nextToken());
                                                 break;
                                            case "MK":
                                                 CCmk = Integer.parseInt(token.nextToken());
                                                 break;
                                            case "|":
                                                 CCtime = Integer.parseInt(token.nextToken());
                                                 break;
                                            case "CH":
                                                 CCch = Integer.parseInt(token.nextToken());
                                                 break;
                                        }
                                    }
                                    break;
                                case "GC":
                                    for(int j = 1; j<=6;j++){
                                        switch(token.nextToken()){
                                            case "FL":
                                                 GCfl = Integer.parseInt(token.nextToken());
                                                 break;
                                            case "BT":
                                                 GCbt = Integer.parseInt(token.nextToken());
                                                 break;
                                            case "SG":
                                                 GCsg = Integer.parseInt(token.nextToken());
                                                 break;
                                            case "MK":
                                                 GCmk = Integer.parseInt(token.nextToken());
                                                 break;
                                            case "|":
                                                 GCtime = Integer.parseInt(token.nextToken());
                                                 break;
                                            case "GN":
                                                 GCgn = Integer.parseInt(token.nextToken());
                                                 break;
                                        }
                                    }
                                    break;
                            }
                        }
                    break;
                    case "#warehouse inventory":
                        for(int i =0; i<6; i++){
                            line = bufferedReader.readLine();
                            String[] parts = line.split(" ", 2);
                            switch(parts[0]){
                                case "FL":
                                    FL = Integer.parseInt(parts[1]);
                                    Inventorypopulated.format("%s%s%s\n", "Flour - ", FL,"kgs");
                                    break;
                                case "BT":
                                    BT = Integer.parseInt(parts[1]);
                                    Inventorypopulated.format("%s%s%s\n", "Butter - ", BT,"kgs");
                                    break;
                                case "CH":
                                    CH = Integer.parseInt(parts[1]);
                                    Inventorypopulated.format("%s%s%s\n", "Chocolate Chips - ", CH,"kgs");
                                    break;
                                case "MK":
                                    MK = Integer.parseInt(parts[1]);
                                    Inventorypopulated.format("%s%s%s\n", "Milk - ", MK,"kgs");
                                    break;
                                case "SG":
                                    SG = Integer.parseInt(parts[1]);
                                    Inventorypopulated.format("%s%s%s\n", "Suggar - ", SG,"kgs");
                                    break;
                                case "GN":
                                    GN = Integer.parseInt(parts[1]);
                                    Inventorypopulated.format("%s%s%s\n", "Ginger - ", GN,"kgs");
                                    break;
                            }
                        }
                    break;
                    case "#Merchandise inventory":
                        for(int i =1; i<=4; i++){
                            line = bufferedReader.readLine();
                            String[] parts = line.split(" ", 2);
                            switch(parts[0]){
                                case "BB":
                                    BB = Integer.parseInt(parts[1]);
                                    startBB = BB;
                                    break;
                                case "BC":
                                    BC = Integer.parseInt(parts[1]);
                                    startBC = BC;
                                    break;
                                case "CC":
                                    CC = Integer.parseInt(parts[1]);
                                    startCC = CC;
                                    break;
                                case "GC":
                                    GC = Integer.parseInt(parts[1]);
                                    startGC = GC;
                                    break;
                            }
                        }
                    break;
                }
            }   
            openFile();
            Warehouse.format("%s\n", "Inventory Manager initiated... .");
            Warehouse.close();
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" +  fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");                  
        }
        Inventorypopulated.close();
    }
    void f1(){
        switch(recipe){
                    case 1://BB
                        BB();
                        f1 = "BB";
                        executor.execute(new Thread(il));
                        break;
                    case 10://BC
                        BC();
                        f1 = "BC";
                        executor.execute(new Thread(il));
                        break;
                    case 11://BC BB
                        BC(); 
                        BB();
                        f1 = "BB";
                        executor.execute(new Thread(il));
                        f1 = "BC";
                        executor.execute(new Thread(il));
                        break;
                    case 100://CC
                        CC();
                        f1 = "CC";
                        executor.execute(new Thread(il));
                        break;
                    case 101://CC BB
                        CC();
                        BB();
                        f1 = "BB";
                        executor.execute(new Thread(il));
                        f1 = "CC";
                        executor.execute(new Thread(il));
                        break;
                    case 110://CC BC
                        CC();
                        BC();
                        f1 = "CC";
                        executor.execute(new Thread(il));
                        f1 = "BC";
                        executor.execute(new Thread(il));
                        break;
                    case 111://CC BC BB
                        CC();
                        BC(); 
                        BB();
                        f1 = "BB";
                        executor.execute(new Thread(il));
                        f1 = "BC";
                        executor.execute(new Thread(il));
                        f1 = "CC";
                        executor.execute(new Thread(il));
                        break;
                    case 1000://GC
                        GC();
                        f1 = "GC";
                        executor.execute(new Thread(il));
                        break;
                    case 1001://GC BB
                        GC(); 
                        BB();
                        f1 = "BB";
                        executor.execute(new Thread(il));
                        f1 = "GC";
                        executor.execute(new Thread(il));
                        break;
                    case 1010://GC BC
                        GC(); 
                        BC();
                        f1 = "BC";
                        executor.execute(new Thread(il));
                        f1 = "GC";
                        executor.execute(new Thread(il));
                        break;
                    case 1011://GC BC BB
                        GC(); 
                        BC(); 
                        BB();
                        f1 = "BB";
                        executor.execute(new Thread(il));
                        f1 = "BC";
                        executor.execute(new Thread(il));
                        f1 = "GC";
                        executor.execute(new Thread(il));
                        break;
                    case 1100://GC CC
                        GC(); 
                        CC();
                        f1 = "GC";
                        executor.execute(new Thread(il));
                        f1 = "CC";
                        executor.execute(new Thread(il));
                        break;
                    case 1101://GC CC BB
                        GC(); 
                        CC();
                        BB();
                        f1 = "BB";
                        executor.execute(new Thread(il));
                        f1 = "CC";
                        executor.execute(new Thread(il));
                        f1 = "GC";
                        executor.execute(new Thread(il));
                        break;
                    case 1110://GC CC BC
                        GC();
                        CC(); 
                        BC();
                        f1 = "CC";
                        executor.execute(new Thread(il));
                        f1 = "BC";
                        executor.execute(new Thread(il));
                        f1 = "GC";
                        executor.execute(new Thread(il));
                        break;
                    case 1111://GC CC BC BB
                        GC();
                        CC();
                        BC(); 
                        BB();
                        f1 = "BB";
                        executor.execute(new Thread(il));
                        f1 = "BC";
                        executor.execute(new Thread(il));
                        f1 = "CC";
                        executor.execute(new Thread(il));
                        f1 = "GC";
                        executor.execute(new Thread(il));
                        break;
                }
    }
    void BB(){
        manufactured += bbCreat;
        manufacturedBB += bbCreat;
    }
    void BC(){
        manufactured += bcCreat;
        manufacturedBC += bcCreat;
    }
    void CC(){
        manufactured += ccCreat;
        manufacturedCC += ccCreat;
    }
    void GC(){
        manufactured += gcCreat;
        manufacturedGC += gcCreat;
    }
}