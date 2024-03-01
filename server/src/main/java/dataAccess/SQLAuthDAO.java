package dataAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import dataAccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import javax.xml.crypto.Data;

import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO{

    public boolean testConnection() throws Exception{
        try {
            // Create the database if it doesn't exist
            DatabaseManager.createDatabase();

            // Obtain a connection to the database
            try (Connection connection = DatabaseManager.getConnection()) {
                if (connection != null && !connection.isClosed()) {
                    System.out.println("Connection to the database established successfully.");
                    return true;
                }
            }
        } catch (DataAccessException | SQLException e) {
            System.err.println("Error while testing database connection: " + e.getMessage());
        }
        return false;
    }

    public void createTables() throws Exception {
        createTable("auth_table");
        createTable("user_table");
        createTable("games_table");
    }

    public UserData insertUser(UserData user) throws DataAccessException{
        String table_name = "user_table";
        var json = new Gson().toJson(user);
        String insertStatement = "INSERT INTO " + table_name + " (username, password, email, json) VALUES (?, ?, ?, ?)";
        executeUpdate(insertStatement, user.username(), user.password(), user.email(), json);
        return user;
    };

    private int executeUpdate(String statement, Object... params) throws DataAccessException
    {
        try {
            try (var ps = DatabaseManager.getConnection().prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }

        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    private boolean createTable(String tableName) throws DataAccessException
    {
        try {
            var statement = "";
            statement = switch (tableName) {
                case "user_table" -> "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "username TEXT NOT NULL, " +
                        "password TEXT NOT NULL, " +
                        "email VARCHAR(100) NOT NULL," +
                        "json TEXT NOT NULL" +
                        ")";
                case "auth_table" -> "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "username VARCHAR(50) NOT NULL, " +
                        "auth_token VARCHAR(100) NOT NULL, " +
                        "json TEXT NOT NULL " +
                        ")";
                case "games_table" -> "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "game_id INT NOT NULL, " +
                        "white_username VARCHAR(100) NOT NULL, " +
                        "black_username VARCHAR(100) NOT NULL, " +
                        "game_name VARCHAR(100) NOT NULL, " +
                        "game VARCHAR(100) NOT NULL, " +
                        "json TEXT NOT NULL" +
                        ")";
                case null, default -> "";
            };

            var conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public UserData selectUser(UserData user) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM user_table WHERE username=? AND password=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, user.username());
                ps.setString(2, user.password());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return null;
    };

    private UserData readUser(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        var user = new Gson().fromJson(json, UserData.class);
        return user;
    }
    public AuthData insertToken(AuthData auth) throws Exception{
        var json = new Gson().toJson(auth);
        String insertStatement = "INSERT INTO auth_table (auth_token, username, json) VALUES (?,?,?)";
        executeUpdate(insertStatement, auth.authToken(), auth.username(), json);
        return auth;
    };
    public UserData loginUser(UserData user) throws Exception {
        UserData foundUser = selectUser(user);
        return foundUser;
    };
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user_table";
        executeUpdate(statement);
        var statement2 = "TRUNCATE auth_table";
        executeUpdate(statement2);
        var statement3 = "TRUNCATE games_table";
        executeUpdate(statement3);
    };

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        var auth = new Gson().fromJson(json, AuthData.class);
        return auth;
    }

    private AuthData selectAuth(AuthData auth, String toMatch) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auth_table WHERE " + toMatch + "=?";
            try (var ps = conn.prepareStatement(statement)) {
                var param = "";
                if (Objects.equals(toMatch, "username")){
                    param = auth.username();
                }
                else
                {
                    param = auth.authToken();
                }
                ps.setString(1, param);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return null;
    };
    public AuthData checkToken(AuthData auth) throws Exception {
        AuthData foundAuth = selectAuth(auth, "username");
        return foundAuth;
    };
    public AuthData removeUser(AuthData auth)throws Exception{
        AuthData foundAuth = selectAuth(auth, "auth_token");
        if (foundAuth != null) {
            var statement = "DELETE FROM auth_table WHERE auth_token = ?";
            executeUpdate(statement, auth.authToken());
        }
        return foundAuth;
    };
    public GameData createGame(String gameName){return null;};
    public HashMap<Integer, GameData> games(){return null;};
    public GameData gameJoin(String username, JoinGameData joinGameData){return null;};

}
