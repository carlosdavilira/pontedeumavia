/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhoso;

import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.*;
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
import static java.lang.Thread.sleep;
import javafx.scene.layout.StackPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Luan P
 */


public class TrabalhoSO extends Application{
    
    private static String sentido_ponte = "vazio";
    private static int ponte=0;
    private static int numcar = 10;
    
    private static Semaphore oeste = new Semaphore(numcar);
    private static Semaphore leste = new Semaphore(numcar);
    private static Semaphore mutex = new Semaphore(1);
    
    private static ArrayList<Carro> lista_oeste = new ArrayList<Carro>();
    private static ArrayList<Carro> lista_leste = new ArrayList<Carro>();
    private static ArrayList<Carro> liscar = new ArrayList<Carro>();
    

    public static void conta_tempo(int tempo){
    
        
        long inicio = System.currentTimeMillis();
        long fim = inicio;
        
        while((fim-inicio)<tempo){
            
            fim = System.currentTimeMillis();

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
        
        private Semaphore acorda = new Semaphore(1);

        public Carro(int id,int tempo_travessia,int tempo_espera,String origem, ImageView btncar, Stage stage,ImageView btncar2){

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
            
        }


        public void run(){
            
            while(mata_thread==0){
            
                try{
                    
                    acorda.acquire();
                
                    mutex.acquire();
                    if(origem.equals("oeste")){
                        lista_oeste.add(this);
                    }
                    else{
                        lista_leste.add(this);
                    }
                    mutex.release();
                    
                    // carro esperando
      
                    btncar.setScaleX(0.08);
                    btncar.setScaleY(0.2);
                    btncar.setLayoutY(-95);
                    
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
                        btncar.setRotate(180);
                        btncar.setX(-325);
                         
                        btncar2.setRotate(180);
                        
                        stage.setX(posx + 15 + new Random().nextInt(186));
                        
                        for(int i=tempo_espera-1;i>0;i--){
                        
                            if(i<10){
                                conta_tempo(1000);
                                btncar2.setImage(new Image(new FileInputStream("C:\\images\\ferro"+i+".png")));
                            }
                            else{
                                conta_tempo(1000);
                                int gd3 = (i%5)+5;
                                btncar2.setImage(new Image(new FileInputStream("C:\\images\\ferro"+gd3+".png")));
                            }
                        
                        }
                        
                        conta_tempo(1000);
                        btncar2.setImage(new Image(new FileInputStream("C:\\images\\truck.png")));
                         
                    }
                    else{
                        
                        
                        btncar.setImage(new Image(new FileInputStream("C:\\images\\truck.png")));
                        btncar.setRotate(0);
                        btncar.setX(550);
                        
                        btncar2.setImage(new Image(new FileInputStream("C:\\images\\truck.png"))); 
                        btncar2.setRotate(0);
                        
                        stage.setX(posx + 700 + new Random().nextInt(191));
                        
                        int gd5 = 5;
                        
                        for(int i=1;i<tempo_espera+1;i++){
                        
                            if(i<10){
                               conta_tempo(1000);
                               btncar2.setImage(new Image(new FileInputStream("C:\\images\\ferro"+i+".png"))); 
                            }
                            else{
                               conta_tempo(1000);  
                               btncar2.setImage(new Image(new FileInputStream("C:\\images\\ferro"+gd5+".png")));
                               gd5++; 
                               if(gd5==10){
                                   gd5=5;
                               } 
                            }
                            
                        }
 
                       
                    }
                    
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
                                System.out.println("Carro "+id+" entrou na ponte");

                            }
                            else{
                                System.out.println("Carro "+id+" dorme");
                                Thread.sleep(1000);
                            }
                        }

                        else{

                            if(!sentido_ponte.equals(origem) && oeste.availablePermits()==numcar && leste.availablePermits()>0){
                                acorda.release();
                                System.out.println("Carro "+id+" entrou na ponte");
                            }
                            else{
                                System.out.println("Carro "+id+" dorme");
                                Thread.sleep(1000);
                            }

                        }
                    }
                    
                    if(origem.equals("oeste")){
                          
                        oeste.acquire(1);

                        int ind = lista_oeste.indexOf(this)-1;
                        
                        mutex.acquire();
                        sentido_ponte="leste";
                        ponte++;
                        mutex.release();

                        for(int i=0;i<tempo_travessia;i++){
                            Thread.sleep(1000);
                            System.out.println("Carro "+id+" atravessando");
                        }

                        mutex.acquire();
                        System.out.println("Carro "+id+" atravessou");
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
                        ponte++;
                        mutex.release();

                        for(int i=0;i<tempo_travessia;i++){
                            Thread.sleep(1000);
                            System.out.println("Carro "+id+" atravessando");
                        }

                        mutex.acquire();
                        System.out.println("Carro "+id+" atravessou");
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
        public void MovimentarCarro() throws InterruptedException{
          
                if(this.origem.equals("oeste"))
                {                   
                   Carro carroGenerico = this;
                    new Thread()
                    {
                        @Override
                        public void run(){
                            
                        for(int i = -325 ; carroGenerico.btncar.getX() != 550 ; i++)
                          {
                            carroGenerico.btncar.setX(i);
                         
                            try {
                                sleep(50);
                            } catch (InterruptedException ex) {
                                //Logger.getLogger(TrabalhoSO.class.getName()).log(Level.SEVERE, null, ex);
                              System.out.println("MovimentarCarro() - Oeste "+carroGenerico.btncar.getX());

                            }
                          }
                           
                    }   
                }.start();
                }
                else
                {                    
                  Carro carroGenerico = this;
                    new Thread()
                    {
                    @Override
                    public void run(){
                   
                    for(int i = 550;carroGenerico.btncar.getX() != -325; i--)
                    {
                        carroGenerico.btncar.setX(i);
                        try {
                            sleep(50);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Pontedeumvia.TrabalhoSO.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("MovimentarCarro() - Leste "+i);

                    }
                    }   
                }.start();
                }
            
        }
        
    }
    

    public static void main(String[] args) {
        // TODO code application logic here
        
        /*Carro carro1 = new Carro(liscar.size()+1,10,10,"oeste");
        liscar.add(carro1);
        
        Carro carro2 = new Carro(liscar.size()+1,10,10,"oeste");
        liscar.add(carro2);
        
        Carro carro3 = new Carro(liscar.size()+1,10,5,"leste");
        liscar.add(carro3);
        
        try{
        carro1.start();
        Thread.sleep(5);
        carro2.start();
        Thread.sleep(5);
        carro3.start();
        }catch(Exception e){}*/
        
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
                    
                    Carro carro = new Carro(liscar.size()+1,tempo_travessia1,tempo_espera1,origem1,btncar,stage2,btncar2);
                    liscar.add(carro);
                    carro.start();
                    root.getChildren().add(btncar);
                     try {
                        carro.MovimentarCarro();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Pontedeumvia.TrabalhoSO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                
            });
            
            // cenario

            Scene scene = new Scene(root,1200,700); 

            stage.setScene(scene);  

            stage.show(); 
        
        }catch(Exception e){}
        
    }
    
}
