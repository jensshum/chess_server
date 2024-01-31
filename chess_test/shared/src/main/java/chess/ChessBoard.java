package chess;

import java.util.Arrays;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board[0][0] = new ChessPiece(WHITE, ROOK);
        board[0][1] = new ChessPiece(WHITE, KNIGHT);
        board[0][2] = new ChessPiece(WHITE, BISHOP);
        board[0][3] = new ChessPiece(WHITE, QUEEN);
        board[0][4] = new ChessPiece(WHITE, KING);
        board[0][5] = new ChessPiece(WHITE, BISHOP);
        board[0][6] = new ChessPiece(WHITE, KNIGHT);
        board[0][7] = new ChessPiece(WHITE, ROOK);
        for (int i = 0; i < 8; i++) {
            board[1][i] = new ChessPiece(WHITE, PAWN);
        }
        board[7][0] = new ChessPiece(BLACK, ROOK);
        board[7][1] = new ChessPiece(BLACK, KNIGHT);
        board[7][2] = new ChessPiece(BLACK, BISHOP);
        board[7][3] = new ChessPiece(BLACK, QUEEN);
        board[7][4] = new ChessPiece(BLACK, KING);
        board[7][5] = new ChessPiece(BLACK, BISHOP);
        board[7][6] = new ChessPiece(BLACK, KNIGHT);
        board[7][7] = new ChessPiece(BLACK, ROOK);
        for (int i = 0; i < 8; i++) {
            board[6][i] = new ChessPiece(BLACK, PAWN);
        }

    }
}
