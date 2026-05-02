package org.layout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import org.app.comenzi.web.views.angajati.FormAngajatView;
import org.app.comenzi.web.views.angajati.NavigableGridAngajatiView;
import org.app.comenzi.web.views.dashboard.DashboardView;
import org.app.comenzi.web.views.trotinete.FormTrotinetaView;
import org.app.comenzi.web.views.trotinete.NavigableGridTrotineteView;
import org.app.comenzi.web.views.utilizatori.NavigableGridUtilizatoriView;

public class MainView extends VerticalLayout implements RouterLayout {
    public MainView() {
        // Aici am configurat aspectul barei de navigare
        setSpacing(false);
        setPadding(false);
        setWidthFull();

        setMenuBar();
    }

    private void setMenuBar() {
        H1 title = new H1("Ride4Fun");
        title.getStyle().set("font-size", "20px");
        title.getStyle().set("margin-left", "15px");
        title.getStyle().set("margin-right", "20px");

        // Creare bara de meniu
        MenuBar mainMenu = new MenuBar();
        mainMenu.addThemeVariants(com.vaadin.flow.component.menubar.MenuBarVariant.LUMO_TERTIARY);

        // BUTON HOME
        MenuItem homeMenu = mainMenu.addItem("Home");
        homeMenu.addClickListener(event -> UI.getCurrent().navigate(DashboardView.class));

        // MENIU TROTINETE
        MenuItem trotineteMenu = mainMenu.addItem("Trotinete");
        SubMenu trotineteSubMenu = trotineteMenu.getSubMenu();
        trotineteSubMenu.addItem("Lista Trotinete...",
                event -> UI.getCurrent().navigate(NavigableGridTrotineteView.class));
        trotineteSubMenu.addItem("Adaugă Trotinetă...",
                event -> UI.getCurrent().navigate(FormTrotinetaView.class));

        // MENIU ANGAJAȚI
        MenuItem angajatiMenu = mainMenu.addItem("Angajați");
        SubMenu angajatiSubMenu = angajatiMenu.getSubMenu();
        angajatiSubMenu.addItem("Lista Angajați...",
                event -> UI.getCurrent().navigate(NavigableGridAngajatiView.class));
        angajatiSubMenu.addItem("Adaugă Angajat...",
                event -> UI.getCurrent().navigate(FormAngajatView.class));

        // MENIU UTILIZATORI
        MenuItem utilizatoriMenu=mainMenu.addItem("Utilizatori");
        SubMenu utilizatoriSubMenu=utilizatoriMenu.getSubMenu();
        utilizatoriSubMenu.addItem("Lista Utilizatori...",
                event->UI.getCurrent().navigate(NavigableGridUtilizatoriView.class));

        // Am pus totul într-un Layout Orizontal (Titlu + Meniu)
        HorizontalLayout header = new HorizontalLayout(title, mainMenu);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.setWidthFull();
        header.getStyle().set("border-bottom", "1px solid #e0e0e0"); // O linie subtilă jos
        header.setPadding(true);

        add(header);
    }
}