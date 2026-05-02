package org.app.comenzi.web.views.trotinete;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.layout.MainView;
import model.Trotineta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Gestiune Trotinete")
@Route(value="trotinete",layout = MainView.class)
public class NavigableGridTrotineteView extends VerticalLayout {
    //Definire model de date
    private EntityManager em;
    private List<Trotineta> trotinete=new ArrayList<>();
    private Trotineta trotineta=null;

    //Definire componente View
    private H1 titluForm=new H1("Lista Trotinete");

    //Definire componente suport navigare
    private VerticalLayout gridLayoutToolbar;
    private TextField fillerText=new TextField("","Caută după cod...");
    private Button cmdAdaugaTrotineta=new Button("Adaugă trotinetă");
    private Button cmdEditeazaDetaliiTrotineta=new Button("Editează detalii trotinetă");
    private Button cmdStergeTrotineta=new Button("Sterge");

    private Grid<Trotineta>grid=new Grid<>(Trotineta.class);

    public NavigableGridTrotineteView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void initDataModel(){
        System.out.println("DEBUG START FROM >>> ");
        EntityManagerFactory emf= Persistence.createEntityManagerFactory("ProduseJPA");
        em=emf.createEntityManager();

        List<Trotineta> lst=em
                .createQuery("SELECT t FROM Trotineta t ORDER BY t.id", Trotineta.class)
                .getResultList();

        trotinete.clear();
        trotinete.addAll(lst);

        if(lst!=null && !lst.isEmpty()){
            Collections.sort(this.trotinete, (t1, t2) -> t1.getId().compareTo(t2.getId()));
            this.trotineta=trotinete.get(0);
            System.out.println("DEBUG: trotineta init >>> "+trotineta.getId());
        }
        grid.setItems(this.trotinete);
        if(this.trotineta!=null) {
            grid.asSingleSelect().setValue(this.trotineta);
        }
    }

    private void initViewLayout(){
        cmdStergeTrotineta.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cmdAdaugaTrotineta.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cmdEditeazaDetaliiTrotineta.addThemeVariants(ButtonVariant.LUMO_WARNING);
        fillerText.setPlaceholder("Caută după cod...");
        HorizontalLayout toolbar=new HorizontalLayout(
                fillerText,
                cmdAdaugaTrotineta,
                cmdEditeazaDetaliiTrotineta,
                cmdStergeTrotineta
        );

        //Aici am folosit numele variabilelor din clasa trotineta si vehicul electric pentru grid
        grid.setColumns("codIdentificare","baterie","kilometraj","locatie","status","model","stareTehnica");

        //Aici am schimbat denumirea generica si am pus denumirile care sa apara in grid cum ar veni
        grid.getColumnByKey("codIdentificare").setHeader("Cod");
        grid.getColumnByKey("baterie").setHeader("Baterie (%)");
        grid.getColumnByKey("kilometraj").setHeader("Km Parcurși");
        grid.getColumnByKey("locatie").setHeader("Locatie");
        grid.getColumnByKey("stareTehnica").setHeader("Stare");

        //Aici am pus comanda pentru latime automata sa se afiseze totul frumos
        grid.getColumns().forEach(col->col.setAutoWidth(true));

        add(titluForm,toolbar,grid);
    }

    private void initControllerActions(){
        //Daca selectam un rand din tabel, actualizam variabila trotineta
        grid.asSingleSelect().addValueChangeListener(e->{
            this.trotineta=e.getValue();
            System.out.println("Selectat: "+ (trotineta!=null?trotineta.getCodIdentificare():"Nimic"));
        });

        //Facem o mica filtrare la tastare
        fillerText.addValueChangeListener(e->updateList());

        cmdAdaugaTrotineta.addClickListener(e->{
            System.out.println("Click pe Adaugă");
            this.getUI().ifPresent(ui -> ui.navigate(FormTrotinetaView.class));
        });

        cmdEditeazaDetaliiTrotineta.addClickListener(e->{
            if(this.trotineta!=null){
                System.out.println("Click pe Editare pentru: "+this.trotineta.getCodIdentificare());
                this.getUI().ifPresent(ui -> ui.navigate(FormTrotinetaView.class, this.trotineta.getId()));
            }else{
                System.out.println("Selectează o trotinetă mai întâi!");
            }
        });

        cmdStergeTrotineta.addClickListener(e -> {
            if (this.trotineta != null) {
                System.out.println("Click pe Ștergere pentru: " + this.trotineta.getCodIdentificare());
                // 1. Executăm ștergerea
                stergeTrotinetaLocala();
                // Notification.show("Trotineta a fost ștearsă!");
            } else {
                System.out.println("Selectează o trotinetă pentru ștergere!");
            }
        });
    }
    //Am mai adaugat eu o metoda ajutătoare pentru filtrare
    private void updateList(){
        String filter=fillerText.getValue();

        if(filter==null || filter.isEmpty()){
            grid.setItems(this.trotinete);
        }else{
            //Filtrez lista doar cu trotinetele care conțin textul căutat în codul de identificare
            List<Trotineta> filteredList=this.trotinete.stream()
                    .filter(t->t.getCodIdentificare()!=null &&
                            t.getCodIdentificare().toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
            grid.setItems(filteredList);
        }
    }

    //Metoda de stergere locala, conform cursului
    private void stergeTrotinetaLocala() {
        try {
            if (this.em.contains(this.trotineta)) {
                this.em.getTransaction().begin();
                this.em.remove(this.trotineta);
                this.em.getTransaction().commit();

                // Actualizăm lista locală și tabelul
                this.trotinete.remove(this.trotineta);
                updateList();
                this.trotineta = null; // Resetăm selecția

                System.out.println("Trotineta ștearsă cu succes.");
            } else {
                // Caz rar: obiectul nu e atașat contextului JPA, trebuie re-găsit
                Trotineta t = em.find(Trotineta.class, this.trotineta.getId());
                if (t != null) {
                    this.em.getTransaction().begin();
                    this.em.remove(t);
                    this.em.getTransaction().commit();
                    this.trotinete.remove(this.trotineta);
                    updateList();
                    this.trotineta = null;
                }
            }
        } catch (Exception ex) {
            if (this.em.getTransaction().isActive()) {
                this.em.getTransaction().rollback();
            }
            ex.printStackTrace();
        }
    }
}
