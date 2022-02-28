package com.example.missremedy.view;

import com.example.missremedy.pojo.Appointment;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Route("/view/app")
public class AppointmentView extends VerticalLayout implements BeforeEnterObserver {

    // Data Provided
    private String token;
    private String tokenId;
    private String appId;
    private Appointment appData;

    // Title Logo
    private HorizontalLayout header;
    private Button logo;
    private Button statusIdle;
    private Button statusActive;
    private Button page;

    // Appointment Header
    private HorizontalLayout appLayout;
    private Icon appIcon;
    private H2 appHeader;

    // Appointment Buttons
    private HorizontalLayout buttonList;
    private Button editApp;
    private Button showApp;

    // Profile Data
    private HorizontalLayout dateLine, infoLine;
    private TextField doctor, disease;
    private DatePicker date;
    private TimePicker time;

    public AppointmentView() {

        // Title Logo
        logo = new Button("MISS REMEDY");
        logo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        statusIdle = new Button("STATUS : IDLE");
        statusIdle.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        statusActive = new Button("STATUS : ACTIVE");
        statusActive.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        statusActive.setVisible(false);
        page = new Button();

        header = new HorizontalLayout();
        header.add(logo, page, statusIdle, statusActive);
        add(header);

        // Profile Header
        appIcon = new Icon(VaadinIcon.CALENDAR_CLOCK);
        appHeader = new H2();

        appLayout = new HorizontalLayout();
        appLayout.add(appIcon, appHeader);
        appLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        add(appLayout);

        // Appointment Buttons

        editApp = new Button("Cancel Appointment");
        editApp.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        editApp.addClickListener(buttonClickEvent -> {
            Boolean isSuccess = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/service/app/delApp/"+appId)
                    .header("Authorization", "Bearer "+token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .log()
                    .block();

            Map<String, List<String>> parameter = new HashMap<>();
            List<String> obj = new ArrayList<>();

            obj.add(token);
            obj.add(tokenId);
            parameter.put("1", obj);

            Notification.show("Cancel Appointment No." + appId + " Successful");

            UI.getCurrent().navigate("/view/main/", new QueryParameters(parameter));
        });

        showApp = new Button("Show Current Appointment");
        showApp.addClickListener(buttonClickEvent -> {
            appData = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/service/app/getApp/"+appId)
                    .header("Authorization", "Bearer "+token)
                    .retrieve()
                    .bodyToMono(Appointment.class)
                    .log()
                    .block();
            updateAppointment();
            showApp.setEnabled(false);
            Notification.show("Populated Current Appointment Successful");
        });

        buttonList = new HorizontalLayout();
        buttonList.add(showApp, editApp);

        add(buttonList);

        // Information
        date = new DatePicker();
        time = new TimePicker();

        date.setLabel("Appointment Date");
        date.setEnabled(false);
        time.setLabel("Appointment Time");
        time.setEnabled(false);

        dateLine = new HorizontalLayout();
        dateLine.add(date, time);

        doctor = new TextField();
        disease = new TextField();

        doctor.setLabel("Your Doctor");
        doctor.setEnabled(false);
        disease.setLabel("Your Disease");
        disease.setEnabled(false);

        date.setWidth("400px");
        time.setWidth("400px");
        doctor.setWidth("400px");
        disease.setWidth("400px");

        infoLine = new HorizontalLayout();
        infoLine.add(doctor, disease);

        add(dateLine, infoLine);

    }

    private void updateAppointment() {

        int year = Integer.parseInt(appData.getYear());
        int month = Integer.parseInt(appData.getMonth());
        int day = Integer.parseInt(appData.getDay());

        date.setValue(LocalDate.of(year, month, day));

        String temp = appData.getTime();
        int hour = Integer.parseInt(temp.substring(0, 2));
        int min = Integer.parseInt(temp.substring(3, 5));
        time.setValue(LocalTime.of(hour, min));
        doctor.setValue(appData.getDoctor());
        disease.setValue(appData.getDisease());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Map<String, List<String>> str = beforeEnterEvent.getLocation().getQueryParameters().getParameters();
        token = str.get("1").get(0);
        appId = str.get("1").get(1);
        tokenId = str.get("1").get(2);

        System.out.println(token);

        page.setText("PAGE : APPOINTMENT NO." + str.get("1").get(1));
        appHeader.setText("APPOINTMENT NO." + str.get("1").get(1));
    }
}
