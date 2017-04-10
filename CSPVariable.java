/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.util.ArrayList;



/**
 *
 * @author David
 */
public class CSPVariable {
        
    public ArrayList<char[]> storage;
    public int position;
    public boolean Row;
    public int count;
    public boolean excluded;
    
    public CSPVariable(int index, boolean Row) {
        this.position = index;
        this.Row = Row;        
        this.storage = new ArrayList<>();
        this.count = 0;
        this.excluded = false;
    }
    
    public void incrementCount(){
        this.count +=1;
    }
    
    public void exclude(){
        this.excluded = true;
    }
    public boolean getExcluded(){
        return this.excluded;
    }
    public int getCount(){
        return this.count;
    }
    public void resetCount(){
        this.count = 0;
    }
        
}
