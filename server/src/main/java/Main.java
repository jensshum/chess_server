import chess.*;
import dataAccess.MemoryAuthDAO;
import server.Server;

public class Main {
    public static void main(String[] args) throws Exception{
        Server s = new Server();
        s.run(8083);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}