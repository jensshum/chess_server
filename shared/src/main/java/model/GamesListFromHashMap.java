package model;

import java.util.*;

public class GamesListFromHashMap {

    private List<GameData> games = new ArrayList<>();

    public GamesListFromHashMap(HashMap<Integer, GameData> gamesMap) {
        if (gamesMap != null) {
            for (Map.Entry<Integer, GameData> entry : gamesMap.entrySet()) {
                GameData gameEntry = entry.getValue();
                games.add(gameEntry);
            }
        }
    }

    public List<GameData> getGames() {
        return games;
    }
}

