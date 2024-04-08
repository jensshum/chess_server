package serverMessages;

public class Error extends ServerMessage{
    private String errorMessage;

    public Error(String errorMessage) {
        this.errorMessage = errorMessage;
        setType(ServerMessageType.ERROR);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
