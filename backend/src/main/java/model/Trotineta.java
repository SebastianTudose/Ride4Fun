package model;

import jakarta.persistence.Entity;

@Entity
public class Trotineta extends VehiculElectric{
    private String model;
    private Double kilometraj;
    private String stareTehnica; //buna, uzata, defecta
    //private List<ServiceMentenanta> interventii; //istoric mentenanta

    public Trotineta() {
    }

    public Trotineta(Integer id, String codIdentificare, Double baterie, String locatie, String status, String model, Double kilometraj, String stareTehnica) {
        super(id, codIdentificare, baterie, locatie, status);
        this.model = model;
        this.kilometraj = kilometraj;
        this.stareTehnica = stareTehnica;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getKilometraj() {
        return kilometraj;
    }

    public void setKilometraj(Double kilometraj) {
        this.kilometraj = kilometraj;
    }

    public String getStareTehnica() {
        return stareTehnica;
    }

    public void setStareTehnica(String stareTehnica) {
        this.stareTehnica = stareTehnica;
    }


}
