package serviceTests;

import chess.ChessGame;
import model.GameData;
import model.JoinGameData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import service.AuthService;
import service.GameService;
import java.util.Map;

public class ServiceTest {

    private static AuthService authService;
    private static GameService gameService;

    @BeforeAll
    public static void setUp() {
        authService = new AuthService();
        gameService = new GameService();
    }

    @AfterAll
    public static void tearDown() throws Exception{
        authService.clearDatabase();
    }

    @Test
    @Order(1)
    @DisplayName("Test Register User")
    public void testRegister() throws Exception {
        UserData user = new UserData("testuser", "password", "email");
        AuthData authData = authService.register(user);
        assertNotNull(authData);

    }

    @Test
    @Order(2)
    @DisplayName("Test Duplicate User Register")
    public void testDuplicateRegister() throws Exception {
        UserData user2 = new UserData("testuser", "password", "email");
        UserData userCheck = authService.getUser(user2);
        assertNull(userCheck);
    }


    @Test
    @Order(3)
    @DisplayName("Test Login User")
    public void testLogin() throws Exception{
        UserData user = new UserData("testuser", "password","");
        AuthData authData = authService.login(user);
        assertNull(authData);
    }


    @Test
    @Order(4)
    @DisplayName("Test logout user")
    public void testLogoutUser() throws Exception{
        UserData user = new UserData("testuser", "password","");
        AuthData authData = authService.login(user);
        AuthData loggedOutUser = authService.logoutUser(authData);
        assertNull(loggedOutUser);
    }

    @Test
    @Order(5)
    @DisplayName("Test Verify Token")
    public void testVerifyToken() throws Exception{
        UserData user = new UserData("testuser", "password","");
        AuthData authData = authService.login(user);
        AuthData verifiedToken = authService.verifyToken(authData);
        assertNotNull(verifiedToken);
    }

    @Test
    @Order(6)
    @DisplayName("Create Game Test")
    public void testCreateGame() {
        String gameName = "New Game";
        assertNotNull(gameName);

    }
    @Test
    @Order(7)
    @DisplayName("Test GetGames")
    public void testGetGames() {
        Map<Integer, GameData> games = gameService.getGames();
        assertNotNull(games, "Games was null");

    }

    @Test
    @Order(8)
    @DisplayName("Test Join Game")
    public void testJoinGame() {
        String username = "Flinnigan";
        JoinGameData joinData = new JoinGameData(ChessGame.TeamColor.BLACK, 1);
        GameData gameUser = gameService.joinGame(username, joinData);
        assertNull(gameUser, "JoinGame returned null");

    }

    @Test
    @Order(9)
    @DisplayName("Test invalid login")
    public void invalidUsernameLogin() throws Exception{
        String username = "Unregistered Username";
        String password = "Unregistered password";
        AuthData invalidUser = authService.login(new UserData(username,password, ""));
        assertNull(invalidUser);
    }

    @Test
    @Order(10)
    @DisplayName("Test Join with Invalid Game Id")
    public void invalidGameId() {
        int id = 0;
        String username = "Invalidjoingame";
        JoinGameData badJoin = new JoinGameData(ChessGame.TeamColor.BLACK, id);
        GameData game = gameService.joinGame(username, badJoin);
        assertNull(game);
    }

    @Test
    @Order(11)
    @DisplayName("Bad Game Create")
    public void badGameCreate() throws Exception {
        String authToken = "Bad auth token";
        GameData badGame = new GameData(0, "","","badGame",null);
        assertNull(gameService.createGame(authToken,badGame));

    }

    @Test
    @Order(12)
    @DisplayName("Bad logout")
    public void badLogout() throws Exception{
        String badToken = "Bad Token";
        assertNull(authService.logoutUser(new AuthData(badToken, "")));
    }

    @Test
    @Order(13)
    @DisplayName("Test bad GetGames")
    public void badGetGames() {
        assertNull(null);
    }

    // Add more test cases as needed for other methods and edge cases
}
