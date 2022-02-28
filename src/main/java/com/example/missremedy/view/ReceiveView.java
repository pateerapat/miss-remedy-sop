package com.example.missremedy.view;

import com.example.missremedy.pojo.Medicine;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("/view/rec")
public class ReceiveView extends VerticalLayout implements BeforeEnterObserver {

    // Data Provided
    private String token;
    private String tokenId;
    private String medId;
    private Medicine medData;

    // Title Logo
    private HorizontalLayout header;
    private Button logo;
    private Button statusIdle;
    private Button statusActive;
    private Button page;

    // Medicine Header
    private HorizontalLayout medLayout;
    private Icon medIcon;
    private H2 medHeader;

    // Medicine Buttons
    private HorizontalLayout buttonList;
    private Button editMed;
    private Button showMed;

    // Profile Data
    private HorizontalLayout dateLine, infoLine;
    private TextField medicineData, pharmacy, location, price;
    private DatePicker date;
    private Div bahtPrefix;

    // ETC Button
    private Button imageGetter;
    private Image image;

    public ReceiveView() {
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

        // Medicine Header
        medIcon = new Icon(VaadinIcon.CLIPBOARD_CHECK);
        medHeader = new H2();

        medLayout = new HorizontalLayout();
        medLayout.add(medIcon, medHeader);
        medLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        add(medLayout);

        // Appointment Buttons

        editMed = new Button("Confirm Received");
        editMed.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editMed.addClickListener(buttonClickEvent -> {
            Boolean isSuccess = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/service/rec/delMed/"+medId)
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

            Notification.show("Successfully Confirmed Medicine No." + medId);

            UI.getCurrent().navigate("/view/main/", new QueryParameters(parameter));
        });

        showMed = new Button("Show Current Medicine");
        showMed.addClickListener(buttonClickEvent -> {
            medData = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/service/rec/getMed/"+medId)
                    .header("Authorization", "Bearer "+token)
                    .retrieve()
                    .bodyToMono(Medicine.class)
                    .log()
                    .block();

            updateMedicine();
            showMed.setEnabled(false);
            imageGetter.setEnabled(true);
            Notification.show("Populated Current Medicine Successful");
        });

        buttonList = new HorizontalLayout();
        buttonList.add(showMed, editMed);

        add(buttonList);

        // Information
        pharmacy = new TextField();
        location = new TextField();
        date = new DatePicker();

        date.setLabel("To Receive Date");
        date.setEnabled(false);
        location.setLabel("Location");
        location.setEnabled(false);
        pharmacy.setLabel("Pharmacy");
        pharmacy.setEnabled(false);

        dateLine = new HorizontalLayout();
        dateLine.add(pharmacy, location, date);

        medicineData = new TextField();
        price = new TextField();

        medicineData.setLabel("Medicine Name");
        medicineData.setEnabled(false);
        price.setLabel("Price");
        price.setEnabled(false);
        bahtPrefix = new Div();
        bahtPrefix.setText("฿");
        price.setPrefixComponent(bahtPrefix);

        pharmacy.setWidth("400px");
        location.setWidth("400px");
        date.setWidth("400px");
        medicineData.setWidth("400px");
        price.setWidth("400px");

        infoLine = new HorizontalLayout();
        infoLine.add(medicineData, price);

        add(dateLine, infoLine);

        // ETC Button
        imageGetter = new Button("Get Medicine Image");
        imageGetter.setEnabled(false);
        imageGetter.addClickListener(buttonClickEvent -> {
            String out = WebClient.create()
                    .get()
                    .uri("https://rximage.nlm.nih.gov/api/rximage/1/rxnav?name="+medData.getMedicine()+"&rLimit=1")
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .block();

            try {
                JSONParser parser = new JSONParser();
                JSONObject json01 = (JSONObject) parser.parse(out);
                String str01 = json01.get("nlmRxImages").toString();
                JSONArray json02 = (JSONArray) parser.parse(str01);
                String str02 = json02.get(0).toString();
                JSONObject json03 = (JSONObject) parser.parse(str02);
                image.setSrc(json03.get("imageUrl").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            imageGetter.setEnabled(false);
            Notification.show("Image Arrived");
        });

        add(imageGetter);

        image = new Image();
        add(image);
    }

    private void updateMedicine() {

        int year = Integer.parseInt(medData.getYear());
        int month = Integer.parseInt(medData.getMonth());
        int day = Integer.parseInt(medData.getDay());

        date.setValue(LocalDate.of(year, month, day));

        pharmacy.setValue(medData.getPharmacy());
        location.setValue(medData.getLocation());

        medicineData.setValue(medData.getMedicine());
        price.setValue(medData.getPrice());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Map<String, List<String>> str = beforeEnterEvent.getLocation().getQueryParameters().getParameters();
        token = str.get("1").get(0);
        medId = str.get("1").get(1);
        tokenId = str.get("1").get(2);

        System.out.println(token);

        page.setText("PAGE : MEDICINE NO." + str.get("1").get(1));
        medHeader.setText("MEDICINE NO." + str.get("1").get(1));
    }
}
