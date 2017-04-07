/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nonogram;

import com.sun.xml.internal.ws.util.StringUtils;
import java.util.ArrayList;

/**
 *
 * @author David
 */
public final class Rules {
    
    private int rowNumber;
    private int colNumber;
    public boolean row;
    private ArrayList<Integer> rules;
    private ArrayList<Character> colors;
    private ArrayList<Rule> Rules;
    private String inputs;
    
    public Rules(int n, String nums){
        this.inputs = nums;
        this.Rules = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.rules = new ArrayList<>();
   
        sortColorsRules();
        makeRules();
    }
    
    
    public ArrayList<Rule> getRules(){
        return Rules;
    }
    

    public void makeRules(){
        for(int i =0; i< rules.size();i++){
            this.Rules.add(new Rule(this.rules.get(i), this.colors.get(i)));
        }        
    }
    
    public void sortColorsRules(){
      String[] hell = this.inputs.split(",");
      for(String bb : hell){
          if(isNumeric(bb)){
              
              this.rules.add(Integer.parseInt(bb));
          }else{
              
              this.colors.add(bb.charAt(0));
          }
      }
    
    }
    
    public static boolean isNumeric(String str)  
    {  
        try  
        {  
            int d = Integer.parseInt(str);  
        }  
        catch(NumberFormatException nfe)  
        {  
            return false;  
        }  
        return true;  
    }
}
