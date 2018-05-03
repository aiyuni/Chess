package chess;

import java.io.Serializable;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class contains the construction of a Player object.
 * Methods include getters/setter for various properties of the Player object, including labels.
 * @author Perry
 *
 */
public class Player implements Serializable{
    
    private int playerID;
    private Label playerTitle;
    private Label capturedPieces;
    private Label status;
    private Color color;
    
    public Player(int id) {
        
        playerID = id;
        if (playerID == 1) {
            playerTitle = new Label("White captures: ");
            color = Color.BLACK;
            
        }
        else {
            playerTitle= new Label("Black captures: ");
            color = Color.RED;
        }
        
        playerTitle.setWrapText(true);
        playerTitle.setFont(Font.font(22));
        
        capturedPieces = new Label();
        capturedPieces.setWrapText(true);
        capturedPieces.setFont(Font.font(22));
        
        status = new Label("Hello there.");
        status.setWrapText(true);
        status.setFont(Font.font(15));
    }
    
    public void writeToLabel(String unicode) {
        capturedPieces.setTextFill(color);
        capturedPieces.setText(getLabelText() + unicode);
    }
    
    public void removeFromLabel(String unicode) {
        capturedPieces.setText(getLabelText().replaceAll(unicode, ""));
    }
    
    public String getLabelText() {
        return capturedPieces.getText();
    }
    /**
     * Gets the player
     * @return playerID
     */
    public int getPlayerID() {
        return playerID;
    }
    
    public Label getTitle() {
        return playerTitle;
    }
    
    public Label getLabel() {
        return capturedPieces;
    }
    
    public Label getStatus() {
        return status;
    }
}
