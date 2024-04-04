package chess;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;


public class ChessPiece implements Cloneable{

    private PieceType pieceType;
    private ChessGame.TeamColor pieceColor;
//    private ChessGame


    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
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


    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }


    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    public PieceType getPieceType() {
        return pieceType;
    }



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

    public String pieceLetter() {
        switch(pieceType) {
            case KING:
                return "K";
            case QUEEN:
                return "Q";
            case BISHOP:
                return "B";
            case ROOK:
                return "R";
            case KNIGHT:
                return "N";
            case PAWN:
                return "P";
            case null:
                return "";
            default:
                return "";
        }
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
                legalMoves = pawnMoves(board, myPosition);
                break;
        }

        return legalMoves;

    }
}
