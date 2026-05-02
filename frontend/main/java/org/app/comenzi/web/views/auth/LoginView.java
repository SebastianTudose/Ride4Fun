package org.app.comenzi.web.views.auth;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.app.comenzi.web.views.service.AdminService;
import org.app.comenzi.web.views.dashboard.DashboardView;

@PageTitle("Login - Ride4Fun Admin")
@Route("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final TextField username = new TextField("Nume Utilizator");
    private final PasswordField password = new PasswordField("Parolă");
    private final Button btnLogin = new Button("Autentificare");

    // Un mesaj de eroare general pe care îl putem ascunde/afișa
    private final Span mesajEroareGeneral = new Span("Nume de utilizator sau parolă incorectă!");

    public LoginView() {
        /* // Cont de administrator creat
        try {
            if (!AdminService.authenticate("SebastianTudose", "@dministrator20Seby")) {
                AdminService.register("SebastianTudose", "@dministrator20Seby");
            }
        } catch (Exception e) {}
         */

        // 1. Setăm pagina să ocupe tot ecranul și centrăm absolut totul
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        getStyle().set("background-color", "#f4f5f7"); // Fundal gri-deschis

        // 2. Titlul paginii
        H2 title = new H2("Ride4Fun Admin");
        title.getStyle().set("margin-top", "0");
        title.getStyle().set("color", "#2c3e50");

        // Configurarea stilului pentru mesajul de eroare general (ascuns implicit)
        mesajEroareGeneral.getStyle().set("color", "var(--lumo-error-text-color)");
        mesajEroareGeneral.getStyle().set("font-size", "14px");
        mesajEroareGeneral.getStyle().set("font-weight", "500");
        mesajEroareGeneral.setVisible(false);

        // 3. Formularul (Setat pe o singură coloană)
        FormLayout form = new FormLayout(username, password);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        form.setWidthFull();

        // 4. Stilizarea butonului de Login
        btnLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnLogin.setWidthFull();
        btnLogin.getStyle().set("margin-top", "20px");
        btnLogin.getStyle().set("cursor", "pointer");

        // Resetează erorile când utilizatorul începe să scrie din nou
        username.addValueChangeListener(e -> reseteazaErori());
        password.addValueChangeListener(e -> reseteazaErori());

        // Setăm acțiunile (Click pe buton sau tasta ENTER pe parolă)
        btnLogin.addClickListener(e -> doLogin());
        password.addKeyPressListener(com.vaadin.flow.component.Key.ENTER, e -> doLogin());

        // 5. Container alb pentru formular
        VerticalLayout card = new VerticalLayout(title, form, btnLogin);
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.setMaxWidth("400px");
        card.setWidth("100%");
        card.getStyle().set("padding", "40px");
        card.getStyle().set("background-color", "#ffffff");
        card.getStyle().set("border-radius", "12px");
        card.getStyle().set("box-shadow", "0 10px 30px rgba(0,0,0,0.1)");

        // Container la centru
        add(card);
    }

    private void reseteazaErori() {
        username.setInvalid(false);
        password.setInvalid(false);
        mesajEroareGeneral.setVisible(false);
    }

    private void doLogin() {
        String user = username.getValue().trim();
        String pwd = password.getValue();

        // Resetăm starea vizuală la fiecare încercare
        username.setInvalid(false);
        password.setInvalid(false);
        mesajEroareGeneral.setVisible(false);

        // Validare simplă înainte de DB
        if (user.isEmpty() || pwd.isEmpty()) {
            if (user.isEmpty()) username.setInvalid(true);
            if (pwd.isEmpty()) password.setInvalid(true);
            return;
        }

        try {
            // Interogăm baza de date
            if (AdminService.authenticate(user, pwd)) {
                VaadinSession.getCurrent().setAttribute("admin_logged_in", true);
                VaadinSession.getCurrent().setAttribute("admin_username", user);
                UI.getCurrent().navigate(DashboardView.class);
            } else {
                // DATE GREȘITE
                username.setInvalid(true);
                password.setInvalid(true);
                password.clear();
                mesajEroareGeneral.setVisible(true);
            }
        } catch (Exception ex) {
            // Dacă ajungem aici, înseamnă că Baza de Date a dat eroare!
            Notification.show("Eroare de conexiune la baza de date: " + ex.getMessage(),
                    5000, Notification.Position.MIDDLE);
            ex.printStackTrace();
        }
    }

    // Paznicul care nu lasă un admin deja logat să vadă din nou pagina de login
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Boolean loggedIn = (Boolean) VaadinSession.getCurrent().getAttribute("admin_logged_in");
        if (Boolean.TRUE.equals(loggedIn)) {
            event.forwardTo(DashboardView.class);
        }
    }
}