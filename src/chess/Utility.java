package chess;

import java.io.Serializable;

import chessboard.Chessboard;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Popup;
import javafx.stage.PopupBuilder;

/**
 * This defines some utility functions for the chess game. Try to use method references or lambas here instead.
 * @author Perry
 *
 */
public class Utility implements Serializable {
    
    GameStart game;
    Chessboard board; 
    Button reset;
    Button undo;
    Button pawnChess;
    Button endgameStudy;
    Button save;
    Button random;
    Button flip;
    
    public Utility(GameStart game, Chessboard board) {
        this.game = game;
        this.board = board;
        
        reset = new Button("New game");
        reset.setOnMouseClicked(new EventHandler<Event>() {
           
            @Override
            public void handle(Event arg0) {
                resetState();
                board.setUpPieces();
               // board.setTurnCount(1);
                
            }
            
        });
        
        undo = new Button("Undo last move");
        undo.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event arg0) {
                
                board.getCurrentPiece().restoreOldPosition();
                board.addTurnCount(1);
            }
            
        });
        
        pawnChess = new Button("Pawn only mode!");
        pawnChess.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                resetState();
                board.setUpPawnChess();
                
            }
            
        });
        
        endgameStudy = new Button("Endgame study");
        endgameStudy.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                resetState();
               board.setUpEndgameStudy();
                
            }
            
        });
        
        random = new Button("Random chess mode!");
        random.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                resetState();
                board.setUpRandomChess();
                
            }
            
        });
        
        flip = new Button("Flip board");
        flip.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                board.flipBoard();
                
            }
            
        });
        save = new Button("Save Game");
      
    }
    
    /**
     * Clears all class objects.
     */
    public void resetState() {
        
        game.resetState();
    }
    
    public Chessboard getBoard() {
        return board;
    }
    
    public void setBoard(Chessboard board) {
        this.board = board;
    }
    
    public Button getResetButton() {
        return reset;
    }
    
    public Button getUndoButton() {
        return undo;
    }
    
    public GameStart getGame() {
        return game;
    }
    
    public Button getPawnChessButton() {
        return pawnChess;
    }
    
    public Button getEndgameButton() {
        return endgameStudy;
    }
    
    public Button getRandomButton() {
        return random;
    }
    
    public Button getFlipButton() {
        return flip;
    }
   
}
