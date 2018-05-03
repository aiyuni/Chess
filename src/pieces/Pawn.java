package pieces;

import java.io.Serializable;

import chess.Player;
import chessboard.ChessTile;
import chessboard.Chessboard;

public class Pawn extends Piece implements Serializable {
    
    //private boolean hasMoved = false;  This variable is in abstract Piece class!
    
    Queen queen;
    
    public Pawn(Player player, int x, int y, Chessboard chessboard) {
        super(player, x, y, chessboard);
        
        board.getSquares()[x][y].setProperties(player, getUnicode());
        board.getSquares()[x][y].setPiece(this);
    }

    @Override
    public void move(ChessTile square) {
        
        //promotion rules
        if (square.getX() == 0) {
            destroy(this.getXPos(), this.getYPos()); 
            System.out.println();
            System.out.println("Spawning new queen...");
            queen = new Queen(player, square.getX(), square.getY(), board);
            board.getArrayPieces(player).add(queen);
        }
        
        else {
            oldHasMovedBefore = hasMoved;
            
            destroy(this.getXPos(), this.getYPos()); //removes this piece from the current square
            
            if (square.getEnPassent() == true) {
                System.out.println();
                System.out.println("Inside move's en passant rules.");
                //if the moving pawn belongs to player 1, we are capturing player 2's pawn that has moved 2 spaces
                if (player.getPlayerID() == 1) {
                    checkCaptured(square.getX()+1, square.getY());
                    destroy(square.getX()+1, square.getY());
   
                }
                if (player.getPlayerID() == 2) {
                    checkCaptured(square.getX()-1, square.getY());
                    destroy(square.getX()-1, square.getY());
                }
                square.setEnPassent(false);
                
            }
            
            else {
                checkCaptured(square.getX(), square.getY());
            }
     
            //Updates the position of the piece (DOES NOT PLACE THIS piece TO THE NEW SQUARE)
            //note that this is a method of the piece, not the square itself!
            this.setXPos(square.getX()); 
            this.setYPos(square.getY());
           
            
            //Places this piece to the new square; note that this is the method of the square!
            square.setPiece(this);
            square.setProperties(player, getUnicode());
            
            //Since this is a pawn piece, needs to keep track of whether it has moved or not
            if (!hasMoved && square.getX() != 2 && square.getX() != 6) {
                enPassant = board.getTurnCounter();
            }
            hasMoved = true;
            
            //Prints out confirmation of move
            System.out.println("new x and y coor of pawn: " +  this.getXPos() + this.getYPos()); //using simple way
        }
    }
    
    @Override
    public void drawAttackedSquares() {
        drawValidMove();
    }
    
    @Override
    public void drawValidMove() {
        
        //If this pawn belongs to player 1:
        if(this.getPlayer().getPlayerID() % 2 != 0) {
            //Pawn can move one space forwards if no piece is in front
            if (board.getSquare(x-1, y).getPiece()== null) {
                board.getSquare(x-1, y).drawOutline();
            }
            
            //if first move, pawn can move 2 spaces forward
            if (!hasMoved && board.getSquare(x-2, y).getPiece() == null && board.getSquare(x-1, y).getPiece() == null) {
                  board.getSquare(x-2, y).drawOutline();
            }
            
            //If there is an enemy piece in the diagonal forward square of the pawn, the pawn can move (capture the piece)
            if (y-1 > 0) { //ensures no arrayoutofbounds error
                if (board.getSquare(x-1, y-1) != null && board.getSquare(x-1, y-1).getPiece() != null &&
                        board.getSquare(x-1, y-1).getPiece().getPlayer()  != this.getPlayer()) {
                    board.getSquare(x-1, y-1).drawOutline();
                }
            }
            
            if(y+1<SIZE) { //ensures no arrayoutofbounds error
                if (board.getSquare(x-1, y+1) != null && board.getSquare(x-1, y+1).getPiece() != null  &&
                        board.getSquare(x-1, y+1).getPiece().getPlayer() != this.getPlayer() ) {
                    board.getSquare(x-1, y+1).drawOutline();
                }
            }
            
            System.out.println("");
            //En passant
            if (y - 1 > 0) {
                
                System.out.println();
                if (board.getSquare(x, y-1).getPiece() != null) { 
                    System.out.println("the piece's enPassant is: " + board.getSquare(x, y-1).getPiece().getEnPassant());
                    System.out.println("The game's turn counter is: " + board.getTurnCounter());
                }
                else {
                    System.out.println("Null piece when checking for en passent.");
                }
                
                if (board.getSquare(x, y-1).getPiece() != null && board.getSquare(x, y-1).getPiece().getPlayer() != this.player &&
                        board.getSquare(x, y-1).getPiece().getEnPassant() == board.getTurnCounter() - 1) {
                    board.getSquare(x-1, y-1).drawOutline();
                    board.getSquare(x-1, y-1).setEnPassent(true);
                }
            }
            
            if (y + 1 < SIZE) {
                if (board.getSquare(x, y+1).getPiece() != null && board.getSquare(x, y+1).getPiece().getPlayer() != this.player &&
                        board.getSquare(x, y+1).getPiece().getEnPassant() == board.getTurnCounter() - 1) {
                    board.getSquare(x-1, y+1).drawOutline();
                    board.getSquare(x-1, y+1).setEnPassent(true);
                }
            }
        }
        
        //If this pawn belongs to player 2:
        else {
            
            if (board.getSquare(x+1, y).getPiece() == null) {
                board.getSquare(x+1, y).drawOutline();
            }
            
            //if first move, pawn can move 2 spaces forward
            if (!hasMoved && board.getSquare(x+2, y).getPiece() == null && board.getSquare(x+1, y).getPiece() == null) {
                board.getSquare(x+2, y).drawOutline();
                
            }
            
            if(y-1 > 0) {
                if (board.getSquare(x+1, y-1) != null && board.getSquare(x+1, y-1).getPiece() != null &&
                        board.getSquare(x+1, y-1).getPiece().getPlayer() != this.getPlayer() ) {
                    board.getSquare(x+1, y-1).drawOutline();
                }
            }
            
            if(y+1 < SIZE) {
                if (board.getSquare(x+1, y+1) != null && board.getSquare(x+1, y+1).getPiece() != null  &&
                        board.getSquare(x+1, y+1).getPiece().getPlayer() != this.getPlayer()) {
                    board.getSquare(x+1, y+1).drawOutline();
                }
            }
            
            //En passant
            if (y - 1 > 0) {
                if (board.getSquare(x, y-1).getPiece() != null &&board.getSquare(x, y-1).getPiece().getPlayer() != this.player &&
                        board.getSquare(x, y-1).getPiece().getEnPassant() == board.getTurnCounter() - 1) {
                    board.getSquare(x+1, y-1).drawOutline();
                    board.getSquare(x+1, y-1).setEnPassent(true);
                }
            }
            
            if (y + 1 < SIZE) {
                if (board.getSquare(x, y+1).getPiece() != null && board.getSquare(x, y+1).getPiece().getPlayer() != this.player &&
                        board.getSquare(x, y+1).getPiece().getEnPassant() == board.getTurnCounter() - 1) {
                    board.getSquare(x+1, y+1).drawOutline();
                    board.getSquare(x+1, y+1).setEnPassent(true);
                }
            }
        }
    }
    
    public String getUnicode() {
        return "\u2659";
    }

}
