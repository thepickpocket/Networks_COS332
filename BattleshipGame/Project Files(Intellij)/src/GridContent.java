import java.util.HashMap;

/**
 * Vivian Venter (13238435) & Jason Evans (13032608)
 * COS 332 - Practical 9
 * Battleship Game
 * Collaboration
 */
public class GridContent {

    private String[] ranking = new String[] {"Leutenant", "Commander", "Captain", "Admiral"};
    private HashMap<Integer, Character> map_Indexes;
    private String tableBoatInfoContent = "";

    private static BattleShip game = null;

    public GridContent(BattleShip battleShip) {
        System.out.println("GridContent Initialized");
        game = battleShip;

        map_Indexes = new HashMap();
        map_Indexes.put(0,'A');
        map_Indexes.put(1,'B');
        map_Indexes.put(2,'C');
        map_Indexes.put(3,'D');
        map_Indexes.put(4,'E');
        map_Indexes.put(5,'F');
        map_Indexes.put(6,'G');
        map_Indexes.put(7,'H');
        map_Indexes.put(8,'I');
        map_Indexes.put(9,'J');
    }

    public String constructGridContent(char[][] grid, int size) {
        String content;
        if (game.hasWon)
            content = getGameFileHeader() + getWinner() + getGameFileFooter();
        else
            content = getGameFileHeader() + getGridContent(grid, size) + getStatsContent() + getTableBoatInfoContent() + getGameFileFooter();

        game.hasWon = false;
        return  content;
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
                "    <script src=\"ShipInformation/animations.js\"></script>\n" +
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

    private String getNotifications() {
        return "<div class=\"container-fluid text-center\" style=\"background-color: rgba(0,0,0,0.7)\">\n" +
                "    <div id=\"notifications\">\n" +
                "        <h2 style=\"color: white; text-shadow: 3px 1px 3px #09b6ff\"> Hi, "+game.getPlayerName()+"!</h2>\n" +
                        game.GLOBAL_Notification+
                "    </div>\n" +
                "</div>\n";
    }

    private String getGridContent(char[][] theGrid, int size) {
        String content =
                "<div class=\"container-fluid\">\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-md-8\" style=\"background-color: rgba(0,0,0,0.7); margin-top: 2.5%;\">\n" +
                "            <table class=\"table\" style=\"color: white;\">\n" +
                "                <thead>\n";
        content += getColHeadings(size);
        content +=
                "                </thead>\n" +
                "                <tbody>";
        char colC;

        for (int i = 0; i < size; i++) {
            content += "                <tr>\n";
            content += "                    <td>" + i + "</td>\n";
            if (size == 6) {
                for (int k = 0; k < size; k++) {
                    colC = map_Indexes.get(k);
                    if (theGrid[i][k] == '1') {
                        content += "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=" + colC + i + "\"';\" formmethod=\"get\" id=\"" + colC + i + "\" disabled><i class=\"fa fa-ban fa-3x\"></i> </button></td>\n";
                    } else if (theGrid[i][k] == '2') {
                        content += "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=" + colC + i + "';\" formmethod=\"get\" id=\"" + colC + i + "\" disabled><i class=\"fa fa-fire fa-3x\" style=\"color: red\"></i></button></td>\n";
                    } else {
                        content += "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=" + colC + i + "';\" formmethod=\"get\" id=\"" + colC + i + "\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n";
                    }
                }
                content += "               </tr>\n";
            }
            else if (size == 8){
                for (int k = 0; k < size; k++) {
                    colC = map_Indexes.get(k);
                    if (theGrid[i][k] == '1') {
                        content += "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=" + colC + i + "\"';\" formmethod=\"get\" id=\"" + colC + i + "\" disabled><i class=\"fa fa-ban fa-2x\"></i> </button></td>\n";
                    } else if (theGrid[i][k] == '2') {
                        content += "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=" + colC + i + "';\" formmethod=\"get\" id=\"" + colC + i + "\" disabled><i class=\"fa fa-fire fa-2x\" style=\"color: red\"></i></button></td>\n";
                    } else {
                        content += "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=" + colC + i + "';\" formmethod=\"get\" id=\"" + colC + i + "\"><i class=\"fa fa-map-marker fa-2x\" style=\"color: #269abc;\"></i></button></td>\n";
                    }
                }
                content += "               </tr>\n";
            }
            else{
                for (int k = 0; k < size; k++) {
                    colC = map_Indexes.get(k);
                    if (theGrid[i][k] == '1') {
                        content += "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=" + colC + i + "\"';\" formmethod=\"get\" id=\"" + colC + i + "\" disabled><i class=\"fa fa-ban\"></i> </button></td>\n";
                    } else if (theGrid[i][k] == '2') {
                        content += "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=" + colC + i + "';\" formmethod=\"get\" id=\"" + colC + i + "\" disabled><i class=\"fa fa-fire\" style=\"color: red\"></i></button></td>\n";
                    } else {
                        content += "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot=" + colC + i + "';\" formmethod=\"get\" id=\"" + colC + i + "\"><i class=\"fa fa-map-marker\" style=\"color: #269abc;\"></i></button></td>\n";
                    }
                }
                content += "               </tr>\n";
            }
        }

        content +=
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>";

        return content;
    }

    private String getColHeadings(int size) {
        String content =
                "                <tr>\n" +
                "                    <th> </th>\n";
        for (int p = 0; p < size; p++) {
            content += "                    <th>"+map_Indexes.get(p)+"</th>\n";
        }

        content += "                </tr>\n";
        return content;

    }

    private  String getStatsContent() {
        return "<div class=\"col-md-3 col-md-offset-1\" style=\"margin-top: 3%\">\n" +
                getNotifications() +
                "<div class=\"container-fluid\" style=\"background-color: rgba(0,0,0,0.7); margin-top: 7.5%;\">"+
                "            <h3 style=\"color: white\">Current Standings</h3>\n" +
                "            <div class=\"container-fluid text-center\">\n" +
                "                <p style=\"color: white;\" class=\"pull-left\"><b>Hits:</b></p><p id=\"Hits\" style=\"color: white;\" class=\"text-right\">"+game.getHits()+"</p>\n" +
                "                <p style=\"color: white;\" class=\"pull-left\"><b>Miss:</b></p><p id=\"Miss\" style=\"color: white;\" class=\"text-right\">"+game.getMisses()+"</p>\n" +
                "                <p style=\"color: white;\" class=\"pull-left\"><b>Total:</b></p><p id=\"Total\" style=\"color: white;\" class=\"text-right\">"+game.getTotalShots()+"</p>\n" +
                "                <p style=\"color: white;\" class=\"pull-left\"><b>Accuracy:</b></p><p id=\"Accuracy\" style=\"color: white;\" class=\"text-right\">"+game.getAccuracy()+"</p>\n" +
                "            </div>";
    }

    private String getGameFileFooter() {
        return "</body>\n" +
                "</html>";
    }


    public String getTableBoatInfoContent() {
        return tableBoatInfoContent;
    }

    public void setTableBoatInfoContent(String tableBoatInfoContent) {
        this.tableBoatInfoContent = tableBoatInfoContent;
    }

    public void setTableBoatInfoContent6() {
        String lineA = "                <tr>\n";
        String lineB = "                <tr>\n";
        String lineC = "                <tr>\n";

        setTableBoatInfoContent(
                "<div class=\"text-center\">"+
                "<table class=\"table\" style=\"color: white;\">\n" +
                "                <thead>\n" +
                "                <tr>\n" +
                "                    <th>Boat Name</th>\n" +
                "                    <th>Length</th>\n" +
                "                    <th>Destroyed</th>\n" +
                "                </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n" +
                                 lineA +
                "                    <td>Aircraft Carrier</td>\n" +
                "                    <td>5</td>\n" +
                "                    <td id=\"AircraftDestroyed\">" +
                                        game.getNumberofA() +
                "                    </td>\n" +
                "                </tr>" +
                                 lineB +
                "                    <td>Battleship</td>\n" +
                "                    <td>2</td>\n" +
                "                    <td id=\"BattleshipDestroyed\">" +
                                        game.getNumberofB() +
                "                    </td>\n" +
                "                </tr>" +
                                 lineC +
                "                    <td>Destroyer</td>\n" +
                "                    <td>6</td>\n" +
                "                    <td id=\"DestroyerDestroyed\">" +
                                        game.getNumberofC() +
                "                    </td>\n" +
                "                </tr>" +
                "                </tbody>\n" +
                "            </table></div></div></div>");
        game.counter++;
    }

    public void setTableBoatInfoContent8() {
        setTableBoatInfoContent(
                "<div class=\"text-center\">" +
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
                        "                    <td id=\"AircraftDestroyed\">" +
                                                game.getNumberofA() +
                        "                    </td>\n" +
                        "                </tr>" +
                        "                <tr>\n" +
                        "                    <td>Battleship</td>\n" +
                        "                    <td>2</td>\n" +
                        "                    <td id=\"BattleshipDestroyed\">" +
                                                game.getNumberofB() +
                        "                    </td>\n" +
                        "                </tr>" +
                        "                <tr>\n" +
                        "                    <td>Destroyer</td>\n" +
                        "                    <td>6</td>\n" +
                        "                    <td id=\"DestroyerDestroyed\">" +
                                                game.getNumberofC() +
                        "                    </td>\n" +
                        "                </tr>" +
                        "                <tr>\n" +
                        "                    <td>Patrol Boat</td>\n" +
                        "                    <td>7</td>\n" +
                        "                    <td id=\"PatrolDestroyed\">" +
                                                game.getNumberofD() +
                        "                    </td>\n" +
                        "                </tr>"+
                        "                </tbody>\n" +
                        "            </table></div></div></div>");

    }

    public void setTableBoatInfoContent10() {
        setTableBoatInfoContent(
                        "<div class=\"text-center\">" +
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
                        "                    <td id=\"AircraftDestroyed\">" +
                                                game.getNumberofA() +
                        "                    </td>\n" +
                        "                </tr>" +
                        "                <tr>\n" +
                        "                    <td>Battleship</td>\n" +
                        "                    <td>2</td>\n" +
                        "                    <td id=\"BattleshipDestroyed\">" +
                                                game.getNumberofB() +
                        "                    </td>\n" +
                        "                </tr>" +
                        "                <tr>\n" +
                        "                    <td>Destroyer</td>\n" +
                        "                    <td>6</td>\n" +
                        "                    <td id=\"DestroyerDestroyed\">" +
                                                game.getNumberofC() +
                        "                    </td>\n" +
                        "                </tr>" +
                        "                <tr>\n" +
                        "                    <td>Patrol Boat</td>\n" +
                        "                    <td>7</td>\n" +
                        "                    <td id=\"PatrolDestroyed\">" +
                                                game.getNumberofD() +
                        "                    </td>\n" +
                        "                </tr>"+
                        "                <tr>\n" +
                        "                    <td>Submarine</td>\n" +
                        "                    <td>9</td>\n" +
                        "                    <td id=\"SubmarineDestroyed\">" +
                                                game.getNumberofE() +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                </tbody>\n" +
                        "            </table></div></div></div>");
    }

    private String getWinner(){
        String currentRanking = "";
        if (game.getAccuracy() >= 80)
            currentRanking = ranking[3];
        else if (game.getAccuracy() >= 60)
            currentRanking = ranking[2];
        else if (game.getAccuracy() >= 40)
            currentRanking = ranking[1];
        else
            currentRanking = ranking[0];
        return "<div class=\"container-fluid\" id=\"Text\" hidden>" +
                "   <div class=\"row\">" +
                "       <div class=\"col-md-4\"></div>" +
                "       <div class=\"col-md-4 text-center\" style=\"background-color: rgba(0,0,0, 0.7); margin-top: 12%;\">" +
                "           <h1 style=\"color: white; text-shadow: 3px 1px 3px #09b6ff;\">Congratulations <br />"+ currentRanking + " " + game.getPlayerName() + "</h1>" +
                "           <p style=\"color: white;\">You have overthrown the enemy and the sea is now yours. Go forth and attack more ships and make even more of the ocean yours. Keep on going like this and you will be noted in history as the best battleship " + currentRanking +".</p>" +
                "           <i class=\"fa fa-ship fa-5x\" style=\"color:white;\"></i><br /><br />" +
                "           <a href=\"index.html\"><button class='btn btn-default'>New Game</button></a>" +
                "       </div>" +
                "       <div class=\"col-md-4\"></div>" +
                "</div>";
    }
}
