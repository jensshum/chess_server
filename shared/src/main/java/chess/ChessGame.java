package chess;

import java.util.Collection;
import java.util.HashSet;

import static chess.ChessPiece.PieceType.KING;

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
        Collection<ChessMove> validMoves = null;
//        System.out.println(myBoard.getPiece(startPosition));

        if (myBoard.getPiece(startPosition) != null) {
            validMoves = myBoard.getPiece(startPosition).pieceMoves(myBoard, startPosition);
        }

        Collection<ChessMove> possibleMoves = new HashSet<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition myPosition = new ChessPosition(i,j);
                ChessPiece myPiece = myBoard.getPiece(myPosition);
                if (myPiece != null && myPiece.getPieceType() != KING) {
                    if (myPiece.getTeamColor() == TeamColor.BLACK) {
                        //Get the moves for each piece
                        Collection<ChessMove> potentialMoves = myPiece.pieceMoves(myBoard, myPosition);
                        // Move the Piece to each potential position
                        for (ChessMove move : potentialMoves) {
                            ChessBoard clonedBoard = myBoard.clone();
                            try {
                                makeMove(move);
                                if (!isInCheck(TeamColor.BLACK)) {
                                    possibleMoves.add(move);
                                }
                                myBoard.addPiece(move.getEndPosition(), null);
                                myBoard.addPiece(move.getStartPosition(), myPiece);
                            } catch (InvalidMoveException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }

        // an invalid move is any one which leaves the king in check.
        // check each potential move of a piece. Move the piece to each position, and in each position check if the king is in check.
        // if the king is in check, do not add that move to the list of valid moves.

        //if the king is in check and cannot move and a piece is available to be moved in front of the king, then the only valid moves
        //will be that which protects the king from check.

        //Also limits the kings moves from placing itself in check.

        return validMoves;
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

        if (pieceToMove == null) {
            throw new InvalidMoveException("No piece at start position");
        }
        if (pieceToMove.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("It's not your turn.");
        }
        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Invalid move for the piece");
        }

        ChessPosition endPosition = move.getEndPosition();
        myBoard.addPiece(endPosition, pieceToMove);
        myBoard.addPiece(move.getStartPosition(), null);

        teamTurn = (teamTurn == teamColor.WHITE) ? teamColor.BLACK : teamColor.WHITE;

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
                        if (myPiece.getTeamColor() == TeamColor.WHITE && myPiece.getPieceType() != KING) {
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
                        if (myPiece.getTeamColor() == TeamColor.BLACK && myPiece.getPieceType() != KING) {
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
        int sleep = 4;
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
        throw new RuntimeException("Not implemented");
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
