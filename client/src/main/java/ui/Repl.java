package ui;
import static ui.EscapeSequences.*;

import server.Server;
import ui.ClientMain;
import webSocketMessages.Notification;
import websocket.NotificationHandler;

import java.util.Scanner;


public class Repl{
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);

    }

    public void run() {
        System.out.println("Welcome to Chess Online! Register or login to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equalsIgnoreCase("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }



    private void printPrompt() {
        System.out.print(">>> " + SET_TEXT_COLOR_GREEN);
    }


}
