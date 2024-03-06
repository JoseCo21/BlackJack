package di.blackjack;

public class Carta {
    private Character palo;
    private String nombre;
    private int valor;
    private String imagen;
    private boolean repartida; //para saber si está en juego o no

    //Constructor
    public Carta(Character palo, String nombre, int valor) {
        this.palo = palo;
        this.nombre = nombre;
        this.valor = valor;
        this.imagen=nombre+palo;
        this.repartida=false;
    }

    public Character getPalo() {
        return palo;
    }

    public void setPalo(Character palo) {
        this.palo = palo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isRepartida() {
        return repartida;
    }

    public void setRepartida(boolean repartida) {
        this.repartida = repartida;
    }

    @Override
    public String toString() {
        return "Carta{" +
                "palo=" + palo +
                ", nombre='" + nombre + '\'' +
                ", valor=" + valor +
                ", imagen='" + imagen + '\'' +
                ", repartida=" + repartida +
                '}';
    }
}
