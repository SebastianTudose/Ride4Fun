package model;

import jakarta.persistence.Entity;


@Entity
public class Angajat extends Persoana{
    private String rol; //administrator, tehnician, operator
    private Double salariu;
    private String tura; //zi sau noapte

    public Angajat() {
    }

    public Angajat(Integer id, String nume, String prenume, String telefon, String rol, Double salariu, String tura) {
        super(id, nume, prenume, telefon);
        this.rol = rol;
        this.salariu = salariu;
        this.tura = tura;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Double getSalariu() {
        return salariu;
    }

    public void setSalariu(Double salariu) {
        this.salariu = salariu;
    }

    public String getTura() {
        return tura;
    }

    public void setTura(String tura) {
        this.tura = tura;
    }
}
