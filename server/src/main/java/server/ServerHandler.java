package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.AuthService;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.Objects;

import exception.ResponseException;

public class ServerHandler {

    private final AuthService authService;

    public ServerHandler() {
        authService = new AuthService();
    }
    public Object deleteAllGames(Request req, Response res)  {
        authService.clearDatabase();
        res.status(200);
        return "";
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
        var bodyObj = getBody(req, Map.class);
        res.type("application/json");
        return new Gson().toJson(bodyObj);
    }
    public Object listGames(Request req, Response res) {
        var bodyObj = getBody(req, Map.class);
        res.type("application/json");
        return new Gson().toJson(bodyObj);
    }
    public Object createGame(Request req, Response res) {
        var bodyObj = getBody(req, Map.class);
        res.type("application/json");
        return new Gson().toJson(bodyObj);
    }
    public Object joinGame(Request req, Response res) {
        var bodyObj = getBody(req, Map.class);
        res.type("application/json");
        return new Gson().toJson(bodyObj);
    }

    private static <T> T getBody(Request req, Class<T> clazz) {
        var body = new Gson().fromJson(req.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing argument body");
        }
        return body;
    }
}
