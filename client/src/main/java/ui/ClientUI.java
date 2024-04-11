package ui;

import chess.*;
import model.GameData;

import javax.swing.text.Position;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;

import static ui.EscapeSequences.*;

public class ClientUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 2;
    private static final int LINE_WIDTH_IN_CHARS = 2;
    private int rowNum;
    private static final String EMPTY = " ";

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBoards(out);
    }

    private static void setGray(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setLightGreen(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
        out.print(SET_TEXT_COLOR_BLACK);
    }


    private static void drawRowLogic(PrintStream out, String squareColor, boolean reverse, int rowNum, ChessPiece[] pieces, ArrayList<ChessPosition> positionsToHighlight, int col, int pieceIndex) {

        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - 1;
        if (Objects.equals(squareColor, "white")) {
            if (!positionsToHighlight.isEmpty()) {
                for (ChessPosition position : positionsToHighlight) {
                    if (position.getColumn() == col+1) {
                        setLightGreen(out);
                        break;
                    }
                    else{
                        setWhite(out);
                    }
                }
            }
            else {
                setWhite(out);
            }
            if (pieces[pieceIndex] != null) {
                if (pieces[pieceIndex].getTeamColor() == ChessGame.TeamColor.BLACK) {
                    out.print(SET_TEXT_COLOR_RED);
                    out.print(EMPTY.repeat(prefixLength));
                    out.print(pieces[pieceIndex].pieceLetter());
                    out.print(EMPTY.repeat(suffixLength));
                } else {
                    out.print(SET_TEXT_COLOR_BLUE);
                    out.print(EMPTY.repeat(prefixLength));
                    out.print(pieces[pieceIndex].pieceLetter());
                    out.print(EMPTY.repeat(suffixLength));
                }
            }
            else {
                out.print(EMPTY.repeat(3));
            }
        }
        else {
            if (!positionsToHighlight.isEmpty()) {
                for (ChessPosition position : positionsToHighlight) {
                    if (position.getColumn() == col+1) {
                        out.print(SET_BG_COLOR_DARK_GREEN);
                        out.print(SET_TEXT_COLOR_BLACK);
                        break;
                    }
                    else{
                        setBlack(out);
                    }
                }
            }
            else {
                setBlack(out);
            }
            if (pieces[pieceIndex] != null) {
                if (pieces[pieceIndex].getTeamColor() == ChessGame.TeamColor.BLACK) {
                    out.print(SET_TEXT_COLOR_RED);
                    out.print(EMPTY.repeat(prefixLength));
                    out.print(pieces[pieceIndex].pieceLetter());
                    out.print(EMPTY.repeat(suffixLength));
                } else {
                    out.print(SET_TEXT_COLOR_BLUE);
                    out.print(EMPTY.repeat(prefixLength));
                    out.print(pieces[pieceIndex].pieceLetter());
                    out.print(EMPTY.repeat(suffixLength));
                }
            }
            else {
                out.print(EMPTY.repeat(3));
            }
        }
        out.print(SET_BG_COLOR_DARK_GREY);
    }
    private static void drawRow(PrintStream out, String squareColor, int rowNum, ChessPiece[] pieces, boolean reverse, ArrayList<ChessPosition> positionsToHighlight) {

        int pieceIndex = 0;
        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
            drawRowLogic(out, squareColor, reverse, rowNum, pieces, positionsToHighlight, col, pieceIndex);
            pieceIndex++;
            squareColor = (squareColor.equals("white")) ? "black" : "white";
        }
        drawGraySquare(out, rowNum);
        out.println();
    }

    private static void printHeaderText(PrintStream out, String headerChar) {
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(headerChar);
    }

    private static void drawHeader(PrintStream out, String headerChar) {
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerChar);
        out.print(" ");
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void drawHeaders(PrintStream out, boolean reverse) {
        setGray(out);
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        if (reverse) headers = new String[]{"h","g","f","e","d","c","b","a"};
        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
            drawHeader(out, headers[col]);
            if (col < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS / 3));
            }
        }
        out.print(SET_BG_COLOR_DARK_GREY);
    }

    private static void drawGraySquare(PrintStream out, int row){
        setGray(out);
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - 1;
        out.print(EMPTY.repeat(prefixLength));
        out.print(SET_TEXT_COLOR_BLACK);
        if (row >= 0) {
            out.print(row);
        }
        else {
            out.print(" ");
        }
        out.print(EMPTY.repeat(suffixLength));
        out.print(SET_BG_COLOR_DARK_GREY);
    }

    public static void drawBoard(PrintStream out, boolean reverse, ChessGame gameData, Collection<ChessMove> highlightMoves) {
        drawGraySquare(out, -1);
        drawHeaders(out, reverse);
        drawGraySquare(out, -1);
        out.println();
        int rowNum = reverse ? 1 : 8;
        String couleur = "white";
        ChessBoard board = gameData.getBoard();

        for (int row = 0; row < BOARD_SIZE_IN_SQUARES ; row++) {
            ArrayList<ChessPosition> validPositions = new ArrayList<>();
            if (highlightMoves != null) {
                for (ChessMove move : highlightMoves) {
                    if (reverse) {
                        if (row + 1 == move.getEndPosition().getRow()) {
                            validPositions.add(move.getEndPosition());
                        }
                    }
                    else {
                        if (8 - row == move.getEndPosition().getRow()) {
                            validPositions.add(move.getEndPosition());
                        }
                    }
                }
            }
            ChessPiece[] pieces = new ChessPiece[8];
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
                try {
                    if (reverse) {
                        pieces[col] = board.getPiece(new ChessPosition(row + 1, col + 1));
                    }
                    else {
                        pieces[col] = board.getPiece(new ChessPosition(8 - row, col + 1));
                    }
                }
                catch (NullPointerException e) {
                    pieces[col] = null;
                }
            }
            drawGraySquare(out, rowNum);
            drawRow(out, couleur, rowNum, pieces, reverse, validPositions);
            if (reverse) rowNum++; else rowNum--;
            couleur = (couleur.equals("white")) ? "black" : "white";
        }
        drawGraySquare(out, -1);
        drawHeaders(out, reverse);
        drawGraySquare(out, -1);
    }


    private static void drawBoards(PrintStream out) {
        ChessGame game = new ChessGame();
        drawBoard(out, true, game, null);
        out.println();
        setBlack(out);
        out.println();
        drawBoard(out, false, game, null);
    }
}
