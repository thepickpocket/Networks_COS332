import java.util.HashMap;

/**
 * Created by VivianWork on 5/16/2015.
 */
public class GridContent {

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
        String content = getGameFileHeader() + getNotifications() + getGridContent(grid, size) + getStatsContent() + getTableBoatInfoContent() + getGameFileFooter();

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
                "        <h2 style=\"color: white; text-shadow: 3px 1px 3px #09b6ff\">Notifications</h2>\n" +
                "        <p style=\"color: white;\" id=\"message\">" +
                            game.GLOBAL_Notification +
                "        </p>\n" +
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
            content += "                    <td>"+i+"</td>\n";
            for (int k = 0; k < size; k++) {
                colC = map_Indexes.get(k);
                if (theGrid[i][k] == '1') {
                    content += "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot="+colC + i+"\"';\" formmethod=\"get\" id=\""+colC + i+"\" disabled><i class=\"fa fa-ban fa-3x\"></i> </button></td>\n";
                }
                else if (theGrid[i][k] == '2') {
                    content +=  "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot="+colC + i+"';\" formmethod=\"get\" id=\""+colC + i+"\" disabled><i class=\"fa fa-fire fa-3x\" style=\"color: red\"></i></button></td>\n";
                }
                else {
                    content +=  "                    <td><button class=\"btn\" onclick=\"location.href = 'shoot="+colC + i+"';\" formmethod=\"get\" id=\""+colC + i+"\"><i class=\"fa fa-map-marker fa-3x\" style=\"color: #269abc;\"></i></button></td>\n";
                }
            }
            content +=  "               </tr>\n";
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
        return "<div class=\"col-md-3 col-md-offset-1\" style=\"background-color: rgba(0,0,0,0.7); margin-top: 7.5%;\">\n" +
                "            <h3 style=\"color: white\">Current Standings</h3>\n" +
                "            <div class=\"container-fluid text-center\">\n" +
                "                <p style=\"color: white;\" class=\"pull-left\"><b>Hits:</b></p><p id=\"Hits\" style=\"color: white;\" class=\"text-right\">"+game.getHits()+"</p>\n" +
                "                <p style=\"color: white;\" class=\"pull-left\"><b>Miss:</b></p><p id=\"Miss\" style=\"color: white;\" class=\"text-right\">"+game.getMisses()+"</p>\n" +
                "                <p style=\"color: white;\" class=\"pull-left\"><b>Total:</b></p><p id=\"Total\" style=\"color: white;\" class=\"text-right\">"+game.getTotalShots()+"</p>\n" +
                "                <p style=\"color: white;\" class=\"pull-left\"><b>Accuracy:</b></p><p id=\"Accuracy\" style=\"color: white;\" class=\"text-right\">"+game.getAccuracy()+"</p>\n" +
                "            </div>";
    }

    private String getGameFileFooter() {
        return " </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }


    public String getTableBoatInfoContent() {
        return tableBoatInfoContent;
    }

    public void setTableBoatInfoContent(String tableBoatInfoContent) {
        this.tableBoatInfoContent = tableBoatInfoContent;
    }

    public void setTableBoatInfoContent6() {
        setTableBoatInfoContent(
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
                "                </tbody>\n" +
                "            </table>");
    }

    public void setTableBoatInfoContent8() {
        setTableBoatInfoContent(
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
                        "            </table>");

    }

    public void setTableBoatInfoContent10() {
        setTableBoatInfoContent(
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
                        "            </table>");
    }
}
