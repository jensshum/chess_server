package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import com.google.gson.Gson;
import model.*;
import exception.ResponseException;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.fields.ExcludeFieldIndexSelector;
import server.Server;

public class ChessClient {
    private String visitorName = null;
    private ServerFacade facade;

    private String serverUrl;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
        facade = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
//            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
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

//    public String listPets() throws ResponseException {
//        assertSignedIn();
//        var pets = server.listPets();
//        var result = new StringBuilder();
//        var gson = new Gson();
//        for (var pet : pets) {
//            result.append(gson.toJson(pet)).append('\n');
//        }
//        return result.toString();
//    }

//    public String adoptPet(String... params) throws ResponseException {
//        assertSignedIn();
//        if (params.length == 1) {
//            try {
//                var id = Integer.parseInt(params[0]);
//                var pet = getPet(id);
//                if (pet != null) {
//                    server.deletePet(id);
//                    return String.format("%s says %s", pet.name(), pet.sound());
//                }
//            } catch (NumberFormatException ignored) {
//            }
//        }
//        throw new ResponseException(400, "Expected: <pet id>");
//    }

//    public String adoptAllPets() throws ResponseException {
//        assertSignedIn();
//        var buffer = new StringBuilder();
//        for (var pet : server.listPets()) {
//            buffer.append(String.format("%s says %s%n", pet.name(), pet.sound()));
//        }
//
//        server.deleteAllPets();
//        return buffer.toString();
//    }
//
    public String signOut() throws Exception {
        assertSignedIn();
        String thing = String.format("%s left the game. Bye!\n", visitorName);
        state = State.SIGNEDOUT;
        facade.logout();
        return thing;
    }
//
//    private Pet getPet(int id) throws ResponseException {
//        for (var pet : server.listPets()) {
//            if (pet.id() == id) {
//                return pet;
//            }
//        }
//        return null;
//    }

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