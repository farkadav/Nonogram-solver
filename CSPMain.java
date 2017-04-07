package nonogram;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author David
 */
public class CSPMain {
    

    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            ArrayList<Integer> dimensions = new ArrayList<>();
            ArrayList<Rules> rows = new ArrayList<>();
            ArrayList<Rules> cols = new ArrayList<>();
        
        try{
            File file = new File("krtkek.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String line;
            int n = 0;
            int m = 0;
            int k = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (n == 0){
                    String[] dims = line.split(",");
                    for (String s : dims){
                        dimensions.add(Integer.parseInt(s));
                    }                    
                }
                if (n != 0 & n <= dimensions.get(0)) {
                    rows.add(new Rules(m,line));
                    m++;
                }
                if (n >dimensions.get(0)){
                    
                    cols.add(new Rules(k,line));
                    k++;
                }
            	n++;
            }
            fileReader.close();
            
            System.out.println("Input parsed");
            
            



        } catch (IOException e) {
            e.printStackTrace();
	}
        
        CSPSolver solver = new CSPSolver(rows, cols, dimensions.get(0), dimensions.get(1));
            if(solver.solve()){
                System.out.println(solver.solutions.size());
                solver.solutions.forEach((sol) -> {
                    System.out.println(sol);
                });
            } else {
                System.out.println("There is no solution.");
            }
    }
    
}
