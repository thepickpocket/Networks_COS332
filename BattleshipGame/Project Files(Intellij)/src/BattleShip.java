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
    static String GLOBAL_Notification = "";

    private static BattleShip instance = null;

    public static BattleShip getInstance() {
        if(instance == null) {
            instance = new BattleShip();
        }
        return instance;
    }

    private BattleShip() {
        System.out.println("Game Initialized!");
    }

    public void initializeGrid() throws IOException {
        // textfile will have naming convention: grid#+* where # is the grid size and * is the puzzle number
        // for example: grid6_2 will be puzzle 2 for a grid of size 6x6
        int size = getGridSize();
        int gridNr = getPuzzleNr();


        //Since each block will consist of a certain number of blocks, this is where we initialize the number of blocks per ship
        if (size == 6) { // only 3 ships: A,B,C
            System.out.println("Initializing a grid of size 6");
            numberofA = 5; // ship A will always have 5 blocks
            numberofB = 2; // ship B will always have 2 blocks
            numberofC = 6; // ship C will always have 6 blocks
            numberofD = 0;
            numberofE = 0;
            setTableBoatInfoContent6();


        }
        else if (size == 8) { // only 4 ships: A,B,C,D
            numberofA = 5; // ship A will always have 5 blocks
            numberofB = 2; // ship B will always have 2 blocks
            numberofC = 6; // ship C will always have 6 blocks
            numberofD = 7; // ship D will always have 7 blocks
            numberofE = 0;
            setTableBoatInfoContent8();

        }
        else if (size == 10) { // only 5 ships: A,B,C,D,E
            numberofA = 5; // ship A will always have 5 blocks
            numberofB = 2; // ship B will always have 2 blocks
            numberofC = 6; // ship C will always have 6 blocks
            numberofD = 7; // ship D will always have 7 blocks
            numberofE = 9; // ship E will always have 9 blocks
            setTableBoatInfoContent10();

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

    public String constructGameFile() {
        return getGameFileHeader() + getNotifications() + getGridContent() + getStatsContent() + getTableBoatInfoContent() + getGameFileFooter();
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

    public String getNumberofA() {
        return "                    <td>"+ numberofA+"</td>\n";
    }

    public String getNumberofB() {
        return "                    <td>"+ numberofB +"</td>\n";
    }

    public String getNumberofC() {
        return "                    <td>"+ numberofC +"</td>\n";
    }

    public String getNumberofD() {
        return "                    <td>"+ numberofD +"</td>\n";
    }

    public String getNumberofE() {
        return "                    <td>"+ numberofE +"</td>\n";
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
    }

    public void noBlockShot() {
        System.out.println("No Ship was shot!");
        GLOBAL_Notification = "No Ship was shot! Try Again...";
    }

    public void shipEShot() {
        System.out.println("Ship E was shot!");
        numberofE--;
        totalNumberOfBlocks--;

        if (gridSize == 6) {
            setTableBoatInfoContent6();
        }
        else if (gridSize == 8) {
            setTableBoatInfoContent8();
        }
        else if (gridSize == 10) {
            setTableBoatInfoContent10();
        }

        if (numberofE == 0) {
            GLOBAL_Notification =  "Ship E was shot!\n Ship E has sunk!";
        }
        else {
            GLOBAL_Notification =  "Ship E was shot!\n Only " + numberofE + " blocks left for Ship E to sink!";
        }
    }

    public void shipDShot() {
        System.out.println("Ship D was shot!");
        numberofD--;
        totalNumberOfBlocks--;

        if (gridSize == 6) {
            setTableBoatInfoContent6();
        }
        else if (gridSize == 8) {
            setTableBoatInfoContent8();
        }
        else if (gridSize == 10) {
            setTableBoatInfoContent10();
        }

        if (numberofD == 0) {
            GLOBAL_Notification =  "Ship D was shot!\n Ship D has sunk!";
        }
        else {
            GLOBAL_Notification =  "Ship D was shot!\n Only " + numberofD + " blocks left for Ship D to sink!";
        }
    }

    public void shipCShot() {
        System.out.println("Ship C was shot!");
        numberofC--;
        totalNumberOfBlocks--;

        if (gridSize == 6) {
            setTableBoatInfoContent6();
        }
        else if (gridSize == 8) {
            setTableBoatInfoContent8();
        }
        else if (gridSize == 10) {
            setTableBoatInfoContent10();
        }

        if (numberofC == 0) {
            GLOBAL_Notification =  "Ship C was shot!\n Ship C has sunk!";
        }
        else {
            GLOBAL_Notification =  "Ship C was shot!\n Only " + numberofC + " blocks left for Ship C to sink!";
        }
    }

    public void shipBShot() {
        System.out.println("Ship B was shot!");
        numberofB--;
        totalNumberOfBlocks--;

        if (gridSize == 6) {
            setTableBoatInfoContent6();
        }
        else if (gridSize == 8) {
            setTableBoatInfoContent8();
        }
        else if (gridSize == 10) {
            setTableBoatInfoContent10();
        }

        if (numberofB == 0) {
            GLOBAL_Notification =  "Ship B was shot!\n Ship B has sunk!";
        }
        else {
            GLOBAL_Notification =  "Ship B was shot!\n Only " + numberofB + " blocks left for Ship B to sink!";
        }
    }

    public void shipAShot() {
        System.out.println("Ship A was shot!");
        numberofA--;
        totalNumberOfBlocks--;

        if (gridSize == 6) {
            setTableBoatInfoContent6();
        }
        else if (gridSize == 8) {
            setTableBoatInfoContent8();
        }
        else if (gridSize == 10) {
            setTableBoatInfoContent10();
        }

        if (numberofA == 0) {
            GLOBAL_Notification = "Ship A was shot!\n Ship A has sunk!";
        }
        else {
            GLOBAL_Notification = "Ship A was shot!\n Only " + numberofA + " blocks left for Ship A to sink!";
        }

    }

    private String getGameFileHeader() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head lang=\"en\">\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <title>Battleship</title>\n" +
                "\n" +
                "    <!--Stylesheets-->\n" +
                "    <link rel=\"stylesheet\" href=\"Frameworks/Bootstrap/css/bootstrap.min.css\">\n" +
                "    <link rel=\"stylesheet\" href=\"//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css\">\n" +
                "\n" +
                "    <!--Javascript-->\n" +
                "    <script src=\"Frameworks/jquery-1.11.3.min.js\"></script>\n" +
                "    <script src=\"Frameworks/Bootstrap/js/bootstrap.min.js\"></script>\n" +
                "\n" +
                "</head>\n" +
                "<body style=\"background-image: url('Images/WorldMap.jpg'); background-size: cover;\">\n" +
                "<nav class=\"navbar navbar-inverse\">\n" +
                "    <div class=\"container-fluid\">\n" +
                "        <div class=\"navbar-header\">\n" +
                "            <a class=\"navbar-brand\" href=\"index.html\">\n" +
                "                <img src=\"Images/BattleShipLogo.png\" class=\"img-rounded\" alt=\"Battleships\" width=\"145\">\n" +
                "            </a>\n" +
                "        </div>\n" +
                "        <div>\n" +
                "            <ul class=\"nav navbar-nav\">\n" +
                "                <li class=\"active\"><a href=\"index.html\"><span class=\"glyphicon glyphicon-home\"></span>  Home</a></li>\n" +
                "                <li class=\"dropdown\"><a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\"><span class=\"glyphicon glyphicon-globe\"></span>  Ship Information (Alpha)<span class=\"caret\"></span></a>\n" +
                "                    <ul class=\"dropdown-menu\">\n" +
                "                        <li><a href=\"ShipInformation/AircraftCarrier.html\">Aircraft Carrier</a></li>\n" +
                "                        <li><a href=\"ShipInformation/Battleship.html\">Battleship</a></li>\n" +
                "                        <li><a href=\"ShipInformation/Destroyer.html\">Destroyer</a></li>\n" +
                "                        <li><a href=\"ShipInformation/PatrolBoat.html\">Patrol Boat</a></li>\n" +
                "                        <li><a href=\"ShipInformation/Submarine.html\">Submarine</a></li>\n" +
                "                    </ul>\n" +
                "                </li>\n" +
                "                <li><a href=\"help.html\"><span class=\"glyphicon glyphicon-question-sign\"></span>  Help</a></li>\n" +
                "                <li><a href=\"about.html\"><span class=\"glyphicon glyphicon-info-sign\"></span>  About Us</a></li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</nav>";
    }

    private String getGridContent() {
        if (gridSize == 6) {
            return getGridContent6();
        }
        else if (gridSize == 8) {
            return getGridContent8();
        }
        else if (gridSize == 10) {
            return getGridContent10();
        }

        return null;
    }

    private String getGridContent10() {
        return null;
    }

    private String getGridContent8() {
        return "<div class=\"container-fluid\">\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-md-8\" style=\"background-color: rgba(0,0,0,0.7); margin-top: 2.5%;\">\n" +
                "            <table class=\"table\" style=\"color: white;\">\n" +
                "                <thead>\n" +
                "                <tr>\n" +
                "                    <th> </th>\n" +
                "                    <th>A</th>\n" +
                "                    <th>B</th>\n" +
                "                    <th>C</th>\n" +
                "                    <th>D</th>\n" +
                "                    <th>E</th>\n" +
                "                    <th>F</th>\n" +
                "                    <th>G</th>\n" +
                "                    <th>H</th>\n" +
                "                </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n" +
                "                <tr>\n" +
                "                    <td>0</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A0';\" formmethod=\"get\" id=\"A0\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button> </button></td> <!--normal-->\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B0';\" formmethod=\"get\" id=\"B0\" disabled><i class=\"fa fa-ban fa-3x\"></i> </button></td> <!--miss-->\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C0';\" formmethod=\"get\" id=\"C0\"><i class=\"fa fa-fire fa-3x\" style=\"color: red\"></i></button></td> <!--Hit-->\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D0';\" formmethod=\"get\" id=\"D0\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E0';\" formmethod=\"get\" id=\"E0\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F0';\" formmethod=\"get\" id=\"F0\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=G0';\" formmethod=\"get\" id=\"G0\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=H0';\" formmethod=\"get\" id=\"H0\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>1</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A1';\" formmethod=\"get\" id=\"A1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B1';\" formmethod=\"get\" id=\"B1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C1';\" formmethod=\"get\" id=\"C1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D1';\" formmethod=\"get\" id=\"D1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E1';\" formmethod=\"get\" id=\"E1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F1';\" formmethod=\"get\" id=\"F1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=G1';\" formmethod=\"get\" id=\"G1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=H1';\" formmethod=\"get\" id=\"H1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                <tr>\n" +
                "                    <td>2</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A2';\" formmethod=\"get\" id=\"A2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B2';\" formmethod=\"get\" id=\"B2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C2';\" formmethod=\"get\" id=\"C2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D2';\" formmethod=\"get\" id=\"D2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E2';\" formmethod=\"get\" id=\"E2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F2';\" formmethod=\"get\" id=\"F2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=G2';\" formmethod=\"get\" id=\"G2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=H2';\" formmethod=\"get\" id=\"H2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>3</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A3';\" formmethod=\"get\" id=\"A3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B3';\" formmethod=\"get\" id=\"B3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C3';\" formmethod=\"get\" id=\"C3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D3';\" formmethod=\"get\" id=\"D3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E3';\" formmethod=\"get\" id=\"E3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F3';\" formmethod=\"get\" id=\"F3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=G3';\" formmethod=\"get\" id=\"G3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=H3';\" formmethod=\"get\" id=\"H3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>4</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A4';\" formmethod=\"get\" id=\"A4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B4';\" formmethod=\"get\" id=\"B4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C4';\" formmethod=\"get\" id=\"C4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D4';\" formmethod=\"get\" id=\"D4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E4';\" formmethod=\"get\" id=\"E4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F4';\" formmethod=\"get\" id=\"F4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=G4';\" formmethod=\"get\" id=\"G4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=H4';\" formmethod=\"get\" id=\"H4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>5</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A5';\" formmethod=\"get\" id=\"A5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B5';\" formmethod=\"get\" id=\"B5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C5';\" formmethod=\"get\" id=\"C5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D5';\" formmethod=\"get\" id=\"D5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E5';\" formmethod=\"get\" id=\"E5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F5';\" formmethod=\"get\" id=\"F5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=G5';\" formmethod=\"get\" id=\"G5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=H5';\" formmethod=\"get\" id=\"H5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>6</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A6';\" formmethod=\"get\" id=\"A6\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B6';\" formmethod=\"get\" id=\"B6\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C6';\" formmethod=\"get\" id=\"C6\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D6';\" formmethod=\"get\" id=\"D6\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E6';\" formmethod=\"get\" id=\"E6\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F6';\" formmethod=\"get\" id=\"F6\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=G6';\" formmethod=\"get\" id=\"G6\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=H6';\" formmethod=\"get\" id=\"H6\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>7</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A7';\" formmethod=\"get\" id=\"A7\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B7';\" formmethod=\"get\" id=\"B7\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C7';\" formmethod=\"get\" id=\"C7\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D7';\" formmethod=\"get\" id=\"D7\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E7';\" formmethod=\"get\" id=\"E7\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F7';\" formmethod=\"get\" id=\"F7\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=G7';\" formmethod=\"get\" id=\"G7\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=H7';\" formmethod=\"get\" id=\"H7\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>";
    }

    private String getGridContent6() {
        return "<div class=\"container-fluid\">\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-md-8\" style=\"background-color: rgba(0,0,0,0.7); margin-top: 5%;\">\n" +
                "            <table class=\"table\" style=\"color: white;\">\n" +
                "                <thead>\n" +
                "                <tr>\n" +
                "                    <th> </th>\n" +
                "                    <th>A</th>\n" +
                "                    <th>B</th>\n" +
                "                    <th>C</th>\n" +
                "                    <th>D</th>\n" +
                "                    <th>E</th>\n" +
                "                    <th>F</th>\n" +
                "                </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n" +
                "                <tr>\n" +
                "                    <td>0</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A0';\" formmethod=\"get\" id=\"A0\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button> </button></td> <!--normal-->\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B0';\" formmethod=\"get\" id=\"B0\" disabled><i class=\"fa fa-ban fa-3x\"></i> </button></td> <!--miss-->\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C0';\" formmethod=\"get\" id=\"C0\"><i class=\"fa fa-fire fa-3x\" style=\"color: red\"></i></button></td> <!--Hit-->\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D0';\" formmethod=\"get\" id=\"D0\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E0';\" formmethod=\"get\" id=\"E0\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F0';\" formmethod=\"get\" id=\"F0\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>1</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A1';\" formmethod=\"get\" id=\"A1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B1';\" formmethod=\"get\" id=\"B1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C1';\" formmethod=\"get\" id=\"C1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D1';\" formmethod=\"get\" id=\"D1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E1';\" formmethod=\"get\" id=\"E1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F1';\" formmethod=\"get\" id=\"F1\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>2</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A2';\" formmethod=\"get\" id=\"A2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B2';\" formmethod=\"get\" id=\"B2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C2';\" formmethod=\"get\" id=\"C2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D2';\" formmethod=\"get\" id=\"D2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E2';\" formmethod=\"get\" id=\"E2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F2';\" formmethod=\"get\" id=\"F2\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>3</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A3';\" formmethod=\"get\" id=\"A3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B3';\" formmethod=\"get\" id=\"B3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C3';\" formmethod=\"get\" id=\"C3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D3';\" formmethod=\"get\" id=\"D3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E3';\" formmethod=\"get\" id=\"E3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F3';\" formmethod=\"get\" id=\"F3\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>4</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A4';\" formmethod=\"get\" id=\"A4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B4';\" formmethod=\"get\" id=\"B4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C4';\" formmethod=\"get\" id=\"C4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D4';\" formmethod=\"get\" id=\"D4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E4';\" formmethod=\"get\" id=\"E4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F4';\" formmethod=\"get\" id=\"F4\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>5</td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=A5';\" formmethod=\"get\" id=\"A5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=B5';\" formmethod=\"get\" id=\"B5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=C5';\" formmethod=\"get\" id=\"C5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=D5';\" formmethod=\"get\" id=\"D5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=E5';\" formmethod=\"get\" id=\"E5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=F5';\" formmethod=\"get\" id=\"F5\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>";
    }

    private  String getStatsContent() {
        return "<div class=\"col-md-3 col-md-offset-1\" style=\"background-color: rgba(0,0,0,0.7); margin-top: 7.5%;\">\n" +
                "            <h3 style=\"color: white\">Current Standings</h3>\n" +
                "            <div class=\"container-fluid text-center\">\n" +
                "                <p style=\"color: white;\" class=\"pull-left\"><b>Hits:</b></p><p id=\"Hits\" style=\"color: white;\" class=\"text-right\">0</p>\n" +
                "                <p style=\"color: white;\" class=\"pull-left\"><b>Miss:</b></p><p id=\"Miss\" style=\"color: white;\" class=\"text-right\">0</p>\n" +
                "                <p style=\"color: white;\" class=\"pull-left\"><b>Total:</b></p><p id=\"Total\" style=\"color: white;\" class=\"text-right\">0</p>\n" +
                "                <p style=\"color: white;\" class=\"pull-left\"><b>Accuracy:</b></p><p id=\"Accuracy\" style=\"color: white;\" class=\"text-right\">0</p>\n" +
                "            </div>";
    }

    private String getGameFileFooter() {
        return " </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    private String getNotifications() {
        return "<div class=\"container-fluid text-center\" style=\"background-color: rgba(0,0,0,0.7)\">\n" +
                "    <div id=\"notifications\">\n" +
                "        <h2 style=\"color: white; text-shadow: 3px 1px 3px #09b6ff\">Notifications</h2>\n" +
                "        <p style=\"color: white;\" id=\"message\">" +
                            GLOBAL_Notification +
                "        </p>\n" +
                "    </div>\n" +
                "</div>";
    }

    private void setTableBoatInfoContent6() {
        setTableBoatInfoContent(                "<table class=\"table\" style=\"color: white;\">\n" +
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
                                        getNumberofA() +
                "                    <td id=\"AircraftDestroyed\">0</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>Battleship</td>\n" +
                                        getNumberofB() +
                "                    <td id=\"BattleshipDestroyed\">0</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>Destroyer</td>\n" +
                                        getNumberofC() +
                "                    <td id=\"DestroyerDestroyed\">0</td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>");
    }

    private void setTableBoatInfoContent8() {
        setTableBoatInfoContent( "<table class=\"table\" style=\"color: white;\">\n" +
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
                                        getNumberofA() +
                "                    <td id=\"AircraftDestroyed\">0</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>Battleship</td>\n" +
                                        getNumberofB() +
                "                    <td id=\"BattleshipDestroyed\">0</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>Destroyer</td>\n" +
                                        getNumberofC() +
                "                    <td id=\"DestroyerDestroyed\">0</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>Patrol Boat</td>\n" +
                                        getNumberofD() +
                "                    <td id=\"PatrolDestroyed\">0</td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>");
    }

    private void setTableBoatInfoContent10() {
        setTableBoatInfoContent(tableBoatInfoContent =
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
                        getNumberofA() +
                        "                    <td id=\"AircraftDestroyed\">0</td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td>Battleship</td>\n" +
                        getNumberofB() +
                        "                    <td id=\"BattleshipDestroyed\">0</td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td>Destroyer</td>\n" +
                        getNumberofC() +
                        "                    <td id=\"DestroyerDestroyed\">0</td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td>Patrol Boat</td>\n" +
                        getNumberofD() +
                        "                    <td id=\"PatrolDestroyed\">0</td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td>Submarine</td>\n" +
                        getNumberofE() +
                        "                    <td id=\"SubmarineDestroyed\">0</td>\n" +
                        "                </tr>\n" +
                        "                </tbody>\n" +
                        "            </table>");
    }
    
}
