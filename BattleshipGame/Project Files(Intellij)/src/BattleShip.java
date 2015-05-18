import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Vivian Venter (13238435) & Jason Evans (13032608)
 * COS 332 - Practical 9
 * Battleship Game
 * Collaboration
 */
public class BattleShip {

    private char grid[][];
    private static int gridSize = 0;
    private static int puzzleNr = 0;
    private int numberofA = 0;
    private int numberofB = 0;
    private int numberofC = 0;
    private int numberofD = 0;
    private int numberofE = 0;
    private int totalNumberOfBlocks = 0;

    private int hits = 0;
    private int misses = 0;
    private int totalShots = 0;

    static String GLOBAL_Notification = "";

    private static BattleShip instance = null;
    private static GridContent gc = null;

    public static BattleShip getInstance() {
        if(instance == null) {
            instance = new BattleShip();
        }
        return instance;
    }

    private BattleShip() {
        System.out.println("Game Initialized!");
        gc = new GridContent(this);
    }

    public void initializeGrid() throws IOException {
        // textfile will have naming convention: grid#+* where # is the grid size and * is the puzzle number
        // for example: grid6_2 will be puzzle 2 for a grid of size 6x6
        clearData();

        int size = getGridSize();
        int gridNr = getPuzzleNr();


        //Since each block will consist of a certain number of blocks, this is where we initialize the number of blocks per ship
        if (size == 6) { // only 3 ships: A,B,C
            gc.setTableBoatInfoContent6();
            setTotalNumberOfBlocks(5 + 2 + 6);
        }
        else if (size == 8) { // only 4 ships: A,B,C,D
            gc.setTableBoatInfoContent8();
            setTotalNumberOfBlocks(5 + 2 + 6 + 7);
        }
        else if (size == 10) { // only 5 ships: A,B,C,D,E
            gc.setTableBoatInfoContent10();
            setTotalNumberOfBlocks(5 + 2 + 6 +7+9);
        }



        String line;
        char ch;
        int rowC = 0;
        String gridFilename = "GameFiles/grid"+size+"_"+gridNr+".txt";
        File gridFile = new File(gridFilename);
        FileInputStream in = new FileInputStream(gridFile);
        DataInputStream dis = new DataInputStream(in);

        while (dis.available() != 0) {
            line = dis.readLine();

            for (int k = 0; k < size; k++) {
                ch = line.charAt(k);
                grid[rowC][k] = ch;
            }
            rowC++;
        }

        printGrid();

    }

    private void clearData() {
        hits = 0;
        misses = 0;
        totalShots = 0;
        totalNumberOfBlocks = 0;
        numberofE = 0;
        numberofD = 0;
        numberofC = 0;
        numberofB = 0;
        numberofA = 0;
        GLOBAL_Notification = "";
    }

    private void printGrid() {
        System.out.println("GRID: ");
        for (int k = 0; k < gridSize; k++) {
            for (int i = 0; i < gridSize; i++) {
                if (i == (gridSize-1)) {
                    System.out.print(grid[k][i]);
                }
                else {
                    System.out.print(grid[k][i] + "	");
                }
            }
            System.out.println();
        }
    }

    public String constructGameFile() {
        return gc.constructGridContent(getGrid(),getGridSize());
    }

    public char[][] getGrid() {
        return grid;
    }

    public void setGrid() {
        grid = new char[gridSize][gridSize];
    }

    public static int getGridSize() {
        return gridSize;
    }

    public static void setGridSize(int gridSize) {
        BattleShip.gridSize = gridSize;
    }

    public static int getPuzzleNr() {
        return puzzleNr;
    }

    public static void setPuzzleNr(int puzzleNr) {
        BattleShip.puzzleNr = puzzleNr;
    }

    public String getNumberofA() {return "" + numberofA;}

    public String getNumberofB() {return "" + numberofB;}

    public String getNumberofC() {return "" + numberofC; }

    public String getNumberofD() {return ""+ numberofD;}

    public String getNumberofE() {return ""+ numberofE;}

    public int getTotalNumberOfBlocks() {
        return totalNumberOfBlocks;
    }

    public void setTotalNumberOfBlocks(int totalNumberOfBlocks) {
        this.totalNumberOfBlocks = totalNumberOfBlocks;
    }

    public char getBlock(int a, int b) {
        if (getGrid() == null) {
            System.out.println("Cannot access Grid!");
            return '1';
        }
        else {
            return grid[a][b];
        }
    }

    public void won() {
        GLOBAL_Notification = "You have sunk all the ships! Congratulations, You have Won!";
        disableGrid();
    }

    private void disableGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int k = 0; k < gridSize; k++){
                if (grid[i][k] == '0') {
                    grid[i][k] = '1';
                }
            }
        }
    }

    public void noBlockShot(int row, int col) {
        grid[row][col] = '1';

        misses++;
        totalShots++;

        System.out.println("No Ship was shot!");
        GLOBAL_Notification = "No Ship was shot! Try Again...";
    }

    public void shipEShot(int row, int col) {
        System.out.println("Ship E was shot!");
        grid[row][col] = '2';

        numberofE++;
        totalNumberOfBlocks--;
        hits++;
        totalShots++;

        if (gridSize == 6) {
            gc.setTableBoatInfoContent6();
        }
        else if (gridSize == 8) {
            gc.setTableBoatInfoContent8();
        }
        else if (gridSize == 10) {
            gc.setTableBoatInfoContent10();
        }

        if (numberofE == 0) {
            GLOBAL_Notification =  "Ship E was shot!\n Ship E has sunk!";
        }
        else {
            GLOBAL_Notification =  "Ship E was shot!\n Only " + numberofE + " blocks left for Ship E to sink!";
        }
    }

    public void shipDShot(int row, int col) {
        System.out.println("Ship D was shot!");
        grid[row][col] = '2';

        numberofD++;
        totalNumberOfBlocks--;

        hits++;
        totalShots++;

        if (gridSize == 6) {
            gc.setTableBoatInfoContent6();
        }
        else if (gridSize == 8) {
            gc.setTableBoatInfoContent8();
        }
        else if (gridSize == 10) {
            gc.setTableBoatInfoContent10();
        }

        if (numberofD == 0) {
            GLOBAL_Notification =  "Ship D was shot!\n Ship D has sunk!";
        }
        else {
            GLOBAL_Notification =  "Ship D was shot!\n Only " + numberofD + " blocks left for Ship D to sink!";
        }
    }

    public void shipCShot(int row, int col) {
        System.out.println("Ship C was shot!");
        grid[row][col] = '2';

        numberofC++;
        totalNumberOfBlocks--;

        hits++;
        totalShots++;

        if (gridSize == 6) {
            gc.setTableBoatInfoContent6();
        }
        else if (gridSize == 8) {
            gc.setTableBoatInfoContent8();
        }
        else if (gridSize == 10) {
            gc.setTableBoatInfoContent10();
        }

        if (numberofC == 0) {
            GLOBAL_Notification =  "Ship C was shot!\n Ship C has sunk!";
        }
        else {
            GLOBAL_Notification =  "Ship C was shot!\n Only " + numberofC + " blocks left for Ship C to sink!";
        }
    }

    public void shipBShot(int row, int col) {
        System.out.println("Ship B was shot!");
        grid[row][col] = '2';

        numberofB++;
        totalNumberOfBlocks--;

        hits++;
        totalShots++;

        if (gridSize == 6) {
            gc.setTableBoatInfoContent6();
        }
        else if (gridSize == 8) {
            gc.setTableBoatInfoContent8();
        }
        else if (gridSize == 10) {
            gc.setTableBoatInfoContent10();
        }

        if (numberofB == 0) {
            GLOBAL_Notification =  "Ship B was shot!\n Ship B has sunk!";
        }
        else {
            GLOBAL_Notification =  "Ship B was shot!\n Only " + numberofB + " blocks left for Ship B to sink!";
        }
    }

    public void shipAShot(int row, int col) {
        System.out.println("Ship A was shot!");
        grid[row][col] = '2';

        numberofA++;
        totalNumberOfBlocks--;

        hits++;
        totalShots++;

        if (gridSize == 6) {
            gc.setTableBoatInfoContent6();
        }
        else if (gridSize == 8) {
            gc.setTableBoatInfoContent8();
        }
        else if (gridSize == 10) {
            gc.setTableBoatInfoContent10();
        }

        if (numberofA == 0) {
            GLOBAL_Notification = "Ship A was shot!\n Ship A has sunk!";
        }
        else {
            GLOBAL_Notification = "Ship A was shot!\n Only " + numberofA + " blocks left for Ship A to sink!";
        }

    }

    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }

    public int getTotalShots() {
        return totalShots;
    }

    public double getAccuracy() {
        if (totalShots == 0) {
            return 0.0;
        }
        return ( (hits*100)/totalShots );
    }
}
