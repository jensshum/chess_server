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

    private PieceType pieceType;
    private ChessGame.TeamColor pieceColor;
//    private ChessGame


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceType == that.pieceType && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, pieceColor);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        pieceType = type;
        this.pieceColor = pieceColor;
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
     *
     */

    private void addAxialMoves(ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement, Collection<ChessMove> legalMoves, boolean singleMove){
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();

        tempRow += rowIncrement;
        tempCol += colIncrement;

        if (!singleMove) {
            while (tempRow >= 1 && tempRow <= 8 && tempCol >= 1 && tempCol <= 8) {
                // Add checks if the position is occupied or if the move is illegal due to other game rules
                ChessPosition newPosition = new ChessPosition(tempRow, tempCol);
                if (board.getPiece(newPosition) == null) {
                    legalMoves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    // If the position is occupied by an opposite color piece, it's a valid move,
                    // but you can't move further in this diagonal.
                    if (board.getPiece(newPosition).getTeamColor() != this.pieceColor) {
                        legalMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
                tempRow += rowIncrement;
                tempCol += colIncrement;
            }
        }
        else
        {
            if (tempRow >= 1 && tempRow <= 8 && tempCol >= 1 && tempCol <= 8) {
                ChessPosition newPosition = new ChessPosition(tempRow, tempCol);
                if (board.getPiece(newPosition) == null) {
                    legalMoves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    // If the position is occupied by an opposite color piece, it's a valid move,
                    if (board.getPiece(newPosition).getTeamColor() != this.pieceColor) {
                        legalMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }
    }


    private void addPawnMoves(ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement, Collection<ChessMove> legalMoves) {
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();

        tempRow += rowIncrement;
        tempCol += colIncrement;
        if (tempRow >= 1 && tempRow <= 8 && tempCol >= 1 && tempCol <= 8) {
            ChessPosition newPosition = new ChessPosition(tempRow, tempCol);
            if (board.getPiece(newPosition) == null) {
                legalMoves.add(new ChessMove(myPosition, newPosition, null));
            }
            else {
                // If the position is occupied by an opposite color piece, it's a valid move,
                if (board.getPiece(newPosition).getTeamColor() != this.pieceColor) {
                    legalMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalBishopMoves = new HashSet<>();
        // Add moves in the "up and to the right" diagonal
        addAxialMoves(board, myPosition, 1, 1, legalBishopMoves, false);
        // Add moves in the "down and to the left" diagonal
        addAxialMoves(board, myPosition, -1, -1, legalBishopMoves, false);
        // Add moves in the "up and to the left" diagonal
        addAxialMoves(board, myPosition, 1, -1, legalBishopMoves, false);
        // Add moves in the "down and to the right" diagonal
        addAxialMoves(board, myPosition, -1, 1, legalBishopMoves, false);
        return legalBishopMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalRookMoves = new HashSet<>();
        // Add moves straight up
        addAxialMoves(board, myPosition, 1, 0, legalRookMoves, false);
        // Add moves to the right
        addAxialMoves(board, myPosition, 0, 1, legalRookMoves, false);
        // Add moves straight down
        addAxialMoves(board, myPosition, -1, 0, legalRookMoves, false);
        // Add moves to the left
        addAxialMoves(board, myPosition, 0, -1, legalRookMoves, false);

        return legalRookMoves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalKingMoves = new HashSet<>();
        // Add moves in the "up and to the right" diagonal
        addAxialMoves(board, myPosition, 1, 1, legalKingMoves, true);
        // Add moves straight up
        addAxialMoves(board, myPosition, 1, 0, legalKingMoves, true);
        // Add moves to the right
        addAxialMoves(board, myPosition, 0, 1, legalKingMoves, true);
        // Add moves in the "down and to the left" diagonal
        addAxialMoves(board, myPosition, -1, -1, legalKingMoves, true);
        // Add moves straight down
        addAxialMoves(board, myPosition, -1, 0, legalKingMoves, true);
        // Add moves to the left
        addAxialMoves(board, myPosition, 0, -1, legalKingMoves, true);
        // Add moves in the "up and to the left" diagonal
        addAxialMoves(board, myPosition, 1, -1, legalKingMoves, true);
        // Add moves in the "down and to the right" diagonal
        addAxialMoves(board, myPosition, -1, 1, legalKingMoves, true);
//
        return legalKingMoves;
    }


    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalQueenMoves = new HashSet<>();
        // Add moves in the "up and to the right" diagonal
        addAxialMoves(board, myPosition, 1, 1, legalQueenMoves, false);
        // Add moves straight up
        addAxialMoves(board, myPosition, 1, 0, legalQueenMoves, false);
        // Add moves to the right
        addAxialMoves(board, myPosition, 0, 1, legalQueenMoves, false);
        // Add moves in the "down and to the left" diagonal
        addAxialMoves(board, myPosition, -1, -1, legalQueenMoves, false);
        // Add moves straight down
        addAxialMoves(board, myPosition, -1, 0, legalQueenMoves, false);
        // Add moves to the left
        addAxialMoves(board, myPosition, 0, -1, legalQueenMoves, false);
        // Add moves in the "up and to the left" diagonal
        addAxialMoves(board, myPosition, 1, -1, legalQueenMoves, false);
        // Add moves in the "down and to the right" diagonal
        addAxialMoves(board, myPosition, -1, 1, legalQueenMoves, false);

        return legalQueenMoves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalKnightMoves = new HashSet<>();
        //A knight will always cover at least three squares.
        // up two, one to the left
        addAxialMoves(board, myPosition, 2, -1, legalKnightMoves, true);
        // up two, one to the right
        addAxialMoves(board, myPosition, 2, 1, legalKnightMoves, true);
        // down two, one to the left
        addAxialMoves(board, myPosition, -2, -1, legalKnightMoves, true);
        // down two, one to the right
        addAxialMoves(board, myPosition, -2, 1, legalKnightMoves, true);
        // up one, two to the left
        addAxialMoves(board, myPosition, 1, -2, legalKnightMoves, true);
        // up one, two to the right
        addAxialMoves(board, myPosition, 1, 2, legalKnightMoves, true);
        // down one, two to the left
        addAxialMoves(board, myPosition, -1, -2, legalKnightMoves, true);
        // down one, two to the right
        addAxialMoves(board, myPosition, -1, 2, legalKnightMoves, true);

        return legalKnightMoves;

    }

    private void addPawnForwardMoves(ChessBoard board, ChessPosition myPosition, int rowIncrement, Collection<ChessMove> legalMoves) {
        int newRow = myPosition.getRow() + rowIncrement;
        if (newRow >= 1 && newRow <= 8) {
            ChessPosition newPosition = new ChessPosition(newRow, myPosition.getColumn());
            if (board.getPiece(newPosition) == null) {
                if (newRow == 1 && pieceColor == BLACK) {
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));

                }
                else if (newRow == 8 && pieceColor == WHITE){
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                }
                else{
                    legalMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }

    private void addPawnCaptureMoves(ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement, Collection<ChessMove> legalMoves) {
        int newRow = myPosition.getRow() + rowIncrement;
        int newCol = myPosition.getColumn() + colIncrement;
        if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
            if (pieceAtNewPosition != null && pieceAtNewPosition.getTeamColor() != this.pieceColor) { // Capture is valid if there's an opposite color piece
                if (pieceColor == BLACK && newRow == 1) {
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                }
                else if (pieceColor == WHITE && newRow == 8) {
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                }
                else {
                    legalMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

        }
    }

//    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
//        Collection<ChessMove> legalPawnMoves = new HashSet<>();
//        int col = myPosition.getColumn();
//        int row = myPosition.getRow();
//        if (pieceColor == BLACK) {
//            addPawnMoves(board, myPosition, -1, 0, legalPawnMoves);
//            // First move can go 2 squares
//            if (myPosition.getRow() == 7 ) {
//                addPawnMoves(board, myPosition, -2, 0, legalPawnMoves);
//            }
//            // piece down to the left
//            if (board.getPiece(new ChessPosition(row - 1, col -1)) != null){
//                addPawnMoves(board, myPosition, -1, -1, legalPawnMoves);
//            }
//            //piece down to the right
//            else if (board.getPiece(new ChessPosition(row - 1, col -1)) != null){
//                addPawnMoves(board, myPosition, -1, 1, legalPawnMoves);
//            }
//        }
//        //WHITE
//        else{
//            addPawnMoves(board, myPosition, 1, 0, legalPawnMoves);
//            if (myPosition.getRow()== 2) {
//                addPawnMoves(board, myPosition, 2, 0, legalPawnMoves);
//            }
//            // piece up to the left
//            if (board.getPiece(new ChessPosition(1, -1)) != null) {
//                addPawnMoves(board, myPosition, 1, -1, legalPawnMoves);
//            }
//            // piece up to the right
//            if (board.getPiece(new ChessPosition(1, 1)) != null) {
//                addPawnMoves(board, myPosition, 1, 1, legalPawnMoves);
//            }
//        }
//
//        for (ChessMove i : legalPawnMoves){
//            System.out.print("{");
//            System.out.print(i.getEndPosition().getRow());
//            System.out.print(",");
//            System.out.print(i.getEndPosition().getColumn());
//            System.out.print("} ");
//        }
//
//        return legalPawnMoves;
//    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalPawnMoves = new HashSet<>();
        int rowDirection = (pieceColor == BLACK) ? -1 : 1; // BLACK moves down, WHITE moves up
        int startRow = (pieceColor == BLACK) ? 7 : 2; // Initial row for pawns of each color

        // Forward moves
        addPawnForwardMoves(board, myPosition, rowDirection, legalPawnMoves);

        // Initial two-square move
        if (myPosition.getRow() == startRow) {
            int col = myPosition.getColumn();
            int row = myPosition.getRow();
            if (board.getPiece(new ChessPosition(rowDirection + row, col)) == null) {
                addPawnForwardMoves(board, myPosition, 2 * rowDirection, legalPawnMoves);
            }
        }

        // Capture moves
        addPawnCaptureMoves(board, myPosition, rowDirection, -1, legalPawnMoves); // Capture left
        addPawnCaptureMoves(board, myPosition, rowDirection, 1, legalPawnMoves);  // Capture right

        return legalPawnMoves;
    }


    @Override
    public String toString() {
        return "ChessPiece{" +
               "pieceType=" + pieceType +
               ", pieceColor=" + pieceColor +
               '}';
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalMoves = null;
        switch (pieceType) {
            case KING:
                legalMoves = kingMoves(board, myPosition);
                break;
            case QUEEN:
                legalMoves = queenMoves(board, myPosition);
                break;
            case BISHOP:
                legalMoves = bishopMoves(board, myPosition);
                break;
            case KNIGHT:
                legalMoves = knightMoves(board, myPosition);
                break;
            case ROOK:
                legalMoves = rookMoves(board, myPosition);
                break;
            case PAWN:
//                System.out.println(board.getPiece(myPosition));
//                System.out.println(myPosition);
                legalMoves = pawnMoves(board, myPosition);
                break;
        }
        // Bishop can only move in a diagonal line, +-1 to row, +-1 to col.
        // If the row == 8||1 the column should not increase or decrease
        // If the column == 8||1 the row should not increase or decrease
//        for (int i=myPosition.getColumn()+1; i<9; i++){
//            for (int j=myPosition.getRow()+1; j<9; j++) {
//                legalMoves.add(new ChessMove(myPosition, new ChessPosition(j,i),pieceType));
//                break;
//
        return legalMoves;

    }
}
