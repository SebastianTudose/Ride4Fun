package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
public class VehiculElectric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Pentru generare automata de id
    private Integer id;
    @Column(unique = true)
    private String codIdentificare;
    private Double baterie; //pentru procent [0-100%]
    private String locatie;
    private String status; //disponibil, inchiriat, service

    public VehiculElectric() {
    }

    public VehiculElectric(Integer id, String codIdentificare, Double baterie, String locatie, String status) {
        this.id = id;
        this.codIdentificare = codIdentificare;
        this.baterie = baterie;
        this.locatie = locatie;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodIdentificare() {
        return codIdentificare;
    }

    public void setCodIdentificare(String codIdentificare) {
        this.codIdentificare = codIdentificare;
    }

    public Double getBaterie() {
        return baterie;
    }

    public void setBaterie(Double baterie) {
        this.baterie = baterie;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

