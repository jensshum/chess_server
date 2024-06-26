package chess;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Collection;
import java.util.HashSet;

import static chess.ChessPiece.PieceType.KING;
import static chess.ChessPiece.PieceType.PAWN;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamColor;
    private TeamColor teamTurn;
    private ChessBoard myBoard;

    public ChessGame() {
        myBoard = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }


    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> playableMoves = new HashSet<>();
        ChessPiece myPiece = myBoard.getPiece(startPosition);
        if (myPiece != null) {
            if (myPiece.getTeamColor() == TeamColor.BLACK) {
                Collection<ChessMove> potentialMoves = myPiece.pieceMoves(myBoard, startPosition);
                for (ChessMove move : potentialMoves) {
                    ChessBoard clonedBoard = myBoard.clone();
                    try {
                        makeClonedMove(move, clonedBoard);
                        if (!cloneIsInCheck(TeamColor.BLACK, clonedBoard)) {
                            playableMoves.add(move);
                        }
                    } catch (InvalidMoveException e) {
                        System.out.println("ERROR CAUGHT");
                        throw new RuntimeException(e);
                    }
                }
            }
            else  {
                Collection<ChessMove> potentialMoves = myPiece.pieceMoves(myBoard, startPosition);
                for (ChessMove move : potentialMoves) {
                    ChessBoard clonedBoard = myBoard.clone();
                    try {
                        makeClonedMove(move, clonedBoard);
                        if (!cloneIsInCheck(TeamColor.WHITE, clonedBoard)) {
                            playableMoves.add(move);
                        }
                    } catch (InvalidMoveException e) {
                        System.out.println("ERROR CAUGHT");
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return playableMoves;
    }

    public void makeClonedMove(ChessMove move, ChessBoard clonedBoard) throws InvalidMoveException {

        ChessPiece pieceToMove = clonedBoard.getPiece(move.getStartPosition());
        int endCol = move.getEndPosition().getColumn();
        int endRow = move.getEndPosition().getRow();

        if (pieceToMove == null) {
            throw new InvalidMoveException("No piece at start position");
        }

        ChessPosition endPosition = move.getEndPosition();
        if (move.getPromotionPiece() != null) {
            clonedBoard.addPiece(endPosition, new ChessPiece(teamTurn, move.getPromotionPiece()));
        }
        else {
            clonedBoard.addPiece(endPosition, pieceToMove);
        }
        clonedBoard.addPiece(move.getStartPosition(), null);

//        teamTurn = (teamTurn == teamColor.WHITE) ? teamColor.BLACK : teamColor.WHITE;

    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPiece pieceToMove = myBoard.getPiece(move.getStartPosition());
        int endCol = move.getEndPosition().getColumn();
        int endRow = move.getEndPosition().getRow();

        System.out.println(endRow + "," + endCol);

        if (pieceToMove == null) {
            throw new InvalidMoveException("No piece at start position");
        }
        if (pieceToMove.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("It's not your turn.");
        }
        if (!validMoves(move.getStartPosition()).contains(move)) {
            System.out.println(move.toString());
            System.out.println(myBoard.getPiece(move.getStartPosition()));
            throw new InvalidMoveException("Invalid move.");
        }

        ChessPosition endPosition = move.getEndPosition();
        if (move.getPromotionPiece() != null) {
            myBoard.addPiece(endPosition, new ChessPiece(teamTurn, move.getPromotionPiece()));
        }
        else {
            myBoard.addPiece(endPosition, pieceToMove);
        }
        myBoard.addPiece(move.getStartPosition(), null);

        teamTurn = (teamTurn == teamColor.WHITE) ? teamColor.BLACK : teamColor.WHITE;
    }

    public boolean cloneIsInCheck(TeamColor teamColor, ChessBoard clonedBoard) {
        ChessPosition whiteKingPosition = null;
        ChessPosition blackKingPosition = null;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition myPosition = new ChessPosition(i,j);
                ChessPiece myPiece = clonedBoard.getPiece(myPosition);
                if (myPiece != null && myPiece.getPieceType() == KING) {
                    if (myPiece.getTeamColor() == TeamColor.BLACK) {
                        blackKingPosition = myPosition;
                    }
                    else {
                        whiteKingPosition = myPosition;
                    }
                }
            }
        }
        // Go through each of the opponent's valid moves' endpositions. If any of them correspond with the position of the king, then the king is in check.

        if (teamColor == TeamColor.BLACK) {
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition myPosition = new ChessPosition(i,j);
                    ChessPiece myPiece = clonedBoard.getPiece(myPosition);
                    if (myPiece != null) {
                        if (myPiece.getTeamColor() == TeamColor.WHITE ) {
                            Collection<ChessMove> potentialMoves = myPiece.pieceMoves(clonedBoard, myPosition);
                            for (ChessMove move : potentialMoves) {
                                if (move.getEndPosition().equals(blackKingPosition)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            //CHECK IF WHITE IS IN CHECK
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition myPosition = new ChessPosition(i,j);
                    ChessPiece myPiece = clonedBoard.getPiece(myPosition);
                    if (myPiece != null) {
                        if (myPiece.getTeamColor() == TeamColor.BLACK ) {
                            Collection<ChessMove> potentialMoves = myPiece.pieceMoves(clonedBoard, myPosition);
                            for (ChessMove move : potentialMoves) {
                                if (move.getEndPosition().equals(whiteKingPosition)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }

        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition whiteKingPosition = null;
        ChessPosition blackKingPosition = null;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition myPosition = new ChessPosition(i,j);
                ChessPiece myPiece = myBoard.getPiece(myPosition);
                if (myPiece != null && myPiece.getPieceType() == KING) {
                    if (myPiece.getTeamColor() == TeamColor.BLACK) {
                        blackKingPosition = myPosition;
                    }
                    else {
                        whiteKingPosition = myPosition;
                    }
                }
            }
        }
        // Go through each of the opponent's valid moves' endpositions. If any of them correspond with the position of the king, then the king is in check.

        if (teamColor == TeamColor.BLACK) {
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition myPosition = new ChessPosition(i,j);
                    ChessPiece myPiece = myBoard.getPiece(myPosition);
                    if (myPiece != null) {
                        if (myPiece.getTeamColor() == TeamColor.WHITE ) {
                            Collection<ChessMove> potentialMoves = myPiece.pieceMoves(myBoard, myPosition);
                            for (ChessMove move : potentialMoves) {
                                if (move.getEndPosition().equals(blackKingPosition)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            //CHECK IF WHITE IS IN CHECK
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition myPosition = new ChessPosition(i,j);
                    ChessPiece myPiece = myBoard.getPiece(myPosition);
                    if (myPiece != null) {
                        if (myPiece.getTeamColor() == TeamColor.BLACK ) {
                            Collection<ChessMove> potentialMoves = myPiece.pieceMoves(myBoard, myPosition);
                            for (ChessMove move : potentialMoves) {
                                if (move.getEndPosition().equals(whiteKingPosition)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }

        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    // Checkmate is true when king is in check, cannot move, and no pieces can move to put it out of check.
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            // If the king is not in check, it's not checkmate
            return false;
        }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = myBoard.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> possibleMoves = piece.pieceMoves(myBoard, position);

                    for (ChessMove move : possibleMoves) {
                        ChessBoard clonedBoard = myBoard.clone();
                        try {
                            makeClonedMove(move, clonedBoard);
                            if (!cloneIsInCheck(teamColor, clonedBoard)) {
                                // Found a move that gets the king out of check
                                return false;
                            }
                        } catch (InvalidMoveException ignored) {
                            // Ignore invalid moves
                        }
                    }
                }
            }
        }

        // No moves found that can get the king out of check
        return true;
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */

    // Stalemate is true when the king is not in check and all remaining pieces have no valid moves.
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition myPosition = new ChessPosition(i,j);
                    ChessPiece myPiece = myBoard.getPiece(myPosition);
                    if (myPiece != null && myPiece.getTeamColor() == teamColor) {
                        if (validMoves(myPosition).size() > 0) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        myBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myBoard;
    }
}
