package game;

public class Checker {
    private ColorOfCheckers colorOfCheckers;
    private boolean king;

    protected Checker(ColorOfCheckers colorOfCheckers) {
        this.colorOfCheckers = colorOfCheckers;
        this.king = false;
    }

    public ColorOfCheckers getColorOfCheckers() {
        return this.colorOfCheckers;
    }

    public boolean isKing() {
        return this.king;
    }

    protected void makeKing() {
        this.king = true;
    }
}
