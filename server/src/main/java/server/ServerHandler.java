package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.*;
import service.AuthService;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.*;

import exception.ResponseException;

public class ServerHandler {

    private final AuthService authService;
    private final GameService gameService;

    public ServerHandler() {
        authService = new AuthService();
        gameService = new GameService();
    }
    public Object deleteAllGames(Request req, Response res)  {
        authService.clearDatabase();
        res.status(200);
        return new Gson().toJson(new ErrorMessage(""));
    };
    public Object registerUser(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        if (Objects.equals(user.username(), "") || Objects.equals(user.password(), "") || Objects.equals(user.email(), "") || Objects.equals(user.username(), null) || Objects.equals(user.password(), null) || Objects.equals(user.email(), null)){
            res.status(400);
            return new Gson().toJson(new ErrorMessage("Error: bad request"));
        }
        if (authService.getUser(user) == null) {
            AuthData newUser = authService.register(user);
            return new Gson().toJson(newUser);
        }
        else {
            res.status(403);
            return new Gson().toJson(new ErrorMessage("Error: already taken"));
        }
    }

    public void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    }

    public Object loginUser(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), UserData.class);
        AuthData newAuth = authService.login(user);
        if (newAuth == null) {
            res.status(401);
            return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
        }
        else {
            return new Gson().toJson(newAuth);
        }
    }
    public Object logoutUser(Request req, Response res) {
        String headers = req.headers("authorization");
        AuthData auth = new AuthData(headers, "");
        if (authService.verifyToken(auth) != null) {
            res.status(200);
            return "";
        }
        else {
            res.status(401);
            return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
        }
//        return new Gson().toJson(headers);

    }
    public Object listGames(Request req, Response res) {
        String authToken = req.headers("authorization");
        AuthData auth = new AuthData(authToken, "");
        if (authService.verifyToken(auth) != null) {
            HashMap<Integer, GameData> games = gameService.getGames();
            return new Gson().toJson(games);
        }
        else {
            res.status(401);
            return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
        }
    }
    public Object createGame(Request req, Response res) {

        String authToken = req.headers("authorization");
        var game = new Gson().fromJson(req.body(), GameData.class);
        if (Objects.equals(game.gameName(), "") || Objects.equals(game.gameName(), null)){
            res.status(400);
            return new Gson().toJson(new ErrorMessage("Error: bad request"));
        }
        GameData gameDataObj = gameService.createGame(authToken, game);
        if (gameDataObj != null) {
            GameIdData gameIdObj = new GameIdData(gameDataObj.gameID());
            return new Gson().toJson(gameIdObj);
        }
        else {
            res.status(401);
            return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
        }
    }
    public Object joinGame(Request req, Response res) {
        String authToken = req.headers("authorization");
        AuthData auth = new AuthData(authToken, "");
        var joinGameData = new Gson().fromJson(req.body(), JoinGameData.class);
        if (Objects.equals(joinGameData.playerColor(), "") || Objects.equals(joinGameData.gameID(), 0) || Objects.equals(joinGameData.playerColor(), null)){
            res.status(400);
            return new Gson().toJson(new ErrorMessage("Error: bad request"));
        }
        if (authService.verifyToken(auth) != null) {
            gameService.joinGame(auth.username(), joinGameData);
        }
        else {
            res.status(401);
            return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
        }
        return null;
    }

    private static <T> T getBody(Request req, Class<T> clazz) {
        var body = new Gson().fromJson(req.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing argument body");
        }
        return body;
    }
}
