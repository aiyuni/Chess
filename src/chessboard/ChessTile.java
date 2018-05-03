/**
 * 
 */
package chessboard;

import java.io.Serializable;

import chess.Player;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import pieces.Piece;

/**Class that constructs and defines the methods of a single square in the chessboard.
 * 
 * Contains methods that set the properties of the square, sets (assigns) a Piece to the square, gets the Piece
 * that is assigned to the square, highlight/unhighlight the square, draw/remove an outline on 
 * the square, check if the square is outlined, and setters and getters for the position of the square 
 * in the chessboard.  A piece is represented on the square visually via text on a Text object.
 * @author Perry
 *
 */
public class ChessTile extends StackPane implements Serializable {
    
    private Text squareText = new Text();
    private Chessboard board;
    private Piece piece;
    private int x;
    private int y;
    private Color color;
    
    private boolean outlined = false;
    private boolean isEnPassent = false;
    
    private Rectangle border;
    
    public ChessTile(int x, int y, String color, Chessboard chessboard) {
        this.x = x;
        this.y = y;
        board = chessboard;
        
        border = new Rectangle(70,70);
        border.setStrokeType(StrokeType.INSIDE);
        
        if(color == "green") {
            border.setFill(Color.GREEN);
        }
        else {
            border.setFill(Color.LIGHTGREY);
        }
        
        squareText.setFont(Font.font(35));
        getChildren().addAll(border,squareText);
        
    }
    
    /**
     * Sets the piece text and color
     * @param player, as int: 1 for red, 2 for black
     * @param piece, as string, in unicode
     */
    public void setProperties(Player player, String piece) {
        if (player.getPlayerID() == 1) {
            squareText.setText(piece);
            squareText.setFill(Color.RED);
            color = Color.RED;
        }
        else {
            squareText.setText(piece);
            squareText.setFill(Color.BLACK);
            color = color.BLACK;
        }
        
    }
    
    public void highlight(Piece piece) {
        squareText.setFill(Color.YELLOW);
        squareText.setStyle("-fx-text-fill: blue;");
    }
    
    public void unHighlight(Piece piece) {
        squareText.setFill(color);
    }
    
    /**
     * Assigns a Piece to this square.
     * However, does not visually place the Piece on this square, thus
     * must be used in conjuction with setText(String piece)
     * @param piece
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    
    public Piece getPiece() {
        //System.out.println("Inside getPiece method, getting: " + piece);
        return piece;
    }
    
    /**
     * Gets the Text object of the square
     * @return squareText the text object of the square
     */
    public Text getSquareText() {
        return squareText;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * This method visually places the piece on this square via unicode
     * However, this method does not set the Piece onto the square. 
     * Must be used in association with setPiece(Piece piece) 
     * @param piece
     */
    public void setText(String piece) {
        squareText.setText(piece);
    }
    
    public String getText() {
        return squareText.getText();
    }
    
    public void drawOutline() {
        border.setStroke(Color.ORANGE);
        border.setStrokeWidth(3);
        outlined = true;
        
    }
    
    public void removeOutline() {
        border.setStroke(null);
        outlined = false;
    }
    
    public boolean checkOutline() {
        if (outlined) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public void flipTile() {
        squareText.setRotate(180);
    }
    
    public void unflipTile() {
        squareText.setRotate(0);
    }
    
    public void setEnPassent(boolean value) {
        isEnPassent = value;
    }
    
    public boolean getEnPassent() {
        return isEnPassent;
    }
}
