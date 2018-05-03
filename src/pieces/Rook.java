package pieces;

import java.io.Serializable;

import chess.Player;
import chessboard.ChessTile;
import chessboard.Chessboard;

public class Rook extends Piece implements Serializable {
    
    private boolean hasMoved = false;
    
    public Rook(Player player, int x, int y, Chessboard chessboard) {
        
        super(player, x, y, chessboard);
        //Sets the proper icon and color based on player
        board.getSquares()[x][y].setProperties(player, getUnicode());
        board.getSquares()[x][y].setPiece(this);
    }
    
    @Override
    public void drawAttackedSquares() {
        drawValidMove();
    }
    
    @Override
    public void drawValidMove() {
        
        //Checks downwards movement
        for(int i = this.getXPos(); i < SIZE; i++) {
            //checks if a square is taken by a piece (BUT NOT THIS PIECE ITSELF), then checks if its a friendly or enemy piece
            if (board.getSquare(i, y).getPiece() != null && board.getSquare(i, y).getPiece() != this) {
                if (board.getSquare(i, y).getPiece().getPlayer() != this.getPlayer()) {  //if enemy piece, draws outline on it before breaking
                    board.getSquare(i, y).drawOutline();
                    break;
                }
                else { //if friendly piece, no outline, just break right away
                    //System.out.println("There is a friendly piece blocking at " + i + y +", breaking.");
                    break;
                }
            }
            board.getSquare(i, y).drawOutline();
        }
        
        //Checks upwards movement
        for(int i = this.getXPos(); i >= 0; i--) {
            if (board.getSquare(i, y).getPiece() != null && board.getSquare(i, y).getPiece() != this) {
                if (board.getSquare(i, y).getPiece().getPlayer() != this.getPlayer()) {
                    board.getSquare(i, y).drawOutline();
                    break;
                }
                else {
                    //System.out.println("There is a friendly piece blocking at " + i + y +", breaking.");
                    break;
                }
            }
            board.getSquare(i, y).drawOutline();
        }
        
        //Checks rightwards movement
        for(int j = this.getYPos(); j < SIZE; j++) {
            if (board.getSquare(x, j).getPiece() != null && board.getSquare(x, j).getPiece() != this){
                if (board.getSquare(x, j).getPiece().getPlayer() != this.getPlayer()) {
                    board.getSquare(x, j).drawOutline();
                    break;
                }
                else {
                    //System.out.println("There is a piece blocking at " + x + j +", breaking.");
                    break;
                }
            }
            board.getSquare(x, j).drawOutline();
        }
        
        //checks leftwards horizontal movement
        for(int j = this.getYPos(); j >= 0; j--) {
            if (board.getSquare(x, j).getPiece() != null && board.getSquare(x, j).getPiece() != this){
                if (board.getSquare(x, j).getPiece().getPlayer() != this.getPlayer()) {
                    board.getSquare(x, j).drawOutline();
                    break;
                }
                else {
                    //System.out.println("There is a piece blocking at " + x + j +", breaking.");
                    break;
                }
            }
            board.getSquare(x, j).drawOutline();
        }
    }
    
    @Override
    public void move(ChessTile square) {
        
        //Prints out test messages
        System.out.println("The coordinate to move is: " + square.getX() + ", " + square.getY());
        System.out.println("old x and y: " + this.getXPos() + ", " + this.getYPos());
        
        destroy(this.getXPos(), this.getYPos()); //removes this piece from the current square
        checkCaptured(square.getX(), square.getY());
        
        //Updates the position of the piece (DOES NOT PLACE THIS piece TO THE NEW SQUARE)
        //note that this is a method of the piece, not the square itself!
        this.setXPos(square.getX()); 
        this.setYPos(square.getY());
        
        //Places this piece to the new square; note that this is the method of the square!
        square.setPiece(this);
        square.setProperties(player, getUnicode());
        hasMoved = true;
        
        //Prints out confirmation of move
        System.out.println("new x and y coor of rook: " +  this.getXPos() + this.getYPos()); //using simple way
    }

    public boolean getHasMoved() {
        return hasMoved;
    }
    
    public String getUnicode() {
        return "\u2656";
    }
}
