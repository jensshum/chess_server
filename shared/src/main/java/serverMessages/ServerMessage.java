package serverMessages;

public class ServerMessage {

    public ServerMessage(ServerMessageType type) {
        this.type = type;
    }

    public ServerMessage() {}

    private ServerMessageType type;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public void setType(ServerMessageType type) {
        this.type = type;
    }

    public ServerMessageType getType() {
        return type;
    }
}

