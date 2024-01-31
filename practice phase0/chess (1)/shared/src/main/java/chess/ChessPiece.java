package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.HashSet;

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


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        pieceType = type;
    }

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


    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", pieceType=" + pieceType +
                '}';
    }

//    public void addAxialMoves(ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement, Collection<ChessMove> legalMoves, boolean singleMove){
//        int tempRow = myPosition.getRow();
//        int tempCol = myPosition.getColumn();
//
//        tempCol += colIncrement;
//        tempRow += rowIncrement;
//
//        if (!singleMove) {
//            while (tempRow >= 1 && tempRow <= 8 && tempCol >= 1 && tempCol <= 8) {
//                ChessPosition newPosition = new ChessPosition(tempRow, tempCol);
//                if (board.getPiece(newPosition) == null){
//                    legalMoves.add(new ChessMove(myPosition, newPosition, null));
//                }
//                else {
//                    if (board.getPiece(newPosition).getTeamColor() != this.pieceColor) {
//                        legalMoves.add(new ChessMove(myPosition, newPosition, null));
//                    }
//                    break;
//                }
//                tempCol += colIncrement;
//                tempRow += rowIncrement;
//            }
//        }
//        if (tempRow >= 1 && tempRow <= 8 && tempCol >= 1 && tempCol <= 8) {
//            ChessPosition newPosition = new ChessPosition(tempRow, tempCol);
//            if (board.getPiece(newPosition) == null){
//                legalMoves.add(new ChessMove(myPosition, newPosition, null));
//            }
//            else {
//                if (board.getPiece(newPosition).getTeamColor() != this.pieceColor) {
//                    legalMoves.add(new ChessMove(myPosition, newPosition, null));
//                }
//            }
//
//        }
//    }


    public void addAxialMoves(ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement, Collection<ChessMove> legalMoves, boolean singleMove) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();

        tempRow += rowIncrement;
        tempCol += colIncrement;

        if (!singleMove) {
            while (tempCol >= 1 && tempCol <= 8 && tempRow >= 1 && tempRow <= 8) {
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
                tempRow += rowIncrement;
                tempCol += colIncrement;
            }
        }
        else {
            if (tempCol >= 1 && tempCol <= 8 && tempRow >= 1 && tempRow <= 8) {
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     *
     *
     *
     */

    public void addPawnForwardMoves(ChessBoard board, ChessPosition myPosition, int rowIncrement, Collection<ChessMove> legalPawnMoves) {
        int tempRow = myPosition.getRow() + rowIncrement;
        if (tempRow >= 1 && tempRow <= 8) {
            ChessPosition newPosition = new ChessPosition(tempRow, myPosition.getColumn());
            if (board.getPiece(newPosition) == null) {
                if (tempRow == 8 && this.pieceColor == WHITE) {
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                } else if ((tempRow == 1 && this.pieceColor == BLACK)) {
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                } else {
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }

    public void addPawnCaptureMoves(ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement, Collection<ChessMove> legalPawnMoves) {
        int newRow = myPosition.getRow() + rowIncrement;
        int newColumn = myPosition.getColumn() + colIncrement;
        if (newRow >= 1 && newRow <= 8 && newColumn >= 1 && newColumn <= 8) {
            ChessPosition newPosition = new ChessPosition(newRow, newColumn);
            ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
            if (board.getPiece(newPosition) != null && pieceAtNewPosition.getTeamColor() != this.pieceColor) {
                if (newRow == 8 && this.pieceColor == WHITE) {
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                } else if ((newRow == 1 && this.pieceColor == BLACK)) {
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                } else {
                    legalPawnMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }

//    public Collection<ChessMove> addPawnMoves(ChessBoard board, ChessPosition myPosition) {
//        Collection<ChessMove> legalPawnMoves = new HashSet<>();
//        int rowDirection = (pieceColor == BLACK) ? -1 : 1;
//        int startRow = (pieceColor == BLACK) ? 7 : 2;
//
//        addPawnForwardMoves(board, myPosition, rowDirection, legalPawnMoves);
//
//        // Initial 2 row move
//        if (myPosition.getRow() == startRow) {
//            int tempCol = myPosition.getColumn();
//            int tempRow = myPosition.getRow();
//            if (board.getPiece(new ChessPosition(tempRow + rowDirection, tempCol)) == null) {
//                addPawnForwardMoves(board, myPosition, 2 * rowDirection, legalPawnMoves);
//            }
//        }
//
//        addPawnCaptureMoves(board, myPosition, rowDirection, -1, legalPawnMoves);
//        addPawnCaptureMoves(board, myPosition, rowDirection, 1, legalPawnMoves);
//
//        return legalPawnMoves;
//
//    }

    public Collection<ChessMove> addPawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalPawnMoves = new HashSet<>();

        int rowDirection = (pieceColor == BLACK) ? -1 : 1;
        int startRow = (pieceColor == BLACK) ? 7 : 2;

        addPawnForwardMoves(board, myPosition, rowDirection, legalPawnMoves);

        // initial 2 moves
        if (startRow == myPosition.getRow()) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            if (board.getPiece(new ChessPosition(row + rowDirection, col)) == null) {
                addPawnForwardMoves(board, myPosition, 2 * rowDirection, legalPawnMoves);

            }
        }

        addPawnCaptureMoves(board, myPosition, rowDirection, -1, legalPawnMoves);
        addPawnCaptureMoves(board, myPosition, rowDirection, 1, legalPawnMoves);

        return legalPawnMoves;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalMoves = new HashSet<>();
        System.out.println(myPosition.toString());

        switch (pieceType) {
            case KING:
                //up and to the right
                addAxialMoves(board, myPosition, 1, 1, legalMoves, true);
                // up and to the left
                addAxialMoves(board, myPosition, -1, 1, legalMoves, true);
                // down and to the left
                addAxialMoves(board, myPosition, -1, -1, legalMoves, true);
                // down and to the right
                addAxialMoves(board, myPosition, 1, -1, legalMoves, true);
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
                //up and to the right
                addAxialMoves(board, myPosition, 1, 1, legalMoves, false);
                // up and to the left
                addAxialMoves(board, myPosition, -1, 1, legalMoves, false);
                // down and to the left
                addAxialMoves(board, myPosition, -1, -1, legalMoves, false);
                // down and to the right
                addAxialMoves(board, myPosition, 1, -1, legalMoves, false);
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
                //up and to the right
                addAxialMoves(board, myPosition, 1, 1, legalMoves, false);
                // up and to the left
                addAxialMoves(board, myPosition, -1, 1, legalMoves, false);
                // down and to the left
                addAxialMoves(board, myPosition, -1, -1, legalMoves, false);
                // down and to the right
                addAxialMoves(board, myPosition, 1, -1, legalMoves, false);
                break;
            case KNIGHT:
                // 2 up and 1 to the right
                addAxialMoves(board, myPosition, 2, 1, legalMoves, true);
                // 1 up and 2 to the right
                addAxialMoves(board, myPosition, 1, 2, legalMoves, true);
                // 2 up and 1 to the left
                addAxialMoves(board, myPosition, 2, -1, legalMoves, true);
                // 1 up and 2 to the left
                addAxialMoves(board, myPosition, 1, -2, legalMoves, true);
                // 2 down and 1 to the right
                addAxialMoves(board, myPosition, -2, 1, legalMoves, true);
                // 1 down and 2 to the right
                addAxialMoves(board, myPosition, -1, 2, legalMoves, true);
                // 2 down and 1 to the left
                addAxialMoves(board, myPosition, -2, -1, legalMoves, true);
                // straight right
                addAxialMoves(board, myPosition, -1, -2, legalMoves, true);
                break;
            case ROOK:
                //straight up
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
