/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nonogram;

/**
 *
 * @author David
 */
public class Line {
    
    public char[] value;
    public int position;
    public boolean Row;
    
    public Line(char[] val,int index, boolean Row) {
        this.position = index;
        this.Row = Row;
        this.value = val;
    }
    
}
