package di.blackjack;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BlackJackControlador implements Initializable {
    int contador = 5;
    @FXML
    Label tuPuntuacion;

    @FXML
    Label npcPuntuacion;

    @FXML
    Label intentos;

    @FXML
    AnchorPane ordenador;

    @FXML
    AnchorPane jugador;

    @FXML
    VBox pararBox;

    @FXML
    VBox pedirBox;

    @FXML
    Button botonPlay = new Button();

    @FXML
    VBox opciones;

    @FXML
    Button botonParar;

    @FXML
    Button botonPedir;

    @FXML
    Button salir = new Button();

    @FXML
    Button reset = new Button();

    List<Carta> baraja;
    List<Carta> cartasOrdenador;
    List<Carta> cartasJugador;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mostrarOpciones();
    }


    public void iniciarPartida() {


        //Se limpia todos los cambios de la partida anterior
        intentos.setText("Intentos: " + contador);

        mostrarOpciones();

        opciones.getChildren().remove(0);

        //Se oculta la selección de opciones

        botonPlay.setVisible(false);

        //Se crean los mazos y las barajas

        this.baraja = new ArrayList<>();
        this.cartasOrdenador = new ArrayList<>();
        this.cartasJugador = new ArrayList<>();
        this.crearBaraja();

        //Se muestra oda la información y botones de juego

        jugador.setVisible(true);
        ordenador.setVisible(true);
        pararBox.setVisible(true);
        pedirBox.setVisible(true);
        tuPuntuacion.setVisible(true);
        npcPuntuacion.setVisible(true);
        intentos.setVisible(true);

        //Se entregan las dos primeras cartas a los jugadores

        cartaJugador();
        cartaOrdenador();
        cartaJugador();
        cartaOrdenador();

    }

    public void mostrarOpciones() {

        //Limpiamos el tablero

        limpiarTablero();

        //Se crean los botones de las opciones y se muestran

        Image imagePlay = new Image(Objects.requireNonNull(getClass().getResourceAsStream("imagenes/play.png")));
        ImageView iconoPlay = new ImageView();

        iconoPlay.setImage(imagePlay);
        iconoPlay.setPreserveRatio(true);
        iconoPlay.setFitHeight(40);

        opciones.getChildren().add(botonPlay);
        botonPlay.setGraphic(iconoPlay);
        botonPlay.setText("Jugar");
        botonPlay.setFont(Font.font("Arial", FontWeight.BOLD,30));
        botonPlay.setContentDisplay(ContentDisplay.RIGHT);
        botonPlay.setOnMouseClicked(event -> iniciarPartida());

        //Se oculta toda la información y botones de juego

        jugador.setVisible(false);
        ordenador.setVisible(false);
        pararBox.setVisible(false);
        pedirBox.setVisible(false);
        tuPuntuacion.setVisible(false);
        npcPuntuacion.setVisible(false);
        intentos.setVisible(false);

        //Se reinician puntuaciones

        tuPuntuacion.setText("Tu puntuación: 0");
        npcPuntuacion.setText("Puntuación npc: ??");

    }

    private void limpiarTablero() {

        //Se limpian los mazos de ambos jugadores

        ordenador.getChildren().clear();
        jugador.getChildren().clear();
        opciones.getChildren().clear();

        //Se deshabilitan los botones de juego

        botonPedir.setDisable(false);
        botonParar.setDisable(false);
    }

    public void terminarTurno() {

        Alert ganador = new Alert(Alert.AlertType.INFORMATION);
        Alert perdedor = new Alert(Alert.AlertType.ERROR);
        Alert empate = new Alert(Alert.AlertType.CONFIRMATION);

        ganador.setTitle("GANADOR");
        ganador.setHeaderText("Maravillosa jugada");
        ganador.setContentText("Enhorabuena por la victoria");
        perdedor.setTitle("DERROTA");
        perdedor.setHeaderText("Lamento decirte que has perdido");
        perdedor.setContentText("Perdiste. Vuelve a intentarlo");
        empate.setTitle("EMPATE");
        empate.setHeaderText("Mala suerte. Vuelve a intentarlo");
        empate.setContentText("La banca gana");

        //Se voltea la primera carta de la máquina para comparar puntuaciones

        mostrarCartaOrdenador();
        npcPuntuacion.setText("Puntuación npc: " + puntuarOrdenador());


        //Comprobamos si la puntuación del jugador supera los 21 puntos

        if (puntuarJugador() < 22) {

            //La puntuacion del jugador no supera los 21 puntos

            //Comprobamos si hay algún BlackJack

            if (puntuarJugador() == 21 || (puntuarOrdenador() == 21)) {

                //Hay por lo menos un BlackJack

                if (puntuarOrdenador() == puntuarJugador()) {

                    //Ambos tienen BlackJack (Empate)

                    empate.showAndWait();
                    contador = contador - 1;
                    intentos.setText("Intentos: " + contador);


                } else {

                    //Solo hay un BlackJack

                    if (puntuarJugador() == 21) {

                        //El jugador tiene BlackJack (Victoria)

                        ganador.showAndWait();
                        contador = contador + 2;
                        intentos.setText("Intentos: " + contador);


                    } else {

                        //La maquina tiene BlackJack (Derrota)

                        perdedor.showAndWait();
                        contador = contador - 1;
                        intentos.setText("Intentos: " + contador);


                    }
                }

            } else {

                //No hay ningún BlackJack

                //Turno de la máquina

                //Si la máquina tiene menos puntos que el jugador pedirá una carta

                while (puntuarOrdenador() < puntuarJugador()) {
                    cartaOrdenador();
                }

                //La máquina ya tiene mss puntos que el jugador

                npcPuntuacion.setText("Puntuación npc: " + puntuarOrdenador());

                //Comprobamos si la máquina tiene una puntuación superior a 21

                if (puntuarOrdenador() > 21) {

                    //Es superior a 21 (Victoria)

                    ganador.showAndWait();
                    contador = contador + 1;
                    intentos.setText("Intentos: " + contador);


                } else if (puntuarOrdenador() > puntuarJugador()){

                    //No es superior a 21 (Derrota)

                    perdedor.showAndWait();
                    contador = contador - 1;
                    intentos.setText("Intentos: " + contador);


                }else if (puntuarOrdenador() == puntuarJugador()){
                    empate.showAndWait();
                    contador = contador - 1;
                    intentos.setText("Intentos: " + contador);

                }else {
                    ganador.showAndWait();
                    contador = contador + 1;
                    intentos.setText("Intentos: " + contador);

                }
            }
        } else {

            //Tu puntuacion supera los 21 puntos (Derrota)

            perdedor.showAndWait();
            contador = contador - 1;
            intentos.setText("Intentos: " + contador);

        }
        terminarPartida();

        if(contador == 0){
            Alert sinIntentos = new Alert(Alert.AlertType.ERROR);
            sinIntentos.setTitle("SIN INTENTOS");
            sinIntentos.setContentText("Te quedaste sin intentos");
            contador = 5;
            mostrarOpciones();
        }
    }
    private void terminarPartida() {

        //Se muestra los botones que permiten reiniciar la partida o salir

        Image imageRestart = new Image(Objects.requireNonNull(getClass().getResourceAsStream("imagenes/restart.png")));
        ImageView iconoRestart = new ImageView();

        iconoRestart.setImage(imageRestart);
        iconoRestart.setPreserveRatio(true);
        iconoRestart.setFitHeight(40);

        Image imagePlay = new Image(Objects.requireNonNull(getClass().getResourceAsStream("imagenes/play.png")));
        ImageView iconoPlay = new ImageView();

        iconoPlay.setImage(imagePlay);
        iconoPlay.setPreserveRatio(true);
        iconoPlay.setFitHeight(40);

        opciones.getChildren().addAll(botonPlay, salir);
        opciones.setSpacing(30);
        salir.setGraphic(iconoRestart);
        salir.setText("Salir al menú");
        salir.setFont(Font.font("Arial", FontWeight.BOLD,30));
        salir.setContentDisplay(ContentDisplay.RIGHT);
        salir.setOnAction(mouseEvent -> {
            mostrarOpciones();
            contador = 5;
        });

        botonPlay.setText("Jugar de nuevo");
        botonPlay.setVisible(true);

        //Se borran todos los mazos y la baraja

        cartasOrdenador.clear();
        cartasJugador.clear();

        //Se deshabilitan los botones de juego

        botonPedir.setDisable(true);
        botonParar.setDisable(true);
    }



    //Método para voltear la primera carta de lá máquina
    private void mostrarCartaOrdenador() {
        ImageView im = (ImageView) ordenador.getChildren().get(0);
        Carta c = cartasOrdenador.get(0);
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("cartas/" + c.getImagen() + ".png")));
        im.setImage(img);
    }

    //Método para crear una baraja
    public void crearBaraja() {
        char[] palos = {'C', 'T', 'P', 'D'};
        String[] nombres = {"AS", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        int valor;

        for (char palo : palos) {
            for (int i = 0; i < 13; i++) {
                valor = (i >= 10) ? 10 : i + 1;
                Carta carta = new Carta(palo, nombres[i], valor);
                this.baraja.add(carta);
            }
        }
    }

    //Genera una carta aleatoria para ser entregada
    public Carta sacarCarta() {
        Carta carta = null;
        Random aleatorio = new Random(System.currentTimeMillis());
        boolean control = true;
        while (control) {
            carta = this.baraja.get(aleatorio.nextInt(52));
            if (!carta.isRepartida()) {
                carta.setRepartida(true);
                control = false;
            }
        }

        return carta;
    }

    //Método para repartir una carta al jugador
    public void cartaJugador() {
        this.cartasJugador.add(this.sacarCarta());
        Carta ultimaCarta = this.cartasJugador.get(this.cartasJugador.size() - 1);
        String cartaSacada = ultimaCarta.getImagen();

        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("cartas/" + cartaSacada + ".png")));
        ImageView image = new ImageView();

        image.setImage(img);
        image.setPreserveRatio(true);
        image.setFitHeight(200);
        image.setLayoutX(60 * (this.cartasJugador.size() - 1));
        image.setEffect(new DropShadow(20, Color.BLACK));
        jugador.getChildren().add(image);

        //Si el jugador recibe un as pasa esto...

        if (ultimaCarta.getValor() == 1) {

            //Comprobamos que la puntuacion se pasa de 21 al sumar 11

            if (puntuarJugador() + 10 <= 21) {

                //Si la puntuacion no se pasa de 21 al sumar 11, se cambia el valor a 11

                ultimaCarta.setValor(11);

            }

            //Si la puntuacion se pasa de 21 al sumar 11 no hacemos nada :)

        }

        puntuarJugador();

        //Si la puntuación del jugador supera los 20 puntos, termina el turno automáticamente
        if (puntuarJugador() >= 21) {
            terminarTurno();
        }

    }

    //Método para repartir una carta al ordenador
    public void cartaOrdenador() {
        this.cartasOrdenador.add(this.sacarCarta());
        Carta ultimaCarta = this.cartasOrdenador.get(this.cartasOrdenador.size() - 1);
        String cartaSacada;

        //Pone la primera carta de la máquina boca abajo
        if (this.cartasOrdenador.size() == 1) {
            cartaSacada = "Trasera";
        } else {
            cartaSacada = ultimaCarta.getImagen();
        }

        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("cartas/" + cartaSacada + ".png")));
        ImageView image = new ImageView();

        image.setImage(img);
        image.setPreserveRatio(true);
        image.setFitHeight(200);
        image.setLayoutX(60 * (this.cartasOrdenador.size() - 1));
        image.setEffect(new DropShadow(20, Color.BLACK));

        ordenador.getChildren().add(image);

        //Si el ordenador recibe un as pasa esto...

        if (ultimaCarta.getValor() == 1) {

            //Comprobamos que la puntuacion se pasa de 21 al sumar 11

            if (puntuarJugador() + 10 <= 21) {

                //Si la puntuacion no se pasa de 21 al sumar 11, se cambia el valor a 11

                ultimaCarta.setValor(11);

            }

            //Si la puntuacion se pasa de 21 al sumar 11 no hacemos nada :)

        }
    }

    //Establece la puntuación de la máquina con la suma de los valores de las cartas
    public int puntuarOrdenador() {
        AtomicInteger suma = new AtomicInteger();
        this.cartasOrdenador.forEach(carta -> {
            suma.addAndGet(carta.getValor());
        });
        return suma.intValue();
    }


    //Establece la puntuación del jugador con la suma de los valores de las cartas
    public int puntuarJugador() {
        AtomicInteger suma = new AtomicInteger();
        this.cartasJugador.forEach(carta -> {
            suma.addAndGet(carta.getValor());
        });
        tuPuntuacion.setText("Tu puntuación: " + suma);
        return suma.intValue();
    }

    //Getters & Setters
    public List<Carta> getBaraja() {
        return baraja;
    }

    public void setBaraja(List<Carta> baraja) {
        this.baraja = baraja;
    }

    public List<Carta> getOrdenador() {
        return cartasOrdenador;
    }

    public void setOrdenador(List<Carta> cartasOrdenador) {
        this.cartasOrdenador = cartasOrdenador;
    }

    public List<Carta> getJugador() {
        return cartasJugador;
    }

    public void setJugador(List<Carta> cartasJugador) {
        this.cartasJugador = cartasJugador;
    }

}