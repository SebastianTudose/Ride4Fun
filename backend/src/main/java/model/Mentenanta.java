package model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Mentenanta {
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name="angajat_id")
    private Angajat angajat;
    //private Trotineta trotineta;
    private LocalDateTime dataInterventie;
    private String descriere;
    @OneToMany(mappedBy = "mentenanta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceMentenanta>servicii=new ArrayList<>();
    public Mentenanta() {
    }

    public Mentenanta(Integer id, Angajat angajat, LocalDateTime dataInterventie, String descriere) {
        this.id = id;
        this.angajat = angajat;
        //this.trotineta = trotineta;
        this.dataInterventie = dataInterventie;
        this.descriere = descriere;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Angajat getAngajat() {
        return angajat;
    }

    public void setAngajat(Angajat angajat) {
        this.angajat = angajat;
    }



    public LocalDateTime getDataInterventie() {
        return dataInterventie;
    }

    public void setDataInterventie(LocalDateTime dataInterventie) {
        this.dataInterventie = dataInterventie;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public List<ServiceMentenanta> getServicii() {
        return servicii;
    }

    public void setServicii(List<ServiceMentenanta> servicii) {
        this.servicii = servicii;
    }
}
