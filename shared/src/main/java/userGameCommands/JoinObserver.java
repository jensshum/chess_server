package userGameCommands;

    public class JoinObserver extends UserGameCommand {
        private int gameID;

        public JoinObserver(int gameID, String authToken, String username) {
            this.commandType = CommandType.JOIN_OBSERVER;
            setAuthToken(authToken);
            this.gameID = gameID;
            setUsername(username);
        }

        public int getGameID() {
            return gameID;
        }

    }


