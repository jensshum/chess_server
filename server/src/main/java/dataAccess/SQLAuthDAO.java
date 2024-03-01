package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public UserData insertUser(UserData user) throws DataAccessException{
        String table_name = "user_table";
        createTable(table_name);
        var json = new Gson().toJson(user);
        String insertStatement = "INSERT INTO " + table_name + " (username, password, email, json) VALUES (?, ?, ?, ?)";
        executeUpdate(insertStatement, user.username(), user.password(), user.email());
        return user;
    };

    private boolean executeUpdate(String statement, Object... params) throws DataAccessException
    {
        try {
            try (var ps = DatabaseManager.getConnection().prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                int rowsAffected = ps.executeUpdate();
            }

        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return true;
    }
    private boolean createTable(String tableName) throws DataAccessException
    {
        try {
            var statement = "";
            statement = switch (tableName) {
                case "user_table" -> "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "username VARCHAR(50) NOT NULL, " +
                        "password VARCHAR(100) NOT NULL, " +
                        "email VARCHAR(100) NOT NULL" +
                        ")";
                case "auth_table" -> "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "username VARCHAR(50) NOT NULL, " +
                        "auth_token VARCHAR(100) NOT NULL " +
                        ")";
                case "games_table" -> "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "game_id INT NOT NULL, " +
                        "white_username VARCHAR(100) NOT NULL, " +
                        "black_username VARCHAR(100) NOT NULL, " +
                        "black_username VARCHAR(100) NOT NULL, " +
                        "game_name VARCHAR(100) NOT NULL, " +
                        "game VARCHAR(100) NOT NULL, " +
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
    public UserData selectUser(UserData user){
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM pet WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readPet(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e));
        }
        return null;
    };
    public AuthData insertToken(AuthData auth){return null;};
    public UserData loginUser(UserData user){return null;};
    public void clear(){};
    public AuthData checkToken(AuthData auth){return null;};
    public AuthData removeUser(AuthData auth){return null;};
    public GameData createGame(String gameName){return null;};
    public HashMap<Integer, GameData> games(){return null;};
    public GameData gameJoin(String username, JoinGameData joinGameData){return null;};

}
