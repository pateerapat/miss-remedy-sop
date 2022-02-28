package com.example.missremedy.view;

import com.example.missremedy.pojo.Appointment;
import com.example.missremedy.pojo.Medicine;
import com.example.missremedy.pojo.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("/view/main")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    // Data Provided
    private String token;
    private String tokenId;
    private User userData;
    private List<Appointment> appList;
    private List<Medicine> recList;

    // Title Logo
    private HorizontalLayout header;
    private Button logo;
    private Button statusIdle;
    private Button statusActive;
    private Button page;

    // Profile Header
    private HorizontalLayout proLayout;
    private Icon proIcon;
    private H2 proHeader;

    // Profile Edit
    private ProgressBar proBar;

    // Profile Buttons
    private HorizontalLayout profileList;
    private Button showPro;
    private Button editPro;
    private Button savePro;

    // Profile Data
    private HorizontalLayout proLine;
    private TextField firstName, lastName, hnId, hospital;

    // Appointment Header
    private HorizontalLayout appLayout;
    private Icon appIcon;
    private H2 appHeader;

    // Appointment Buttons
    private HorizontalLayout buttonList;
    private Button addApp;
    private Button showApp;

    // Add Appointment
    private HorizontalLayout addList;
    private DatePicker dateInput;
    private TimePicker timeInput;
    private TextField diseaseInput;
    private Button saveApp;

    // List Of Receives
    private Grid<Appointment> grid;

    // Receives Header
    private HorizontalLayout recLayout;
    private Icon recIcon;
    private H2 recHeader;

    // Receive Button
    private Button showRec;

    // List Of Receive
    private Grid<Medicine> recGrid;

    public MainView() {

        // Title Logo
        logo = new Button("MISS REMEDY");
        logo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        statusIdle = new Button("STATUS : IDLE");
        statusIdle.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        statusActive = new Button("STATUS : ACTIVE");
        statusActive.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        statusActive.setVisible(false);
        page = new Button("PAGE : HOME");

        header = new HorizontalLayout();
        header.add(logo, page, statusIdle, statusActive);
        add(header);

        // Edit In Progress
        proBar = new ProgressBar();
        proBar.setVisible(false);
        proBar.setIndeterminate(true);

        add(proBar);

        // Profile Header
        proIcon = new Icon(VaadinIcon.USER_CARD);
        proHeader = new H2("PROFILE");

        proLayout = new HorizontalLayout();
        proLayout.add(proIcon, proHeader);
        proLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        add(proLayout);

        // Profile Buttons
        showPro = new Button("Show Profile");
        showPro.addClickListener(buttonClickEvent -> {
            userData = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/service/user/"+tokenId)
                    .header("Authorization", "Bearer "+token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<User>(){})
                    .log()
                    .block();

            firstName.setValue(userData.getFirstName());
            lastName.setValue(userData.getLastName());
            hnId.setValue(userData.getHnId());
            hospital.setValue(userData.getHospital());

            showPro.setEnabled(false);
            editPro.setEnabled(true);
            Notification.show("Populated Profile Successful");
        });

        editPro = new Button("Edit Name");
        editPro.addClickListener(buttonClickEvent -> {
            if (editPro.getText().equals("Edit Name")) {
                proBar.setVisible(true);
                statusIdle.setVisible(false);
                statusActive.setVisible(true);

                firstName.setEnabled(true);
                lastName.setEnabled(true);

                editPro.setText("Cancel Edit");
                savePro.setEnabled(true);
            } else {
                proBar.setVisible(false);
                statusIdle.setVisible(true);
                statusActive.setVisible(false);

                firstName.setValue(userData.getFirstName());
                lastName.setValue(userData.getLastName());

                firstName.setEnabled(false);
                lastName.setEnabled(false);
                editPro.setText("Edit Name");
                savePro.setEnabled(false);
            }
        });

        savePro = new Button("Save Name");
        savePro.addClickListener(buttonClickEvent -> {
            MultiValueMap<String, String> updater = new LinkedMultiValueMap<>();
            updater.add("1", firstName.getValue());
            updater.add("2", lastName.getValue());
            updater.add("3", userData.getUsername());
            User user = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/service/updateUser/")
                    .header("Authorization", "Bearer "+token)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(updater))
                    .retrieve()
                    .bodyToMono(User.class)
                    .log()
                    .block();
            userData = user;
            savePro.setEnabled(false);
            editPro.setText("Edit Name");
            firstName.setEnabled(false);
            lastName.setEnabled(false);
            firstName.setValue(userData.getFirstName());
            lastName.setValue(userData.getLastName());

            proBar.setVisible(false);
            statusIdle.setVisible(true);
            statusActive.setVisible(false);

            Notification.show("Save Profile Successful");
        });

        editPro.setEnabled(false);
        savePro.setEnabled(false);

        profileList = new HorizontalLayout();
        profileList.add(showPro, editPro, savePro);
        add(profileList);

        // Profile Data
        firstName = new TextField("Name");
        lastName = new TextField("Surname");
        hnId = new TextField("Hospital Number ID");
        hospital = new TextField("Hospital");
        firstName.setEnabled(false);
        lastName.setEnabled(false);
        hnId.setEnabled(false);
        hospital.setEnabled(false);
        firstName.setWidth("400px");
        lastName.setWidth("400px");
        hnId.setWidth("400px");
        hospital.setWidth("400px");

        proLine = new HorizontalLayout();
        proLine.add(firstName, lastName, hnId, hospital);

        add(proLine);

        // Appointment Header
        appIcon = new Icon(VaadinIcon.CALENDAR_CLOCK);
        appHeader = new H2("APPOINTMENT");

        appLayout = new HorizontalLayout();
        appLayout.add(appIcon, appHeader);
        appLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        add(appLayout);

        // Appointment Buttons
        addApp = new Button("Add Appointment");
        addApp.addClickListener(buttonClickEvent -> {
            if (addList.isVisible()) {
                statusIdle.setVisible(true);
                statusActive.setVisible(false);
                addList.setVisible(false);
                proBar.setVisible(false);
            } else {
                statusIdle.setVisible(false);
                statusActive.setVisible(true);
                addList.setVisible(true);
                proBar.setVisible(true);
            }
        });

        showApp = new Button("Show Appointment");
        showApp.addClickListener(buttonClickEvent -> {
            appList = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/service/app/getAll/"+tokenId)
                    .header("Authorization", "Bearer "+token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Appointment>>(){})
                    .log()
                    .block();
            updateAppointmentList();
            showApp.setEnabled(false);
            Notification.show("Populated Appointments Successful");
        });

        buttonList = new HorizontalLayout();
        buttonList.add(showApp, addApp);

        add(buttonList);

        // Appointment Input
        dateInput = new DatePicker();
        timeInput = new TimePicker();
        diseaseInput = new TextField();

        dateInput.setLabel("Appointment Date");
        dateInput.setMin(LocalDate.now());
        timeInput.setLabel("Appointment Time");
        diseaseInput.setLabel("Disease");

        saveApp = new Button("Save Appointment");
        saveApp.addClickListener(buttonClickEvent -> {
            statusIdle.setVisible(true);
            statusActive.setVisible(false);
            addList.setVisible(false);

            MultiValueMap<String, String> updater = new LinkedMultiValueMap<>();

            String dateUpdate = dateInput.getValue().toString();
            String y = dateUpdate.substring(0, 4);
            String m = dateUpdate.substring(5, 7);
            String d = dateUpdate.substring(8, 10);

            String timeUpdate = timeInput.getValue().toString();

            updater.add("1", tokenId);
            updater.add("2", y);
            updater.add("3", m);
            updater.add("4", d);
            updater.add("5", timeUpdate);
            updater.add("6", diseaseInput.getValue());

            System.out.println(updater);

            Appointment out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/service/app/addApp/")
                    .header("Authorization", "Bearer "+token)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(updater))
                    .retrieve()
                    .bodyToMono(Appointment.class)
                    .log()
                    .block();

            diseaseInput.setValue("");
            dateInput.setValue(null);
            timeInput.setValue(null);

            appList = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/service/app/getAll/"+tokenId)
                    .header("Authorization", "Bearer "+token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Appointment>>(){})
                    .log()
                    .block();

            updateAppointmentList();
            Notification.show("Save Appointment Successful");
        });

        addList = new HorizontalLayout();
        addList.add(diseaseInput, dateInput, timeInput, saveApp);
        addList.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        addList.setVisible(false);
        add(addList);

        // List Of Appointments
        grid = new Grid<>(Appointment.class);
        setSizeFull();
        configurationAppointmentGrid();

        add(grid);

        // Receive Header
        recIcon = new Icon(VaadinIcon.CLIPBOARD_CHECK);
        recHeader = new H2("RECEIVE");

        recLayout = new HorizontalLayout();
        recLayout.add(recIcon, recHeader);
        recLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        add(recLayout);

        // Receive Buttons
        showRec = new Button("Show Receive");
        showRec.addClickListener(buttonClickEvent -> {
            recList = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/service/rec/getAll/"+tokenId)
                    .header("Authorization", "Bearer "+token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Medicine>>(){})
                    .log()
                    .block();
            updateReceiveList();
            showRec.setEnabled(false);
            Notification.show("Populated Receives Successful");
        });

        add(showRec);

        // List Of Receive
        recGrid = new Grid<>(Medicine.class);
        setSizeFull();
        configurationReceiveGrid();

        add(recGrid);
    }

    private void updateAppointmentList() {
        grid.setItems(appList);
    }

    private void configurationAppointmentGrid() {
        grid.setSizeFull();
        grid.setColumns("disease", "day", "month", "year");
        grid.addItemClickListener(event -> {
            Map<String, List<String>> parameter = new HashMap<>();
            List<String> obj = new ArrayList<>();

            obj.add(token);
            obj.add(event.getItem().getAppId());
            obj.add(tokenId);
            parameter.put("1", obj);

            UI.getCurrent().navigate("/view/app/", new QueryParameters(parameter));
        });
    }

    private void updateReceiveList() {
        recGrid.setItems(recList);
    }

    private void configurationReceiveGrid() {
        recGrid.setSizeFull();
        recGrid.setColumns("medicine", "day", "month", "year");
        recGrid.addItemClickListener(event -> {

            Map<String, List<String>> parameterRev = new HashMap<>();
            List<String> obj = new ArrayList<>();

            obj.add(token);
            obj.add(event.getItem().getMedId());
            obj.add(tokenId);
            parameterRev.put("1", obj);

            UI.getCurrent().navigate("/view/rec/", new QueryParameters(parameterRev));
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Map<String, List<String>> str = beforeEnterEvent.getLocation().getQueryParameters().getParameters();
        token = str.get("1").get(0);
        tokenId = str.get("1").get(1);
    }
}
