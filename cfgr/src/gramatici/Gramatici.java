/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gramatici;
import java.io.File; 
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Gramatici {

    public static void main(String[] args) {
        File file;
        Scanner scan = null;
        String stringBuffer; //variabile pentru citire strings
        Productie productieBuffer = new Productie();
        Casuta casutaBuffer = new Casuta();
        String[] arrayBuffer;
        String productieLHS;
        String productieRHS;
        int casutaCounter = 0;
        int nrProductie = 0;
        char lastLHS = '\0';
        List<String> nonTerminale;
        List<String> terminale;
        List<Productie> productii = new ArrayList<>();
        char simbolInitial;
        try {
            file = new File("input.txt");
            scan = new Scanner(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Gramatici.class.getName()).log(Level.SEVERE, null, ex);
        }
        assert scan != null;
        stringBuffer = scan.nextLine();
        arrayBuffer = stringBuffer.split(" ");
        nonTerminale = Arrays.asList(arrayBuffer);
        System.out.println("Simboluri non-terminale : ");
        System.out.println(nonTerminale);
        stringBuffer = scan.nextLine();
        arrayBuffer = stringBuffer.split(" ");
        terminale = Arrays.asList(arrayBuffer);
        System.out.println("Simboluri terminale : ");
        System.out.println(terminale);
        while(scan.hasNextLine()){
            stringBuffer = scan.nextLine();
            System.out.println(stringBuffer);
            arrayBuffer = stringBuffer.split("->");
            productieLHS = arrayBuffer[0];
            productieRHS = arrayBuffer[1];
            productieBuffer.lhs = arrayBuffer[0];

            productieBuffer.rhs = arrayBuffer[1];
            for(int i = 0;i<productieRHS.length();i++){
                casutaBuffer.a = productieRHS.charAt(i);
                casutaCounter++;
                casutaBuffer.index = casutaCounter;
                System.out.println(casutaBuffer.a);
                productieBuffer.casute.add(casutaBuffer);
            }
            productii.add(productieBuffer);
        }
    }
}
