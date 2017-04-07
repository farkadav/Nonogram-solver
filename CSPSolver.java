/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nonogram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;


/**
 *
 * @author David
 */
public class CSPSolver {
    
    public ArrayList<Rules> rowRules;
    public ArrayList<Rules> colRules;
    public ArrayList<String> solutions;
    public int rowDim;
    public int colDim;
    public ArrayList<CSPVariable> rowCombinations;
    public ArrayList<CSPVariable> colCombinations;   
    public PriorityQueue<CSPVariable> orderedVars;
    private Line[] rowSolution;
    private Line[] colSolution;
    public int helpX,helpY;
    Comparator comparator = (Comparator<CSPVariable>) (CSPVariable o1, CSPVariable o2) -> {
        int var1 = o1.storage.size();
        int var2 = o2.storage.size();
        
        if (var1 < var2) return -1;
        if (var1< var2) return 1;
        return 0;
    };
    
    public CSPSolver(ArrayList<Rules> r, ArrayList<Rules> c, int rows, int cols){
        this.rowRules = r;
        this.colRules = c;
        this.rowDim = rows;
        this.colDim = cols;
        this.solutions = new ArrayList<>();        
        this.rowCombinations = new ArrayList<>();
        this.colCombinations = new ArrayList<>();
        this.colSolution = new Line[this.colDim];
        this.rowSolution = new Line[this.rowDim];        
        this.orderedVars = new PriorityQueue<>(comparator);
       
                
    }

    
    
    public boolean solve(){
        for (int i =0 ; i < rowDim ; i++){             
            rowCombinations.add(makeCombs(new CSPVariable(i,true),new char[colDim],'_',rowRules.get(i),0,0));
        }
        for (int i =0 ; i < colDim ; i++){             
            colCombinations.add(makeCombs(new CSPVariable(i,false),new char[rowDim],'_',colRules.get(i),0,0));
        }
        
        arcConsistency();
        orderVars();        
        backtracking();
        return !solutions.isEmpty();
    }
    
    public void orderVars(){        
        for(int i =0; i < rowDim; i++){            
            orderedVars.add(rowCombinations.get(i));
        }
        for(int i =0; i < colDim; i++){
            orderedVars.add(colCombinations.get(i));
        }
    }
  
    
    public CSPVariable makeCombs(CSPVariable var, char[] oldSequence, char lastColor,  Rules rules,int offSet, int index){
        if(offSet<oldSequence.length){
            if(index < rules.getRules().size()){
                
                Rule rule = rules.getRules().get(index);
                
                char color = rule.color;
                int size = rule.size;
                
                boolean colorNotEmptyOrSame = lastColor != '_' && lastColor != color;
                boolean fitToTheGrid = offSet+size-1<oldSequence.length;
                
                if((colorNotEmptyOrSame || lastColor == '_' )&& fitToTheGrid){
                    
                    for(int i = 0; i< size;i++){
                        oldSequence[offSet+i] = color;
                    }
                    makeCombs(var,oldSequence,color,rules,offSet+size,index+1);
                    for(int i=0;i<size;i++){
                        oldSequence[offSet+i] = '_';
                    }
                }
            }
            oldSequence[offSet]='_';
            makeCombs(var,oldSequence,'_',rules,offSet+1,index);
        }
        else if(rules.getRules().size()<= index) var.storage.add(oldSequence.clone());
        
        return var;
    }
    
    @SuppressWarnings("UnnecessaryReturnStatement")
    public void backtracking(){
        if(orderedVars.isEmpty()){
            
            String[] solString = new String[rowDim];
            for(Line line : rowSolution){
                
                solString[line.position] = new String();
                for(int j=0; j<colDim; j++){
                    solString[line.position] +=line.value[j];
                }
              
            }
            String solution = new String();
            for(String sol : solString){
                solution += sol +"\n";
            }
            solutions.add(solution);
            return;
        }
        CSPVariable cspVar = orderedVars.poll();
        cspVar.storage.stream().filter((var) -> (consistent(var, cspVar))).forEachOrdered((var) -> {
            if(cspVar.Row){                
                rowSolution[helpX++] = new Line(var,cspVar.position,cspVar.Row);
                backtracking();
                rowSolution[--helpX] = null;
            } else{
                colSolution[helpY++] = new Line(var,cspVar.position,cspVar.Row);
                backtracking();
                colSolution[--helpY] = null;
            }
        });
        orderedVars.add(cspVar);
    }
    
    public boolean consistent(char[] val, CSPVariable var){
        if(var.Row){
            for(int i =0;i< helpY;i++){
                if(val[colSolution[i].position] != colSolution[i].value[var.position]){
                    return false;
                }
            }
        }else{
            for(int i =0;i< helpX;i++){
                if(val[rowSolution[i].position] != rowSolution[i].value[var.position]){
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public void arcConsistency(){
        ArrayList<Arc> consistencyList = new ArrayList<>();
        for(int i = 0; i < rowDim; i++){
            for(int j = 0; j<colDim;j++){                
                consistencyList.add(new Arc(rowCombinations.get(i),colCombinations.get(j)));
                consistencyList.add(new Arc(colCombinations.get(j),rowCombinations.get(i)));
            }
        }
        while(!consistencyList.isEmpty()){
            Arc arc = consistencyList.get(0);
            consistencyList.remove(0);
            if(!arc.isConsistent()){                
                for (Integer toDelete : arc.deletedVars){
                    int help = toDelete;
                    arc.cspVar1.storage.remove(help);
                }
                if(arc.cspVar1.Row){
                    for(int i =0; i<colDim;i++){
                        consistencyList.add(new Arc(arc.cspVar1,colCombinations.get(i)));
                    }                    
                }else{
                    for(int i =0; i<rowDim;i++){
                        consistencyList.add(new Arc(arc.cspVar1,rowCombinations.get(i)));
                    }   
                }
            }
        }
    }
}
