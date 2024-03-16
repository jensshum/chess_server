package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class ClientUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 2;
    private static final int LINE_WIDTH_IN_CHARS = 2;
    private int rowNum;

    private static final String EMPTY = "  ";
    private static Random rand = new Random();

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBoard(out);
    }

    private static void setGray(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawRow(PrintStream out, String color, int rowNum) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 1; col++ ) {
                if (color == "white") setWhite(out);
                else setBlack(out);
                if (row == SQUARE_SIZE_IN_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_CHARS - 1;
                    out.print(EMPTY.repeat(prefixLength));
                    out.print(SET_TEXT_COLOR_RED);
                    out.print("");
                    out.print(EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
                }

                if (col < BOARD_SIZE_IN_SQUARES -1 ) {
                    if (color == "white")
                    out.print(SET_BG_COLOR_BLACK);
                    else out.print(SET_BG_COLOR_WHITE);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
                }
            }
//            setBlack(out);

            out.print(SET_BG_COLOR_DARK_GREY);
        }
        drawGraySquare(out, rowNum);
        out.println();
    }

    private static void printHeaderText(PrintStream out, String headerChar) {
//        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(headerChar);

//        setGray(out);
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

    private static void drawHeaders(PrintStream out) {
        setGray(out);
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
            drawHeader(out, headers[col]);
            if (col < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS / 3));
            }
        }
        out.print(SET_BG_COLOR_DARK_GREY);
    }


    private static void drawVerticalLine(PrintStream out) {
//        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES *
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

    private static void drawBoard(PrintStream out) {
        drawGraySquare(out, -1);
        drawHeaders(out);
        drawGraySquare(out, -1);


        out.println();
//        drawRow(out);

        int rowNum = 8;
        for (int row = 0; row < BOARD_SIZE_IN_SQUARES / 2; row++) {
            drawGraySquare(out, rowNum);
            drawRow(out, "white", rowNum);
            rowNum--;
            drawGraySquare(out, rowNum);
            drawRow(out, "black", rowNum);
            rowNum--;
//            if (row < BOARD_SIZE_IN_SQUARES - 1) {
//
//            }
        }
        drawGraySquare(out, -1);
        drawHeaders(out);
        drawGraySquare(out, -1);

    }
}
