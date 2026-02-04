package g6.dynamodb;

import java.util.List;

public class Aula {
    
    private String id;
    private Integer tamaño;
    private List<Reserva> reservas;
    
    public Aula() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Integer getTamaño() {
        return tamaño;
    }
    public void setTamaño(Integer tamaño) {
        this.tamaño = tamaño;
    }
    public List<Reserva> getReservas() {
        return reservas;
    }
    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }


}
