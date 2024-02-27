package exception;

public class ResponseException extends Exception {
    final private int statusCode;
    final private String codeMessage;

    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.codeMessage = message;
    }

    public int StatusCode() {
        return statusCode;
    }

    public String CodeMessage() {
        return codeMessage;
    }


}