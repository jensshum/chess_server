package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.UserData;
import service.AuthService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class ServerHandler {

    private final AuthService authService;

    public ServerHandler() {
        authService = new AuthService();
    }
    public Object deleteAllGames(Request req, Response res)  {
        var bodyObj = getBody(req, Map.class);
        res.type("application/json");
        return new Gson().toJson(bodyObj);
    };
    public Object registerUser(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        if (authService.getUser(user) == null) {
            user = authService.register(user);
            return new Gson().toJson(user);
        }
        return new DataAccessException("Error: already taken");
    }

//    public void exceptionHandler(DataAccessException ex, Request req, Response res) {
//        res.status(ex);
//    }

    public Object loginUser(Request req, Response res) {
        var bodyObj = getBody(req, Map.class);
        res.type("application/json");
        return new Gson().toJson(bodyObj);
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
