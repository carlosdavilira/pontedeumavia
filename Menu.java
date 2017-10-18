/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhoso;

import java.util.concurrent.Semaphore;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.*;
import javafx.scene.canvas.Canvas;
import javafx.animation.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.util.*;
import javafx.event.*;
import javafx.geometry.*;
import java.io.FileInputStream;
import javafx.stage.Stage;  
import java.io.FileNotFoundException;
import javafx.scene.layout.StackPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.animation.FadeTransition;
import static javafx.application.Application.launch;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;

/**
 *
 * @author Luan P
 */
public class Menu extends Application{
    
    private static String[] args2;
    
    public static void main(String[] args) {

        launch(args);
        
    }

    @Override
    public void start(Stage stage) throws Exception{
        
        stage.setTitle("Trabalho SO - menu");  
            
        Image back10 = new Image(new FileInputStream("C:\\images\\fundomenu.png"));
        ImageView back = new ImageView(back10);
        back.setScaleX(0.65);
        back.setScaleY(0.8);
        back.setX(-185);
        back.setY(-100);
        
        ComboBox origem = new ComboBox();
            
        origem.setMaxWidth(200);
        origem.setMinWidth(200);
        origem.setMaxHeight(50);
        origem.setMinHeight(50);
        origem.setLayoutX(100);
        origem.setLayoutY(300);
        origem.getItems().addAll("leste","oeste","sem preferência");
        origem.setValue("leste");
        
        Button btncriar = new Button();
        btncriar.setText("Começar");
        btncriar.setMaxWidth(150);
        btncriar.setMinWidth(150);
        btncriar.setMaxHeight(50);
        btncriar.setMinHeight(50);
        btncriar.setLayoutX(310);
        btncriar.setLayoutY(300);
        
        Group root = new Group(back,origem,btncriar);
        
        Scene scene = new Scene(root,600,400); 

        stage.setScene(scene); 
        
        btncriar.setOnAction(new EventHandler<ActionEvent>(){
        
            public void handle(ActionEvent event){
                
                try{
                    String strpref = origem.getValue().toString();

                    if(strpref.equals("leste")){
                       Application app2 = TrabalhoSO_Leste.class.newInstance(); 
                       Stage anotherStage = new Stage();
                       app2.start(anotherStage);
                    }
                    else if(strpref.equals("oeste")){
                       Application app2 = TrabalhoSO_Oeste.class.newInstance(); 
                       Stage anotherStage = new Stage();
                       app2.start(anotherStage); 
                    }
                    else{
                       Application app2 = TrabalhoSO.class.newInstance(); 
                       Stage anotherStage = new Stage();
                       app2.start(anotherStage);
                    }

                    stage.close();
                }catch(Exception ex){}
                
            }
            
        });

        stage.show();
        
    }
    
      
}
