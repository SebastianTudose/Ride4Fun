package model;

import jakarta.persistence.*;

@Entity
public class ServiceMentenanta{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tipService; //reparatie, verificare sau inlocuire piese
    private Double cost; //Pentru piese
    @ManyToOne
    @JoinColumn(name="vehicul_id")
    private VehiculElectric vehicul;
    @ManyToOne
    private Mentenanta mentenanta;

    public ServiceMentenanta() {
    }

    public ServiceMentenanta(Integer id, String tipService, Double cost, VehiculElectric vehicul, Mentenanta mentenanta) {
        this.id = id;
        this.tipService = tipService;
        this.cost = cost;
        this.vehicul = vehicul;
        this.mentenanta = mentenanta;
    }

    public String getTipService() {
        return tipService;
    }

    public void setTipService(String tipService) {
        this.tipService = tipService;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public VehiculElectric getVehicul() {
        return vehicul;
    }

    public void setVehicul(VehiculElectric vehicul) {
        this.vehicul = vehicul;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Mentenanta getMentenanta() {
        return mentenanta;
    }

    public void setMentenanta(Mentenanta mentenanta) {
        this.mentenanta = mentenanta;
    }
}
