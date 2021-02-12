package game;

public class Board {
    private Checker[][] board;

    protected Board() {
        board = new Checker[8][8];
        for (int i = 1; i < 8; i += 2) {
            board[0][i] = new Checker(ColorOfCheckers.BlackChecker);
            board[1][i - 1] = new Checker(ColorOfCheckers.BlackChecker);
            board[2][i] = new Checker(ColorOfCheckers.BlackChecker);
            board[5][i - 1] = new Checker(ColorOfCheckers.WhiteChecker);
            board[6][i] = new Checker(ColorOfCheckers.WhiteChecker);
            board[7][i - 1] = new Checker(ColorOfCheckers.WhiteChecker);
        }
    }

    protected void move(int sRow, int sCol, int dRow, int dCol) throws InvalidMoveException, InvalidInputException {    // source and destination row, column
        if (!isValidCell(sRow, sCol))
            throw new InvalidInputException("[" + sRow + ", " + sCol + "] are not valid cell coordinates.");
        if (!isValidCell(dRow, dCol))
            throw new InvalidInputException("[" + dRow + ", " + dCol + "] are not valid cell coordinates.");
        if (!isValidMove(sRow, sCol, dRow, dCol))
            throw new InvalidMoveException("The given move cannot be made.");

        Checker cr = board[sRow][sCol];

        if (canCapture(sRow, sCol)) {
            int victimRow = sRow - dRow < 0 ? dRow - 1 : dRow + 1;
            int victimCol = sCol - dCol < 0 ? dCol - 1 : dCol + 1;
            board[victimRow][victimCol] = null;
        }
        board[dRow][dCol] = cr;
        board[sRow][sCol] = null;

        if (cr.getColorOfCheckers() == ColorOfCheckers.BlackChecker && dRow == 7 || cr.getColorOfCheckers() == ColorOfCheckers.WhiteChecker && dRow == 0)
            cr.makeKing();
    }

    public boolean[][] getValidMoves(int sRow, int sCol) {
        boolean[][] map = new boolean[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j] == null && isValidMove(sRow, sCol, i, j))
                    map[i][j] = true;
                else
                    map[i][j] = false;
        return map;
    }

    protected boolean hasMoves(ColorOfCheckers colorOfCheckers) {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j] != null && board[i][j].getColorOfCheckers() == colorOfCheckers && hasMoves(i, j))
                    return true;
        return false;
    }

    private boolean hasMoves(int sRow, int sCol) {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (isValidMove(sRow, sCol, i, j))
                    return true;
        return false;
    }

    private boolean isValidMove(int sRow, int sCol, int dRow, int dCol) {
        Checker cr = board[sRow][sCol];

        if (cr == null)
            return false;                                                                                               // cell is empty

        if (board[dRow][dCol] != null)
            return false;                                                                                               // destination is occupied

        if (sRow == dRow || sCol == dCol)
            return false;                                                                                               // no diagonal

        int vd = dRow - sRow < 0 ? -1 : 1;                                                                              // vertical direction
        int hd = dCol - sCol < 0 ? -1 : 1;                                                                              // horizontal direction

        if (canCapture(sRow, sCol)) {
            int origRow = sRow;
            int origCol = sCol;

            if(cr.isKing()) {
                int n = 1;
                while (isValidCell(origRow + n * vd, origCol + n * hd)
                        && board[origRow + n * vd][origCol + n  * hd] == null) {
                    sRow = origRow + n * vd;
                    sCol = origCol + n * hd;
                    n++;
                }
            }
            if (isValidCell(sRow + 2 * vd, sCol + 2 * hd)
                    && isValidCell(sRow + 1 * vd, sCol + 1 * hd))
                if (board[sRow + 2 * vd][sCol + 2 * hd] == null && board[sRow + 1 * vd][sCol + 1 * hd] != null)
                    if (board[sRow + 1 * vd][sCol + 1 * hd].getColorOfCheckers() != cr.getColorOfCheckers())
                        if (sRow + 2 * vd == dRow && sCol + 2 * hd == dCol)
                            return true;                                                                                // valid capture

            return false;
        }

        if (cr.isKing()) {
            int n = 1;
            while (isValidCell(sRow + n * vd, sCol + n * hd)
                    && board[sRow + n * vd][sCol + n  * hd] == null) {
                if (sRow + n * vd == dRow && sCol + n * hd == dCol)
                    return true;                                                                                        // valid king move
                n++;
            }
            return false;
        }

        if (!cr.isKing() && cr.getColorOfCheckers() == ColorOfCheckers.BlackChecker && dRow == sRow + 1 && Math.abs(dCol - sCol) == 1)
            return true;                                                                                                // black move forward

        if (!cr.isKing() && cr.getColorOfCheckers() == ColorOfCheckers.WhiteChecker && dRow == sRow - 1 && Math.abs(dCol - sCol) == 1)
            return true;                                                                                                // white move forward

        return false;
    }

    protected boolean canCapture(int sRow, int sCol) {
        Checker cr = board[sRow][sCol];
        int origRow = sRow;
        int origCol = sCol;

        if (cr.isKing()) {                                                                                              // find most distant position
            int n = 1;
            while (isValidCell(origRow - n, origCol - n) && board[origRow - n][origCol - n] == null) {
                sRow = origRow - n;
                sCol = origCol - n;
                n++;
            }
        }
        if (isValidCell(sRow - 2, sCol - 2) && isValidCell(sRow - 1, sCol - 1))
            if (board[sRow - 2][sCol - 2] == null && board[sRow - 1][sCol - 1] != null)
                if (board[sRow - 1][sCol - 1].getColorOfCheckers() != cr.getColorOfCheckers())
                    return true;
        sRow = origRow;
        sCol = origCol;

        if (cr.isKing()) {
            int n = 1;
            while (isValidCell(origRow - n, origCol + n) && board[origRow - n][origCol + n] == null) {
                sRow = origRow - n;
                sCol = origCol + n;
                n++;
            }
        }
        if (isValidCell(sRow - 2, sCol + 2) && isValidCell(sRow - 1, sCol + 1))
            if (board[sRow - 2][sCol + 2] == null && board[sRow - 1][sCol + 1] != null)
                if (board[sRow - 1][sCol + 1].getColorOfCheckers() != cr.getColorOfCheckers())
                    return true;
        sRow = origRow;
        sCol = origCol;

        if (cr.isKing()) {
            int n = 1;
            while (isValidCell(origRow + n, origCol - n) && board[origRow + n][origCol - n] == null) {
                sRow = origRow + n;
                sCol = origCol - n;
                n++;
            }
        }
        if (isValidCell(sRow + 2, sCol - 2) && isValidCell(sRow + 1, sCol - 1))
            if (board[sRow + 2][sCol - 2] == null && board[sRow + 1][sCol - 1] != null)
                if (board[sRow + 1][sCol - 1].getColorOfCheckers() != cr.getColorOfCheckers())
                    return true;
        sRow = origRow;
        sCol = origCol;

        if(cr.isKing()) {
            int n = 1;
            while (isValidCell(origRow + n, origCol + n) && board[origRow + n][origCol + n] == null) {
                sRow = origRow + n;
                sCol = origCol + n;
                n++;
            }
        }
        if (isValidCell(sRow + 2, sCol + 2) && isValidCell(sRow + 1, sCol + 1))
            if (board[sRow + 2][sCol + 2] == null && board[sRow + 1][sCol + 1] != null)
                if (board[sRow + 1][sCol + 1].getColorOfCheckers() != cr.getColorOfCheckers())
                    return true;

        return false;
    }

    private boolean isValidCell(int row, int col) {
        return !(row < 0 || row > 7 || col < 0 || col > 7);
    }

    public Checker getCell(int row, int col) throws InvalidInputException {
        if (row < 0 || row > 7 || col < 0 || col > 7)
            throw new InvalidInputException("Cell coordinates are not valid.");
        return board[row][col];
    }
}
