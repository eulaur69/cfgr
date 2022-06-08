/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gramatici;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Productie {
    public String lhs;
    public String rhs;
    public List<Casuta> casute = new ArrayList<>();

    public Productie() {
        this.lhs = "";
        this.rhs = "";
    }
    public void PrintProd(){
        System.out.println(this.lhs+"->"+this.rhs);
        for (Casuta i: Collections.unmodifiableList(casute)) {
            System.out.println(i.index + " " + i.a + " " + i.b + " " + i.c + " " + i.d);
        }
    }
}
