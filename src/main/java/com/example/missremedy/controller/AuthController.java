package com.example.missremedy.controller;

import com.example.missremedy.pojo.User;
import com.example.missremedy.repository.user.UserRepository;
import com.example.missremedy.repository.user.UserService;
import com.example.missremedy.security.authentication.AuthenticationResponse;
import com.example.missremedy.security.authentication.SubscribeRequest;
import com.example.missremedy.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/dashboard")
    private String testingToken() {
        return "Welcome To The Dashboard " + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/subs")
    private ResponseEntity<?> subscribeClient(@RequestBody SubscribeRequest subscribeRequest){
        String username = subscribeRequest.getUsername();
        String password = subscribeRequest.getPassword();
        String firstName = subscribeRequest.getFirstName();
        String lastName = subscribeRequest.getLastName();
        String hnId = subscribeRequest.getHnId();
        String hospital = subscribeRequest.getHospital();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setHnId(hnId);
        user.setHospital(hospital);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseEntity.ok(new AuthenticationResponse("Error During Client Subscription" + username));
        }

        return ResponseEntity.ok(new AuthenticationResponse("Successful Subscription For Client " + username));
    }

    @PostMapping("/auth")
    private ResponseEntity<?> authenticateClient(@RequestBody MultiValueMap<String, String> n){
        Map<String, String> d = n.toSingleValueMap();
        String username = d.get("1");
        String password = d.get("2");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            return ResponseEntity.ok(new AuthenticationResponse("Error"));
        }

        UserDetails loadedUser = userService.loadUserByUsername(username);

        String generatedToken = jwtUtils.generateToken(loadedUser);

        return ResponseEntity.ok(new AuthenticationResponse(generatedToken));
    }

}
