import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by VivianWork on 5/16/2015.
 */
public class BattleShip {

    private char grid[][];
    private static int gridSize = 0;
    private static int puzzleNr = 0;
    private int numberofA;
    private int numberofB;
    private int numberofC;
    private int numberofD;
    private int numberofE;
    private int totalNumberOfBlocks;
    private String tableBoatInfoContent = "";

    public BattleShip() {
    }

    public void initializeGrid() throws IOException {
        // textfile will have naming convention: grid#+* where # is the grid size and * is the puzzle number
        // for example: grid6_2 will be puzzle 2 for a grid of size 6x6
        int size = getGridSize();
        int gridNr = getPuzzleNr();

        //Since each block will consist of a certain number of blocks, this is where we initialize the number of blocks per ship
        if (size == 6) { // only 3 ships: A,B,C
            numberofA = 5; // ship A will always have 5 blocks
            numberofB = 2; // ship B will always have 2 blocks
            numberofC = 6; // ship C will always have 6 blocks
            numberofD = 0;
            numberofE = 0;

            tableBoatInfoContent =
                    "<table class=\"table\" style=\"color: white;\">\n" +
                            "                <thead>\n" +
                            "                <tr>\n" +
                            "                    <th>Boat Name</th>\n" +
                            "                    <th>Length</th>\n" +
                            "                    <th>Destroyed</th>\n" +
                            "                </tr>\n" +
                            "                </thead>\n" +
                            "                <tbody>\n" +
                            "                <tr>\n" +
                            "                    <td>Aircraft Carrier</td>\n" +
                            "                    <td>5</td>\n" +
                            "                    <td id=\"AircraftDestroyed\">0</td>\n" +
                            "                </tr>\n" +
                            "                <tr>\n" +
                            "                    <td>Battleship</td>\n" +
                            "                    <td>5</td>\n" +
                            "                    <td id=\"BattleshipDestroyed\">0</td>\n" +
                            "                </tr>\n" +
                            "                <tr>\n" +
                            "                    <td>Destroyer</td>\n" +
                            "                    <td>5</td>\n" +
                            "                    <td id=\"DestroyerDestroyed\">0</td>\n" +
                            "                </tr>\n" +
                            "                </tbody>\n" +
                            "            </table>";
        }
        else if (size == 8) { // only 4 ships: A,B,C,D
            numberofA = 5; // ship A will always have 5 blocks
            numberofB = 2; // ship B will always have 2 blocks
            numberofC = 6; // ship C will always have 6 blocks
            numberofD = 7; // ship D will always have 7 blocks
            numberofE = 0;
        }
        else if (size == 10) { // only 5 ships: A,B,C,D,E
            numberofA = 5; // ship A will always have 5 blocks
            numberofB = 2; // ship B will always have 2 blocks
            numberofC = 6; // ship C will always have 6 blocks
            numberofD = 7; // ship D will always have 7 blocks
            numberofE = 9; // ship E will always have 9 blocks
        }

        setTotalNumberOfBlocks(numberofA+numberofB+numberofC+numberofD+numberofE);

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

    public int getNumberofA() {
        return numberofA;
    }

    public void setNumberofA(int numberofA) {
        this.numberofA = numberofA;
    }

    public int getNumberofB() {
        return numberofB;
    }

    public void setNumberofB(int numberofB) {
        this.numberofB = numberofB;
    }

    public int getNumberofC() {
        return numberofC;
    }

    public void setNumberofC(int numberofC) {
        this.numberofC = numberofC;
    }

    public int getNumberofD() {
        return numberofD;
    }

    public void setNumberofD(int numberofD) {
        this.numberofD = numberofD;
    }

    public int getNumberofE() {
        return numberofE;
    }

    public void setNumberofE(int numberofE) {
        this.numberofE = numberofE;
    }

    public int getTotalNumberOfBlocks() {
        return totalNumberOfBlocks;
    }

    public void setTotalNumberOfBlocks(int totalNumberOfBlocks) {
        this.totalNumberOfBlocks = totalNumberOfBlocks;
    }

    public String getTableBoatInfoContent() {
        return tableBoatInfoContent;
    }

    public void setTableBoatInfoContent(String tableBoatInfoContent) {
        this.tableBoatInfoContent = tableBoatInfoContent;
    }

    public char getBlock(int a, int b) {
        return grid[a][b];
    }

    public String shipEShot() {
        System.out.println("Ship E was shot!");
        numberofE--;
        totalNumberOfBlocks--;

        if (numberofE == 0) {
            return "Ship E was shot!\n Ship E has sunk!";
        }
        else {
            return "Ship E was shot!\n Only " + numberofE + " blocks left for Ship E to sink!";
        }
    }

    public String shipDShot() {
        System.out.println("Ship D was shot!");
        numberofD--;
        totalNumberOfBlocks--;

        if (numberofD == 0) {
            return "Ship D was shot!\n Ship D has sunk!";
        }
        else {
            return "Ship D was shot!\n Only " + numberofD + " blocks left for Ship D to sink!";
        }
    }

    public String shipCShot() {
        System.out.println("Ship C was shot!");
        numberofC--;
        totalNumberOfBlocks--;

        if (numberofC == 0) {
            return "Ship C was shot!\n Ship C has sunk!";
        }
        else {
            return "Ship C was shot!\n Only " + numberofC + " blocks left for Ship C to sink!";
        }
    }

    public String shipBShot() {
        System.out.println("Ship B was shot!");
        numberofB--;
        totalNumberOfBlocks--;

        if (numberofB == 0) {
            return "Ship B was shot!\n Ship B has sunk!";
        }
        else {
            return "Ship B was shot!\n Only " + numberofB + " blocks left for Ship B to sink!";
        }
    }

    public String shipAShot() {
        System.out.println("Ship A was shot!");
        numberofA--;
        totalNumberOfBlocks--;

        if (numberofA == 0) {
            return "Ship A was shot!\n Ship A has sunk!";
        }
        else {
            return "Ship A was shot!\n Only " + numberofA + " blocks left for Ship A to sink!";
        }
    }
}
