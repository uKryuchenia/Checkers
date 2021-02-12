package ui;

import game.ColorOfCheckers;
import game.Game;
import game.InvalidInputException;
import game.InvalidMoveException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class GUI{
    JFrame jFrame = new JFrame();
    Game game = new Game();
    int moveCounter = 1;
    int xPos;
    int yPos;

    public GUI() {
        jFrame.setSize(1010, 1090);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        drawBoard();
        drawCheckers(game, this.jFrame.getGraphics());

        jFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                listenerMove(xToBoard(e.getX()), yToBoard(e.getY()));
                buttons(e.getX(), e.getY());
                moveCounter++;
            }
        });
    }

    private void buttons(int x, int y){
        if(game.isOver()) {
            if(x > 126 && x < 416 && y < 736 && y > 650 ) {
                game = new Game();
                drawBoard();
                drawCheckers(game, this.jFrame.getGraphics());
            }
            if(x > 582 && x < 872 && y < 736 && y > 650 ) {
                System.exit(0);
            }
        }

        if(x > 211 && x < 311 && y < 1073 && y > 1042) {
            game.resign();
            isOver();
        }

        if(x > 456 && x < 556 && y < 1073 && y > 1042) {
            game.draw();
            Image draw = new ImageIcon("res/draw.jpg").getImage();
            this.jFrame.getGraphics().drawImage(draw,5,30,null);
        }

        if(x > 698 && x < 798 && y < 1073 && y > 1042) {
            System.exit(0);
        }
    }


    private void listenerMove(int xPos1, int yPos1){
        if(moveCounter == 1) {
            xPos = xPos1;
            yPos = yPos1;
            move(yPos,xPos);
        }
        if(moveCounter == 2) {
            move(yPos, xPos, yPos1, xPos1);
            moveCounter = 0;
        }
    }

    private void drawBoard(){
        Graphics g = this.jFrame.getGraphics();
        Image board = new ImageIcon("res/board.jpg").getImage();
        g.drawImage(board,5,30,null);
    }

    private void move(int sRow, int sCol) {
        if(!game.isOver()) {
            try {
                game.getValidMoves(sRow, sCol);
                drawValidMoves(game, this.jFrame.getGraphics(),sRow, sCol);
                isOver();

             } catch (InvalidInputException e) {
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    private void move(int sRow, int sCol, int dRow, int dCol) {
        if(!game.isOver()) {
            try {
                game.move(sRow, sCol, dRow, dCol);
                drawBoard();
                drawCheckers(game, this.jFrame.getGraphics());
                isOver();

            } catch (InvalidInputException | InvalidMoveException e) {
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    private void isOver() {
        Image whiteWin = new ImageIcon("res/whiteWin.jpg").getImage();
        Image blackWin = new ImageIcon("res/blackWin.jpg").getImage();

        if(game.isOver()) {
            if (game.getWinner() == ColorOfCheckers.WhiteChecker){
                this.jFrame.getGraphics().drawImage(whiteWin, 5, 30, null);
            }

            if (game.getWinner() == ColorOfCheckers.BlackChecker){
                this.jFrame.getGraphics().drawImage(blackWin, 5, 30, null);
            }
        }
    }

    private static void drawCheckers(Game game, Graphics g) {
        Image white = new ImageIcon("res/white.png").getImage();
        Image black = new ImageIcon("res/black.png").getImage();
        Image whiteKing = new ImageIcon("res/whiteKing.png").getImage();
        Image blackKing = new ImageIcon("res/blackKing.png").getImage();

        try {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (game.getBoard().getCell(i, j) == null){}
                    else if (game.getBoard().getCell(i, j).getColorOfCheckers() == ColorOfCheckers.BlackChecker) {
                        if (game.getBoard().getCell(i, j).isKing())
                            g.drawImage(blackKing, (57 + j * 112), (81 + i * 112), null);
                        else
                            g.drawImage(black, (57 + j * 112), (81 + i * 112), null);
                    } else if (game.getBoard().getCell(i, j).getColorOfCheckers() == ColorOfCheckers.WhiteChecker) {
                        if (game.getBoard().getCell(i, j).isKing())
                            g.drawImage(whiteKing, (57 + j * 112), (81 + i * 112), null);
                        else
                            g.drawImage(white, (57 + j * 112), (82 + i * 112), null);
                    }
                }
            }

        } catch (InvalidInputException e) {
            System.out.println(e.getMessage() + "\n");
        }

    }

    private static void drawValidMoves(Game game, Graphics g, int sRow, int sCol) {
        Image validMoves = new ImageIcon("res/validMoves.png").getImage();
        try {
            boolean[][] map = game.getValidMoves(sRow, sCol);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (map[i][j])
                        g.drawImage(validMoves, (52 + j * 112), (76 + i * 113), null);
                }
            }

        } catch (InvalidInputException e) {
            System.out.println(e.getMessage() + "\n");
        }
    }


    private int xToBoard(int x){
        return (x-57)/112;
    }

    private int yToBoard(int y){
        return (y-81)/112;
    }
}
