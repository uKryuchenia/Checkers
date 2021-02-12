package game;

public class Game {
    private Board board;
    private ColorOfCheckers playingColorOfCheckers;
    private ColorOfCheckers winner;
    private boolean gameOver;

    private int mustMoveRow, mustMoveCol;

    public Game() {
        board = new Board();
        playingColorOfCheckers = ColorOfCheckers.WhiteChecker;
        gameOver = false;
        mustMoveRow = -1;
        mustMoveCol = -1;
    }

    public void move(int sRow, int sCol, int dRow, int dCol) throws InvalidMoveException, InvalidInputException {       // source, destination
        if (board.getCell(sRow, sCol) == null)
            throw new InvalidInputException("There is no checker at [" + sRow + ", " + sCol + "]");
        if (board.getCell(sRow, sCol).getColorOfCheckers() != null && board.getCell(sRow, sCol).getColorOfCheckers() != playingColorOfCheckers)
            throw new InvalidMoveException("It's " + playingColorOfCheckers + "'s turn.");
        if (gameOver)
            throw new InvalidMoveException("The game is over.");
        if (!(mustMoveRow == -1 && mustMoveCol == -1) && (mustMoveRow != sRow || mustMoveCol != sCol))
            throw new InvalidMoveException("You must go with " + getCellName(mustMoveRow, mustMoveCol));

        ColorOfCheckers oppositeColorOfCheckers = playingColorOfCheckers == ColorOfCheckers.BlackChecker ? ColorOfCheckers.WhiteChecker : ColorOfCheckers.BlackChecker;
        boolean couldCapture = board.canCapture(sRow, sCol);

        board.move(sRow, sCol, dRow, dCol);

        if (!board.hasMoves(oppositeColorOfCheckers)) {
            winner = playingColorOfCheckers;
            gameOver = true;
        }
        if (couldCapture && board.canCapture(dRow, dCol)) {
            mustMoveRow = dRow;
            mustMoveCol = dCol;
        } else {
            playingColorOfCheckers = oppositeColorOfCheckers;
            mustMoveRow = -1;
            mustMoveCol = -1;
        }
    }

    private String getCellName(int row, int col) {                                                                      // throws ArrayIndexOutOfRangeException
        return String.valueOf("ABCDEFGH".charAt(col)) + (8 - row);
    }

    public boolean isOver() {
        return gameOver;
    }

    public ColorOfCheckers getWinner() {
        return winner;
    }

    public void draw() {
        gameOver = true;
    }

    public void resign() {
        gameOver = true;
        winner = playingColorOfCheckers == ColorOfCheckers.BlackChecker ? ColorOfCheckers.WhiteChecker : ColorOfCheckers.BlackChecker;
    }

    public boolean[][] getValidMoves(int sRow, int sCol) throws InvalidInputException {
        Checker cr = board.getCell(sRow, sCol);
        if (cr != null && cr.getColorOfCheckers() != playingColorOfCheckers)
            return new boolean[8][8];
        else if (!(mustMoveRow == -1 && mustMoveCol == -1) && (mustMoveRow != sRow || mustMoveCol != sCol))
            return new boolean[8][8];
        else
            return board.getValidMoves(sRow, sCol);
    }

    public Board getBoard() {
        return this.board;
    }
}
