package userGameCommands;

    public class JoinObserver extends UserGameCommand {
        private int gameID;

        public JoinObserver(int gameID, String authToken) {
            this.commandType = CommandType.JOIN_OBSERVER;
            setAuthToken(authToken);
            this.gameID = gameID;
        }

        public int getGameID() {
            return gameID;
        }
    }


