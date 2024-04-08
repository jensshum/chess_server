package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import serverMessages.ServerMessage;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;
    private String authToken;

    public Connection(String visitorName, Session session, String authToken) {
        this.visitorName = visitorName;
        this.session = session;
        this.authToken = authToken;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
    public void send(ServerMessage msg) throws IOException {
        send(new Gson().toJson(msg));
    }

}