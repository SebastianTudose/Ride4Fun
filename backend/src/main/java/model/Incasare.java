package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
public class Incasare {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "inchiriere_id")
    private Inchiriere inchiriere;
    @NonNull
    private Double suma;
    private String metoda; //card, wallet
    private LocalDateTime dataIncasare;
    private String status; //procesata/neprocesata

    public Incasare() {
    }

    public Incasare(@NonNull Integer id, Inchiriere inchiriere, @NonNull Double suma, String metoda, LocalDateTime dataIncasare, String status) {
        this.id = id;
        this.inchiriere = inchiriere;
        this.suma = suma;
        this.metoda = metoda;
        this.dataIncasare = dataIncasare;
        this.status = status;
    }

    public @NonNull Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public Inchiriere getInchiriere() {
        return inchiriere;
    }

    public void setInchiriere(Inchiriere inchiriere) {
        this.inchiriere = inchiriere;
    }

    public @NonNull Double getSuma() {
        return suma;
    }

    public void setSuma(@NonNull Double suma) {
        this.suma = suma;
    }

    public String getMetoda() {
        return metoda;
    }

    public void setMetoda(String metoda) {
        this.metoda = metoda;
    }

    public LocalDateTime getDataIncasare() {
        return dataIncasare;
    }

    public void setDataIncasare(LocalDateTime dataIncasare) {
        this.dataIncasare = dataIncasare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
