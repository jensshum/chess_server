package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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

    private void addDiagonalMoves(ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement, Collection<ChessMove> legalMoves){
        int tempRow = myPosition.getRow();
        int tempCol = myPosition.getColumn();

        tempRow += rowIncrement;
        tempCol += colIncrement;

        while (tempRow >= 1 && tempRow <= 8 && tempCol >= 1 && tempCol <= 8) {
            // Add checks if the position is occupied or if the move is illegal due to other game rules
            ChessPosition newPosition = new ChessPosition(tempRow, tempCol);
//            legalMoves.add(new ChessMove(myPosition, newPosition, null));
//            try {
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
//            }
//            catch (ArrayIndexOutOfBoundsException e) {
//                break;
//            }
            tempRow += rowIncrement;
            tempCol += colIncrement;
        }


    }

    private Collection<ChessMove> diagonalMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> legalMoves = new HashSet<ChessMove>();

        // Add moves in the "up and to the right" diagonal
        addDiagonalMoves(board, myPosition, 1, 1, legalMoves);

        // Add moves in the "down and to the left" diagonal
        addDiagonalMoves(board, myPosition, -1, -1, legalMoves);

        // Add moves in the "up and to the left" diagonal
        addDiagonalMoves(board, myPosition, 1, -1, legalMoves);

        // Add moves in the "down and to the right" diagonal
        addDiagonalMoves(board, myPosition, -1, 1, legalMoves);
//        //----------------------------------------------------------------------------------------------------------------
//        //up to the right: +1 col/row
//        int col = myPosition.getColumn();
//        int row = myPosition.getRow();
//        System.out.println(row + " " + col);
//        if (row > col) {
//            int tempRow = row;
//            int tempCol = col;
//            while (tempRow < 8) {
//                tempRow++;
//                tempCol++;
////                if (board.getPiece(new ChessPosition(tempRow, tempCol)) == null) {
//                    ChessMove newMove = new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), pieceType);
//                    legalMoves.add(newMove);
////                }
//            }
//        }
//        else if (row < col){
//            int tempRow = row;
//            int tempCol = col;
//            while (tempCol < 8){
//                tempRow++;
//                tempCol++;
////                if (board.getPiece(new ChessPosition(tempRow, tempCol)) == null) {
//                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), pieceType));
////                }
//            }
//        }
//        // row == col
//        else{
//            int tempRow = row;
//            int tempCol = col;
//            while (tempRow < 8){
//                tempRow++;
//                tempCol++;
////                if (board.getPiece(new ChessPosition(tempRow, tempCol)) == null) {
//                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), pieceType));
////                }
//            }
//        }
//
//        //-----------------------------------------------------------------------------------------------------------------
//        //down to the left: -1col -1row
//        if (row > col){
//            int tempRow = row;
//            int tempCol = col;
//            while (tempCol > 1) {
//                tempRow--;
//                tempCol--;
////                if (board.getPiece(new ChessPosition(tempRow, tempCol)) == null) {
//                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), pieceType));
////                }
//            }
//        }
//        else if (row < col) {
//            int tempRow = row;
//            int tempCol = col;
//            while (tempRow > 1) {
//                tempRow--;
//                tempCol--;
////                if (board.getPiece(new ChessPosition(tempRow, tempCol)) == null) {
//                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), pieceType));
////                }
//            }
//        }
//        //row == col
//        else {
//            int tempRow = row;
//            int tempCol = col;
//            while (tempRow > 1) {
//                tempRow--;
//                tempCol--;
////                if (board.getPiece(new ChessPosition(tempRow, tempCol)) == null) {
//                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), pieceType));
////                }
//            }
//        }
//        //down to the right: +1 col -1 row
//        //lower diagonal triangle
//        if (col + row < 9) {
//            int tempRow = row;
//            int tempCol = col;
//            while (tempRow > 1)
//            {
//                tempRow--;
//                tempCol++;
////                if (board.getPiece(new ChessPosition(tempRow, tempCol)) == null) {
//                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), pieceType));
////                }
//            }
//        }
//        //upper diagonal triangle
//        else if (col + row > 9) {
//            int tempRow = row;
//            int tempCol = col;
//            while (tempCol < 8)
//            {
//                tempRow--;
//                tempCol++;
////                if (board.getPiece(new ChessPosition(tempRow, tempCol)) == null) {
//                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), pieceType));
////                }
//            }
//        }
//        // On diagonal
//        else
//        {
//            int tempRow = row;
//            int tempCol = col;
//            while (tempCol < 8)
//            {
//                tempRow--;
//                tempCol++;
////                if (board.getPiece(new ChessPosition(tempRow, tempCol)) == null) {
//                legalMoves.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), pieceType));
////                }
//            }
//        }
//
//        //-----------------------------------------------------------------------------------------------------------------
//        // Up to the left -1col +1 row
//
//        if (col + row < 9) {
//            int tempRow = row;
//            int tempCol = col;
//            while (tempRow < 8)
//            {
//                tempRow++;
//                tempCol--;
////                if (board.getPiece(new ChessPosition(tempRow, tempCol)) == null) {
//                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), pieceType));
////                }
//            }
//        }
//        //upper diagonal triangle
//        else if (col + row > 9) {
//            int tempRow = row;
//            int tempCol = col;
//            while (tempCol > 1)
//            {
//                tempRow++;
//                tempCol--;
////                if (board.getPiece(new ChessPosition(tempRow, tempCol)) == null) {
//                    legalMoves.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), pieceType));
////                }
//            }
//        }
//        // On diagonal
//        else
//        {
//            int tempRow = row;
//            int tempCol = col;
//            while (tempCol > 1)
//            {
//                tempRow++;
//                tempCol--;
////                if (board.getPiece(new ChessPosition(tempRow, tempCol)) == null) {
//                legalMoves.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), pieceType));
////                }
//            }
//        }


        for (ChessMove i : legalMoves){
            System.out.print("{");
            System.out.print(i.getEndPosition().getRow());
            System.out.print(",");
            System.out.print(i.getEndPosition().getColumn());
            System.out.print("} ");
        }

        return legalMoves;
    }



    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalKingMoves = new HashSet<>();
        return null;
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalBishopMoves = diagonalMoves(board, myPosition);
        // Bishop can only move in a diagonal line, +-1 to row, +-1 to col.
        // If the row == 8||1 the column should not increase or decrease
        // If the column == 8||1 the row should not increase or decrease
//        for (int i=myPosition.getColumn()+1; i<9; i++){
//            for (int j=myPosition.getRow()+1; j<9; j++) {
//                legalMoves.add(new ChessMove(myPosition, new ChessPosition(j,i),pieceType));
//                break;
//
        return legalBishopMoves;

    }
}
