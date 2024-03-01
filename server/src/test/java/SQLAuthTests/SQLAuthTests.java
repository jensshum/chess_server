package SQLAuthTests;

import dataAccess.DatabaseManager;
import dataAccess.SQLAuthDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthTests {
    private static SQLAuthDAO sqlAuthDAO;

    @BeforeAll
    public static void setUp() {
        sqlAuthDAO = new SQLAuthDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Test Connection")
    public void testConnection() throws Exception {
        assertTrue(sqlAuthDAO.testConnection());

    }

    @Test
    @Order(2)
    @DisplayName("Register New User")
    public void testInsertUser() throws Exception {
        UserData newUser = new UserData("jensshum","Wilberforce1!","jensshum@gmail.com");
        assertNull(sqlAuthDAO.insertUser(newUser));
    }

}
