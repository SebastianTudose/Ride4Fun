package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
public class Tarif {
    @Id
    private Integer id;
    private Double tarifMinut;
    private Double tarifKm;
    private Double taxaDeblocare;
    private Boolean activ;

    public Tarif() {
    }

    public Tarif(Integer id, Double tarifMinut, Double tarifKm, Double taxaDeblocare, Boolean activ) {
        this.id = id;
        this.tarifMinut = tarifMinut;
        this.tarifKm = tarifKm;
        this.taxaDeblocare = taxaDeblocare;
        this.activ = activ;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTarifMinut() {
        return tarifMinut;
    }

    public void setTarifMinut(Double tarifMinut) {
        this.tarifMinut = tarifMinut;
    }

    public Double getTarifKm() {
        return tarifKm;
    }

    public void setTarifKm(Double tarifKm) {
        this.tarifKm = tarifKm;
    }

    public Double getTaxaDeblocare() {
        return taxaDeblocare;
    }

    public void setTaxaDeblocare(Double taxaDeblocare) {
        this.taxaDeblocare = taxaDeblocare;
    }

    public Boolean getActiv() {
        return activ;
    }

    public void setActiv(Boolean activ) {
        this.activ = activ;
    }
}
