package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collection;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import serverMessages.LoadGame;
import webSocketMessages.Notification;
import websocket.WebSocketFacade;
import model.*;
import exception.ResponseException;
import websocket.NotificationHandler;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class ChessClient implements NotificationHandler{
    private String visitorName = null;
    private ServerFacade facade;
    int inGameID;

    ChessGame currentGame;
    ChessGame.TeamColor currColor;
    private AuthData signedIn;
    private boolean inGame = false;
//    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
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
            Scanner s = new Scanner(System.in);

            return switch (cmd) {
                case "help" -> help();
                case "login" -> signIn(s);
                case "register" -> register(s);
                case "create" -> createGame(s);
                case "join" -> joinGame(s);
                case "redraw" -> redrawBoard();
                case "leave" -> leaveGame();
                case "move" -> makeMove(s);
                case "resign" -> resign();
                case "highlight" -> highlightValidMoves(s, null);
                case "logout" -> signOut();
                case "list" -> listGames(s);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String redrawBoard() {
        if (currColor == WHITE) {
            System.out.println();
            ClientUI.drawBoard(System.out, false, currentGame, null);
            System.out.println();
        }
        else {
            System.out.println();
            ClientUI.drawBoard(System.out, true, currentGame, null);
            System.out.println();
        }
        return "";
    }
    public String leaveGame() {
        return "";
    }

    private boolean isValidInput(String input) {
        String regex = "^[1-8],[a-h]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public ChessPosition getPosition(String location) {
        String[] rowcol = location.split(",");
        String row = rowcol[0];
        int rowInt = Integer.parseInt(row);
        String col = rowcol[1];
        int colInt = 0;
        if (currColor == WHITE) {
            switch (col) {
                case "a" -> colInt = 1;
                case "b" -> colInt = 2;
                case "c" -> colInt = 3;
                case "d" -> colInt = 4;
                case "e" -> colInt = 5;
                case "f" -> colInt = 6;
                case "g" -> colInt = 7;
                case "h" -> colInt = 8;
            }
        }
        else{
            switch (col) {
                case "a" -> colInt = 8;
                case "b" -> colInt = 7;
                case "c" -> colInt = 6;
                case "d" -> colInt = 5;
                case "e" -> colInt = 4;
                case "f" -> colInt = 3;
                case "g" -> colInt = 2;
                case "h" -> colInt = 1;
            }
        }
        ChessPosition position = new ChessPosition(rowInt, colInt);
        return position;
    }

    public String makeMove(Scanner s) throws Exception{
        assertSignedIn();
        String confirm = "n";
        boolean match = false;
        ChessGame.TeamColor pieceColor = null;
        while (confirm.equalsIgnoreCase("n") || !match)
        {
            System.out.print("Which piece would you like to move?\n(<number 1-8>,<letter a-h>)>>>");
            String pieceToMove = s.nextLine();
            while (!isValidInput(pieceToMove)) {
                System.out.println("Invalid input. Please match the form <number 1-8>,<letter a-h>\n>>>");
                pieceToMove = s.nextLine();
            }
//            if (currColor == WHITE) {
//                pieceColor = currentGame.getBoard().getPiece(getPosition(pieceToMove)).getTeamColor();
//            }
//            else {
//                pieceColor = currentGame.getBoard().getPiece(getPosition(pieceToMove)).getTeamColor();
//            }
//            if (pieceColor == currColor) {
                highlightValidMoves(s, pieceToMove);
                System.out.print(SET_TEXT_COLOR_GREEN);
                System.out.print("Where would you like to move?\n[row,col]>>>");
                String move = s.nextLine();
                while (!isValidInput(move)) {
                    System.out.println("Invalid input. Please match the form <number 1-8>,<letter a-h>\n>>>");
                    move = s.nextLine();
                }
                System.out.print("Move " + currentGame.getBoard().getPiece(getPosition(pieceToMove)).getPieceType().toString() +  " on " + pieceToMove + " to " + move + "?\n(y or n)>>>");
                confirm = s.nextLine();
                if (confirm.equalsIgnoreCase("y")) {
                    ChessPosition startPosition = getPosition(pieceToMove);
                    ChessPosition endPosition = null;
                    if (currColor == WHITE) {
                         endPosition = getPosition(move);
                         endPosition = new ChessPosition(endPosition.getRow(), endPosition.getColumn());
                    }
                    else{
                        endPosition = getPosition(move);
                    }
                    Collection<ChessMove> validMoves = currentGame.validMoves(getPosition(pieceToMove));
                    ChessMove newMove = new ChessMove(getPosition(pieceToMove), getPosition(move), null);
                    for (ChessMove validMove : validMoves) {
                        if (validMove.equals(newMove)) {
                            match = true;
                        }
                    }
                    if (match) {
                        ChessMove toMove = new ChessMove(startPosition, endPosition, null);
                        ws.makeMove(inGameID, signedIn.authToken(), signedIn.username(), toMove);
                        redrawBoard();
                    }
                    else {
                        System.out.println("Not a valid move.");
                    }
                }
//            }
//            else{
//                System.out.print("That's not your piece! Your color is " + currColor.toString() + ".\n");
//            }
        }
        return "";
    }

    public String resign() {
        return "";
    }

    public String highlightValidMoves(Scanner s, String pieceToValidate) {
        if (pieceToValidate == null) {
            System.out.print("For which piece?\n(<1-8>,<a-h>)>>>");
            pieceToValidate = s.nextLine();
            while (!isValidInput(pieceToValidate)) {
                System.out.println("Invalid input. Please match the form <number 1-8>,<letter a-h>\n>>>");
                pieceToValidate = s.nextLine();
            }
        }
        Collection<ChessMove> validMoves = currentGame.validMoves(getPosition(pieceToValidate));
        if (currColor == WHITE) {
            System.out.println();
            ClientUI.drawBoard(System.out, false, currentGame, validMoves);
            System.out.println();
        } else {
            System.out.println();
            ClientUI.drawBoard(System.out, true, currentGame, validMoves);
            System.out.println();
        }

        return "";
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
            signedIn = response;
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
            signedIn = auth;
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
        if (games.getGames().size() == 0) {
            System.out.println("No games found.");
        }
        return "";
    }

    public String joinGame(Scanner s) throws Exception {
        assertSignedIn();
        ClientUI board = new ClientUI();
        System.out.print("Join as a (p)layer or (o)bserver?\n>>> ");
        String playerType = s.nextLine();
        while (!playerType.equalsIgnoreCase("p") && !playerType.equalsIgnoreCase("o") && !playerType.equalsIgnoreCase("player") && !playerType.equalsIgnoreCase("observer")) {
            System.out.print("Please input a valid player type.\n>>>");
            playerType = s.nextLine();
        }
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if (Objects.equals(playerType, "o") || Objects.equals(playerType, "observer")) {
            System.out.print("Enter game number:\n>>> ");
            inGameID = s.nextInt();
            System.out.print(String.format("Observing game %d.", inGameID));
            facade.gameJoin("", inGameID);
            ws = new WebSocketFacade(serverUrl, this);
            ws.joinObserver(inGameID, signedIn.authToken(), signedIn.username());

        } else {
            listGames(s);
            System.out.print("Enter game number:\n>>> ");
            inGameID = s.nextInt();
            System.out.print("Join as (w)hite or (b)lack?\n>>>");
            String color = s.nextLine();
            while (!color.equalsIgnoreCase("w") && !color.equalsIgnoreCase("b") && !color.equalsIgnoreCase("white") && !color.equalsIgnoreCase("black")) {
                System.out.print("\"" + color + "\"" + " is not a valid color. (use \"b\" or \"w\")\n>>>");
                color = s.nextLine();
            }
            try {
                if (color.equalsIgnoreCase("w") || color.equalsIgnoreCase("white")) {

                    facade.gameJoin("white", inGameID);
                    inGame = true;
                    currColor = WHITE;
                    ws = new WebSocketFacade(serverUrl, this);
                    ws.joinPlayer(inGameID, signedIn.authToken(), signedIn.username(), WHITE);

                    help();
                } else if (color.equalsIgnoreCase("b") || color.equalsIgnoreCase("black")) {
                    facade.gameJoin("black", inGameID);
                    currColor = ChessGame.TeamColor.BLACK;
                    inGame = true;
                    ws = new WebSocketFacade(serverUrl, this);
                    ws.joinPlayer(inGameID, signedIn.authToken(), signedIn.username(), ChessGame.TeamColor.BLACK);
                    help();
                }
            }
            catch (ResponseException e) {
                System.out.println("Already taken.");
                help();
            }
        }
        return "";
    }

    public String signOut() throws Exception {
        assertSignedIn();
        String thing = String.format("%s left the game. Bye!\n", visitorName);
        state = State.SIGNEDOUT;
        facade.logout();
        ws.leaveChess(visitorName);
        ws = null;
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
        else if (state == State.SIGNEDIN && !inGame) {
            return """
                    1. Help
                    2. Logout
                    3. Create
                    4. List
                    5. Join
                    6. Quit
                    """;
        }
        else {
            return """
            1. Help
            2. [Redraw] board
            3. Leave
            4. [Move] piece
            5. Resign
            6. [Highlight] valid moves
            """;
        }
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException("You must sign in");
        }
    }

    @Override
    public void notify(Notification notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification.message());
    }

    @Override
    public void updateGame(LoadGame game) {
        currentGame = game.getGame();
        if (currColor == WHITE) {
            System.out.println();
            ClientUI.drawBoard(System.out, false, currentGame, null);
            System.out.println();
        }
        else {
            System.out.println();
            ClientUI.drawBoard(System.out, true, currentGame, null);
            System.out.println();
        }

    }


}