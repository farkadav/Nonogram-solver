/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nonogram;

import java.util.ArrayList;


/**
 *
 * @author David
 */
public class CSPVariable {
    
    public ArrayList<char[]> storage;
    public int position;
    public boolean Row;
    
    public CSPVariable(int index, boolean Row) {
        this.position = index;
        this.Row = Row;
        this.storage = new ArrayList<>();
    }
        
}
