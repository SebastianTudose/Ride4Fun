package org.app.comenzi.web.views.utilizatori;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.layout.MainView;
import org.example.Utilizator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Gestiune Utilizatori")
@Route(value = "utilizatori", layout = MainView.class)
public class NavigableGridUtilizatoriView extends VerticalLayout {

    // --- Model Date ---
    private EntityManager em;
    private List<Utilizator> utilizatori = new ArrayList<>();
    private Utilizator utilizatorSelectat = null;

    // --- Componente UI ---
    private H1 titluPagina = new H1("Lista Clienți / Utilizatori");

    private TextField textCautare = new TextField("", "Caută după nume sau CNP...");
    private Button btnSterge = new Button("Șterge Utilizator");

    private Grid<Utilizator> grid = new Grid<>(Utilizator.class);

    public NavigableGridUtilizatoriView() {
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void initDataModel() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        em = emf.createEntityManager();
        refreshGrid();
    }

    private void initViewLayout() {
        textCautare.getStyle().setWidth("250px");
        // Buton de stergere care e rosu
        btnSterge.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout toolbar = new HorizontalLayout(textCautare, btnSterge);

        grid.removeAllColumns();

        // Aici am adaugat coloanele manual folosind Getter
        grid.addColumn(Utilizator::getNume).setHeader("Nume").setSortable(true);
        grid.addColumn(Utilizator::getPrenume).setHeader("Prenume").setSortable(true);
        grid.addColumn(Utilizator::getTelefon).setHeader("Telefon");
        grid.addColumn(Utilizator::getCNP).setHeader("CNP");
        grid.addColumn(Utilizator::getMetodaPlataPreferata).setHeader("Metoda Plată");

        // Coloană specială pentru statusul "Blocat" (vizual frumos)
        grid.addColumn(new ComponentRenderer<>(user -> {
            if (user.getBlocat() != null && user.getBlocat()) {
                Span badge = new Span("BLOCAT");
                badge.getElement().getThemeList().add("badge error"); // Roșu
                return badge;
            } else {
                Span badge = new Span("Activ");
                badge.getElement().getThemeList().add("badge success"); // Verde
                return badge;
            }
        })).setHeader("Status Cont");

        // Setări tabel
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        add(titluPagina, toolbar, grid);
    }

    private void initControllerActions() {
        grid.asSingleSelect().addValueChangeListener(e -> {
            this.utilizatorSelectat = e.getValue();
        });

        textCautare.addValueChangeListener(e -> filtrareLista());

        btnSterge.addClickListener(e -> {
            if (this.utilizatorSelectat != null) {
                stergeUtilizator();
            } else {
                System.out.println("Selectează un utilizator pentru a-l șterge!");
            }
        });
    }

    private void refreshGrid() {
        // Luăm toți utilizatorii din baza de date
        List<Utilizator> dbList = em.createQuery("SELECT u FROM Utilizator u", Utilizator.class).getResultList();
        this.utilizatori.clear();
        this.utilizatori.addAll(dbList);
        grid.setItems(this.utilizatori);
    }

    private void filtrareLista() {
        String filtru = textCautare.getValue().toLowerCase();

        if (filtru.isEmpty()) {
            grid.setItems(this.utilizatori);
        } else {
            List<Utilizator> filtrate = this.utilizatori.stream()
                    .filter(u -> (u.getNume() != null && u.getNume().toLowerCase().contains(filtru)) ||
                            (u.getPrenume() != null && u.getPrenume().toLowerCase().contains(filtru)) ||
                            (u.getCNP() != null && u.getCNP().toLowerCase().contains(filtru)))
                    .collect(Collectors.toList());
            grid.setItems(filtrate);
        }
    }

    private void stergeUtilizator() {
        try {
            em.getTransaction().begin();
            // Verificăm dacă apare
            if (!em.contains(utilizatorSelectat)) {
                utilizatorSelectat = em.find(Utilizator.class, utilizatorSelectat.getId());
            }
            em.remove(utilizatorSelectat);
            em.getTransaction().commit();

            // Actualizare vizuală
            this.utilizatori.remove(utilizatorSelectat);
            grid.setItems(this.utilizatori);
            this.utilizatorSelectat = null;

            System.out.println("Utilizator șters cu succes.");
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace(); // Aici stergerea poate esua daca are inchirieri active
        }
    }
}