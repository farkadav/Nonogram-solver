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
public class Arc {
    public CSPVariable cspVar1;
    public CSPVariable cspVar2;
    public ArrayList<Integer> deletedVars;
    public boolean consistent;
    
    public Arc(CSPVariable var1, CSPVariable var2){
        this.cspVar1 = var1;
        this.cspVar2 = var2;
    }
    
    public boolean isConsistent(){
            
            consistent = true;
            int x = cspVar1.position;
            int y = cspVar2.position; 
            
           deletedVars = new ArrayList<>();
            for (int i = 0; i < cspVar1.storage.size();i++){
                boolean identical = false;
                char[] var1 = cspVar1.storage.get(i);
                for(int j=0; j<cspVar2.storage.size();j++){
                    char[] var2 = cspVar2.storage.get(j);
           
                    char help1 = var1[y];
                    char help2 = var2[x];
                    if( help1 == help2){
                        identical = true;
                        break;
                    }
            }
            if(!identical){
                deletedVars.add(0, i);
                consistent = false;
            }

        }      
        
        return consistent;
    }
}
