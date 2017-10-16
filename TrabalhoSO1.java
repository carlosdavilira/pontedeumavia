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
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;

/**
 *
 * @author Luan P
 */


public class TrabalhoSO extends Application{
    
    private static String sentido_ponte = "vazio";
    private static int ponte=0;
    private static int numcar = 10;
    private static double espaco=0;
    
    private static Semaphore oeste = new Semaphore(numcar);
    private static Semaphore leste = new Semaphore(numcar);
    private static Semaphore mutex = new Semaphore(1);
    
    private static ArrayList<Carro> lista_oeste = new ArrayList<Carro>();
    private static ArrayList<Carro> lista_leste = new ArrayList<Carro>();
    private static ArrayList<Carro> liscar = new ArrayList<Carro>();
    private static ArrayList<Carro> lisponte = new ArrayList<Carro>();
    

    public static void conta_tempo(int tempo){
    
        
        long inicio = System.currentTimeMillis();
        long fim = inicio;
        
        while((fim-inicio)<tempo){
            
            fim = System.currentTimeMillis();

        }
    
    }
    
    public static boolean colide(Carro img1,Carro img2){
    
        /*Bounds bound1 = img1.localToScene(img1.getBoundsInLocal());
	Bounds bound2 = img2.localToScene(img2.getBoundsInLocal());
        
        if(bound1.contains(bound2)){
            return(true);
        }
        else{
            return(false);
        }*/
        
        if(img1.retcar.intersects(img2.retcar.getBoundsInParent())){
            return(true);
        }
        else{
            return(false);
        }
        
        
    }
    
    public static class Carro extends Thread{
    
        private int id;
        private int tempo_travessia;
        private int tempo_espera;
        private String origem;
        private int guarda_tempo;
        private int mata_thread=0;
        private ImageView btncar;
        private Stage stage;
        private ImageView btncar2;
        private double posx = 0;
        private double posy = 0;
        private int tempf = 0;
        private Rectangle retcar;
        
        private Semaphore acorda = new Semaphore(1);

        public Carro(int id,int tempo_travessia,int tempo_espera,String origem, ImageView btncar, Stage stage,ImageView btncar2,Rectangle retcar){

            this.id = id;
            this.tempo_espera = tempo_espera;
            this.tempo_travessia = tempo_travessia;
            this.origem = origem;
            this.guarda_tempo = tempo_travessia;
            this.btncar = btncar;
            this.stage = stage;
            this.btncar2 = btncar2;
            this.posx = stage.getX();
            this.posy = stage.getY();
            this.tempf = tempo_travessia*1000;
            this.retcar = retcar;
            
        }


        public void run(){
            
            while(mata_thread==0){
            
                try{
                    
                    acorda.acquire();
                
                    mutex.acquire();
                    if(origem.equals("oeste")){
                        
                        /*if(lista_oeste.size()>0){
                            tempo_travessia = lista_oeste.get(lista_oeste.size()-1).tempo_travessia + 2;
                        }*/
                        
                        lista_oeste.add(this);
                        
                        btncar.setRotationAxis(Rotate.Y_AXIS);
                        btncar.setRotate(180);
                        btncar.setX(-323);
                         
                        btncar2.setRotationAxis(Rotate.Y_AXIS);
                        btncar2.setRotate(180);
                        
                        stage.setX(posx + 15 + new Random().nextInt(186));
                        
                    }
                    else{
                        lista_leste.add(this);
                        
                        btncar.setRotate(0);
                        btncar.setX(545);
                        btncar2.setRotate(0);
                        
                        stage.setX(posx + 700 + new Random().nextInt(191));
                        
                    }
                    
                    
                    mutex.release();
                    
                    // carro esperando
      
                    btncar.setScaleX(0.08);
                    btncar.setScaleY(0.2);
                    btncar.setLayoutY(-95);
                    
                    retcar.setX(btncar.getX());
                    retcar.setLayoutY(-95);
                    retcar.setScaleX(btncar.getScaleX());
                    retcar.setScaleY(btncar.getScaleY());
                    
                    btncar2.setScaleX(0.3);
                    btncar2.setScaleY(0.4);
                    stage.setY(posy + 290 + new Random().nextInt(241));
                    
                    if(origem.equals("oeste")){
                        
                        if(tempo_espera<9){
                            btncar2.setImage(new Image(new FileInputStream("C:\\images\\ferro"+tempo_espera+".png")));
                        }
                        else{
                            int gd2 = (tempo_espera%5)+5;
                            btncar2.setImage(new Image(new FileInputStream("C:\\images\\ferro"+gd2+".png")));
                        }
                        
                        btncar.setImage(btncar2.getImage());
                        
                        
                        for(int i=tempo_espera-1;i>0;i--){
                        
                            if(i<10){
                                System.out.println("Carro "+id+" esperando");
                                conta_tempo(1000);
                                btncar2.setImage(new Image(new FileInputStream("C:\\images\\ferro"+i+".png")));
                            }
                            else{
                                System.out.println("Carro "+id+" esperando");
                                conta_tempo(1000);
                                int gd3 = (i%5)+5;
                                btncar2.setImage(new Image(new FileInputStream("C:\\images\\ferro"+gd3+".png")));
                            }
                        
                        }
                        
                        System.out.println("Carro "+id+" esperando");
                        conta_tempo(1000);
                        btncar2.setImage(new Image(new FileInputStream("C:\\images\\truck.png")));
                        btncar.setImage(btncar2.getImage());
                         
                    }
                    else{
                        
                        
                        btncar.setImage(new Image(new FileInputStream("C:\\images\\truck.png")));
                        
                        btncar2.setImage(new Image(new FileInputStream("C:\\images\\truck.png"))); 
                        
                        
                        int gd5 = 5;
                        
                        for(int i=1;i<tempo_espera+1;i++){
                        
                            if(i<10){
                               System.out.println("Carro "+id+" esperando");
                               conta_tempo(1000);
                               btncar2.setImage(new Image(new FileInputStream("C:\\images\\ferro"+i+".png"))); 
                            }
                            else{
                               System.out.println("Carro "+id+" esperando"); 
                               conta_tempo(1000);  
                               btncar2.setImage(new Image(new FileInputStream("C:\\images\\ferro"+gd5+".png")));
                               gd5++; 
                               if(gd5==10){
                                   gd5=5;
                               } 
                            }
                            
                        }
 
                        btncar.setImage(btncar2.getImage());
                       
                    }
                    
                    System.out.println("Carro "+id+" tenta atravessar");
                    // carro tenta atravessar
                    
                    Image img1 = btncar2.getImage();
                    
                    for(int i=1;i<4;i++){
                        conta_tempo(20);
                        btncar2.setImage(new Image(new FileInputStream("C:\\images\\tenta"+i+".png")));
                    }

                    while(acorda.availablePermits()==0){
                        if(origem.equals("oeste")){

                            if(!sentido_ponte.equals(origem) && leste.availablePermits()==numcar && oeste.availablePermits()>0){
                                acorda.release();
                            }
                            else{
                                System.out.println("Carro "+id+" dorme");
                                
                                for(int j=1;j<8;j++){
                                        
                                    btncar2.setImage(new Image(new FileInputStream("C:\\images\\dorme"+j+".png")));
                                    conta_tempo(1);

                                }
                                
                            }
                        }

                        else{

                            if(!sentido_ponte.equals(origem) && oeste.availablePermits()==numcar && leste.availablePermits()>0){
                                acorda.release();
                            }
                            else{
                                System.out.println("Carro "+id+" dorme");
                                
                                for(int j=1;j<8;j++){
                                        
                                    btncar2.setImage(new Image(new FileInputStream("C:\\images\\dorme"+j+".png")));
                                    conta_tempo(1);

                                }
                                
                            }

                        }
                    }
                    
                    System.out.println("Carro "+id+" entrou na ponte");
                    // atravessando a ponte
                    
                    if(origem.equals("oeste")){
                          
                        oeste.acquire(1);

                        int ind = lista_oeste.indexOf(this)-1;
                        
                        mutex.acquire();
                        sentido_ponte="leste";
                        lisponte.add(this);
                        ponte++;
                        mutex.release();
                        
                        Timeline timeline = new Timeline();
                        final KeyValue kv = new KeyValue(btncar.xProperty(), 545);
                        final KeyFrame kf = new KeyFrame(Duration.millis(tempo_travessia*1000), kv);
                        timeline.getKeyFrames().add(kf);
                        timeline.play();
                        /*timeline.setOnFinished(new EventHandler<ActionEvent>(){
                            public void handle(ActionEvent event){
                                timeline.stop();
                            }
                        });
                        
                        while(timeline.getStatus().equals(Timeline.Status.RUNNING) || 
                                timeline.getStatus().equals(Timeline.Status.PAUSED)){
                        
                            retcar.setX(btncar.getX());
                            
                            if((lisponte.indexOf(this)>0) &&
                                    colide(this,lisponte.get(lisponte.indexOf(this)-1))){
                                timeline.pause();
                            }
                            else{
                                if(timeline.getStatus().equals(Timeline.Status.PAUSED)){
                                    timeline.play();
                                }
                            }
                        
                        }*/
                        
                        int l = tempo_travessia;
                        for(int i=0;i<l;i++){
                            
                            System.out.println("Carro "+id+" atravessando");
                            for(int j=0;j<500;j++){
                                conta_tempo(1);
                            }
                            btncar2.setImage(new Image(new FileInputStream("C:\\images\\truck2.png")));
                            for(int j=0;j<500;j++){
                               
                                conta_tempo(1);
                            }
                            btncar2.setImage(new Image(new FileInputStream("C:\\images\\truck.png")));
                            
                        }
                        timeline.stop();

                        mutex.acquire();
                        System.out.println("Carro "+id+" atravessou");
                        lisponte.remove(this);
                        ponte--;
                        lista_oeste.remove(this);
                        
                        origem = "leste";
                        mutex.release();
                        
                        oeste.release(1);
                        
                        if(oeste.availablePermits()==numcar && leste.availablePermits()==numcar){
                            sentido_ponte="vazio";
                        }
                                                
                    }
                    else{
                    
                        leste.acquire(1);
                        
                        int ind = lista_leste.indexOf(this)-1;

                        mutex.acquire();
                        sentido_ponte="oeste";
                        lisponte.add(this);
                        ponte++;
                        mutex.release();

                        final Timeline timeline = new Timeline();
                        final KeyValue kv = new KeyValue(btncar.xProperty(), -323);
                        final KeyFrame kf = new KeyFrame(Duration.millis(tempo_travessia*1000), kv);
                        timeline.getKeyFrames().add(kf);
                        timeline.play();
                        
                        int l = tempo_travessia;
                        for(int i=0;i<l;i++){
                            
                            System.out.println("Carro "+id+" atravessando");
                            for(int j=0;j<500;j++){
                                conta_tempo(1);
                            }
                            btncar2.setImage(new Image(new FileInputStream("C:\\images\\truck2.png")));
                            for(int j=0;j<500;j++){
                               
                                conta_tempo(1);
                            }
                            btncar2.setImage(new Image(new FileInputStream("C:\\images\\truck.png")));
                            
                        }
                        timeline.stop();

                        mutex.acquire();
                        System.out.println("Carro "+id+" atravessou");
                        lisponte.remove(this);
                        ponte--;

                        lista_leste.remove(this);
                        
                        origem = "oeste";
                        mutex.release();
                        
                        leste.release(1);
                        
                        if(oeste.availablePermits()==numcar && leste.availablePermits()==numcar){
                            sentido_ponte="vazio";
                        }
                        
                    }

                }catch(Exception e){}
            
            }
                
        }
        
    }
    

    public static void main(String[] args) {
        
        launch(args);
        
    }
    
    
    public void start(Stage stage) {

        try{
            
            stage.setTitle("Trabalho SO");  
            
            //imagens
            
            Image back1 = new Image(new FileInputStream("C:\\images\\fundo.png"));
            Image side1 = new Image(new FileInputStream("C:\\images\\side2.png"));
            Image mina1 = new Image(new FileInputStream("C:\\images\\mina2.png"));
            
            ImageView back = new ImageView(back1);
            ImageView side = new ImageView(side1);
            ImageView mina = new ImageView(mina1);
            
            mina.setOnMousePressed(new EventHandler<MouseEvent>(){
            
                public void handle(MouseEvent event){
                    System.out.println("mina");
                }
            
            });
            
            side.setLayoutX(0);
            mina.setLayoutX(1000);
            
            // entradas
            
            ComboBox origem = new ComboBox();
            
            origem.setMaxWidth(150);
            origem.setMinWidth(150);
            origem.setLayoutX(525);
            origem.setLayoutY(320);
            origem.getItems().addAll("leste","oeste");
            
            
            TextField tempo_espera = new TextField();
            tempo_espera.setMaxWidth(150);
            tempo_espera.setMinWidth(150);
            tempo_espera.setLayoutX(525);
            tempo_espera.setLayoutY(430);
            
            TextField tempo_travessia = new TextField();
            tempo_travessia.setMaxWidth(150);
            tempo_travessia.setMinWidth(150);
            tempo_travessia.setLayoutX(525);
            tempo_travessia.setLayoutY(530);
            
            // botoes
            
            Button btncriar = new Button();
            btncriar.setText("Criar Carro");
            btncriar.setMaxWidth(150);
            btncriar.setMinWidth(150);
            btncriar.setLayoutX(525);
            btncriar.setLayoutY(600);
            
            Group root = new Group(back,side,mina,origem,tempo_espera,tempo_travessia,btncriar);
            
            btncriar.setOnAction(new EventHandler<ActionEvent>() {
 
                public void handle(ActionEvent event) {
                    String origem1= origem.getValue().toString();
                    int tempo_travessia1 = Integer.parseInt(tempo_travessia.getText().toString());
                    int tempo_espera1 = Integer.parseInt(tempo_espera.getText().toString());
                    
                    ImageView btncar = new ImageView();
                    ImageView btncar2 = new ImageView();
                    Rectangle retcar = new Rectangle(btncar.getFitHeight(),btncar.getFitHeight(),btncar.getFitWidth(),btncar.getFitWidth());
                    retcar.setFill(Paint.valueOf("red"));
                    
                    btncar2.setLayoutX(-347);
                    btncar2.setLayoutY(-115);

                    Group root2 = new Group(btncar2);

                    Scene scene2 = new Scene(root2,294,156);

                    Stage stage2 = new Stage();
                    stage2.setX(stage.getX());
                    stage2.setY(stage.getY());

                    stage2.setScene(scene2);
                    
                    stage2.setTitle("Carro "+(liscar.size()+1));

                    stage2.show();
                    stage2.toFront();
                    stage.toBack();
                    
                    Carro carro = new Carro(liscar.size()+1,tempo_travessia1,tempo_espera1,origem1,btncar,stage2,btncar2,retcar);
                    liscar.add(carro);
                    carro.start();
                    root.getChildren().add(retcar);
                    root.getChildren().add(btncar);
                    
                }
                
            });
            
            // cenario

            Scene scene = new Scene(root,1200,700); 

            stage.setScene(scene);  

            stage.show(); 
        
        }catch(Exception e){}
        
    }
    
}
