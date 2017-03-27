import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid; // initialie a grid with boolean data types
    private int N; //variable used in other methods of Percolation
    private int NumOpen;
    private WeightedQuickUnionUF UFgrid;
    private int virtualtop;
    private int virtualbottom;
    
    public Percolation(int n) {  // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new IllegalArgumentException("N must be at least 1");
        }
        
        N = n; //grid size variable transfer
        UFgrid = new WeightedQuickUnionUF(n*n+2); //create grid object, includes virtual top and bottom
        grid = new boolean [n][n]; // makes a blank grid sized n x n, all false
        
        virtualtop = n*n; //virtual top is second to last value in UFgrid
        virtualbottom = n*n+1; //virtual bottom is last value in UFgrid
    }
    
    public void open(int row, int col) {    // open site (row, col) if it is not open already, true is open
        if (!isOpen(row,col)) {
            if (ValidateIndex(row,col)) {
                grid[row-1][col-1] = true; //opens (row,col)
                int ArrayIndex = ElementIndex(row,col);
                
                if (row == 1) {  //(row,col) connected to virtual top
                    UFgrid.union(ArrayIndex,virtualtop);
                }
                
                if (row == N) { //(row,col) connected to virtual bottom
                    UFgrid.union(ArrayIndex,virtualbottom);
                }
                
                if (row != 1 && isOpen(row-1,col)) {  //(row,col) connected to component at the top
                    UFgrid.union(ArrayIndex,ElementIndex(row-1,col));
                }
                
                if (row < N && isOpen(row+1,col)) {  //(row,col) connected to component at the bottom
                    UFgrid.union(ArrayIndex,ElementIndex(row+1,col));
                }
                
                if (col != 1 && isOpen(row,col-1)) {  //(row,col) connected to component at the left
                    UFgrid.union(ArrayIndex,ElementIndex(row,col-1));
                }
                
                if (col < N && isOpen(row,col+1)) {  //(row,col) connected to composnent at the right
                    //how do you handle columns outside?
                    UFgrid.union(ArrayIndex,ElementIndex(row,col+1));
                }
            }
            else throw new IndexOutOfBoundsException();
        }
    }

    public boolean isOpen(int row, int col) {  // is site (row, col) open or TRUE?
        return grid[row-1][col-1]; //returns true if open, and false is closed
    }
    
    public boolean isFull(int row, int col) { // is site (row, col) full, connected to the top?
        if (isOpen(row,col)) {
            return UFgrid.connected(virtualtop, ElementIndex(row,col));
        }
        return false;
    }
    
    public int numberOfOpenSites() {       // number of open sites
        for (int x = 0; x < N; x ++ ) {
           
            for (int y = 0; y < N; y ++ ) { 
                if (grid[x][y] == true) {
                    NumOpen ++;
                }    
            }
        }     
        return NumOpen;
    } 
    
    public boolean percolates() { // does the system percolate?
        return UFgrid.connected(virtualtop,virtualbottom);
    }
    
    private int ElementIndex(int row, int col) { //assigns a number to each matrix element
        return (row - 1) * N + col; 
    }
    
    private boolean ValidateIndex(int row, int col) { // validates if input are within matrix range
        if (row > 0 && row <= N || col > 0 && col <= N) {
            return true;
        }
        throw new IndexOutOfBoundsException();
    }
}