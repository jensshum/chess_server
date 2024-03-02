package SQLAuthTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.SQLAuthDAO;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import service.AuthService;
import service.GameService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthTests {

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
    }

    @AfterAll
    public static void breakDown() throws Exception {
//        sqlAuthDAO.clear();
    }

    @Test
    @Order(0)
    @DisplayName("Clear")
    public void testClear() throws Exception {
        sqlAuthDAO.clear();
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
        sqlAuthDAO.clear();
        assertNull(sqlAuthDAO.selectUser(testUser));
        UserData newUser = sqlAuthDAO.insertUser(testUser);
        AuthData newAuth = new AuthData(createAuthToken(), newUser.username());
        assertNotNull(sqlAuthDAO.insertToken(newAuth));

    }

    @Test
    @Order(6)
    @DisplayName("Login Existing User")
    public void loginExistingUser() throws Exception {
        sqlAuthDAO.clear();
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
        sqlAuthDAO.clear();
        AuthData newAuth = new AuthData(createAuthToken(), "");
        sqlAuthDAO.insertToken(newAuth);
        assertNotNull(sqlAuthDAO.checkToken(newAuth));
    }

    @Test
    @Order(9)
    @DisplayName("Check logout")
    public void validLogout() throws Exception {
        sqlAuthDAO.clear();
        AuthData newAuth = new AuthData(createAuthToken(), "auth_token");
        AuthData thing = sqlAuthDAO.insertToken(newAuth);
        System.out.println(thing);
        AuthData newThing = sqlAuthDAO.removeUser(newAuth);
        System.out.println(newThing);
        assertNotNull(newThing);
    }

    @Test
    @Order(10)
    @DisplayName("Create Game")
    public void createGame() throws Exception {
        sqlAuthDAO.clear();
        sqlAuthDAO.createGame("cloopy");
        assertNotNull(sqlAuthDAO.createGame("Prloopy"));
    }





}
