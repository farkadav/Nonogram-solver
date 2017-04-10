/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

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
    public int tempX,tempY;
    Comparator comparator = (Comparator<CSPVariable>) (CSPVariable o1, CSPVariable o2) -> {
        int var1 = o1.storage.size();
        int var2 = o2.storage.size();
        
        if (var1 < var2) return -1;
        if (var1> var2) return 1;
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
    
    //This method solves the nonogram
    public boolean solve(){
        for (int i =0 ; i < rowDim ; i++){             
            rowCombinations.add(makeCombs(new CSPVariable(i,true),new char[colDim],colDim,'_',rowRules.get(i),0,0));
        }
        for (int i =0 ; i < colDim ; i++){             
            colCombinations.add(makeCombs(new CSPVariable(i,false),new char[rowDim],rowDim,'_',colRules.get(i),0,0));
        }
        
        arcConsistency();
        orderVars(); 
        
        backtracking();
        return !solutions.isEmpty();
    }
    
    // Order variables using priority queue
    public void orderVars(){         
        rowCombinations.forEach((r) -> {
            orderedVars.add(r);
        });       
        colCombinations.forEach((c) -> {
            orderedVars.add(c);
        });
    }
    
    //Make all valid combinations of blocks for row/col 
    public CSPVariable makeCombs(CSPVariable var, char[] oldSequence,int lineLength, char lastColor,  Rules rules,int offSet, int index){
        if(offSet<lineLength){
            if(index < rules.getRules().size()){
                
                Rule rule = rules.getRules().get(index);
                
                char color = rule.color;
                int size = rule.size;
                
                boolean NotEmptyAndSame = lastColor != '_' && lastColor != color;
                boolean fitToTheGrid = offSet+size-1<lineLength;
                
                if((NotEmptyAndSame || lastColor == '_' )&& fitToTheGrid){
                    
                    for(int i = 0; i< size;i++){
                        oldSequence[offSet+i] = color;
                    }
                    makeCombs(var,oldSequence,lineLength,color,rules,offSet+size,index+1);
                    for(int i=0;i<size;i++){
                        oldSequence[offSet+i] = '_';
                    }
                }
            }
            oldSequence[offSet]='_';
            makeCombs(var,oldSequence,lineLength,'_',rules,offSet+1,index);
        }
        else if(rules.getRules().size()<= index) var.storage.add(oldSequence.clone());
        
        return var;
    }
    
    // if solution exists record it
    public void makeSolution(){
        String[] solString = new String[rowDim];
        for(Line line : rowSolution){
            solString[line.position] = new String();
            for(int j=0; j<colDim; j++){
                solString[line.position] +=line.sequence[j];
            }
        }
        String solution = new String();
        for(String sol : solString){
             solution += sol +"\n";
        }
        solutions.add(solution);
        
    } 
    
    // backtraing algortihm which is trying to solve the nonogram
    @SuppressWarnings("UnnecessaryReturnStatement")
    public void backtracking(){        
        if(orderedVars.isEmpty()) {
            makeSolution();
            return;
        }
            
        CSPVariable cspVar;
        cspVar = orderedVars.poll();
 
        cspVar.storage.stream().filter((var) -> (consistent(var, cspVar))).forEachOrdered((var) -> {
            if(cspVar.Row){                
                rowSolution[tempX++] = new Line(var,cspVar.position,cspVar.Row);
                backtracking();
                rowSolution[--tempX] = null;
            } else{
                colSolution[tempY++] = new Line(var,cspVar.position,cspVar.Row);
                backtracking();
                colSolution[--tempY] = null;
            }
        });
        
        orderedVars.add(cspVar);
    }

    //check for consistency of solution so far
    public boolean consistent(char[] val, CSPVariable var){
        if(var.Row){
            for(int i =0;i< tempY;i++){
                if(val[colSolution[i].position] != colSolution[i].sequence[var.position]){
                    return false;
                }
            }
        }else{
            for(int i =0;i< tempX;i++){
                if(val[rowSolution[i].position] != rowSolution[i].sequence[var.position]){
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public void deleteInconsistent(Arc arc){
        for (Integer toDelete : arc.deletedVars){
            int help = toDelete;
            arc.cspVar1.storage.remove(help);
        }
    }
    
    public ArrayList<Arc> makeArcs(){
        ArrayList<Arc> consistencyList = new ArrayList<>();
        for(CSPVariable row : rowCombinations){
            for(CSPVariable col : colCombinations){
                consistencyList.add(new Arc(row,col));
                consistencyList.add(new Arc(col,row));
            }
        }
        return consistencyList;
    }
    //AC-3 algortihm 
    public void arcConsistency(){
        ArrayList<Arc> consistencyList = makeArcs();
        
        while(!consistencyList.isEmpty()){
            Arc arc = consistencyList.get(0);
            consistencyList.remove(0);
            
            if(!arc.isConsistent()){ 
                deleteInconsistent(arc);
                
                if(arc.cspVar1.Row){
                    for(CSPVariable col : colCombinations){
                        consistencyList.add(new Arc(arc.cspVar1,col));
                    }
                }else{
                    for(CSPVariable row : rowCombinations){
                        consistencyList.add(new Arc(arc.cspVar1,row));
                    }                    
                }
            }
        }        
    }
}
