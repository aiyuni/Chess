/**
 * 
 */
package pieces;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import chess.Player;
import chessboard.ChessTile;
import chessboard.Chessboard;

/**
 * @author Perry
 *
 */
public class Bishop extends Piece implements Serializable {
    
    FileOutputStream fileOut;
    ObjectOutputStream out;
    FileInputStream fileIn;
    ObjectInputStream in;
    
    public Bishop(Player player, int x, int y, Chessboard chessboard) {
        super(player, x, y, chessboard);
        
        board.getSquares()[x][y].setProperties(player, this.getUnicode());
        board.getSquares()[x][y].setPiece(this);
        
        try
        {   
            if (this.player.getPlayerID() == 1) {
                fileIn = new FileInputStream("bishop.txt");
                in = new ObjectInputStream(fileIn);
                
                this.x = (int)in.readObject();
                this.y = (int)in.readObject();
                 
                in.close();
                fileIn.close();
                 
                System.out.println("Object has been deserialized ");
            }
            // Reading the object from a file
             
            // Method for deserialization of object

        }
         
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
         
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }
    }
    
    @Override
    public void drawAttackedSquares() {
        drawValidMove();
    }
    
    @Override
    public void drawValidMove() {
        
        //Checks forwards downwards diagonal
        int i = 1;
        while (this.getXPos() + i < SIZE && this.getYPos() + i < SIZE ) {
            if (board.getSquare(getXPos() + i,  getYPos() + i).getPiece() != null) {
                if(board.getSquare(getXPos() + i,  getYPos() + i).getPiece().getPlayer() != this.player) {
                    board.getSquare(getXPos() + i, getYPos() + i).drawOutline();
                    break;
                }
                break;
            }
            else {
                board.getSquare(getXPos() + i, getYPos() + i).drawOutline();
            }
            i++;
        }
        
        //Checks forward upwards diagonal
        i = 1;
        while (this.getXPos() - i >= 0 && this.getYPos() + i < SIZE ) {
            if (board.getSquare(getXPos() - i,  getYPos() + i).getPiece() != null) {
                if(board.getSquare(getXPos() - i,  getYPos() + i).getPiece().getPlayer() != this.player) {
                    board.getSquare(getXPos() - i, getYPos() + i).drawOutline();
                    break;
                }
                break;
            }
            else {
                board.getSquare(getXPos() - i, getYPos() + i).drawOutline();
            }
            i++;
        }
        
        //Checks backwards upwards diagonal
        i = 1;
        while (this.getXPos() + i < SIZE && this.getYPos() - i >= 0 ) {
            if (board.getSquare(getXPos() + i,  getYPos() - i).getPiece() != null) {
                if(board.getSquare(getXPos() + i,  getYPos() - i).getPiece().getPlayer() != this.player) {
                    board.getSquare(getXPos() + i, getYPos() - i).drawOutline();
                    break;
                }
                break;
            }
            else {
                board.getSquare(getXPos() + i, getYPos() - i).drawOutline();
            }
            i++;   
        }
        
        //Checks backwards downwards diagonal
        i = 1;
        while (this.getXPos() - i >=0 && this.getYPos() - i >= 0 ) {
            if (board.getSquare(getXPos() - i,  getYPos() - i).getPiece() != null) {
                if(board.getSquare(getXPos() - i,  getYPos() - i).getPiece().getPlayer() != this.player) {
                    board.getSquare(getXPos() - i, getYPos() - i).drawOutline();
                    break;
                }
                break;
            }
            else {
                board.getSquare(getXPos() - i, getYPos() - i).drawOutline();
            }
            i++;
        } 
    }
    
    @Override
    public void move(ChessTile square) {
        
        //Prints out test messages
        System.out.println("The coordinate to move is: " + square.getX() + ", " + square.getY());
        System.out.println("old x and y: " + this.getXPos() + ", " + this.getYPos());
        
        destroy(this.getXPos(), this.getYPos()); //removes this bishop from the current square
        checkCaptured(square.getX(), square.getY());
        //Updates the position of the bishop (DOES NOT PLACE THIS BISHOP TO THE NEW SQUARE)
        //note that this is a method of the bishop, not the square itself!
        this.setXPos(square.getX()); 
        this.setYPos(square.getY());
        
        //checkCaptured(square.getX(), square.getY());
        //Places this bishop to the new square; note that this is the method of the square!
        square.setPiece(this);
        square.setProperties(player, getUnicode());
        hasMoved = true;
        
        //Prints out confirmation of move
        System.out.println("new x and y coor of bishop: " +  this.getXPos() + this.getYPos()); //using simple way
        
        try
        {   
            //Saving of object in a file
            fileOut = new FileOutputStream("bishop.txt");
            out = new ObjectOutputStream(fileOut);
             
            // Method for serialization of object
            out.writeObject(this.getXPos());
            out.writeObject(this.getYPos());
             
            out.close();
            fileOut.close();
             
            System.out.println("Object has been serialized");

        }
         
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
    }
    
    public String getUnicode() {
        return "\u2657";
    }
}
