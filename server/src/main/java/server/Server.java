package server;


import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import exception.ResponseException;
import dataAccess.DatabaseManager;
import spark.*;

public class Server {

    private ServerHandler handler;
    public Server() {
        handler = new ServerHandler();

    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.delete("/db", (req, res) -> handler.deleteAllGames(req, res));
        Spark.post("/user", (req, res) -> handler.registerUser(req, res));
        Spark.post("/session", (req, res) -> handler.loginUser(req, res));
        Spark.delete("/session", (req, res) -> handler.logoutUser(req, res));
        Spark.get("/game", (req, res) -> handler.listGames(req, res));
        Spark.post("/game", (req, res) -> handler.createGame(req, res));
        Spark.put("/game", (req, res) -> handler.joinGame(req, res));
        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
