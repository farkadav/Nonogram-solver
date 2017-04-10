# Nonogram-solver
 What is nonogram? Check wiki https://en.wikipedia.org/wiki/Nonogram 
 
 CSPMain is a main class where you load an input file (examples krtkek.txt and dino.txt... this program cannot solve dino.txt not sure where is the issue I will try correct it). It will create set of rules given in the input file. This program can solve even colored nonograms when you have rules as B,3 that means there will be a block of 3 cells colored blue, this program just writes letter B instead of color and '_' for empty space) 
 
 CSPMain class will procces the rules and store it in Rules class, where it creates individual rules which are stored in Rule class. 
 
 CSPSolver is class where nonogram is solved. The program starts with making all possible combinations of blocks for rows and columns and stores it in an array of CSPVariables (CSPVariable is a class that stores my csp variable in this program it is a row or column). Then it checks for arc consistency(AC-3 algorithm)... so it checks whether the intersection of column ad row has hase value (color or empty space) and if it that isnt the case it deletes that possibility from variable (reducing domain). 
 
 After that it orders variables using a priority queue so that it starts solving the puzzle with those variables that have smallest domain (MVR heuristic). At this point the program starts to solve the puzzle using backtracking algorithm. If it finds a solution it  is stored and printed out. This program can find all the solutions if they exists.
