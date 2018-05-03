/**
 * 
 */
package pieces;

import java.io.Serializable;

import chess.Player;
import chessboard.ChessTile;
import chessboard.Chessboard;

/**
 * @author Perry
 *
 */
public class Knight extends Piece implements Serializable{
    
    public Knight(Player player, int x, int y, Chessboard chessboard) {
        super(player, x, y, chessboard);
        
        board.getSquares()[x][y].setProperties(player, getUnicode());
        board.getSquares()[x][y].setPiece(this);
    }
    
    @Override
    public void drawAttackedSquares() {
        drawValidMove();
    }
    
    @Override
    public void drawValidMove() {
        if (x + 2 < SIZE && y + 1 < SIZE) {
            if (board.getSquare(x + 2, y + 1).getPiece() != null 
                    && board.getSquare(x + 2, y + 1).getPiece().getPlayer() == this.player) {
                
            }
            else {
                board.getSquare(x + 2, y + 1).drawOutline();
            }
        }
        
        if (x + 2 < SIZE && y - 1 >= 0 ) {
            if (board.getSquare(x + 2, y - 1).getPiece() != null 
                    && board.getSquare(x + 2, y - 1).getPiece().getPlayer() == this.player) {
                
            }
            else {
                board.getSquare(x + 2, y - 1).drawOutline();
            }
        }
        
        if (x - 2 >= 0 && y + 1 < SIZE) {
            if (board.getSquare(x - 2, y + 1).getPiece() != null 
                    && board.getSquare(x - 2, y + 1).getPiece().getPlayer() == this.player) {
                
            }
            else {
                board.getSquare(x - 2, y + 1).drawOutline();
            }
        }
        
        if (x - 2 >= 0 && y - 1>= 0) {
            if (board.getSquare(x - 2, y - 1).getPiece() != null 
                    && board.getSquare(x - 2, y - 1).getPiece().getPlayer() == this.player) {
                
            }
            else {
                board.getSquare(x - 2, y - 1).drawOutline();
            }
        }
        
        if (x + 1 < SIZE && y + 2 < SIZE) {
            if (board.getSquare(x + 1, y + 2).getPiece() != null 
                    && board.getSquare(x + 1, y + 2).getPiece().getPlayer() == this.player) {
                
            }
            else {
                board.getSquare(x + 1, y + 2).drawOutline();
            }
        }
        
        if (x + 1 < SIZE && y - 2 >= 0) {
            if (board.getSquare(x + 1, y - 2).getPiece() != null 
                    && board.getSquare(x + 1, y - 2).getPiece().getPlayer() == this.player) {
                
            }
            else {
                board.getSquare(x + 1, y - 2).drawOutline();
            }
        }
        
        if (x - 1 >= 0 && y + 2 < SIZE) {
            if (board.getSquare(x - 1, y + 2).getPiece() != null 
                    && board.getSquare(x - 1, y + 2).getPiece().getPlayer() == this.player) {
                
            }
            else {
                board.getSquare(x - 1, y + 2).drawOutline();
            }
        }
        
        if (x - 1 >= 0 && y - 2 >= 0) {
            if (board.getSquare(x - 1, y - 2).getPiece() != null 
                    && board.getSquare(x - 1, y - 2).getPiece().getPlayer() == this.player) {
                
            }
            else {
                board.getSquare(x - 1, y - 2).drawOutline();
            }
        }
    }
    
    @Override
    public void move(ChessTile square) {
        
        //Prints out test messages
        System.out.println("The coordinate to move is: " + square.getX() + ", " +  square.getY());
        System.out.println("The old x and y: " + this.getXPos() + ", " + this.getYPos());
        
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
        System.out.println("new x and y coor of knight: " +  this.getXPos() + this.getYPos()); //using simple way
    }
   

    public String getUnicode() {
        return "\u2658";
    }
}
