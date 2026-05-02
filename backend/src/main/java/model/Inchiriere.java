package model;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;


@Entity
public class Inchiriere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="utilizator_id")
    private Utilizator utilizator; //cheie straina - 1 utilizator poate inchiria de mai multe ori
    @ManyToOne
    @JoinColumn(name="trotineta_id")
    private Trotineta trotineta; //cheie straina - 1
    private LocalDateTime startInchiriere;
    private LocalDateTime finalInchiriere;
    private Double distantaParcursa; //in km

    public Inchiriere() {
    }

    public Inchiriere(Integer id, Utilizator utilizator, Trotineta trotineta, LocalDateTime startInchiriere, LocalDateTime finalInchiriere, Double distantaParcursa) {
        this.id = id;
        this.utilizator = utilizator;
        this.trotineta = trotineta;
        this.startInchiriere = startInchiriere;
        this.finalInchiriere = finalInchiriere;
        this.distantaParcursa = distantaParcursa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    public Trotineta getTrotineta() {
        return trotineta;
    }

    public void setTrotineta(Trotineta trotineta) {
        this.trotineta = trotineta;
    }

    public LocalDateTime getStartInchiriere() {
        return startInchiriere;
    }

    public void setStartInchiriere(LocalDateTime startInchiriere) {
        this.startInchiriere = startInchiriere;
    }

    public LocalDateTime getFinalInchiriere() {
        return finalInchiriere;
    }

    public void setFinalInchiriere(LocalDateTime finalInchiriere) {
        this.finalInchiriere = finalInchiriere;
    }

    public Double getDistantaParcursa() {
        return distantaParcursa;
    }

    public void setDistantaParcursa(Double distantaParcursa) {
        this.distantaParcursa = distantaParcursa;
    }

    public double calculeazaCostInchiriere(Tarif tarif){
        double cost;
        double minuteInchiriere= Duration.between(startInchiriere,finalInchiriere).toMinutes();
        cost = tarif.getTaxaDeblocare()+minuteInchiriere* tarif.getTarifMinut()+distantaParcursa* tarif.getTarifKm();
        return cost;
    }


}
