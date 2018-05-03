/**
 * 
 */
package pieces;

import java.io.Serializable;
import java.util.ArrayList;

import chess.Player;
import chessboard.ChessTile;
import chessboard.Chessboard;

/**This is the abstract piece class that all Pieces extend from.
 * Constructor takes in the chessboard object so that all Pieces can make use of chessboard methods.
 * 
 * Contains methods that shows the valid moves of the Piece, moves the Piece, removes the Piece from its current 
 * square, check if the move location captured a Piece, and displays it, checks if the Piece has been moved, 
 * and setters/getters for the position of the Piece and the player that owns the Piece.
 * 
 * @author Perry
 *
 */
public abstract class Piece implements Serializable {
    
    public static final int SIZE = 8;
    //These variables are used in the subclasses to place the pieces in the right position
    protected Player player;
    protected int x;
    protected int y;
    protected Chessboard board;
    protected boolean hasMoved = false;
    protected boolean beenCaptured = false;
    protected int enPassant = 0;
    
    protected int oldX;
    protected int oldY;
    //this stores whether or not the piece has moved
    protected boolean hasMovedBefore = false;
    //oldHasMovedBefore = hasMovedBefore in case restoreOldPosition is called (which changes hasMovedBefore = false)
    protected boolean oldHasMovedBefore = false;
    
    protected Piece deletedPiece;
    protected int deletedPieceX;
    protected int deletedPieceY;
    
    //this is assigned to the same object as deletedPiece at first, but this will never get set to null afterwards!
    //see restoredOldPosition 
    protected Piece capturedPiece; 
    
    ArrayList <Object> objects = new ArrayList();
    
    
    /**Constructor that takes in the position of the piece, the player that owns the piece, 
     * and also takes in the chessboard object so that the chessboard methods can be used. */
    public Piece(Player player, int x, int y, Chessboard chessboard) {
        this.player = player;
        this.x = x;
        this.y = y;
        board = chessboard;
        board.getSquares()[x][y].setPiece(this);
    }
    
    /**
     * Method that checks if the piece is under attack. Currently only used in King, and it is overridden,
     * meaning this print statement will never get called.
     * @return false placeholder return value since this method is overridden in King
     */
    public boolean isThreatened(){
        System.out.println("In piece isThreatened");
        return false;
    }
    
    public boolean isThreatenedOverride() {
        System.out.println("never gets printed.");
        return false;
    }
    
    /**Outlines the squares the current piece can move to; in most cases this will be the same
     * as the squares the piece can attack, but not always (i.e King castling)
     */
    public abstract void drawValidMove(); 
    
    /**
     * Outlines the squares the current piece is attacking; in most cases this will be the same
     * as the squares the pieces can move, but not always (i.e King castling)
     */
    public abstract void drawAttackedSquares(); //outlines the squares the current piece can attack
    
    public abstract String getUnicode();
    
    /**
     * Moves the piece to the targetted square. This DOES NOT check for move logic, thus is pretty much the same for all pieces.
     * Consider making this non-abstract and implement in Pieces, and override in special cases like King? 
     * @param square the square that the piece wants to move to
     */
    public abstract void move(ChessTile square);
    
    /**
     * Removes the piece from the square by setting it to null; universal method for all pieces.
     * @param x
     * @param y
     */
    public void destroy(int x, int y) {
        storeOldPosition(x, y, hasMoved);
        System.out.println("The Coordinates to destroy (the square of where the piece is being removed "
                + ": " + x + ", " +  y );
        board.getSquare(x, y).setPiece(null);
        board.getSquare(x, y).setText(null);
        board.getSquare(x, y).setProperties(player, null);
    }
    
    /**
     * Checks if the move captures a piece, if so, tracks it. Can be set to Void since the return doesn't do anything right now.
     * @param x
     * @param y
     * @return 
     */
    public boolean checkCaptured(int x, int y) {
        
        System.out.println();
        //If the move does capture a piece, sets the piece to capturedPiece/deletedPiece
        if (board.getSquare(x, y).getPiece() != null) {
            if (player.getPlayerID() == 1 && board.getSquare(x, y).getPiece() != null) {
                deletedPiece = board.getSquare(x, y).getPiece(); //THIS IS NOT THE SAME AS CAPTURED PIECE, AS THIS WILL GET SET TO NULL LATER, WHILE capturedPiece wont be
                capturedPiece = board.getSquare(x, y).getPiece(); //easier to use a variable than method calls
                player.writeToLabel(board.getSquare(x, y).getText());
                capturedPiece.beenCaptured = true;
                System.out.println("Piece captured! The captured piece is: " + capturedPiece);
                
                System.out.println("Deleted piece (captured piece) is: " + deletedPiece );
                System.out.println("This is the coordinate of the captured piece: " + board.getSquare(x,y).getX() + ", " + board.getSquare(x, y).getY());
                storeDeletedPosition(board.getSquare(x, y).getX(), board.getSquare(x, y).getY());
                System.out.println("Position of deleted piece is (should be same as captured piece): " + deletedPiece.getDeletedPieceXPosition() + ", " + deletedPiece.getDeletedPieceYPosition());
                return true;
            }
            if (player.getPlayerID() == 2 && board.getSquare(x, y).getPiece() != null) {
                deletedPiece = board.getSquare(x, y).getPiece(); //THIS IS NOT THE SAME AS capturedPiece.
                capturedPiece = board.getSquare(x, y).getPiece();//easier to use a variable than method calls
                player.writeToLabel(board.getSquare(x, y).getText());
                capturedPiece.beenCaptured = true;
                System.out.println("Piece captured! The captured piece is: " + capturedPiece);
                
                System.out.println("Deleted piece (captured piece) is: " + deletedPiece );
                System.out.println("This is the coordinate of the captured piece: " + board.getSquare(x,y).getX() + ", " + board.getSquare(x, y).getY());
                storeDeletedPosition(board.getSquare(x, y).getX(), board.getSquare(x, y).getY());
                System.out.println("Position of deleted piece is (should be same as captured piece): " + deletedPiece.getDeletedPieceXPosition() + ", " + deletedPiece.getDeletedPieceYPosition());
                return true;
            }
        }
        //If the move does not capture a piece, set deletedPiece to null
        else {
            System.out.println("The move does not capture a piece. Setting deletedPiece to null.");
            deletedPiece = null;
        }
        return false;
    }
    
    public void restoreOldPosition() {
        
        System.out.println();
        //Variables to hold the new position before this.getXPos gets set the old square
        int lastX = this.getXPos();
        int lastY = this.getYPos();
        
        //Set the piece to the old square
        this.setXPos(getOldXPosition());
        this.setYPos(getOldYPosition());
        if (oldHasMovedBefore == false) {
            hasMoved = this.hasMovedBefore;
        }
        
        //Remove the piece from the new square
        destroy(lastX, lastY);
        
        
        //Set the old square to the piece
        System.out.println("The old position is: " + board.getSquare(x, y).getX() + ", " + board.getSquare(x, y).getX());
        board.getSquare(x, y).setPiece(this);
        board.getSquare(x, y).setText(this.getUnicode());
        board.getSquare(x, y).setProperties(player, this.getUnicode());
        
        //If a piece has been deleted, place it back.
        if(deletedPiece != null) {
            
            System.out.println("Moving piece has been set back. Deleted Piece is not null. Setting deleted piece back: ");
            System.out.println("Setting deleted piece back: DeletedPiecePos = (" + deletedPieceX + ", " + deletedPieceY + ")");
            board.getSquare(deletedPieceX, deletedPieceY).setPiece(deletedPiece);
            deletedPiece.beenCaptured = false; //CRITICAL STATEMENT LOL: REMEMBER THAT ITS NOT CAPTURED
            //originally wanted to set a temp var to make it easier to read when calling methods,
            //but realized that temp == deletedPiece.
            Piece temp = board.getSquare(deletedPieceX, deletedPieceY).getPiece();
            System.out.println("Is temp = deletedPiece (always true)" + (temp == deletedPiece));  //this is true
            
            deletedPiece.setXPos(deletedPieceX);
            deletedPiece.setYPos(deletedPieceY);
            //board.getSquare(deletedPieceX, deletedPieceY).setPiece(deletedPiece);
            board.getSquare(deletedPieceX, deletedPieceY).setText(deletedPiece.getUnicode());
            board.getSquare(deletedPieceX, deletedPieceY).setProperties(deletedPiece.player, deletedPiece.getUnicode());
            player.removeFromLabel(deletedPiece.getUnicode());
            deletedPiece = null;
        }
        
        else {
            System.out.println("Moving Piece has been reset. Deleted Piece is null (moved to empty square) ");
        }
    }
    
    public void storeOldPosition(int x, int y, boolean hasItMovedBefore) {
        oldX = x;
        oldY = y;
        hasMoved = hasItMovedBefore; 
    }
    
    public void storeDeletedPosition(int x, int y) {
        deletedPieceX = x;
        deletedPieceY = y;
        
    }
    
    
    public void repositionPiece(int x, int y) {
        this.x= x;
        this.y = y;
        board.getSquare(x, y).setPiece(this);
        board.getSquare(x, y).setText(this.getUnicode());
        board.getSquare(x, y).setProperties(player, this.getUnicode());
    }
    
    public int getOldXPosition() {
        return oldX;
    }
    
    public int getOldYPosition() {
        return oldY;
    }
    
    public int getDeletedPieceXPosition() {
        return deletedPieceX;
    }
    
    public int getDeletedPieceYPosition() {
        return deletedPieceY;
    }
    
    public void setXPos(int xCoor) {
        x = xCoor;
    }
    
    public void setYPos(int yCoor) {
        y = yCoor;
    }
    
    public int getXPos() {
        return x;
    }
    public int getYPos() {
        return y;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public boolean getHasMoved() {
        return hasMoved;
    }
    
    public boolean getBeenCaptured() {
        return beenCaptured;
    }
    
    public ChessTile getSquare() {
        return board.getSquare(x, y);
    }
    
    public String toString() {
        return super.toString() + "Coordinates: " + x + y + ". Player: " + player.getPlayerID();
    }
    
    public void setHasMoved(boolean value) {
        hasMoved = value;
    }
    
    public int getEnPassant() {
        return enPassant;
    }
}
