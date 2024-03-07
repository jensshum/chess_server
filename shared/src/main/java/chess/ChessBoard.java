package chess;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Arrays;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {

    private ChessPiece[][] boardMatrix = new ChessPiece[8][8];


    public ChessBoard() {
        this.resetBoard();
    }

//    public ChessBoard clone()

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        boardMatrix[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard cloned = (ChessBoard) super.clone();
            cloned.boardMatrix = new ChessPiece[8][8];
            for (int i = 0; i < boardMatrix.length; i++) {
                for (int j = 0; j < boardMatrix[i].length; j++) {
                    if (this.boardMatrix[i][j] != null) {
                        cloned.boardMatrix[i][j] = this.boardMatrix[i][j].clone();
                    }
                }
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (int i = 0; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                ChessPiece piece = boardMatrix[i][j];
                if (piece != null) {
                    // Append the piece type and color to the builder.
                    // You might want to modify this part to better represent the piece.
                    builder.append(piece.getTeamColor().name().charAt(0)) // Append the first letter of the color
                            .append(piece.getPieceType().name().charAt(0)) // Append the first letter of the piece type
                            .append(" ");
                } else {
                    // Append a placeholder for empty positions.
                    builder.append("__ ");
                }
            }
            // Add a new line after each row.
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return boardMatrix[position.getRow() - 1][position.getColumn() - 1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(boardMatrix, that.boardMatrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(boardMatrix);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        boardMatrix[0][0] = new ChessPiece(WHITE, ChessPiece.PieceType.ROOK);
        boardMatrix[0][1] = new ChessPiece(WHITE, ChessPiece.PieceType.KNIGHT);
        boardMatrix[0][2] = new ChessPiece(WHITE, ChessPiece.PieceType.BISHOP);
        boardMatrix[0][3] = new ChessPiece(WHITE, ChessPiece.PieceType.QUEEN);
        boardMatrix[0][4] = new ChessPiece(WHITE, ChessPiece.PieceType.KING);
        boardMatrix[0][5] = new ChessPiece(WHITE, ChessPiece.PieceType.BISHOP);
        boardMatrix[0][6] = new ChessPiece(WHITE, ChessPiece.PieceType.KNIGHT);
        boardMatrix[0][7] = new ChessPiece(WHITE, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < 8; i++) {
            boardMatrix[1][i] = new ChessPiece(WHITE, ChessPiece.PieceType.PAWN);
        }
        boardMatrix[7][0] = new ChessPiece(BLACK, ChessPiece.PieceType.ROOK);
        boardMatrix[7][1] = new ChessPiece(BLACK, ChessPiece.PieceType.KNIGHT);
        boardMatrix[7][2] = new ChessPiece(BLACK, ChessPiece.PieceType.BISHOP);
        boardMatrix[7][3] = new ChessPiece(BLACK, ChessPiece.PieceType.QUEEN);
        boardMatrix[7][4] = new ChessPiece(BLACK, ChessPiece.PieceType.KING);
        boardMatrix[7][5] = new ChessPiece(BLACK, ChessPiece.PieceType.BISHOP);
        boardMatrix[7][6] = new ChessPiece(BLACK, ChessPiece.PieceType.KNIGHT);
        boardMatrix[7][7] = new ChessPiece(BLACK, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < 8; i++) {
            boardMatrix[6][i] = new ChessPiece(BLACK, ChessPiece.PieceType.PAWN);
        }

//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                System.out.println(boardMatrix[i][j]);
//            }
//        }
    }
}
