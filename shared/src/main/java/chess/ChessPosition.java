package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int row;
    private int col;

    public ChessPosition(int row_, int col_) {
        row = row_;
        col = col_;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }
}

//var nameBuilder = new StringBuilder();
//nameBuilder.append("James");
//nameBuilder.append(" ");
//nameBuilder.append("Gosling");
//
//var name = nameBuilder.toString();
//System.out.println(name);