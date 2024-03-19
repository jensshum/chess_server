package ui;
import static ui.EscapeSequences.*;
import ui.ClientMain;
import java.util.Scanner;


public class Repl {
    private final ChessClient client;

    public Repl(int serverNum) {
        client = new ChessClient(serverNum);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to CHESS. Register or Login to start.");
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
