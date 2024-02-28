package serviceTests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private static GameService gameService;
    @BeforeAll
    public static void setUp() {
        gameService = new GameService();
    }


    @Test
    void createGame() {
    }

    @Test
    void getGames() {
    }

    @Test
    void joinGame() {
    }
}