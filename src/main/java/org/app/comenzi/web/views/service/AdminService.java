package org.app.comenzi.web.views.service;

import jakarta.persistence.*;
import model.Administrator;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

public class AdminService {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");

    public static boolean authenticate(String username, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Administrator> results = em.createQuery(
                            "SELECT a FROM Administrator a WHERE a.username = :username", Administrator.class)
                    .setParameter("username", username)
                    .getResultList();
            if (results.isEmpty()) return false;
            return BCrypt.checkpw(password, results.get(0).getPasswordHash());
        } finally { em.close(); }
    }

    // Metoda pentru ÎNREGISTRARE (Creează contul și criptează parola)
    public static boolean register(String username, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            // Verificăm dacă utilizatorul există deja
            long count = em.createQuery(
                            "SELECT COUNT(a) FROM Administrator a WHERE a.username = :username",
                            Long.class)
                    .setParameter("username", username)
                    .getSingleResult();

            if (count > 0) return false; // Utilizatorul există deja

            // Creăm noul administrator
            Administrator admin = new Administrator();
            admin.setUsername(username);
            // CRIPTĂM parola înainte de a o salva în baza de date
            admin.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));

            em.getTransaction().begin();
            em.persist(admin);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}
