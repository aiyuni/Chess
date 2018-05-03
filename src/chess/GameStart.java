package chess;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import chessboard.Chessboard;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The main class. Creates the window layout. 
 * @author Perry
 * Stuff to work on: the game modes arent fully resetting so there are some null pieces and randomly spawning pieces?
 * Try to make it so that if you click a button, everything gets reset.
 */
public class GameStart extends Application implements Serializable {
    
    private Chessboard board = new Chessboard();
    
    private static HBox top = new HBox();
    private static HBox middle = new HBox();
    private static HBox bottom = new HBox(10);
    private static VBox players = new VBox(20);
    private static VBox root = new VBox(5);
    private static GridPane side = new GridPane();
    
    private static Rectangle box = new Rectangle();
    private static GridPane numbers = new GridPane();
    private static GridPane letters = new GridPane();
    private Text number;
    private Text letter;
    
    private static Player player1;
    private static Player player2;
    
    private static Utility functions;

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        player1 = board.getPlayer1();
        player2 = board.getPlayer2();
        
        functions = new Utility(this, board);
            
        Label title = new Label("Chess for Beginners");
        title.setFont(Font.font(30));        
        
        side.setVgap(50);
        side.setHgap(30);
        setupCoordinates();
        
        top.setAlignment(Pos.CENTER);
        top.getChildren().addAll(title);
        middle.getChildren().addAll(functions.getResetButton(), functions.getUndoButton(), functions.getFlipButton(), functions.getPawnChessButton(), functions.getRandomButton(), functions.getEndgameButton());
        players.getChildren().addAll(player1.getTitle(), player1.getLabel(), player1.getStatus(), player2.getTitle(), player2.getLabel(), player2.getStatus());
        bottom.getChildren().addAll( board.getGrid(), players);
        root.getChildren().addAll(top,middle, bottom);
        Scene scene = new Scene(root,1000, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Worst Chess Game Ever");
        primaryStage.show();
        
       /*//Saving of object in a file
        FileOutputStream file = new FileOutputStream("board.ser");
        ObjectOutputStream out = new ObjectOutputStream(file);
         
        // Method for serialization of object
        out.writeObject(board);
         
        out.close();
        file.close();
         
        System.out.println("Object has been serialized");
        
        try
        {   
            // Reading the object from a file
            FileInputStream file2 = new FileInputStream("board.ser");
            ObjectInputStream in = new ObjectInputStream(file2);
             
            // Method for deserialization of object
            board = (Chessboard)in.readObject();
             
            in.close();
            file.close();
             
            System.out.println("Object has been deserialized ");
        }
         
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        } */
    }
    
    public static void main (String[] args) {
        launch(args);
    }
    
    private Chessboard getBoard () {
        return board;
    }
    
    private GameStart getGame() {
        return this;
    }
    
    public void setupCoordinates() {
        String a;
        for (int i = 8; i > 0; i--) {
            number = new Text(Integer.toString(i));
            box = new Rectangle(50, 50);
            box.setFill(null);
            number.setFont(Font.font(20));
            //number.setLineSpacing(0);
            //number.set
            //number.setTextAlignment(TextAlignment.center);
            numbers.add(number, 0, i * -1 + 8);;
            side.getChildren().addAll(number, box);
        }
        
    }
    
    public void resetState() {
        // TODO Auto-generated method stub

    }
}
