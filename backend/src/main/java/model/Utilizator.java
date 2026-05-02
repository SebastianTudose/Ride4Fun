package model;

import jakarta.persistence.Entity;

@Entity
public class Utilizator extends Persoana {
    private String CNP;
    private String metodaPlataPreferata; //plata cu card, wallet, Gpay, ApplePay, etc.
    private Boolean blocat; //pentru situatiile cand clientul nu este unul ideal - card invalid, trotinete afectate, etc.
    //private List<Inchiriere>inchirieri; //istoricul curselor facute de utilizator cu trotinetele

    public Utilizator() {
    }

    public Utilizator(Integer id, String nume, String prenume, String telefon, String CNP, String metodaPlataPreferata, Boolean blocat) {
        super(id, nume, prenume, telefon);
        this.CNP = CNP;
        this.metodaPlataPreferata = metodaPlataPreferata;
        this.blocat = blocat;
    }


    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public String getMetodaPlataPreferata() {
        return metodaPlataPreferata;
    }

    public void setMetodaPlataPreferata(String metodaPlataPreferata) {
        this.metodaPlataPreferata = metodaPlataPreferata;
    }

    public Boolean getBlocat() {
        return blocat;
    }

    public void setBlocat(Boolean blocat) {
        this.blocat = blocat;
    }

}
