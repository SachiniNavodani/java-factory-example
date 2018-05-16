package cookiefac;

/**
 *
 * @author Sachini
 */

import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FactoryManager extends Thread{
    private Formatter Initial;
    private Formatter InvoiceArrive;
    private Formatter Totalmanufactured;
    public void openFile(){
        try{
            Initial = new Formatter("processReport2.txt");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void openFileInvoice(){
        try{
            InvoiceArrive = new Formatter("processReport4.txt");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void openFileTotalmanufactured(){
        try{
            Totalmanufactured = new Formatter("processReport6.txt");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    int bb = 0,bc = 0,cc = 0,gc = 0;
    int recipe = 0;
    int oldArrivalTime = 0;
    int Invoicecompleted =0;
    int requirement = 0;
    int dispatched =0;
    public void run(){
        openFileInvoice();
        InventoryManager _imm = new InventoryManager();
        FactoryRunner k =new FactoryRunner();
        String fileName = "invoice.dat";// The file to read
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {                 
                String[] parts = line.split(" ", 3);
                int arrivalTime = Integer.parseInt(parts[1]);
                try {
                    Thread.sleep(arrivalTime - oldArrivalTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(InventoryManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                 InvoiceArrive.format("%s%s%s%s%s%s\n", "Invoice ",parts[0]," arrival: ",arrivalTime,"ms ",parts[2]);
                PL(parts[0],parts[2]);
                Merchandise(recipe);
                recipe = 0;
            }
            InvoiceArrive.close();
            openFile();
            Initial.format("%s\n", "Factory Manager Initiated... .");
            Initial.close();
            _imm.executor.shutdown();  
            while (!_imm.executor.isTerminated()) {}
            bufferedReader.close();
            openFileTotalmanufactured();
        Totalmanufactured.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s\n", "Total manufactured ","[",_imm.manufactured,"]"," BB"," " ,_imm.manufacturedBB,",BC"," " ,_imm.manufacturedBC,",CC"," " ,_imm.manufacturedCC,",GC"," " ,_imm.manufacturedGC);
        dispatched =_imm.manufactured+_imm.startBB+_imm.startBC+_imm.startCC+_imm.startGC-_imm.BB-_imm.BC-_imm.CC-_imm.GC;
        Totalmanufactured.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s\n", "Total dispatched ","[",dispatched,"]"," BB"," " ,_imm.manufacturedBB+_imm.startBB-_imm.BB,",BC"," " ,_imm.manufacturedBC+_imm.startBC-_imm.BC,",CC"," " ,_imm.manufacturedCC+_imm.startCC-_imm.CC,",GC"," " ,_imm.manufacturedGC+_imm.startGC-_imm.GC);
        Totalmanufactured.format("%s%s\n", "Invoice completed ",Invoicecompleted);
        requirement += _imm.startBB+_imm.startBC+_imm.startCC+_imm.startGC-_imm.BB-_imm.BC-_imm.CC-_imm.GC;
        Totalmanufactured.format("%s%s\n", "Total requirement ",requirement);
        Totalmanufactured.format("%s%s%s\n", "Effective throughput ",((dispatched*100)/requirement),"%");
        Totalmanufactured.close();
        new Final().FinalizedReport();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");                  
        }
    }
    void PL(String invoiceId, String cookiecodeQuantity){
        InventoryManager _imm = new InventoryManager();
        cookiecodeQuantity = cookiecodeQuantity.replace("[","");
        cookiecodeQuantity = cookiecodeQuantity.replace("]","");
        cookiecodeQuantity = cookiecodeQuantity.replace(",","");
        StringTokenizer token = new StringTokenizer(cookiecodeQuantity, " ");
        while (token.hasMoreTokens()) {
            switch(token.nextToken()){
                case "BB":
                    recipe += 1;
                    bb = _imm.BB;
                    bb -= Integer.parseInt(token.nextToken());
                    if(bb <= 0){
                        if(bb==0){
                            _imm.bbCreat = _imm.BB;
                            _imm.BB = 0;
                        }else{
                        _imm.bbCreat =bb * -1;
                        _imm.BB = 0;   
                        }
                    }else{
                        _imm.bbCreat = _imm.BB - bb;
                        _imm.BB = bb;
                    }
                    requirement +=_imm.bbCreat;
                    break;
                case "BC":
                    recipe += 10;
                    bc = _imm.BC;
                    bc -= Integer.parseInt(token.nextToken());
                    if(bc <= 0){
                        if(bc==0){
                            _imm.bcCreat = _imm.BC;
                            _imm.BC = 0;
                        }else{
                        _imm.bcCreat =bc * -1;
                        _imm.BC = 0;   
                        }
                    }else{
                        _imm.bcCreat = _imm.BC - bc;
                        _imm.BC = bc;
                    }
                    requirement +=_imm.bcCreat;
                    break;
                case "CC":
                    recipe += 100;
                    cc = _imm.CC;
                    cc -= Integer.parseInt(token.nextToken());
                    if(cc <= 0){
                        if(cc==0){
                            _imm.ccCreat = _imm.CC;
                            _imm.CC = 0;
                        }else{
                        _imm.ccCreat =cc * -1;
                        _imm.CC = 0;   
                        }
                    }else{
                        _imm.ccCreat = _imm.CC - cc;
                        _imm.CC = cc;
                    }
                    requirement +=_imm.ccCreat;
                    break;
                case "GC":
                    recipe += 1000;
                    gc = _imm.GC;
                    gc -= Integer.parseInt(token.nextToken());
                    if(gc <= 0){
                        if(gc==0){
                            _imm.gcCreat = _imm.GC;
                            _imm.GC = 0;
                        }else{
                        _imm.gcCreat =gc * -1;
                        _imm.GC = 0;   
                        }
                    }else{
                        _imm.gcCreat = _imm.GC - gc;
                        _imm.GC = gc;
                    }
                    requirement +=_imm.gcCreat;
                    break;
            }
        } 
    }
    void Merchandise(int recipe){
        InventoryManager _imm = new InventoryManager();
        _imm.recipe = recipe;
        switch(recipe){
            case 1://BB
                if( stoCul(_imm.bbCreat,0,0,0) == 1 ){
                   _imm.f1();
                   InvoiceArrive.format("%s\n", "Completed: ");
                   Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 10://BC
                if( stoCul(0,_imm.bcCreat,0,0) == 1 ){
                    _imm.f1();
                    InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 11://BC BB
                if( stoCul(_imm.bbCreat,_imm.bcCreat,0,0) == 1 ){
                    _imm.f1();
                    InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 100://CC
                if( stoCul(0,0,_imm.ccCreat,0) == 1 ){
                    _imm.f1();
                    InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 101://CC BB
                if(stoCul(_imm.bbCreat,0,_imm.ccCreat,0) == 1 ){
                    _imm.f1();
                    InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 110://CC BC
                if(stoCul(0,_imm.bcCreat,_imm.ccCreat,0) == 1 ){
                    _imm.f1();
                    InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 111://CC BC BB
                if(stoCul(_imm.bbCreat,_imm.bcCreat,_imm.ccCreat,0) == 1 ){
                   _imm.f1();
                   InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 1000://GC
                if(stoCul(0,0,0,_imm.gcCreat) == 1 ){
                   _imm.f1();
                   InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 1001://GC BB
                if(stoCul(_imm.bbCreat,0,0,_imm.gcCreat) == 1 ){
                 _imm.f1();
                 InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 1010://GC BC
                if(stoCul(0,_imm.bcCreat,0,_imm.gcCreat) == 1 ){
                    _imm.f1();
                    InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 1011://GC BC BB
                if(stoCul(_imm.bbCreat,_imm.bcCreat,0,_imm.gcCreat) == 1 ){
                 _imm.f1();
                 InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 1100://GC CC
                if(stoCul(0,0,_imm.ccCreat,_imm.gcCreat) == 1 ){
                   _imm.f1();
                   InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 1101://GC CC BB
                if(stoCul(_imm.bbCreat,0,_imm.ccCreat,_imm.gcCreat) == 1 ){
                   _imm.f1();
                   InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 1110://GC CC BC
                if(stoCul(0,_imm.bcCreat,_imm.ccCreat,_imm.gcCreat) == 1 ){
                   _imm.f1();
                   InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            case 1111://GC CC BC BB
                if(stoCul(_imm.bbCreat,_imm.bcCreat,_imm.ccCreat,_imm.gcCreat) == 1 ){
                    _imm.f1();
                    InvoiceArrive.format("%s\n", "Completed: ");
                    Invoicecompleted++;
                }else{
                   InvoiceArrive.format("%s\n", "Incompleted: "); 
                }
                break;
            
        }
    }
    int stoCul(int BB, int BC, int CC, int GC){
        int x = 1;
        InventoryManager _im = new InventoryManager();
        if( BB != 0 ){
            if( ((0.6*BB) < _im.FL) && ((0.1*BB) < _im.BT) && ((0.2*BB) < _im.SG) && ((0.1*BB) < _im.MK)){
                x *= 1;
            }else{
                x *= 0;
            }
        }
        if(BC!=0){
            if( ((0.5*BC) < _im.FL) && ((0.2*BC) < _im.BT) && ((0.2*BC) < _im.SG) && ((0.1*BC) < _im.MK)){
                x *= 1;
            }else{
                x *= 0;
            }
        }
        if(CC!=0){
            if( ((0.45*CC) < _im.FL) && ((0.15*CC) < _im.BT) && ((0.2*CC) < _im.SG) && ((0.1*CC) <_im.MK) && ((0.1*CC) < _im.CH)){
                x *= 1;
            }else{
                x *= 0;
            }
        }
        if(GC!=0){
            if( ((0.45*GC) < _im.FL) && ((0.2*GC) < _im.BT) && ((0.15*GC) < _im.GN) && ((0.1*GC) < _im.MK) && ((0.1*GC) < _im.GN)){
                x *= 1;
            }else{
                x *= 0;
            }
        }
        return x;
    }

}
