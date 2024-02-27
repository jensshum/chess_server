package model;

import java.util.*;

public class GamesListFromHashMap {

    private List<GameData> games = new ArrayList<>();

    public GamesListFromHashMap(HashMap<Integer, GameData> games_) {
        if (games_ != null) {
            for (Map.Entry<Integer, GameData> entry : games_.entrySet()) {
                GameData gameEntry = entry.getValue();
                games.add(gameEntry);
            }
        }
    }

    public List<GameData> getGames() {
        return games;
    }
}

