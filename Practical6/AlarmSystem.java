import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Creators for this server.
 * @author Jason Richard Evans u13032608
 * @author Vivian Laura-Lee Venter u13238435
 */

@Deprecated
public class AlarmSystem {
    static final String CLEAR_SCREEN = (char)27 + "[2J";
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLACK = "\u001B[30m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final String ANSI_PURPLE = "\u001B[35m";
    static final String ANSI_CYAN = "\u001B[36m";
    static final String ANSI_WHITE = "\u001B[37m";
    static final String ALARM_MESSAGE = " has been activated at ";

    public static void main(String[] args){

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            PrintStart();
            String input = br.readLine();
            while(!(input.equals("QUIT") || input.equals("quit"))){
                char alarm = input.charAt(0);
                alarm = Character.toUpperCase(alarm);
                String alarmMess;
                Date theDate = new Date();
                EmailServer myServer = new EmailServer();

                switch (alarm){
                    case 'W':   alarmMess = "Window alarm " + ALARM_MESSAGE + theDate + ".\r\n";
                                myServer.sendEmail(alarmMess);
                                System.out.println(ANSI_GREEN + alarmMess);
                                break;
                    case 'F':   alarmMess = "Front door alarm " + ALARM_MESSAGE + theDate + ".\r\n";
                                myServer.sendEmail(alarmMess);
                                System.out.println(ANSI_GREEN + alarmMess);
                                break;
                    case 'B':   alarmMess = "Back door alarm " + ALARM_MESSAGE + theDate + ".\r\n";
                                myServer.sendEmail(alarmMess);
                                System.out.println(ANSI_GREEN + alarmMess);
                                break;
                    case 'O':   alarmMess = "Outside perimeter alarm " + ALARM_MESSAGE + theDate + ".\r\n";
                                myServer.sendEmail(alarmMess);
                                System.out.println(ANSI_GREEN + alarmMess);
                                break;
                    case 'I':   alarmMess = "Inside perimeter alarm " + ALARM_MESSAGE + theDate + ".\r\n";
                                myServer.sendEmail(alarmMess);
                                System.out.println(ANSI_GREEN + alarmMess);
                                break;
                    case 'V':   alarmMess = "Vault alarm " + ALARM_MESSAGE + theDate + ".\r\n";
                                myServer.sendEmail(alarmMess);
                                System.out.println(ANSI_GREEN + alarmMess);
                                break;
                }
                PrintStart();
                input = br.readLine();
            }
        }
        catch(IOException error){
            error.printStackTrace();
        }
    }

    private static void PrintStart(){
        //System.out.println(CLEAR_SCREEN);
        System.out.println(ANSI_WHITE + "Practic Alarm System Active\r\n");
        System.out.println("Alarms Active: \r\n");
        System.out.println(ANSI_GREEN + "--W: "+ ANSI_WHITE +"Windows");
        System.out.println(ANSI_GREEN + "--F: "+ ANSI_WHITE +"Front Door");
        System.out.println(ANSI_GREEN + "--B: "+ ANSI_WHITE +"Back Door");
        System.out.println(ANSI_GREEN + "--O: "+ ANSI_WHITE +"Outside Perimeter");
        System.out.println(ANSI_GREEN + "--I: "+ ANSI_WHITE +"Inside Perimeter");
        System.out.println(ANSI_GREEN + "--V: "+ ANSI_WHITE +"Vault");
    }
}
