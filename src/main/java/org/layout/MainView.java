package org.layout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinSession;
import org.app.comenzi.web.views.angajati.FormAngajatView;
import org.app.comenzi.web.views.angajati.NavigableGridAngajatiView;
import org.app.comenzi.web.views.auth.LoginView;
import org.app.comenzi.web.views.dashboard.DashboardView;
import org.app.comenzi.web.views.trotinete.FormTrotinetaView;
import org.app.comenzi.web.views.trotinete.NavigableGridTrotineteView;
import org.app.comenzi.web.views.utilizatori.NavigableGridUtilizatoriView;

public class MainView extends VerticalLayout implements RouterLayout, BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        Boolean loggedIn=(Boolean) VaadinSession.getCurrent().getAttribute("admin_logged_in");
        if (!Boolean.TRUE.equals(loggedIn)) {
            event.forwardTo(LoginView.class); // Dacă nu e logat, îl forțează să meargă la Login
        }
    }

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

        // SECTIUNE PENTRU AFISARE AVATAR SI OPTIUNE DELOGARE PENTRU ADMINISTRATOR
        String adminUser = (String) VaadinSession.getCurrent().getAttribute("admin_username");
        String displayName = adminUser != null ? adminUser : "Admin";
        // Aici am creat componenta vizuala pentru avatar
        Avatar avatar = new Avatar(displayName);
        avatar.getStyle().set("cursor", "pointer");
        // 2. Creăm un meniu special doar pentru profil (în dreapta)
        MenuBar userMenu = new MenuBar();
        userMenu.addThemeVariants(com.vaadin.flow.component.menubar.MenuBarVariant.LUMO_TERTIARY);
        // 3. Adăugăm avatarul ca buton principal în acest meniu
        MenuItem avatarItem = userMenu.addItem(avatar);

        // 4. Creăm lista de opțiuni (dropdown) când dai click pe avatar
        SubMenu userSubMenu = avatarItem.getSubMenu();

        // Adăugăm un text de salut și o linie despărțitoare vizuală
        userSubMenu.addItem("Salut, " + displayName).setEnabled(false); // .setEnabled(false) îl face text static, nu buton
        userSubMenu.add(new Hr()); // O linie gri orizontală subtilă

        // 5. Adăugăm opțiunile reale (Setări și Deconectare)
        userSubMenu.addItem("Setări cont", e -> {
            // Aici vei putea naviga spre o pagină de profil în viitor, dacă o creezi
            Notification.show("Pagină în dezvoltare...", 2000, Notification.Position.MIDDLE);
        });

        // Butonul de Logout (cu text colorat în roșu pentru accentuare)
        MenuItem logoutItem = userSubMenu.addItem("Deconectare");
        logoutItem.getStyle().set("color", "var(--lumo-error-text-color)");
        logoutItem.getStyle().set("font-weight", "500");
        logoutItem.addClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("admin_logged_in", null);
            VaadinSession.getCurrent().setAttribute("admin_username", null);
            UI.getCurrent().navigate(LoginView.class);
        });

        // Aliniere la dreapta
        HorizontalLayout rightSide = new HorizontalLayout(userMenu);
        rightSide.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        // Am pus totul într-un Layout Orizontal (Titlu + Meniu)
        HorizontalLayout header = new HorizontalLayout(title, mainMenu, rightSide);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.getStyle().set("border-bottom", "1px solid #e0e0e0"); // O linie subtilă jos
        header.setPadding(true);

        add(header);
    }
}