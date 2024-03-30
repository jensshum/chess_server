package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import model.*;
import exception.ResponseException;
import websocket.NotificationHandler;
public class ChessClient {
    private String visitorName = null;
    private ServerFacade facade;

    private final NotificationHandler notificationHandler;


    private String serverUrl;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        this.serverUrl = serverUrl;
        facade = new ServerFacade(serverUrl);
        this.notificationHandler = notificationHandler;

    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            Scanner s = new Scanner(System.in);

            return switch (cmd) {
                case "help" -> help();
                case "login" -> signIn(s);
                case "register" -> register(s);
                case "create" -> createGame(s);
                case "join" -> joinGame(s);
                case "logout" -> signOut();
                case "list" -> listGames(s);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(Scanner s) throws ResponseException {
        System.out.print("(1/3) Enter your username:\n>>> ");
        String username = s.nextLine();
        System.out.print("(2/3) Enter your password:\n>>> ");
        String password = s.nextLine();
        System.out.print("(3/3) Enter your email:\n>>> ");
        String email = s.nextLine();
        try {
            AuthData response = facade.register(username, password, email);
            state = State.SIGNEDIN;
            visitorName = username;
            return String.format("Hi %s! Welcome to Chess Online.\n", username);
        }
        catch (ResponseException e) {
            System.out.println("Invalid register. " + e.getMessage());
            help();

        }
        return "";
    }

    public String signIn(Scanner s) throws Exception {
        System.out.print("Enter your username:\n>>> ");
        String username = s.nextLine();
        System.out.print("Enter your password:\n>>> ");
        String password = s.nextLine();
        if (Objects.equals(username, "")) {
            throw new ResponseException("Expected: username");
        }
        try {
            AuthData auth = facade.login(username, password);
            state = State.SIGNEDIN;
            visitorName = username;
            return String.format("Hi %s! Welcome to Chess Online.\n", username);
        }
        catch (ResponseException e) {
            System.out.println("Invalid login" + e.getMessage());
            help();
        }
//        System.out.println();
        return "";
    }

    public String createGame(Scanner s) throws Exception {
        assertSignedIn();
        System.out.print("Name the game: \n>>> ");
        String gameName = s.nextLine();
        if (Objects.equals(gameName, "")) {
            throw new ResponseException("Error. No game name.");
        }
        String game = facade.createGame(gameName).toString();
        return String.format("Game: \"%s\" created! (Next enter \"join\" to join a game)\n", gameName);

    }

    public String listGames(Scanner s) throws Exception {
        assertSignedIn();
        GamesListFromHashMap games = facade.listGames();
        for (GameData g : games.getGames()) {
            System.out.println(g.getGameID() + ": " + g.getGameName());
        }
        return "";
    }

    public String joinGame(Scanner s) throws Exception {
        assertSignedIn();
        ClientUI board = new ClientUI();
        System.out.print("Join as a (p)layer or (o)bserver?\n>>> ");
        String playerType = s.nextLine();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if (Objects.equals(playerType, "o") || Objects.equals(playerType, "observer")) {
            System.out.print("Enter game number:\n>>> ");
            int gameId = s.nextInt();
            System.out.print(String.format("Observing game %d.", gameId));
            facade.gameJoin("", gameId);
        }
        else {
            System.out.print("Enter game number:\n>>> ");
            int gameId = s.nextInt();
            System.out.print("Join as (w)hite or (b)lack?\n>>> ");
            String color = s.nextLine();
            if (Objects.equals(color, "w") || Objects.equals(color, "white")) {
                System.out.println();
                ClientUI.drawBoard(out, false);
                System.out.println();
                System.out.println();
                ClientUI.drawBoard(out, true);
                System.out.println();
                facade.gameJoin("white", gameId);

            }
            else {

                facade.gameJoin("black", gameId);
                System.out.println();
                ClientUI.drawBoard(out, true);
                System.out.println();
                System.out.println();
                ClientUI.drawBoard(out, false);
                System.out.println();
            }
        }
        return "";
    }

    public String signOut() throws Exception {
        assertSignedIn();
        String thing = String.format("%s left the game. Bye!\n", visitorName);
        state = State.SIGNEDOUT;
        facade.logout();
        return thing;
    }


    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    1. Help
                    2. Login
                    3. Register
                    4. Quit
                    """;
        }
        return """
                1. Help
                2. Logout
                3. Create
                4. List
                5. Join
                6. Quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException("You must sign in");
        }
    }
}