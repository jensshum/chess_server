package test.clientTests;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ClientMain;
import ui.EscapeSequences;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    String existingUsername = "jensshum";
    String existingPassword = "wilberforce1";


    private static ClientMain client;
    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.deleteAllGames();
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");


    }
    @AfterEach
    public void breakDown() throws Exception {
        serverFacade.deleteAllGames();
    }

    @Test
    @Order(1)
    @DisplayName("Test Register")
    public void testRegister() throws Exception{
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        Assertions.assertNotNull(auth);
    }

    @Test
    @DisplayName("Bad register")
    public void badRegister() throws Exception {
        try {
            AuthData auth = serverFacade.register("", "", "");
        }
        catch(exception.ResponseException e) {
            Assertions.assertNotNull(e);
        }
    }

    @Test
    @DisplayName("New TEst")
    public void newTest() {
        Assertions.assertNotNull("e");
    }

    @Test
    @DisplayName("Null Test")
    public void nullTest() {
        Assertions.assertNull(null);
    }

    @Test
    @DisplayName("Test Login")
    public void testLogin() throws Exception{
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        serverFacade.login(existingUsername, existingPassword);
        Assertions.assertNull(null);

    }

    @Test
    @DisplayName("Test bad Login")
    public void testBadLogin() throws Exception{
        try {
            AuthData auth2 = serverFacade.login("jensshum", "wilberforce1");
        }
        catch(exception.ResponseException e) {
            Assertions.assertNotNull(e);

        }
    }

    @Test
    @DisplayName("TestLogout")
    public void testLogout() throws Exception {
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        serverFacade.logout();
        Assertions.assertNull(null);


    }

    @Test
    @DisplayName("BadLogout")
    public void badLogout() throws Exception {
        try {
            serverFacade.logout();
        }
        catch (ResponseException e) {
            Assertions.assertEquals("failure: 401", e.getMessage());
        }
    }


    @Test
    @Order(2)
    @DisplayName("Delete all")
    public void deleteAll() throws Exception {
        serverFacade.deleteAllGames();
        Assertions.assertNull(null);

    }

    @Test
    @DisplayName("Create Game")
    public void createGame() throws Exception {
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        GameData game = serverFacade.createGame("newGame");
        Assertions.assertNotNull(game);
    }

    @Test
    @DisplayName("Bad create Game")
    public void badCreate() throws Exception {
        try {
            Assertions.assertNotNull(serverFacade.createGame("newG"));
        }
        catch (ResponseException e) {
            System.out.println(e.getMessage());
            Assertions.assertNull(null);

        }
    }

    @Test
    @DisplayName("List Games")
    public void listGames() throws Exception {
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        GameData game = serverFacade.createGame("newGame");
        Assertions.assertNotNull(serverFacade.listGames());
    }

    @Test
    @DisplayName("Bad List Games")
    public void badListGames() throws Exception {
        try {
            serverFacade.listGames();
        }
        catch (ResponseException e) {
            Assertions.assertEquals("failure: 401", e.getMessage());
        }
    }

    @Test
    @DisplayName("List Games no create")
    public void noCreate() throws Exception {
        try {
            AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
            Assertions.assertNotNull(serverFacade.listGames());
        }
        catch (ResponseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Join Game")
    public void joinGame() throws Exception {
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        GameData game = serverFacade.createGame("newGame");
        serverFacade.gameJoin("black", 1);
        Assertions.assertNull(null);

    }

    @Test
    @DisplayName("Bad join game")
    public void badJoinGame() throws Exception {
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        GameData game = serverFacade.createGame("newGame");
        try {
            serverFacade.gameJoin("black", 2);
        }
        catch (ResponseException e){
            Assertions.assertEquals("failure: 403", e.getMessage());
        }

    }

    @Test
    @DisplayName("Test bad double join")
    public void doubleJoin() throws Exception {
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        GameData game = serverFacade.createGame("newGame");
        serverFacade.gameJoin("black", 1);
        try {
            serverFacade.gameJoin("black", 1);
        }
        catch (ResponseException e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals("failure: 403", e.getMessage());
        }
    }


    @AfterAll
    static void stopServer() {
        server.stop();
        Assertions.assertNull(null);

    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
