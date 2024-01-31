package chess;

import java.util.Arrays;

import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessGame.TeamColor.BLACK;

import static chess.ChessPiece.PieceType.ROOK;
import static chess.ChessPiece.PieceType.KNIGHT;
import static chess.ChessPiece.PieceType.BISHOP;
import static chess.ChessPiece.PieceType.KING;
import static chess.ChessPiece.PieceType.QUEEN;
import static chess.ChessPiece.PieceType.PAWN;


/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] chessBoard = new ChessPiece[8][8];

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(chessBoard, that.chessBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(chessBoard);
    }

    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        chessBoard[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return chessBoard[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Bottom is white
        chessBoard[0][0] = new ChessPiece(WHITE, ROOK);
        chessBoard[0][1] = new ChessPiece(WHITE, KNIGHT);
        chessBoard[0][2] = new ChessPiece(WHITE, BISHOP);
        chessBoard[0][3] = new ChessPiece(WHITE, QUEEN);
        chessBoard[0][4] = new ChessPiece(WHITE, KING);
        chessBoard[0][5] = new ChessPiece(WHITE, BISHOP);
        chessBoard[0][6] = new ChessPiece(WHITE, KNIGHT);
        chessBoard[0][7] = new ChessPiece(WHITE, ROOK);
        for (int i=0; i < 8; i++){
            chessBoard[1][i] = new ChessPiece(WHITE, PAWN);
        }
        chessBoard[7][0] = new ChessPiece(BLACK, ROOK);
        chessBoard[7][1] = new ChessPiece(BLACK, KNIGHT);
        chessBoard[7][2] = new ChessPiece(BLACK, BISHOP);
        chessBoard[7][3] = new ChessPiece(BLACK, QUEEN);
        chessBoard[7][4] = new ChessPiece(BLACK, KING);
        chessBoard[7][5] = new ChessPiece(BLACK, BISHOP);
        chessBoard[7][6] = new ChessPiece(BLACK, KNIGHT);
        chessBoard[7][7] = new ChessPiece(BLACK, ROOK);
        for (int i=0; i < 8; i++){
            chessBoard[6][i] = new ChessPiece(BLACK, PAWN);
        }

    }
}
