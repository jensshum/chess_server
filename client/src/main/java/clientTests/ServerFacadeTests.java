package clientTests;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ClientMain;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;


    private static ClientMain client;
    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(8081);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:8081");
        serverFacade.deleteAllGames();
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
    @DisplayName("Test Login")
    public void testLogin() throws Exception{
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        try {
            AuthData auth2 = serverFacade.login("", "wilberforce1");
        }
        catch (ResponseException e) {
            System.out.println("Bloop");
        }
//        System.out.println(auth2.authToken());
//        System.out.println(auth2);
//        assertNotNull(auth2);
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

    }

    @Test
    @Order(2)
    @DisplayName("Delete all")
    public void deleteAll() throws Exception {
        serverFacade.deleteAllGames();
    }

    @Test
    @DisplayName("Create Game")
    public void createGame() throws Exception {
        AuthData auth = serverFacade.register("jensshum", "wilberforce1", "email.com");
        GameData game = serverFacade.createGame("newGame");
        System.out.print(game.getGameName());
    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
