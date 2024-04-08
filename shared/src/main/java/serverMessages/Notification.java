package serverMessages;

public class Notification extends ServerMessage{
    private String message;

    public Notification(String notification) {
        message = notification;
        setType(ServerMessageType.NOTIFICATION);
    }

    public String getMessage() {
        return message;
    }
}
