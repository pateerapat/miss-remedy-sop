package com.example.missremedy.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "/login")
public class LoginView extends Composite<LoginOverlay> {

    private LoginI18n loginI18n;
    private LoginI18n.Form form;

    private LoginOverlay loginOverlay;

    public LoginView() {

        loginI18n = LoginI18n.createDefault();
        form = loginI18n.getForm();
        form.setTitle("Login");
        form.setUsername("Identification ID");
        form.setForgotPassword("Forgot Password ?");
        form.setSubmit("Login");

        loginOverlay = getContent();
        loginOverlay.setOpened(true);
        loginOverlay.setTitle("Miss Remedy");
        loginOverlay.setDescription("Welcome To Your Medication Service");
        loginOverlay.setForgotPasswordButtonVisible(false);
        loginOverlay.setI18n(loginI18n);

        loginOverlay.addLoginListener(event -> {

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("1", event.getUsername());
            formData.add("2", event.getPassword());

            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/auth")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            String str;
            JSONParser parser = new JSONParser();
            try {
                JSONObject json = (JSONObject) parser.parse(out);
                str = json.get("response").toString();
                if (str.equals("Error")) {
                    Notification.show("Wrong Credentials");
                    loginOverlay.setError(true);
                } else {
                    Map<String, List<String>> parameter = new HashMap<>();
                    List<String> obj = new ArrayList<>();

                    System.out.println(str);

                    obj.add(str);
                    obj.add(event.getUsername());
                    parameter.put("1", obj);
                    UI.getCurrent().navigate("/view/main", new QueryParameters(parameter));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }
}
