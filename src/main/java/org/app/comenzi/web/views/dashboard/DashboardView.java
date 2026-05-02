package org.app.comenzi.web.views.dashboard;

import org.layout.MainView; // Importul vital pentru meniul tău
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

// Importuri pentru ApexCharts
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.helper.Series;

// Import pentru harta
import com.vaadin.flow.component.html.Div;

//Import pentru date din baza de date
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.Trotineta;

@PageTitle("Dashboard | Ride4Fun")
@Route(value = "panou-control", layout = MainView.class)
public class DashboardView extends VerticalLayout {
    private EntityManager em;

    public DashboardView() {
        // Setări pentru alinierea paginii
        setSpacing(true);
        setPadding(true);
        setWidthFull();

        H2 titlu = new H2("Panou de Control (Dashboard)");

        // Integrarea elementelor vizuale în pagină
        add(titlu, createKPISection(), createChartSection(), createMapSection());
    }

    // 1. Indicatori de performanță (KPI) cu date dinamice
    private HorizontalLayout createKPISection() {
        HorizontalLayout kpiLayout = new HorizontalLayout();
        kpiLayout.setWidthFull();
        kpiLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        // Inițializarea conexiunii la baza de date
        initDatabaseConnection();

        // Preluarea datelor reale prin metodele de mai jos
        long totalTrotinete = countTotalTrotinete();
        long trotineteDisponibile = countTrotineteDisponibile();
        long trotineteInService = countTrotineteInService();

        // Construcția containerelor cu datele extrase
        kpiLayout.add(
                createCard("Trotinete disponibile / Total", trotineteDisponibile + " / " + totalTrotinete),
                createCard("Trotinete în service", String.valueOf(trotineteInService)),
                // Păstrăm un container cu mock-up static pentru venituri până la implementarea modulului financiar
                createCard("Curse finalizate (Astăzi)", "124")
        );

        return kpiLayout;
    }

    // METODE PENTRU INTEROGAREA BAZEI DE DATE

    private void initDatabaseConnection() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
            this.em = emf.createEntityManager();
        } catch (Exception e) {
            System.err.println("Eroare la conectarea cu baza de date în Dashboard: " + e.getMessage());
        }
    }

    private long countTotalTrotinete() {
        if (em == null) return 0;
        try {
            return em.createQuery("SELECT COUNT(t) FROM Trotineta t", Long.class).getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    private long countTrotineteDisponibile() {
        if (em == null) return 0;
        try {
            // Căutăm exact statusul "disponibil" definit de tine în FormTrotinetaView
            return em.createQuery("SELECT COUNT(t) FROM Trotineta t WHERE t.status = 'disponibil'", Long.class).getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    private long countTrotineteInService() {
        if (em == null) return 0;
        try {
            // Căutăm exact starea tehnică "service"
            return em.createQuery("SELECT COUNT(t) FROM Trotineta t WHERE t.stareTehnica = 'service'", Long.class).getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    // Metodă pentru generarea containerelor
    private VerticalLayout createCard(String titlu, String valoare) {
        VerticalLayout card = new VerticalLayout();

        card.getStyle().set("border", "1px solid #ddd");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("padding", "20px");
        card.getStyle().set("box-shadow", "0 4px 6px rgba(0,0,0,0.1)");
        card.getStyle().set("background-color", "#ffffff");
        card.getStyle().set("width", "30%");

        Span titleSpan = new Span(titlu);
        titleSpan.getStyle().set("color", "gray");
        titleSpan.getStyle().set("font-size", "14px");
        titleSpan.getStyle().set("text-transform", "uppercase");

        Span valueSpan = new Span(valoare);
        valueSpan.getStyle().set("font-size", "28px");
        valueSpan.getStyle().set("font-weight", "bold");
        valueSpan.getStyle().set("color", "#2c3e50");

        card.add(titleSpan, valueSpan);
        return card;
    }

    // 2. Secțiunea pentru graficul de venituri
    private VerticalLayout createChartSection() {
        VerticalLayout chartLayout = new VerticalLayout();
        chartLayout.setWidthFull();
        chartLayout.getStyle().set("margin-top", "20px");
        chartLayout.getStyle().set("background-color", "#ffffff");
        chartLayout.getStyle().set("padding", "20px");
        chartLayout.getStyle().set("border-radius", "8px");
        chartLayout.getStyle().set("box-shadow", "0 4px 6px rgba(0,0,0,0.1)");

        H2 titluGrafic = new H2("Analiza Veniturilor (Săptămâna Curentă)");
        titluGrafic.getStyle().set("font-size", "18px");
        titluGrafic.getStyle().set("color", "#34495e");

        // Versiunea sigură (Safe Mode) pentru ApexCharts
        ApexCharts simpleChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.BAR)
                        .build())
                .withSeries(
                        new Series<>("Tarif Standard", 150.0, 200.0, 180.0, 220.0, 170.0, 190.0, 210.0),
                        new Series<>("Tarif Dinamic", 90.0, 120.0, 150.0, 130.0, 100.0, 140.0, 160.0)
                )
                .withXaxis(XAxisBuilder.get()
                        .withCategories("Luni", "Marți", "Miercuri", "Joi", "Vineri", "Sâmbătă", "Duminică")
                        .build())
                .build();

        simpleChart.setWidth("100%");
        simpleChart.setHeight("350px");

        chartLayout.add(titluGrafic, simpleChart);
        return chartLayout;
    }


    // 3. Secțiunea pentru Harta Interactivă
    private VerticalLayout createMapSection() {
        VerticalLayout mapLayout = new VerticalLayout();
        mapLayout.setWidthFull();
        mapLayout.getStyle().set("margin-top", "20px");
        mapLayout.getStyle().set("background-color", "#ffffff");
        mapLayout.getStyle().set("padding", "20px");
        mapLayout.getStyle().set("border-radius", "8px");
        mapLayout.getStyle().set("box-shadow", "0 4px 6px rgba(0,0,0,0.1)");

        H2 titluHarta = new H2("Localizarea Flotei în Timp Real (Iași)");
        titluHarta.getStyle().set("font-size", "18px");
        titluHarta.getStyle().set("color", "#34495e");

        // Containerul HTML simplu pentru hartă
        Div mapContainer = new Div();
        mapContainer.setId("harta-trotinete");
        mapContainer.setWidthFull();
        mapContainer.setHeight("400px");
        mapContainer.getStyle().set("border", "1px solid #ced4da");
        mapContainer.getStyle().set("border-radius", "8px");
        mapContainer.getStyle().set("z-index", "1"); // Previne suprapunerea vizuală

        // Injectarea librăriei gratuite Leaflet.js direct prin cod Java
        mapContainer.addAttachListener(event -> {
            String scriptJavaScript =
                    "if (!window.leafletIncarcat) {" +
                            "   const link = document.createElement('link');" +
                            "   link.rel = 'stylesheet';" +
                            "   link.href = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.css';" +
                            "   document.head.appendChild(link);" +
                            "   const script = document.createElement('script');" +
                            "   script.src = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.js';" +
                            "   script.onload = function() { window.leafletIncarcat = true; incarcaHarta(); };" +
                            "   document.head.appendChild(script);" +
                            "} else { incarcaHarta(); }" +
                            "function incarcaHarta() {" +
                            "   setTimeout(function() {" +
                            "       var container = document.getElementById('harta-trotinete');" +
                            "       if(container && container._leaflet_id) { container._leaflet_id = null; }" +
                            // Coordonatele pentru centrul orașului Iași
                            "       var map = L.map('harta-trotinete').setView([47.1585, 27.5873], 14);" +
                            "       L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);" +
                            // Trotineta 1: Zona Palatul Culturii / Palas
                            "       L.marker([47.1570, 27.5870]).addTo(map).bindPopup('Trotineta 1 - Baterie 85% (Zona Palas)');" +
                            // Trotineta 2: Zona Copou / Universitate
                            "       L.marker([47.1750, 27.5700]).addTo(map).bindPopup('Trotineta 2 - Baterie 30% (Zona Copou)');" +
                            "   }, 300);" +
                            "}";
            event.getUI().getPage().executeJs(scriptJavaScript);
        });

        mapLayout.add(titluHarta, mapContainer);
        return mapLayout;
    }
}