package model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Angajat a1=new Angajat(null, "Popescu", "Florin", "0712345670", "operator", 1200.0, "noapte");
        //Utilizator u1=new Utilizator(null, "Andrei", "Ciprian", "1234567890112","1234567891022","wallet", false);
        Utilizator u2=new Utilizator(null, "Adam", "Florentina", "0789456718", "726152478901","card",true);
        //Trotineta t1=new Trotineta(null, "12", 80.0,"Alexandru Cel Bun", "disponibil", "lime", 88.0,"ok");
        LocalDateTime start=LocalDateTime.now();
        LocalDateTime finish=start.plusMinutes(25);
        double distanta=3.5;

       // Inchiriere inch1=new Inchiriere(null, u1, t1, start, finish, distanta);
        Tarif tarif=new Tarif();
        tarif.setTarifMinut(0.2);
        tarif.setTarifKm(1.0);
        tarif.setTaxaDeblocare(2.5);
        tarif.setActiv(true);

        EntityManagerFactory emf= Persistence.createEntityManagerFactory("ProduseJPA");
        EntityManager em=emf.createEntityManager();
        em.getTransaction().begin();
       // em.persist(a1);
        em.persist(u2);
       // em.persist(t1);
        // em.persist(inch1);
        em.getTransaction().commit();

        List<Utilizator> lstBD=em.createQuery("select u from Utilizator u").getResultList();
        System.out.println(lstBD);

       // System.out.println("Suma de plata pentru inchirierea cu ID: "+inch1.getId() + " este "+inch1.calculeazaCostInchiriere(tarif)+" RON.");
    }
}