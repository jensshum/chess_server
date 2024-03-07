package dataAccessTests;

import chess.ChessGame;
import dataAccess.SQLAuthDAO;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class dataAccessTests {


    private static String testUsername = "jensshum";
    private static String testPassword = "12345";
    private static String testEmail = "jensshum.com";

    private static UserData testUser;

    private static SQLAuthDAO sqlAuthDAO;

    @BeforeAll
    public static void setUp() throws Exception {
        sqlAuthDAO = new SQLAuthDAO();
        sqlAuthDAO.createTables();
        testUser = new UserData(testUsername, testPassword, testEmail);
//        sqlAuthDAO.clear();
    }

    @AfterEach
    public void breakDown() throws Exception {
//        sqlAuthDAO.clear();
    }

    @Test
    @Order(0)
    @DisplayName("Clear")
    public void testClear() throws Exception {
        sqlAuthDAO.clear();
        assertNull(null);
    }
    @Test
    @Order(1)
    @DisplayName("Test Connection")
    public void testConnection() throws Exception {
        assertTrue(sqlAuthDAO.testConnection());

    }

    @Test
    @Order(2)
    @DisplayName("Insert New User (No AuthToken)")
    public void testInsertUser() throws Exception {
        assertNotNull(sqlAuthDAO.insertUser(testUser));
    }

    @Test
    @Order(3)
    @DisplayName("Select Non-existing User")
    public void getNonexistingUser() throws Exception {
        sqlAuthDAO.clear();
        assertNull(sqlAuthDAO.selectUser(testUser));
    }

    @Test
    @Order(4)
    @DisplayName("Select Existing User")
    public void getExistingUser() throws Exception {
        sqlAuthDAO.insertUser(testUser);
        assertNotNull(sqlAuthDAO.selectUser(testUser));
    }

    private String createAuthToken() {
        final String authToken = UUID.randomUUID().toString();
        return authToken;
    }
    @Test
    @Order(5)
    @DisplayName("Register New User (Return AuthToken)")
    public void fullRegister() throws Exception {
        UserData newUser = sqlAuthDAO.insertUser(testUser);
        AuthData newAuth = new AuthData(createAuthToken(), newUser.username());
        assertNotNull(sqlAuthDAO.insertToken(newAuth));

    }

    @Test
    @Order(6)
    @DisplayName("Login Existing User")
    public void loginExistingUser() throws Exception {
        sqlAuthDAO.insertUser(testUser);
        assertNotNull(sqlAuthDAO.loginUser(testUser));
    }

    @Test
    @Order(7)
    @DisplayName("Insert Token")
    public void insertValidToken() throws Exception {
        AuthData newAuth = new AuthData(createAuthToken(), "jensshum");
        assertNotNull(sqlAuthDAO.insertToken(newAuth));
    }

    @Test
    @Order(8)
    @DisplayName("Check valid token")
    public void checkValidToken() throws Exception {
        AuthData newAuth = new AuthData(createAuthToken(), "");
        sqlAuthDAO.insertToken(newAuth);
        assertNotNull(sqlAuthDAO.checkToken(newAuth));
    }

    @Test
    @Order(9)
    @DisplayName("Check logout")
    public void validLogout() throws Exception {
        AuthData newAuth = new AuthData(createAuthToken(), "auth_token");
        AuthData thing = sqlAuthDAO.insertToken(newAuth);
        AuthData newThing = sqlAuthDAO.removeUser(newAuth);
        assertNotNull(newThing);
    }

    @Test
    @Order(10)
    @DisplayName("Create Game")
    public void createGame() throws Exception {
        assertNotNull(sqlAuthDAO.createGame("Prloopy"));
    }

    @Test
    @Order(11)
    @DisplayName("MultiGame Create")
    public void multiGames() throws Exception {
        sqlAuthDAO.createGame("scooby");
        assertNotNull(sqlAuthDAO.createGame("rooby"));
    }

    @Test
    @Order(12)
    @DisplayName("Get all Games")
    public void getGames() throws Exception {
        sqlAuthDAO.createGame("scooby");
        sqlAuthDAO.createGame("rooby");
        HashMap<Integer, GameData> games = sqlAuthDAO.games();
        for (Map.Entry<Integer, GameData> entry : games.entrySet()) {
            GameData game = entry.getValue();
        }
        assertNotNull(games);

    }

    @Test
    @Order(13)
    @DisplayName("Test Join Game")
    public void joinGames() throws Exception {
        sqlAuthDAO.createGame("scooby");
        JoinGameData newGameJoin = new JoinGameData(ChessGame.TeamColor.BLACK, 1);
        GameData newGame = sqlAuthDAO.gameJoin("jensshum",newGameJoin);
        assertNotNull(newGame);
    }

    @Test
    @Order(14)
    @DisplayName("Test Same color join game")
    public void duplicateJoin() throws Exception {
        sqlAuthDAO.createGame("scoob");
        JoinGameData newGameJoin = new JoinGameData(ChessGame.TeamColor.BLACK, 1);
        JoinGameData doubleGameJoin = new JoinGameData(ChessGame.TeamColor.BLACK, 1);
        GameData newGame = sqlAuthDAO.gameJoin("jensshum",newGameJoin);
        GameData doubleGame = sqlAuthDAO.gameJoin("flubdub", newGameJoin);
        assertNull(doubleGame);
    }

    @Test
    @Order(15)
    @DisplayName("Watcher join")
    public void watcherJoin() throws Exception {
        sqlAuthDAO.createGame("scoob");
        JoinGameData newGameJoin = new JoinGameData(null, 1);
        GameData response = sqlAuthDAO.gameJoin("bloop", newGameJoin);
        assertNotNull(response);

    }

    @Test
    @Order(15)
    @DisplayName("bad id join")
    public void poopJoin() throws Exception {
        sqlAuthDAO.createGame("scoob");
        JoinGameData newGameJoin = new JoinGameData(null, 0);
        GameData response = sqlAuthDAO.gameJoin("bloop", newGameJoin);
        assertNull(response);
    }





}
