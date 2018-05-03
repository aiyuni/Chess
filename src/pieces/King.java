/**
 * 
 */
package pieces;

import java.io.Serializable;

import chess.Player;
import chessboard.ChessTile;
import chessboard.Chessboard;

/**Creates the King
 * @author Perry
 *
 */
public class King extends Piece implements Serializable{
    
    //private int playerID; dont need these due to polymorphism
   // private int x;
    //private int y;
    //Chessboard board; 
    
    //private boolean hasMoved = false;
    
    public King(Player player, int x, int y, Chessboard chessboard) {
        super(player, x, y, chessboard);
        
        //Sets the proper icon and color based on player
        board.getSquares()[x][y].setProperties(player, getUnicode());
        board.getSquares()[x][y].setPiece(this);
    }
    
    
    /**
     * This method outlines the squares this King is attacking. 
     * The King can attack 1 square in all directions.
     */
    @Override
    public void drawAttackedSquares() {
        //Checks the square on top of the King
        if ((x + 1 < SIZE) && board.getSquare(x + 1, y).getPiece() != null) {
            if (board.getSquare(this.getXPos() + 1, this.getYPos()).getPiece().getPlayer() != this.getPlayer()) {
                board.getSquare(this.getXPos() + 1, this.getYPos()).drawOutline();
            }    
        }
        else if (x + 1 < SIZE){
            board.getSquare(this.getXPos() + 1, this.getYPos()).drawOutline();
        }
        
        //Checks the square below the King
        if ((x - 1 >= 0) && board.getSquare(this.getXPos() - 1, this.getYPos()).getPiece() != null) {
            if (board.getSquare(this.getXPos() - 1, this.getYPos()).getPiece().getPlayer() != this.getPlayer()) {
                board.getSquare(this.getXPos() - 1, this.getYPos()).drawOutline();
            }    
        }
        else if (x - 1 >= 0){
            board.getSquare(this.getXPos() - 1, this.getYPos()).drawOutline();
        }
        
        //Checks the square to the right of the King
        if ((y + 1 < SIZE) && board.getSquare(this.getXPos(), this.getYPos() + 1).getPiece() != null) {
            if (board.getSquare(this.getXPos(), this.getYPos() + 1).getPiece().getPlayer() != this.getPlayer()) {
                board.getSquare(this.getXPos(), this.getYPos() + 1).drawOutline();
            }    
        }
        else if (y + 1 < SIZE) {
            board.getSquare(this.getXPos(), this.getYPos() + 1).drawOutline();
        }
        
        //Checks the square to the left of the King
        if ((y-1 >=0) && board.getSquare(this.getXPos(), this.getYPos() - 1).getPiece() != null) {
            if (board.getSquare(this.getXPos(), this.getYPos() - 1).getPiece().getPlayer() != this.getPlayer()) {
                board.getSquare(this.getXPos(), this.getYPos() - 1).drawOutline();
            }    
        }
        else if (y-1 >=0){
            board.getSquare(this.getXPos(), this.getYPos() - 1).drawOutline();
        }
        
        //Checks the square to the northwest
        if ((y-1 >=0) && (x-1>=0) && board.getSquare(this.getXPos() - 1, this.getYPos() - 1).getPiece() != null) {
            if (board.getSquare(this.getXPos() - 1, this.getYPos() - 1).getPiece().getPlayer() != this.getPlayer()) {
                board.getSquare(this.getXPos() - 1, this.getYPos() - 1).drawOutline();
            }    
        }
        else if (y-1 >=0 && (x-1 >= 0)){
            board.getSquare(this.getXPos() - 1, this.getYPos() - 1).drawOutline();
        }
        
        //Checks the square to the southwest
        if ((y-1 >=0) && (x + 1 < SIZE) && board.getSquare(this.getXPos() + 1, this.getYPos() - 1).getPiece() != null) {
            if (board.getSquare(this.getXPos() + 1, this.getYPos() - 1).getPiece().getPlayer() != this.getPlayer()) {
                board.getSquare(this.getXPos() + 1, this.getYPos() - 1).drawOutline();
            }    
        }
        else if (y-1 >=0 && (x + 1 < SIZE)){
            board.getSquare(this.getXPos() + 1, this.getYPos() - 1).drawOutline();
        }
        
        //Checks the square to the northeast
        if ((y+1 < SIZE) && (x-1>=0) && board.getSquare(this.getXPos() - 1, this.getYPos() + 1).getPiece() != null) {
            if (board.getSquare(this.getXPos() - 1, this.getYPos() + 1).getPiece().getPlayer() != this.getPlayer()) {
                board.getSquare(this.getXPos() - 1, this.getYPos() + 1).drawOutline();
            }    
        }
        else if (y+1 < SIZE && (x-1 >= 0)){
            board.getSquare(this.getXPos() - 1, this.getYPos() + 1).drawOutline();
        }
        
        //Checks the square to the southwest
        if ((y + 1 <SIZE) && (x + 1 < SIZE) && board.getSquare(this.getXPos() + 1, this.getYPos() + 1).getPiece() != null) {
            if (board.getSquare(this.getXPos() + 1, this.getYPos() + 1).getPiece().getPlayer() != this.getPlayer()) {
                board.getSquare(this.getXPos() + 1, this.getYPos() + 1).drawOutline();
            }    
        }
        else if (y+1 < SIZE && (x + 1 < SIZE)){
            board.getSquare(this.getXPos() + 1, this.getYPos() + 1).drawOutline();
        }
    }
    
    /**
     * Outlines the valid moves for this King. This simple method checks for castling, then calls drawValidMove.
     */
    @Override
    public void drawValidMove() { //Casting does not attack a square, so this separates castling from drawAttackedSquares
        
        //Checks for castle
        if (!hasMoved && !this.isThreatened()) {
            board.checkCastle(this.getPlayer());
        } 
        
        drawAttackedSquares();
    }
    
    @Override
    public void move(ChessTile square) {
        
        oldHasMovedBefore = hasMoved;
        
        //checking for castling validation is done in drawValidMoves, but actual castling is implemented here
        if (square.getPiece() != null && square.getPiece().getPlayer() == this.getPlayer()) {
            
            Piece castlingRook = square.getPiece();
           
            //If Queenside castle for white
            if (square.getX() == 7 && square.getY() == 0 ) {
                
                destroy(this.getXPos(), this.getYPos()); //destroys the king
                destroy(7,0); //destroys the rook
                
                //Sets the king to the correct spot
                this.setYPos(this.getYPos() - 2);
                board.getSquare(this.getXPos(), this.getYPos()).setPiece(this);
                board.getSquare(this.getXPos(), this.getYPos()).setProperties(player, getUnicode());
                
                //Sets the rook to the correct spot
                castlingRook.setYPos(this.getYPos() + 1); //this.getYPos refers to the updated king's Y pos
                board.getSquare(7, this.getYPos() + 1).setPiece(castlingRook);
                board.getSquare(7, 3).setProperties(player, castlingRook.getUnicode());
            }
            
            //If Kingside castle for white
            if (square.getX() == 7 && square.getY() == 7) {
                destroy(this.getXPos(), this.getYPos()); //destroys the king
                destroy(7,7); //destroys the rook
                
                //Sets the king to the correct spot
                this.setYPos(this.getYPos() + 2);
                board.getSquare(this.getXPos(), this.getYPos()).setPiece(this);
                board.getSquare(this.getXPos(), this.getYPos()).setProperties(player, getUnicode());
                
                //Sets the rook to the correct spot
                castlingRook.setYPos(this.getYPos() - 1); //this.getYPos refers to the updated king's Y pos
                board.getSquare(7, this.getYPos() -+ 1).setPiece(castlingRook);
                board.getSquare(7, 5).setProperties(player, castlingRook.getUnicode());
            }
            
            //If Queenside castle for black
            if (square.getX() == 0 && square.getY() == 0) {
                destroy(this.getXPos(), this.getYPos()); //destroys the king
                destroy(0,0); //destroys the rook
                
                //Sets the king to the correct spot
                this.setYPos(this.getYPos() - 2);
                board.getSquare(this.getXPos(), this.getYPos()).setPiece(this);
                board.getSquare(this.getXPos(), this.getYPos()).setProperties(player, getUnicode());
                
                //Sets the rook to the correct spot
                castlingRook.setYPos(this.getYPos() + 1); //this.getYPos refers to the updated king's Y pos
                board.getSquare(0, this.getYPos() + 1).setPiece(castlingRook);
                board.getSquare(0, 3).setProperties(player, castlingRook.getUnicode());    
            }
            
            //If Kingside castle for black
            if (square.getX() == 0 && square.getY() == 7) {
                destroy(this.getXPos(), this.getYPos()); //destroys the king
                destroy(0,7); //destroys the rook
                
                //Sets the king to the correct spot
                this.setYPos(this.getYPos() + 2);
                board.getSquare(this.getXPos(), this.getYPos()).setPiece(this);
                board.getSquare(this.getXPos(), this.getYPos()).setProperties(player, getUnicode());
                
                //Sets the rook to the correct spot
                castlingRook.setYPos(this.getYPos() - 1); //this.getYPos refers to the updated king's Y pos
                board.getSquare(0, this.getYPos() - 1).setPiece(castlingRook);
                board.getSquare(0, 5).setProperties(player, castlingRook.getUnicode());
            }
            hasMoved = true;
        }
        
        else { 
            System.out.println("The coordinate to move is: " + square.getX() + ", " +  square.getY());
           // System.out.println("If there is a piece in the coordinate to move, it is: " + square.getPiece());
            System.out.println("old x and y: " + this.getXPos() + ", " +  this.getYPos()); 
            
            destroy(this.getXPos(), this.getYPos());
            checkCaptured(square.getX(), square.getY());
            
            this.setXPos(square.getX());
            this.setYPos(square.getY());
    
            System.out.println("new x and y coor of king: " +  this.board.getSquare(x, y).getX() + this.board.getSquare(x, y).getY()); //using complex way
    
            square.setPiece(this);
            square.setProperties(player, getUnicode());
            
            hasMoved = true;
        }
    }
    

    /**
     * Checks if the King is under attack.
     */
    public boolean isThreatened() {
        if (board.findThreats(this.board.getSquare(x, y), this.getPlayer())){
            this.getPlayer().getStatus().setText("Your King is in check!");
            return true;
        }
        else {
            this.getPlayer().getStatus().setText("Hai.");
            return false;
        }
    }
    
    public boolean isThreatenedOverride() {
        if (board.findThreats(this.board.getSquare(x, y), this.getPlayer())){
            return true;
        }
        else {
            return false;
        }
    }
    
    //this is needed though
    public String getUnicode() {
        return "\u2654";
    }
    
}
