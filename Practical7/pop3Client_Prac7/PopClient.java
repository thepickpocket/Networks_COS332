package pop3Client_Prac7;

/**
 * Creators for this PopClient.
 * @author Vivian Laura-Lee Venter u13238435
 * @author Jason Richard Evans u13032608
 */

public class PopClient {
    String serverName;
    private Pop3Server myServer;

    public PopClient() {

    }

    public void connectToServer() {
        setMyServer(new Pop3Server(getServerName()));
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Pop3Server getMyServer() {
        return myServer;
    }

    public void setMyServer(Pop3Server myServer) {
        this.myServer = myServer;
    }
}
