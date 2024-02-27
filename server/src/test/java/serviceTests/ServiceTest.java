package serviceTests;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import service.AuthService;
import service.GameService;

public class AuthServiceTest {

    private static AuthService authService;
    private static GameService gameService;

    @BeforeAll
    public static void setUp() {
        authService = new AuthService();
        gameService = new GameService();
    }

    @AfterAll
    public static void tearDown() {
        authService.clearDatabase();
    }

    @Test
    @Order(1)
    @DisplayName("Test Register User")
    public void testRegister() throws ResponseException {
        UserData user = new UserData("testuser", "password", "email");
        AuthData authData = authService.register(user);
        assertNotNull(authData);
    }

    @Test
    @Order(2)
    @DisplayName("Test Login User")
    public void testLogin() {
        UserData user = new UserData("testuser", "password","");
        AuthData authData = authService.login(user);
        assertNotNull(authData);
    }

    @Test
    @Order(3)
    @DisplayName("Test logout user")
    public void testLogoutUser() {
        UserData user = new UserData("testuser", "password","");
        AuthData authData = authService.login(user);
        AuthData loggedOutUser = authService.logoutUser(authData);
        assertNotNull(loggedOutUser);
    }

    @Test
    @Order(4)
    @DisplayName("Test Verify Token")
    public void testVerifyToken() {
        UserData user = new UserData("testuser", "password","");
        AuthData authData = authService.login(user);
        AuthData verifiedToken = authService.verifyToken(authData);
        assertNotNull(verifiedToken);
    }

    @Test
    @Order(5)
    @DisplayName("")

    // Add more test cases as needed for other methods and edge cases
}
