package org.app.comenzi.web.views.angajati;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Angajat;
import org.layout.MainView;

@PageTitle("Formular Angajat")
@Route(value = "angajat", layout = MainView.class)
public class FormAngajatView extends VerticalLayout implements HasUrlParameter<Integer> {

    private EntityManager em;
    private Angajat angajat = new Angajat();
    private Binder<Angajat> binder = new BeanValidationBinder<>(Angajat.class);

    private H1 titlu = new H1("Detalii Angajat");

    private TextField nume = new TextField("Nume");
    private TextField prenume = new TextField("Prenume");
    private TextField telefon = new TextField("Telefon");
    private NumberField salariu = new NumberField("Salariu");
    private Select<String> rol = new Select<>();
    private Select<String> tura = new Select<>();

    private Button btnSalveaza = new Button("Salvează");
    private Button btnAnuleaza = new Button("Anulează");
    private Button btnSterge = new Button("Șterge");

    public FormAngajatView() {
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        // Aici decidem dacă formularul va fi pe editare sau adaugare in functie de id
        if (id != null) {
            this.angajat = em.find(Angajat.class, id);
            if (this.angajat == null) {
                event.forwardTo(NavigableGridAngajatiView.class);
            }
        } else {
            this.angajat = new Angajat();
        }
        binder.setBean(this.angajat);
    }

    private void initDataModel() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        this.em = emf.createEntityManager();
        binder.bindInstanceFields(this);
    }

    private void initViewLayout() {
        btnSterge.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnAnuleaza.addThemeVariants(ButtonVariant.LUMO_WARNING);
        btnSalveaza.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        // Aici am pus pentru select-uri
        rol.setLabel("Funcție / Rol");
        rol.setItems("administrator", "tehnician", "operator");
        tura.setLabel("Tură");
        tura.setItems("zi", "noapte");

        FormLayout form = new FormLayout();
        form.add(nume, prenume, telefon, salariu, rol, tura);

        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        form.setMaxWidth("500px");

        HorizontalLayout buttons = new HorizontalLayout(btnSalveaza, btnSterge, btnAnuleaza);
        add(titlu, form, buttons);
    }

    private void initControllerActions() {
        btnSalveaza.addClickListener(e -> {
            if (binder.validate().isOk()) {
                salveazaAngajat();
                this.getUI().ifPresent(ui -> ui.navigate(NavigableGridAngajatiView.class));
            }
        });

        btnAnuleaza.addClickListener(e ->
                this.getUI().ifPresent(ui -> ui.navigate(NavigableGridAngajatiView.class))
        );

        btnSterge.addClickListener(e -> {
            stergeAngajat();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridAngajatiView.class));
        });
    }

    private void salveazaAngajat() {
        try {
            em.getTransaction().begin();
            // dacă ID e null (angajat nou), folosește ID-ul generat de DB
            this.angajat = em.merge(this.angajat);
            em.getTransaction().commit();
            System.out.println("Angajat salvat!");
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
        }
    }

    private void stergeAngajat() {
        try {
            if (this.angajat != null && this.angajat.getId() != null) {
                em.getTransaction().begin();
                // Verificăm dacă exista
                if (!em.contains(this.angajat)) {
                    this.angajat = em.find(Angajat.class, this.angajat.getId());
                }
                em.remove(this.angajat);
                em.getTransaction().commit();
            }
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
        }
    }
}