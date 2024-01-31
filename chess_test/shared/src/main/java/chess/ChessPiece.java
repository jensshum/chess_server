package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor pieceColor;
    PieceType pieceType;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public void addAxialMoves(ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement, Collection<ChessMove> legalMoves, boolean singleMove){
        int tempCol = myPosition.getColumn();
        int tempRow = myPosition.getRow();

        tempCol += colIncrement;
        tempRow += rowIncrement;

        if (!singleMove) {
            while (tempCol <= 8 && tempCol >= 1 && tempRow <= 8 && tempRow >= 1) {
                ChessPosition newPosition = new ChessPosition(tempRow, tempCol);
                if (board.getPiece(newPosition) == null) {
                    legalMoves.add(new ChessMove(myPosition, newPosition, null));

                }
                else {
                    if (board.getPiece(newPosition).getTeamColor() != this.pieceColor) {
                        legalMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
                tempCol += colIncrement;
                tempRow += rowIncrement;
            }
        }
        else {
            if (tempCol <= 8 && tempCol >= 1 && tempRow <= 8 && tempRow >= 1) {
                ChessPosition newPosition = new ChessPosition(tempRow, tempCol);
                if (board.getPiece(newPosition) == null) {
                    legalMoves.add(new ChessMove(myPosition, newPosition, null));

                }
                else {
                    if (board.getPiece(newPosition).getTeamColor() != this.pieceColor) {
                        legalMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }

    }

    public void addForwardMove(ChessBoard board, ChessPosition myPosition, int rowIncrement, Collection<ChessMove> legalMoves) {
        int col = myPosition.getColumn();
        int row = myPosition.getRow();

        int newRow = row + rowIncrement;
        if (newRow <= 8 && newRow >= 1) {
            ChessPosition newPosition = new ChessPosition(newRow, col);
            if (board.getPiece(newPosition) == null) {
                if (this.pieceColor == BLACK && newRow == 1) {
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                }
                else if (this.pieceColor == WHITE && newRow == 8) {
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                }
                else{
                    legalMoves.add(new ChessMove(myPosition, newPosition, null));

                }
            }
        }
    }public void addCapturemove(ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement, Collection<ChessMove> legalMoves) {
        int col = myPosition.getColumn();
        int row = myPosition.getRow();

        int newRow = row + rowIncrement;
        int newCol = col + colIncrement;
        if (newRow <= 8 && newRow >= 1 && newCol <= 8 && newRow >= 1) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
            if (board.getPiece(newPosition) != null && pieceAtNewPosition.pieceColor != this.pieceColor) {
                if (this.pieceColor == BLACK && newRow == 1) {
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                }
                else if (this.pieceColor == WHITE && newRow == 8) {
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                }
                else{
                    legalMoves.add(new ChessMove(myPosition, newPosition, null));

                }
            }
        }
    }

    public Collection<ChessMove> addPawnMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> legalMoves = new HashSet<>();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();
        int rowDirection = (this.pieceColor == BLACK) ? -1 : 1;
        int startRow = (this.pieceColor == BLACK) ? 7 : 2;

        addForwardMove(board, myPosition, rowDirection, legalMoves);

        //initial two square move
        if (startRow == row) {
            if (board.getPiece(new ChessPosition(row + rowDirection, col)) == null) {
                addForwardMove(board, myPosition, 2 * rowDirection, legalMoves);
            }
        }

        addCapturemove(board, myPosition, rowDirection, -1, legalMoves);
        addCapturemove(board, myPosition, rowDirection, 1, legalMoves);



        return legalMoves;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalMoves = new HashSet<>();

        switch (pieceType) {
            case KING:
                addAxialMoves(board, myPosition, 1, 1, legalMoves, true);
                // up and to the left
                addAxialMoves(board, myPosition, 1, -1, legalMoves, true);
                // down and to the right
                addAxialMoves(board, myPosition, -1, 1, legalMoves, true);
                // down and to the left
                addAxialMoves(board, myPosition, -1, -1, legalMoves, true);
                // straight up
                addAxialMoves(board, myPosition, 1, 0, legalMoves, true);
                // straight down
                addAxialMoves(board, myPosition, -1, 0, legalMoves, true);
                // straight left
                addAxialMoves(board, myPosition, 0, -1, legalMoves, true);
                // straight right
                addAxialMoves(board, myPosition, 0, 1, legalMoves, true);
                break;
            case QUEEN:
                // up and to the right
                addAxialMoves(board, myPosition, 1, 1, legalMoves, false);
                // up and to the left
                addAxialMoves(board, myPosition, 1, -1, legalMoves, false);
                // down and to the right
                addAxialMoves(board, myPosition, -1, 1, legalMoves, false);
                // down and to the left
                addAxialMoves(board, myPosition, -1, -1, legalMoves, false);
                // straight up
                addAxialMoves(board, myPosition, 1, 0, legalMoves, false);
                // straight down
                addAxialMoves(board, myPosition, -1, 0, legalMoves, false);
                // straight left
                addAxialMoves(board, myPosition, 0, -1, legalMoves, false);
                // straight right
                addAxialMoves(board, myPosition, 0, 1, legalMoves, false);
                break;
            case BISHOP:
                // up and to the right
                addAxialMoves(board, myPosition, 1, 1, legalMoves, false);
                // up and to the left
                addAxialMoves(board, myPosition, 1, -1, legalMoves, false);
                // down and to the right
                addAxialMoves(board, myPosition, -1, 1, legalMoves, false);
                // down and to the left
                addAxialMoves(board, myPosition, -1, -1, legalMoves, false);
                break;
            case KNIGHT:
                // up 2 and 1 to the right
                addAxialMoves(board, myPosition, 2, 1, legalMoves, true);
                // up 2 and 1 to the left
                addAxialMoves(board, myPosition, 2, -1, legalMoves, true);
                // down 2 and 1 to the right
                addAxialMoves(board, myPosition, -2, 1, legalMoves, true);
                // down 2 and 1 to the left
                addAxialMoves(board, myPosition, -2, -1, legalMoves, true);
                // up 1 and 2 to the right
                addAxialMoves(board, myPosition, 1, 2, legalMoves, true);
                // up 1 and 2 to the left
                addAxialMoves(board, myPosition, 1, -2, legalMoves, true);
                // down 1 and 2 to the right
                addAxialMoves(board, myPosition, -1, 2, legalMoves, true);
                // down 1 and 2 to the left
                addAxialMoves(board, myPosition, -1, -2, legalMoves, true);
                break;
            case ROOK:
                addAxialMoves(board, myPosition, 1, 0, legalMoves, false);
                // straight down
                addAxialMoves(board, myPosition, -1, 0, legalMoves, false);
                // straight left
                addAxialMoves(board, myPosition, 0, -1, legalMoves, false);
                // straight right
                addAxialMoves(board, myPosition, 0, 1, legalMoves, false);
                break;
            case PAWN:
                legalMoves = addPawnMoves(board, myPosition);
                break;
        }
        return legalMoves;

    }

}
