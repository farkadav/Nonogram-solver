/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

/**
 *
 * @author David
 */
public class Line {
    
    public char[] sequence;
    public int position;
    public boolean Row;
    public CSPVariable parent;
    
    public Line(char[] val,int index, boolean Row) {
        this.position = index;
        this.Row = Row;
        this.sequence = val;
        
    }
    
    public CSPVariable getParent(){
        return this.parent;
    }
}
