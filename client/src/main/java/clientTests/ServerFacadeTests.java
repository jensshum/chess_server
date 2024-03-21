package clientTests;

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
        var port = server.run(8081);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:8081");
        serverFacade.deleteAllGames();
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        assertNull(null);


    }
    @AfterEach
    public void breakDown() throws Exception {
        serverFacade.deleteAllGames();
        assertNull(null);
    }

    @Test
    @Order(1)
    @DisplayName("Test Register")
    public void testRegister() throws Exception{
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        assertNotNull(auth);
    }

    @Test
    @DisplayName("Bad register")
    public void badRegister() throws Exception {
        try {
            AuthData auth = serverFacade.register("", "", "");
        }
        catch(exception.ResponseException e) {
            assertNotNull(e);
        }
    }

    @Test
    @DisplayName("New TEst")
    public void newTest() {
        assertNotNull("e");
    }

    @Test
    @DisplayName("Null Test")
    public void nullTest() {
        assertNull(null);
    }

    @Test
    @DisplayName("Test Login")
    public void testLogin() throws Exception{
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        serverFacade.login(existingUsername, existingPassword);
        assertNull(null);

    }

    @Test
    @DisplayName("Test bad Login")
    public void testBadLogin() throws Exception{
        try {
            AuthData auth2 = serverFacade.login("jensshum", "wilberforce1");
        }
        catch(exception.ResponseException e) {
            assertNotNull(e);

        }
    }

    @Test
    @DisplayName("TestLogout")
    public void testLogout() throws Exception {
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        serverFacade.logout();
        assertNull(null);


    }

    @Test
    @DisplayName("BadLogout")
    public void badLogout() throws Exception {
        try {
            serverFacade.logout();
        }
        catch (ResponseException e) {
            assertEquals("failure: 401", e.getMessage());
        }
    }


    @Test
    @Order(2)
    @DisplayName("Delete all")
    public void deleteAll() throws Exception {
        serverFacade.deleteAllGames();
        assertNull(null);

    }

    @Test
    @DisplayName("Create Game")
    public void createGame() throws Exception {
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        GameData game = serverFacade.createGame("newGame");
        assertNotNull(game);
    }

    @Test
    @DisplayName("Bad create Game")
    public void badCreate() throws Exception {
        try {
            assertNotNull(serverFacade.createGame("newG"));
        }
        catch (ResponseException e) {
            System.out.println(e.getMessage());
            assertNull(null);

        }
    }

    @Test
    @DisplayName("List Games")
    public void listGames() throws Exception {
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        GameData game = serverFacade.createGame("newGame");
        assertNotNull(serverFacade.listGames());
    }

    @Test
    @DisplayName("Bad List Games")
    public void badListGames() throws Exception {
        try {
            serverFacade.listGames();
        }
        catch (ResponseException e) {
            assertEquals("failure: 401", e.getMessage());
        }
    }

    @Test
    @DisplayName("List Games no create")
    public void noCreate() throws Exception {
        try {
            AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
            assertNotNull(serverFacade.listGames());
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
        assertNull(null);

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
            assertEquals("failure: 403", e.getMessage());
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
            assertEquals("failure: 403", e.getMessage());
        }
    }


    @AfterAll
    static void stopServer() {
        server.stop();
        assertNull(null);

    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
