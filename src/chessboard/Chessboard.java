/**
 * 
 */
package chessboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import chess.Player;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

/**Class that contains the construction and methods of the chessboard. Creates the two player objects, and 
 * all 64 squares.
 * 
 * Chessboard methods include setting up pieces on the board, getting a specific square in the chessboard, checking for mate,
 * checking if a given square is empty, checking a square for threats,  remove all outlines of all squares on the board, 
 * checks if castling is possible, and a getter that get the entire chessboard as 
 * an 2d array, as well a getter that gets a specific square in the board.
 * 
 * Issues that are fixed: when you move a piece thats not allowed since you are in check, it does not 
 * unselect the piece but also does not highlight the piece, making users guess if they have selected
 * a piece or not.
 * Also, does not check if you are moving a pinned piece to the king. FIXED
 * 
 * Unimplemented features: En passent, stalement, undo function, serialization, 3d???
 * 
 * @author Perry
 *
 */

//that is the unicode text, there's an 
public class Chessboard implements Serializable  {
    
    private static final int SIZE = 8;
    
    private Player player1;
    private Player player2;
    
    private transient GridPane grid = new GridPane();
    private transient ChessTile[][] boardSquares = new ChessTile[SIZE][SIZE];
    private int count = 1; //used to make the board
    private boolean isFlipped = false;
    private boolean gameOver = false ;
    
    private int turnCount = 1; //original turnCounter that needs to be modified to keep track of who's turn it is (i.e undo)
    private int turnCounter = 1; //this is the accurate turnCounter that will never get modified apart from +1 per move
    private int clicks = 0; //this value is either 0 or 1, so can change to boolean
                    //(it keeps of track if the user is clicking to select a piece, or clicking to move a selected piece
    private int startX;
    private int startY;
    private Piece currentPiece; 
    
    private Piece removedPiece;
    
    private Piece whiteKing;
    private Piece blackKing;
    private Piece whiteQueen;
    private Piece blackQueen;
    private Piece whiteRook1;
    private Piece whiteRook2;
    private Piece blackRook1;
    private Piece blackRook2;
    private Piece whiteKnight1;
    private Piece whiteKnight2;
    private Piece blackKnight1;
    private Piece blackKnight2;
    private Piece whiteBishop1;
    private Piece whiteBishop2;
    private Piece blackBishop1;
    private Piece blackBishop2;
    private Piece whitePawn1;
    private Piece whitePawn2;
    private Piece whitePawn3;
    private Piece whitePawn4;
    private Piece whitePawn5;
    private Piece whitePawn6;
    private Piece whitePawn7;
    private Piece whitePawn8;
    private Piece blackPawn1;
    private Piece blackPawn2;
    private Piece blackPawn3;
    private Piece blackPawn4;
    private Piece blackPawn5;
    private Piece blackPawn6;
    private Piece blackPawn7;
    private Piece blackPawn8;
    
    ArrayList<Piece> whitePieces = new ArrayList<Piece>();
    ArrayList<Piece> blackPieces = new ArrayList<Piece>();
    
    
    /**
     * Creates the chessboard by calling the Tile constructor, and adds eventhandler to the tiles
     */
    public Chessboard() {
        player1 = new Player(1);
        player2 = new Player(2);
        
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessTile square;
                if(count % 2 != 0) {
                    square = new ChessTile(i, j, "white", this);
                }
                else {
                    square  = new ChessTile(i, j, "green", this);
                }
                
                square.setOnMouseClicked(new EventHandler<Event>() {
                    @Override
                    public void handle(Event firstClick) {
                        
                        if (clicks == 0 && !gameOver) {
                            startX = square.getX();
                            startY = square.getY();
                            currentPiece = square.getPiece();
                            
                            if(!squareIsEmpty(square) && currentPiece.getPlayer().getPlayerID()%2 == turnCount%2) {
                                
                                square.highlight(currentPiece);
                                System.out.println(); //new line
                                currentPiece.drawValidMove();
                                System.out.println("Drawing the valid move for the selected piece.");
                                clicks = 1;
                            }
                        }     
                        
                        //If the user has successfully selected their own piece:
                        else if(clicks == 1 && !gameOver) {
                            //Checks if user has clicked on the same square (still checks) or their own piece (does not check)
                            if (square.getX() != startX || square.getY() != startY) {                  
                                //Checks if move is legal
                                if (square.checkOutline() == true) {
                                    removedPiece = square.getPiece();
                                    currentPiece.move(square);
                                    System.out.println("Is white king threatened: " + whiteKing.isThreatened());
                                    System.out.println("Is black king threatened: " + blackKing.isThreatened());
                                    //If the player's king is in check after the move, undo the move
                                    if (isPlayerTurn(player1) && whiteKing.isThreatened() || isPlayerTurn(player2) && blackKing.isThreatened() ) {
                                        
                                        currentPiece.restoreOldPosition(); 
                                        clicks = 0;

                                    }
                                    else {
                                        turnCount++;
                                        turnCounter++;
                                        removeOutlines();
                                        whiteKing.isThreatened();
                                        blackKing.isThreatened();
                                        clicks = 0;
                                    }
                                    
                                }
                                else {
                                    System.out.println("Invalid move, try again.");
                                    removeOutlines();
                                    square.unHighlight(square.getPiece());
                                    clicks = 1;
                                }
                                
                                //checks for mate
                                if (isPlayerTurn(player1) && whiteKing.isThreatened()) {
                                    System.out.println();
                                    System.out.println("White king is threatened, checking for black win.");
                                    if(checkWin(player2)) {
                                        player2.getStatus().setText("Black wins by Checkmate!" );
                                        player1.getStatus().setText("White loses.");
                                        gameOver = true;
                                    }
                                }
                                else if (isPlayerTurn(player2) && blackKing.isThreatened()){
                                    System.out.println();
                                    System.out.println("Black king is threatened, checking for white win: ");
                                    if(checkWin(player1)) {
                                        player1.getStatus().setText("White wins by Checkmate!");
                                        player2.getStatus().setText("Black loses.");
                                        gameOver = true;
                                    }
                                } 
                                
                                //checks for stalemate
                                else if (isPlayerTurn(player1) && !whiteKing.isThreatened())  {
                                    System.out.println();
                                    System.out.println("Checking for stalement.");
                                    if(checkStalemate(player1)) {
                                        player2.getStatus().setText("Stalemate! White has no legal moves." );
                                        player1.getStatus().setText("Stalemate! White has no legal moves.");
                                        gameOver = true;
                                    }
                                }
                                else if (isPlayerTurn(player2) && !blackKing.isThreatened())  {
                                    System.out.println();
                                    System.out.println("Checking for stalement.");
                                    if(checkStalemate(player2)) {
                                        player2.getStatus().setText("Stalemate! Black has no legal moves." );
                                        player1.getStatus().setText("Stalemate! Black has no legal moves.");
                                        gameOver = true;
                                    }
                                } 
                                
                            }
                            
                            else {
                                System.out.println("Repeated square, unselecting your piece for you.");
                                square.unHighlight(square.getPiece());
                                removeOutlines();
                                clicks = 0;
                            }
                           // clicks = 0; //clicks is always 0 after this big else if lol
                        }
                    }});
                
                boardSquares[i][j] = square;
                grid.add(square, j, i);
                count++;
            }
            count++; //because the starting color of each row alternates
        }
        this.setUpPieces();
    }
    
    /**
     * Gets the square, or return null if arrayoutofbounds
     * @param square
     * @return
     */
    public ChessTile getSquare(int x, int y) {
        if (x >= 0 && x< boardSquares.length || y >= 0 && y< boardSquares.length) {
            return boardSquares[x][y];
        }
        else {
            return null;
        }
    }
    
    /**
     * Checks if square has a piece, returns false if empty (null)
     * @param square
     * @return 
     */
    private boolean squareIsEmpty(ChessTile square) {
        if(square.getPiece() != null) {
            return false;
        }
        return true;
    }
    
    /**
     * Simple method that removes the outlines of ALL squares on the board.
     */
    private void removeOutlines() {
        for (int i = 0; i<8; i++) {
            for (int j = 0; j < 8; j++) {
                this.getSquare(i, j).removeOutline();
            }
        }
    }
    
    /**
     * Checks if the player can castle. This method is called through King, and only if King has not been moved.
     * Castling is only valid if the King and rook has not moved yet, and none of the squares between them are taken or under attack.
     * Might consider placing this method elsewhere? Kinda awkward here
     * THIS METHOD IS BAD SINCE IT IS HARD CODED: FIX IT LATER?
     * @param player
     */
    public void checkCastle(Player player) {
        
        boolean whiteKingside = false;
        boolean whiteQueenside = false;
        boolean blackKingside = false;
        boolean blackQueenside = false;
        
        if(player.getPlayerID() == 1) {
           
            if (this.getSquare(7, 0).getPiece() != null && !this.getSquare(7, 0).getPiece().getHasMoved()) {
                
                if (this.getSquare(7, 1).getPiece() == null && this.getSquare(7, 2).getPiece() == null && this.getSquare(7, 3).getPiece() == null
                        && !this.findThreats(this.getSquare(7, 1),getPlayer1()) && !this.findThreats(this.getSquare(7, 2), getPlayer1())
                                && !this.findThreats(this.getSquare(7, 3), getPlayer1())){
                   
                    whiteQueenside = true;
                    System.out.println("Can castle queenside!");
                }
            }
            if (this.getSquare(7, 7).getPiece() != null && !this.getSquare(7, 7).getPiece().getHasMoved()) {
                
                if (this.getSquare(7, 6).getPiece() == null && this.getSquare(7, 5).getPiece() == null 
                        && !this.findThreats(this.getSquare(7, 6),getPlayer1()) 
                            && !this.findThreats(this.getSquare(7, 5), getPlayer1())){
                   
                    whiteKingside = true;
                    System.out.println("Can castle kingside!");
                }
            }
        }
        
        if(player.getPlayerID() == 2) {
            if (this.getSquare(0, 0).getPiece() != null && !this.getSquare(0, 0).getPiece().getHasMoved()) {
                
                if (this.getSquare(0, 1).getPiece() == null && this.getSquare(0, 2).getPiece() == null && this.getSquare(0, 3).getPiece() == null
                        && !this.findThreats(this.getSquare(0, 1),getPlayer2()) && !this.findThreats(this.getSquare(0, 2), getPlayer2())
                                && !this.findThreats(this.getSquare(0, 3), getPlayer2())){
                   
                    blackQueenside = true;
                    System.out.println("Can castle queenside!");
                }
            }
            if (this.getSquare(0, 7).getPiece() != null && !this.getSquare(0, 7).getPiece().getHasMoved()) {
                
                if (this.getSquare(0, 6).getPiece() == null && this.getSquare(0, 5).getPiece() == null 
                        && !this.findThreats(this.getSquare(0, 6),getPlayer2()) 
                            && !this.findThreats(this.getSquare(0, 5), getPlayer2())){
                   
                    blackKingside = true;
                    System.out.println("Can castle kingside!");
                }
            }
        } 
        
        if (whiteKingside) {
            this.getSquare(7, 7).drawOutline();
        }
        if (whiteQueenside) {
            this.getSquare(7, 0).drawOutline();
        }
        if (blackKingside) {
            this.getSquare(0, 7).drawOutline();
        }
        if (blackQueenside) {
            this.getSquare(0, 0).drawOutline();
        }
    }
    
    /**
     * Checks if a given square is under attack or not.
     * @param square The square to check for threats
     * @param player The friendly player  
     * @return
     */
    //THIS RETURNS FALSE IF KING IS THE ONE MOVING FOR SOME REASON, fixed i think
    public boolean findThreats(ChessTile square, Player player) {
        System.out.println();
        
        this.removeOutlines(); //in case previous methods didnt remove outlines
        
        if (player.getPlayerID() == 1) {
            Iterator<Piece> blackPiecesIterator = blackPieces.iterator(); 
            while (blackPiecesIterator.hasNext()) {
                Piece nextPiece = blackPiecesIterator.next();
                if (!nextPiece.getBeenCaptured()){ 
                    nextPiece.drawAttackedSquares();
                }
            }
            if(square.checkOutline()) {
                System.out.println("The square " + square.getX() + ", " + square.getY() +" is being attacked.");
                this.removeOutlines();
                return true;
            }
            else {
                this.removeOutlines();
                return false;
            }
        }
        else {
            Iterator<Piece> whitePiecesIterator = whitePieces.iterator(); 
            while (whitePiecesIterator.hasNext()) {
                Piece nextPiece = whitePiecesIterator.next();
                if (!nextPiece.getBeenCaptured()){ 
                    nextPiece.drawAttackedSquares();
                }
            }
            if(square.checkOutline()) {
                System.out.println("The square is being attacked.");
                this.removeOutlines();
                return true;
            }
            else {
                this.removeOutlines();
                return false;
            }
        }
        
    }
    
    /**
     * Gets the attacker of the given square.Parameter player = player that owns the square.
     * Does not check if the square is under attack,
     * check it in another method (findThreats) then call this.
     * If square is not under attack, returns a null piece.
     * @param square
     * @param player
     * @return
     */
    public Piece getAttacker(ChessTile square, Player player) {
        
        Piece currentPiece;
        Piece attacker = null;
        
        if (player.getPlayerID() == 1) {
            Iterator<Piece> blackPiecesIterator = blackPieces.iterator();
            
            while (blackPiecesIterator.hasNext()) {
                currentPiece = blackPiecesIterator.next();
                
                System.out.println("Inside getAttacker. Checking this piece for attacking: " + currentPiece);
                if (currentPiece.getBeenCaptured() == false) {
                    currentPiece.drawAttackedSquares();
                    if (square.checkOutline()) {
                        attacker = currentPiece;
                        System.out.println("This piece is an attacker!");
                    }
                    this.removeOutlines();
                }
            }
        }
        
        else {
            Iterator <Piece> whitePiecesIterator = whitePieces.iterator();
            
            while (whitePiecesIterator.hasNext()) {
                currentPiece = whitePiecesIterator.next();
                
                System.out.println("Inside getAttacker. Checking this piece for attacking: " + currentPiece);
                
                if (currentPiece.getBeenCaptured()) {
                    currentPiece.drawAttackedSquares();
                    if (square.checkOutline()) {
                        attacker = currentPiece;
                        System.out.println("This piece is an attacker!");
                    }
                    this.removeOutlines();
                }
            }
        }
        
        return attacker;
    }
    
    //Test method, simply prints out attacker UNUSED RIGHT NOW
    //This method does not work properly right now? none of the if blocks are triggering
    //because there is no method that is calling this method??!?!!
    public void checkAttacker() {
      //Test statement: check if getAttacker gets the correct attacker
        System.out.println("Is white King threatened: " + whiteKing.isThreatened());
        System.out.println("Is black King threatened: " + blackKing.isThreatened());
        if (whiteKing.isThreatened()) {
            System.out.println("White king under attack: Attacker is: " +
                    getAttacker(whiteKing.getSquare(), player1));
        }
        if (blackKing.isThreatened()) {
            System.out.println("Black king under attack: Attacker is: " + 
                    getAttacker(blackKing.getSquare(), player2));
        }
        if (!blackKing.isThreatened() && !whiteKing.isThreatened()) {
            System.out.println("King is not threatened in checkAttacker.");
        }
    }
    
    private boolean isPlayerTurn(Player player) {
        if (player.getPlayerID() % 2 == turnCount % 2) {
            System.out.println("It is currently " + player.getPlayerID() +"'s turn.");
            return true;
        }
        else {
            return false;
        }
    }
    
    private boolean checkWin(Player player) {
        
        System.out.println();
        
        Piece currentPiece;
        Piece lastMovedPiece = null;
        ChessTile lastMovedSquare = null;
        Boolean win = true;
        
        if (player.getPlayerID() == player2.getPlayerID()) {
            Iterator<Piece> whitePiecesIterator = whitePieces.iterator();
            
            outer:
            while (whitePiecesIterator.hasNext() && win) {
                System.out.println("IN THE RIGHT WHILE LOOP.");
                System.out.println();
                currentPiece = whitePiecesIterator.next();
                if (!currentPiece.getBeenCaptured()) {
                    System.out.println("Checking for possible moves by " + currentPiece + " to see if it can prevent checkmate.");
                    currentPiece.drawValidMove();
                    for (int i = 0; i < 8; i++ ) {
                        for (int j = 0; j < 8; j++) {
                            currentPiece.drawValidMove();
                            if (this.getSquare(i, j).checkOutline()) {
                                currentPiece.move(this.getSquare(i, j));
                                if (whiteKing.isThreatenedOverride()) {
                                    System.out.println("Current Piece's coordinates: " + currentPiece.getXPos() + 
                                            " , " + currentPiece.getYPos());
                                    lastMovedPiece = currentPiece;
                                    lastMovedSquare = currentPiece.getSquare();
                                    this.removeOutlines();
                                    currentPiece.restoreOldPosition();
                                    System.out.println("Current Piece's new coordinates: " + currentPiece.getXPos() + 
                                            " , " + currentPiece.getYPos());

                                  //do nothing   
                                }
                                else {
                                    System.out.println("Not a checkmate");
                                    win = false;
                                    this.removeOutlines();
                                    currentPiece.restoreOldPosition();
                                    System.out.println("Breaking outer.");
                                    break outer;
                                }
                            }
                        }
                    }
                }
                if (win) {
                    System.out.println("The last moved piece/square is: " + lastMovedPiece + lastMovedSquare);
                    //lastMovedPiece.move(lastMovedSquare);
                }
                this.removeOutlines();
            }
        }
        
        if (player.getPlayerID() == player1.getPlayerID()) {
            Iterator<Piece> blackPiecesIterator = blackPieces.iterator();
            
            outer:
            while (blackPiecesIterator.hasNext() && win) {
                System.out.println();
                currentPiece = blackPiecesIterator.next();
                if (!currentPiece.getBeenCaptured()) {
                    currentPiece.drawValidMove();
                    for (int i = 0; i < 8; i++ ) {
                        for (int j = 0; j < 8; j++) {
                            currentPiece.drawValidMove();
                            if (this.getSquare(i, j).checkOutline()) {
                                currentPiece.move(this.getSquare(i, j));
                                if (blackKing.isThreatenedOverride()) {
                                    this.removeOutlines();
                                    lastMovedPiece = currentPiece;
                                    lastMovedSquare = currentPiece.getSquare();
                                    currentPiece.restoreOldPosition();
                                  //do nothing   
                                }
                                else {
                                    System.out.println("Not a checkmate");
                                    win = false;
                                    this.removeOutlines();
                                    currentPiece.restoreOldPosition();
                                    break outer;
                                }
                            }
                        }
                    }
                }
           
                if (win) {
                    System.out.println("The last moved piece/square is: " + lastMovedPiece + lastMovedSquare);
                    //lastMovedPiece.move(lastMovedSquare);
                }
                this.removeOutlines();
            }
        }
        System.out.println("The Win variable is: " + win);
        return win;
    }
    
    
    public boolean checkStalemate(Player player) {
        System.out.println();
        
        Piece currentPiece;
        Piece lastMovedPiece = null;
        ChessTile lastMovedSquare = null;
        Boolean draw = false;
        
        if (player.getPlayerID() == player1.getPlayerID()) {
            Iterator<Piece> whitePiecesIterator = whitePieces.iterator();
            
            outer:
            while (whitePiecesIterator.hasNext()) {
                System.out.println();
                currentPiece = whitePiecesIterator.next();
                
                if (!currentPiece.getBeenCaptured() && currentPiece != whiteKing) {
                    System.out.println("Checking for possible moves by " + currentPiece + " to see if it has valid move.");
                    currentPiece.drawValidMove();
                    for (int i = 0; i < 8; i++ ) {
                        for (int j = 0; j < 8; j++) {
                            currentPiece.drawValidMove();
                            if (this.getSquare(i, j).checkOutline()) {
                                this.removeOutlines();
                                return false;
                            }
                                
                                
                            else {
                                draw = false;
                                this.removeOutlines();

                            }
                            
                        }
                    }
                }
                if (currentPiece == whiteKing) {
                    System.out.println("Inside whiteKing.");
                    currentPiece.drawValidMove();
                    for (int i = 0; i < 8; i++ ) {
                        for (int j = 0; j < 8; j++) {
                            currentPiece.drawValidMove();
                            if (this.getSquare(i, j).checkOutline()) {
                                currentPiece.move(this.getSquare(i, j));
                                if (!whiteKing.isThreatened()) {
                                    System.out.println("inside stalement if white king threatened");
                                    currentPiece.restoreOldPosition();
                                    return false;
                                }
                                System.out.println("Current Piece's coordinates: " + currentPiece.getXPos() + 
                                        " , " + currentPiece.getYPos()); 
                                lastMovedPiece = currentPiece;
                                lastMovedSquare = currentPiece.getSquare();
                                this.removeOutlines();
                                currentPiece.restoreOldPosition();
                                System.out.println("Current Piece's new coordinates: " + currentPiece.getXPos() + 
                                        " , " + currentPiece.getYPos()); 
                                
                            }
                            
                            else {
                                System.out.println("Not a legal move.");
                                draw = true;
                                this.removeOutlines();
                               // currentPiece.restoreOldPosition();
                            }
                            
                        }
                    }
                }
                this.removeOutlines();
               
            }
        }
        
        if (player.getPlayerID() == player2.getPlayerID()) {
            Iterator<Piece> blackPiecesIterator = blackPieces.iterator();
            
            outer:
            while (blackPiecesIterator.hasNext()) {
                System.out.println();
                currentPiece = blackPiecesIterator.next();
                
                if (!currentPiece.getBeenCaptured() && currentPiece != blackKing) {
                    System.out.println("Checking for possible moves by " + currentPiece + " to see if it has valid movie");
                    currentPiece.drawValidMove();
                    for (int i = 0; i < 8; i++ ) {
                        for (int j = 0; j < 8; j++) {
                            currentPiece.drawValidMove();
                            if (this.getSquare(i, j).checkOutline()) {
                                this.removeOutlines();
                                return false;
                            }
                                
                                
                            else {
                                draw = false;
                                this.removeOutlines();
                            }
                            
                        }
                    }
                }
                if (currentPiece == blackKing) {
                    System.out.println("inside black king");
                    currentPiece.drawValidMove();
                    for (int i = 0; i < 8; i++ ) {
                        for (int j = 0; j < 8; j++) {
                            currentPiece.drawValidMove();
                            if (this.getSquare(i, j).checkOutline()) {
                                currentPiece.move(this.getSquare(i, j));
                                if (!blackKing.isThreatened()) {
                                    System.out.println("inside stalement if black king threatened.");
                                    currentPiece.restoreOldPosition();
                                    return false;
                                }
                                /*System.out.println("Current Piece's coordinates: " + currentPiece.getXPos() + 
                                        " , " + currentPiece.getYPos()); */
                                lastMovedPiece = currentPiece;
                                lastMovedSquare = currentPiece.getSquare();
                                this.removeOutlines();
                                currentPiece.restoreOldPosition();
                                /*System.out.println("Current Piece's new coordinates: " + currentPiece.getXPos() + 
                                        " , " + currentPiece.getYPos()); */
                                
                            }
                            
                            else {
                                System.out.println("Not a legal move.");
                                draw = true;
                                this.removeOutlines();
                                //currentPiece.restoreOldPosition();
                            }
                        }
                    }
                }
                this.removeOutlines();
            }
        }
        return draw;   
    }
    /**
     * Resets or sets up the playing board and pieces.
     */
    //sets up the pieces, try to do this in a loop?
    public void setUpPieces() {
        
        gameOver = false;
        
        for (int i = 0; i<SIZE; i++) {
            for (int j = 0; j<SIZE; j++) {
                if (this.getSquare(i,j).getPiece() != null) {
                    this.getSquare(i, j).getPiece().destroy(i, j);
                }
            }
        }
        
        whitePieces.clear();
        blackPieces.clear();
        
        whiteKing = new King(player1, 7, 4, this);
        blackKing = new King(player2, 0, 4, this);
        whiteQueen = new Queen(player1, 7, 3, this);
        blackQueen = new Queen(player2, 0, 3, this);
        whiteRook1 = new Rook(player1, 7, 0, this);
        whiteRook2 = new Rook(player1, 7, 7, this);
        blackRook1 = new Rook(player2, 0, 0, this);
        blackRook2 = new Rook(player2, 0, 7, this); 
        whiteKnight1 = new Knight(player1, 7, 1, this);
        whiteKnight2 = new Knight(player1, 7, 6, this);
        blackKnight1 = new Knight(player2, 0, 1, this);
        blackKnight2 = new Knight(player2, 0, 6, this);
        whiteBishop1 = new Bishop(player1, 7, 2, this);
        whiteBishop2 = new Bishop(player1, 7, 5, this);
        blackBishop1 = new Bishop(player2, 0, 2, this);
        blackBishop2 = new Bishop(player2, 0, 5, this);
        
        whitePawn1 = new Pawn(player1, 6, 0, this);
        whitePawn2 = new Pawn(player1, 6, 1, this);
        whitePawn3 = new Pawn(player1, 6, 2, this);
        whitePawn4 = new Pawn(player1, 6, 3, this);
        whitePawn5 = new Pawn(player1, 6, 4, this);
        whitePawn6 = new Pawn(player1, 6, 5, this);
        whitePawn7 = new Pawn(player1, 6, 6, this);
        whitePawn8 = new Pawn(player1, 6, 7, this);
        
        blackPawn1 = new Pawn(player2, 1, 0, this);
        blackPawn2 = new Pawn(player2, 1, 1, this);
        blackPawn3 = new Pawn(player2, 1, 2, this);
        blackPawn4 = new Pawn(player2, 1, 3, this);
        blackPawn5 = new Pawn(player2, 1, 4, this);
        blackPawn6 = new Pawn(player2, 1, 5, this);
        blackPawn7 = new Pawn(player2, 1, 6, this);
        blackPawn8 = new Pawn(player2, 1, 7, this);
        
        whitePieces.addAll(Arrays.asList(whitePawn1, whitePawn2, whitePawn3, whitePawn4, whitePawn5, whitePawn6,
                whitePawn7, whitePawn8, whiteRook1, whiteRook2, whiteKnight1, whiteKnight2, whiteBishop1, whiteBishop2,
                whiteKing, whiteQueen));
        blackPieces.addAll(Arrays.asList(blackPawn1, blackPawn2, blackPawn3, blackPawn4, blackPawn5, blackPawn6,
                blackPawn7, blackPawn8, blackRook1, blackRook2, blackKnight1, blackKnight2, blackBishop1, blackBishop2,
                blackKing, blackQueen));
    }
    
    public GridPane getGrid() {
        return grid;
    }
    
    public void setUpPawnChess() {
        gameOver = false;
        
        for (int i = 0; i<SIZE; i++) {
            for (int j = 0; j<SIZE; j++) {
                if (this.getSquare(i,j).getPiece() != null) {
                    this.getSquare(i, j).getPiece().destroy(i, j);
                }
            }
        }
        
        whitePieces.clear();
        blackPieces.clear();
        
        whiteKing = new King(player1, 7, 4, this);
        blackKing = new King(player2, 0, 4, this);
        
        whitePawn1 = new Pawn(player1, 6, 0, this);
        whitePawn2 = new Pawn(player1, 6, 1, this);
        whitePawn3 = new Pawn(player1, 6, 2, this);
        whitePawn4 = new Pawn(player1, 6, 3, this);
        whitePawn5 = new Pawn(player1, 6, 4, this);
        whitePawn6 = new Pawn(player1, 6, 5, this);
        whitePawn7 = new Pawn(player1, 6, 6, this);
        whitePawn8 = new Pawn(player1, 6, 7, this);
        
        blackPawn1 = new Pawn(player2, 1, 0, this);
        blackPawn2 = new Pawn(player2, 1, 1, this);
        blackPawn3 = new Pawn(player2, 1, 2, this);
        blackPawn4 = new Pawn(player2, 1, 3, this);
        blackPawn5 = new Pawn(player2, 1, 4, this);
        blackPawn6 = new Pawn(player2, 1, 5, this);
        blackPawn7 = new Pawn(player2, 1, 6, this);
        blackPawn8 = new Pawn(player2, 1, 7, this);
    }
    
    public void setUpEndgameStudy() {
        gameOver = false;
        Random rand = new Random();
        int random = rand.nextInt(3) + 1;
        
        for (int i = 0; i<SIZE; i++) {
            for (int j = 0; j<SIZE; j++) {
                if (this.getSquare(i,j).getPiece() != null) {
                    this.getSquare(i, j).getPiece().destroy(i, j);
                }
            }
        }
        
        //whitePieces.clear();
        //blackPieces.clear();
        
        switch (random) {
        case 1: 
            whitePieces.clear();
            blackPieces.clear();
            whiteKing = new King(player1, 7, 4, this);
            blackKing = new King(player2, 0, 4, this);
            whiteBishop1 = new Bishop(player1, 7, 2, this);
            whiteBishop2 = new Bishop(player1, 7, 5, this);
            whitePieces.addAll(Arrays.asList(whiteKing, whiteBishop1, whiteBishop2));
            blackPieces.addAll(Arrays.asList(blackKing));
            break;
        
        case 2:
            whitePieces.clear();
            blackPieces.clear();
            whiteKing = new King(player1, 3, 4, this);
            blackKing = new King(player2, 0, 4, this);
            whiteRook1 = new Rook(player1, 1, 7, this);
            blackRook1 = new Rook(player2, 2, 0, this);
            whitePawn1 = new Pawn(player1, 3, 3, this);
            whitePieces.addAll(Arrays.asList(whiteKing, whiteRook1, whitePawn1));
            blackPieces.addAll(Arrays.asList(blackKing, blackRook1));
            
            whitePawn1.setHasMoved(true);
            break;
            
        case 3:
            whitePieces.clear();
            blackPieces.clear();
            whiteKing = new King(player1, 7, 4, this);
            whitePawn1 = new Pawn(player1, rand.nextInt(4) + 2,rand.nextInt(4) + 2, this);
            whiteRook1 = new Rook(player1, 1, 0, this);
            blackRook1 = new Rook(player2, 6, 7, this);
            blackKing = new King(player2, 0, 4, this);
            blackPawn1 = new Pawn(player2, rand.nextInt(4) + 2,rand.nextInt(4) + 2, this);
            
            whitePawn1.setHasMoved(true);
            blackPawn1.setHasMoved(true);
            whitePieces.addAll(Arrays.asList(whiteKing, whiteRook1, whitePawn1));
            blackPieces.addAll(Arrays.asList(blackKing, blackRook1, blackPawn1));
            break;
        }
        
        whiteKing.setHasMoved(true);
        blackKing.setHasMoved(true);
    }
    
    /**
     * Use better logic/design for this lol
     */
    public void setUpRandomChess() {
        
        for (int i = 0; i<SIZE; i++) {
            for (int j = 0; j<SIZE; j++) {
                if (this.getSquare(i,j).getPiece() != null) {
                    this.getSquare(i, j).getPiece().destroy(i, j);
                }
            }
        }
        Set setX = new HashSet();
        Set setY = new HashSet();
        Random rand = new Random();
        
        /*while (setX.size()<8) {

                setX.add(rand.nextInt(8));
        }
        
        while (setY.size()<3) {
            setY.add(rand.nextInt(3));
        } */
        
        whitePieces.clear();
        blackPieces.clear();
        
        whiteKing = new King(player1, 7, 4, this);
        blackKing = new King(player2, 0, 4, this);
        whiteQueen = new Queen(player1, 7, 3, this);
        blackQueen = new Queen(player2, 0, 3, this);
        
        int x;
        int y;
        boolean set = false;
        while (!set) {
            x = 7 - rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whiteRook1 = new Rook(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whiteRook2 = new Rook(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whiteKnight1 = new Knight(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whiteKnight2 = new Knight(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whiteBishop1 = new Bishop(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whiteBishop2 = new Bishop(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whitePawn1 = new Pawn(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whitePawn2 = new Pawn(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whitePawn3 = new Pawn(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whitePawn4 = new Pawn(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whitePawn5 = new Pawn(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whitePawn6 = new Pawn(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whitePawn7 = new Pawn(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = 7-rand.nextInt(3); //vertical
            y = rand.nextInt(8); //horizontal
            if (this.getSquare(x, y).getPiece() == null) {
                whitePawn8 = new Pawn(player1, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackPawn1 = new Pawn(player2, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackPawn2 = new Pawn(player2, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackPawn3 = new Pawn(player2, x, y, this);
                set = true;
            }
        }
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackPawn4 = new Pawn(player2, x, y, this);
                set = true;
            }
        }
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackPawn5 = new Pawn(player2, x, y, this);
                set = true;
            }
        }
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackPawn6 = new Pawn(player2, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackPawn7 = new Pawn(player2, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackPawn8 = new Pawn(player2, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackRook1 = new Rook(player2, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackRook2 = new Rook(player2, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackBishop1 = new Bishop(player2, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackBishop2 = new Bishop(player2, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackKnight1 = new Knight(player2, x, y, this);
                set = true;
            }
        }
        
        set = false;
        while (!set) {
            x = rand.nextInt(3);
            y = rand.nextInt(8);
            if (this.getSquare(x, y).getPiece() == null) {
                blackKnight2 = new Knight(player2, x, y, this);
                set = true;
            }
        }
        
        whitePieces.addAll(Arrays.asList(whitePawn1, whitePawn2, whitePawn3, whitePawn4, whitePawn5, whitePawn6,
                whitePawn7, whitePawn8, whiteRook1, whiteRook2, whiteKnight1, whiteKnight2, whiteBishop1, whiteBishop2
                ));
        blackPieces.addAll(Arrays.asList(blackPawn1, blackPawn2, blackPawn3, blackPawn4, blackPawn5, blackPawn6,
                blackPawn7, blackPawn8, blackRook1, blackRook2, blackKnight1, blackKnight2, blackBishop1, blackBishop2));
        whitePieces.addAll(Arrays.asList(whiteKing, whiteQueen));
        blackPieces.addAll(Arrays.asList(blackKing, blackQueen));
    }
    /**
     * Gets the array of squares in the chessboard
     * @return boardSquares, the array of tiles
     */
    public ChessTile[][] getSquares(){
        return boardSquares;
    }
    
    public Player getPlayer1() {
        return player1;
    }
    
    public Player getPlayer2() {
        return player2;
    }
    
    public Piece getCurrentPiece() {
        return currentPiece;
    }
    
    //this is different from setTurnCount, but how is it used differently?
    public void addTurnCount(int numToAdd) {
        turnCount = turnCount + numToAdd;
    }
    
    public void setTurnCount(int count) {
        turnCount = count;
    }
    
    public int getTurnCounter() {
        return turnCounter;
    }
    
    public ArrayList <Piece> getArrayPieces(Player player) {
        if (player.getPlayerID() == 1) {
            return whitePieces;
        }
        else {
            return blackPieces;
        }
    }
    
    
    public void flipBoard() {
        if (isFlipped == false) {
            grid.setRotate(180);
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    this.getSquare(i, j).flipTile();
                }
            }
            isFlipped = true;
        }
        else {
            grid.setRotate(0);
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    this.getSquare(i, j).unflipTile();
                }
            }
            isFlipped = false;
        }
    }
    
}
