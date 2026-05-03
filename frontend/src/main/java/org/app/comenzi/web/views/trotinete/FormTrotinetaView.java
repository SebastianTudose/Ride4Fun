package org.app.comenzi.web.views.trotinete;

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
import org.layout.MainView;
import model.Trotineta;

import java.util.List;

@PageTitle("Formular Trotinetă")
@Route(value="trotineta", layout= MainView.class)
public class FormTrotinetaView extends VerticalLayout implements HasUrlParameter<Integer>{
    //Aici am definit modelul de date
    private EntityManager em;
    private Trotineta trotineta=null;
    private Binder<Trotineta> binder=new BeanValidationBinder<>(Trotineta.class);

    //Componente view (A.K.A. Câmpuri de editare)
    private H1 titluForm=new H1("Formular Trotinetă");

    //Aici am făcut câmpurile corespunzătoare clasei Trotinetă/VehiculElectric (/=extinde=clasa copil)
    private TextField codIdentificare=new TextField("Cod Identificare");
    private NumberField baterie=new NumberField("Baterie (%)");
    private NumberField kilometraj=new NumberField("Kilometraj");
    private TextField locatie=new TextField("Locatie");
    private Select<String> status=new Select<>();
    private TextField model=new TextField("Model");
    private Select<String> stareTehnica=new Select<>();

    private Button cmdSalveaza=new Button("Salvează");
    private Button cmdSterge=new Button("Șterge");
    private Button cmdAbandon=new Button("Anulează");

    public FormTrotinetaView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null) {
            // Editare: Căutăm trotineta în baza de date
            this.trotineta = em.find(Trotineta.class, id);
            if (this.trotineta == null) {
                // Dacă nu există ID-ul, ne întoarcem la listă
                event.forwardTo(NavigableGridTrotineteView.class);
            }
        } else {
            // Adăugare: Creăm o trotinetă nouă
            this.trotineta = new Trotineta();
        }
        // Legăm datele de formular
        binder.setBean(this.trotineta);
    }

    private void initDataModel(){
        EntityManagerFactory emf= Persistence.createEntityManagerFactory("ProduseJPA");
        this.em= emf.createEntityManager();

        //Aici legam automat campurile din UI cu variabilele din clasa
        binder.bindInstanceFields(this);
    }

    private void initViewLayout(){
        cmdSterge.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cmdAbandon.addThemeVariants(ButtonVariant.LUMO_WARNING);
        cmdSalveaza.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        //Asezam campurile intr-un FormLayout ca sa arate mai frumos
        status.setLabel("Status");
        status.setItems("disponibil","indisponibil");
        status.setPlaceholder("Alege...");
        stareTehnica.setLabel("Stare");
        stareTehnica.setItems("functional","service","defect");
        stareTehnica.setPlaceholder("Alege...");
        FormLayout formLayout=new FormLayout();
        formLayout.add(codIdentificare, baterie,kilometraj, locatie, status, model, stareTehnica);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",1));
        formLayout.setMaxWidth("500px");

        //Bara de butoane aici
        HorizontalLayout actions=new HorizontalLayout(cmdSalveaza, cmdSterge, cmdAbandon);
        add(titluForm, formLayout, actions);
    }

    private void initControllerActions(){
        //Buton salvare
        cmdSalveaza.addClickListener(e->{
            salveazaTrotineta();
            //Navigam inapoi la lista dupa salvare
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridTrotineteView.class));
        });

        //Buton anulare
        cmdAbandon.addClickListener(e->{
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridTrotineteView.class));
        });

        //Buton stergere
        cmdSterge.addClickListener(e->{
            stergeTrotineta();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridTrotineteView.class));
        });
    }

    //AICI AM LOGICA CRUD
    private void salveazaTrotineta() {
        // 1. Validăm datele din formular
        if (binder.validate().isOk()) {

            // VERIFICARE UNICITATE COD
            String codIntrodus = this.trotineta.getCodIdentificare();

            // Căutăm în bază dacă mai există o trotinetă cu acest cod
            List<Trotineta> duplicate = em.createQuery(
                            "SELECT t FROM Trotineta t WHERE t.codIdentificare = :cod", Trotineta.class)
                    .setParameter("cod", codIntrodus)
                    .getResultList();

            // Dacă am găsit una, verificăm să nu fie chiar cea pe care o edităm acum
            if (!duplicate.isEmpty()) {
                Trotineta existenta = duplicate.get(0);

                // Dacă id-ul trotinetei din bază diferă de id-ul trotinetei curente (sau curenta e nouă și id e null)
                // înseamnă că e un duplicat.
                if (this.trotineta.getId() == null || !existenta.getId().equals(this.trotineta.getId())) {
                    System.out.println("EROARE: Codul " + codIntrodus + " există deja!");

                    // Afișăm eroare pe câmpul text (Vaadin face asta roșu)
                    codIdentificare.setErrorMessage("Acest cod există deja!");
                    codIdentificare.setInvalid(true);
                    return; // Oprim salvarea
                }
            }
            // ---------------------------------

            try {
                this.em.getTransaction().begin();
                this.trotineta = this.em.merge(this.trotineta);
                this.em.getTransaction().commit();
                System.out.println("Trotinetă Salvată cu SUCCES!");
            } catch (Exception ex) {
                if (this.em.getTransaction().isActive()) {
                    this.em.getTransaction().rollback();
                }
                ex.printStackTrace();
            }
        } else {
            System.out.println("Formular invalid");
        }
    }

    private void stergeTrotineta() {
        try {
            if (this.trotineta != null && this.em.contains(this.trotineta)) {
                this.em.getTransaction().begin();
                this.em.remove(this.trotineta);
                this.em.getTransaction().commit();
                System.out.println("Trotineta ștearsă!");
            }
        } catch (Exception ex) {
            if (this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            ex.printStackTrace();
        }
    }
}
