package org.app.comenzi.web.views.angajati;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Angajat;
import org.layout.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Gestiune Angajați")
@Route(value = "angajati", layout = MainView.class)
public class NavigableGridAngajatiView extends VerticalLayout {

    private EntityManager em;
    private List<Angajat> angajati = new ArrayList<>();
    private Angajat angajatSelectat = null;

    private H1 titluForm = new H1("Lista Angajați");
    private TextField fillerText = new TextField("", "Caută după nume...");
    private Button cmdAdauga = new Button("Adaugă Angajat");
    private Button cmdEditeaza = new Button("Editează");
    private Button cmdSterge = new Button("Șterge");

    private Grid<Angajat> grid = new Grid<>(Angajat.class);

    public NavigableGridAngajatiView() {
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
        cmdSterge.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cmdAdauga.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cmdEditeaza.addThemeVariants(ButtonVariant.LUMO_WARNING);
        HorizontalLayout toolbar = new HorizontalLayout(fillerText, cmdAdauga, cmdEditeaza, cmdSterge);

        grid.setColumns("nume", "prenume", "telefon", "salariu", "rol", "tura");

        grid.getColumnByKey("nume").setHeader("Nume");
        grid.getColumnByKey("prenume").setHeader("Prenume");
        grid.getColumnByKey("telefon").setHeader("Telefon");
        grid.getColumnByKey("salariu").setHeader("Salariu");
        grid.getColumnByKey("rol").setHeader("Funcție");
        grid.getColumnByKey("tura").setHeader("Tură");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        add(titluForm, toolbar, grid);
    }

    private void initControllerActions() {
        grid.asSingleSelect().addValueChangeListener(e -> this.angajatSelectat = e.getValue());
        fillerText.addValueChangeListener(e -> filtrareLista());

        cmdAdauga.addClickListener(e -> this.getUI().ifPresent(ui -> ui.navigate(FormAngajatView.class)));

        cmdEditeaza.addClickListener(e -> {
            if (this.angajatSelectat != null) {
                this.getUI().ifPresent(ui -> ui.navigate(FormAngajatView.class, this.angajatSelectat.getId()));
            } else {
                Notification notification = Notification.show("Selectează un angajat pentru a edita informații!", 3000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
            }
        });

        cmdSterge.addClickListener(e -> {
            if (this.angajatSelectat != null) {
                stergeAngajatLocal();
            } else {
                Notification notification = Notification.show("Selectează un angajat pentru a-l șterge!", 3000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
            }
        });
    }

    private void refreshGrid() {
        List<Angajat> lst = em.createQuery("SELECT a FROM Angajat a", Angajat.class).getResultList();
        this.angajati.clear();
        this.angajati.addAll(lst);
        grid.setItems(this.angajati);
    }

    private void filtrareLista() {
        String filter = fillerText.getValue().toLowerCase();
        if (filter.isEmpty()) {
            grid.setItems(this.angajati);
        } else {
            List<Angajat> filtrate = this.angajati.stream()
                    .filter(a -> a.getNume().toLowerCase().contains(filter) ||
                            a.getPrenume().toLowerCase().contains(filter))
                    .collect(Collectors.toList());
            grid.setItems(filtrate);
        }
    }

    private void stergeAngajatLocal() {
        try {
            em.getTransaction().begin();
            if (!em.contains(angajatSelectat)) {
                angajatSelectat = em.find(Angajat.class, angajatSelectat.getId());
            }
            em.remove(angajatSelectat);
            em.getTransaction().commit();

            this.angajati.remove(angajatSelectat);
            grid.setItems(this.angajati);
            this.angajatSelectat = null;
            Notification notification = Notification.show("Angajat șters!", 3000, Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
        }
    }
}