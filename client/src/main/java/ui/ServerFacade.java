
package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import java.io.*;
import java.net.*;
import java.util.Objects;

public class ServerFacade {

    private final String serverUrl;

    private AuthData loggedInUser;

    public ServerFacade(String url) {

        serverUrl = url;
    }

    public AuthData register(String username, String password, String email) throws ResponseException {
        UserData newUser = new UserData(username, password, email);
        var path = "/user";
        AuthData auth = makeRequest("POST", path, newUser, AuthData.class);
        loggedInUser = auth;
        return auth;

    }

    public AuthData login(String userName, String password) throws Exception {
        UserData existingUser = new UserData(userName, password, "");
        var path = "/session";
        AuthData auth = makeRequest("POST", path, existingUser, AuthData.class);
        loggedInUser = auth;
        return auth;
    }

    public void logout() throws Exception {
        var path = "/session";
        this.makeRequest("DELETE", path, loggedInUser, null);
    }

    public GameData createGame(String gameName) throws Exception {
        var path = "/game";
        GameData game = new GameData(0, "", "", gameName, null);
        return this.makeRequest("POST", path, game, GameData.class);
    }

    public GameData gameJoin(String color, int gameId) throws Exception {
        var path = "/game";
        JoinGameData newJoiner;
        if (Objects.equals(color, "")) {
            newJoiner = new JoinGameData(null, gameId);
        }
        else {
            if (Objects.equals(color, "black")) {
                newJoiner = new JoinGameData(ChessGame.TeamColor.BLACK, gameId);
            }
            else {
                newJoiner = new JoinGameData(ChessGame.TeamColor.WHITE, gameId);
            }
        }
        return this.makeRequest("PUT", path, newJoiner, GameData.class);

    }

    public GamesListFromHashMap listGames() throws Exception {
        var path = "/game";
        GamesListFromHashMap games = this.makeRequest("GET", path, null, GamesListFromHashMap.class);
        return games;
    }

    public void deleteAllGames() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (loggedInUser != null) {
                http.addRequestProperty("authorization", loggedInUser.authToken());
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        System.out.println(status);
        if (!isSuccessful(status)) {
            throw new ResponseException("failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
