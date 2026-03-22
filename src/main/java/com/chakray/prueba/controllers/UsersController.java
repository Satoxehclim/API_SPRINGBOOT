package com.chakray.prueba.controllers;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.chakray.prueba.components.EncryptComponent;
import com.chakray.prueba.models.UsersModel;
import com.chakray.prueba.services.UsersService;


@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    UsersService usersService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationTime;

    @Autowired
    EncryptComponent encrypt;
    private final String regexTaxId = "^[A-ZÑ&]{3,4}[0-9]{6}[A-Z0-9]{3}$";
    private final String regexPhone = "^(\\+[0-9]{1,3} )?[0-9]{2} [0-9]{3} [0-9]{3} [0-9]{2}$";

    @GetMapping()
    public ArrayList<UsersModel> getUsers(@RequestParam(name ="sortedBy", required = false) String sortedBy){
        ArrayList<UsersModel> users = usersService.getUsers();
        if (sortedBy != null && !sortedBy.isEmpty()) {
            switch (sortedBy) {
                case "email" -> users.sort(Comparator.comparing(UsersModel::getEmail));
                case "id" -> users.sort(Comparator.comparing(UsersModel::getId));
                case "name" -> users.sort(Comparator.comparing(UsersModel::getName));
                case "phone" -> users.sort(Comparator.comparing(UsersModel::getPhone));
                case "tax_id" -> users.sort(Comparator.comparing(UsersModel::getTax_id));
                case "created_at" -> users.sort(Comparator.comparing(UsersModel::getCreated_at));
                default -> {
                }
            }
        }
        return users;
    }

    @GetMapping(params = "filter")
    public Object filterUsers(@RequestParam(name = "filter") String filter) {
        if (filter == null || filter.isEmpty()) {
            return "{\"message\": \"filter parameter is required\"}";
        }
        ArrayList<UsersModel> users = new ArrayList<>();
        String[] filterParts = filter.split("\\+");
        switch (filterParts[0]){
            case "email" -> {
                switch (filterParts[1]){
                    case "co" -> users = usersService.findUserContainsEmail(filterParts[2]);
                    case "eq" -> users = usersService.findUserEqualsEmail(filterParts[2]);
                    case "sw" -> users = usersService.findUserStartsWithEmail(filterParts[2]);
                    case "ew" -> users = usersService.findUserEndsWithEmail(filterParts[2]);
                }
            }
            case "id" -> {
                switch (filterParts[1]){
                    case "co" -> users = usersService.findUserContainsId(Long.valueOf(filterParts[2]));
                    case "eq" -> users = usersService.findUserEqualsId(Long.valueOf(filterParts[2]));
                    case "sw" -> users = usersService.findUserStartsWithId(Long.valueOf(filterParts[2]));
                    case "ew" -> users = usersService.findUserEndsWithId(Long.valueOf(filterParts[2]));
                }
            }
            case "name" -> {
                switch (filterParts[1]){
                    case "co" -> users = usersService.findUserContainsName(filterParts[2]);
                    case "eq" -> users = usersService.findUserEqualsName(filterParts[2]);
                    case "sw" -> users = usersService.findUserStartsWithName(filterParts[2]);
                    case "ew" -> users = usersService.findUserEndsWithName(filterParts[2]);
                }
            }
            case "phone" -> {
                switch (filterParts[1]){
                    case "co" -> users = usersService.findUserContainsPhone(filterParts[2]);
                    case "eq" -> users = usersService.findUserEqualsPhone(filterParts[2]);
                    case "sw" -> users = usersService.findUserStartsWithPhone(filterParts[2]);
                    case "ew" -> users = usersService.findUserEndsWithPhone(filterParts[2]);
                }
            }
            case "tax_id" -> {
                switch (filterParts[1]){
                    case "co" -> users = usersService.findUserContainsTaxId(filterParts[2]);
                    case "eq" -> users = usersService.findUserEqualsTaxId(filterParts[2]);
                    case "sw" -> users = usersService.findUserStartsWithTaxId(filterParts[2]);
                    case "ew" -> users = usersService.findUserEndsWithTaxId(filterParts[2]);
                }
            }
            case "created_at" -> {
                switch (filterParts[1]){
                    case "co" -> users = usersService.findUserContainsCreatedAt(filterParts[2]);
                    case "eq" -> users = usersService.findUserEqualsCreatedAt(filterParts[2]);
                    case "sw" -> users = usersService.findUserStartsWithCreatedAt(filterParts[2]);
                    case "ew" -> users = usersService.findUserEndsWithCreatedAt(filterParts[2]);
                }
            }
        }

        if (users.isEmpty()) {
            return "{\"message\": \"no users found\"}";
        }
        return users;
    }

    @PostMapping()
    public Object saveUser(@RequestBody UsersModel user){

        if (user.getEmail() == null || user.getName() == null || user.getPhone() == null || user.getPassword() == null || user.getTax_id() == null || user.getAddresses() == null) {
            return "{\"message\": \"missing required fields, the following fields are required: email, name, phone, password, tax_id and addresses object\"}";
        }

        Pattern patternTaxId = Pattern.compile(regexTaxId);
        Matcher matcherTaxId = patternTaxId.matcher(user.getTax_id());
        while (!matcherTaxId.find()) {
            return "{\"message\": \"invalid tax_id format, it should be like ABCD981220KM7\"}";
        }

        Pattern patternPhone = Pattern.compile(regexPhone);
        Matcher matcherPhone = patternPhone.matcher(user.getPhone());
        while (!matcherPhone.find()) {
            return "{\"message\": \"invalid phone format, it should be like +1 55 555 555 55 or +12 55 555 555 55 or +123 55 555 555 55 or 55 555 555 55\"}";
        }

        ZoneId zoneId = ZoneId.of("GMT+3");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        user.setCreated_at(now.format(formatter));
        
        user.setPassword(encrypt.encryptAES256(user.getPassword()));

        try {
            return this.usersService.saveUser(user);
        } catch (Exception e) {
            return "{\"message\": \"error saving user, review the data\"}";
        }
    }

    @DeleteMapping( path = "/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        boolean ok = usersService.deleteUser(id);
        if (ok) {
            return "{\"message\": \"user deleted successfully\"}";
        } else {
            return "{\"message\": \"could not delete user\"}";
        }
    }

    @PatchMapping( path = "/{id}")
    public Object updateUser(@PathVariable("id") Long id, @RequestBody UsersModel user){
        UsersModel userToUpdate = usersService.getUserById(id);
        if (userToUpdate == null) {
            return "{\"message\": \"user not found\"}";
        }
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userToUpdate.setName(user.getName());
        }
        
        if (user.getPhone() != null ) {
            Pattern patternPhone = Pattern.compile(regexPhone);
            Matcher matcherPhone = patternPhone.matcher(user.getPhone());
            if (matcherPhone.find()){
                userToUpdate.setPhone(user.getPhone());
            } else {
                return "{\"message\": \"invalid phone format, it should be like +1 55 555 555 55 or +12 55 555 555 55 or +123 55 555 555 55 or 55 555 555 55\"}";
            }
            
        }
        if (user.getPassword() != null) {
            userToUpdate.setPassword(encrypt.encryptAES256(user.getPassword()));
        }
        
        if (user.getTax_id() != null) {
            Pattern patternTaxId = Pattern.compile(regexTaxId);
            Matcher matcherTaxId = patternTaxId.matcher(user.getTax_id());
            if( matcherTaxId.find()){
                userToUpdate.setTax_id(user.getTax_id());
            } else {
                return "{\"message\": \"invalid tax_id format, it should be like ABCD981220KM7\"}";
            }
        }
        if (user.getAddresses() != null) {
            userToUpdate.setAddresses(user.getAddresses());
        }
        try {
            return usersService.saveUser(userToUpdate);
        } catch (Exception e) {
            return "{\"message\": \"error updating user, review the data\"}";
        }
    }

    @PostMapping("/login")
    public Object loginUser(@RequestBody UsersModel user){
        if (user.getTax_id() == null || user.getPassword() == null) {
            return "{\"message\": \"missing required fields, the following fields are required: tax_id and password\"}";
        }
        ArrayList<UsersModel> users = usersService.findUserEqualsTaxId(user.getTax_id());
        if (users.isEmpty()) {
            return "{\"message\": \"user not found\"}";
        }
        UsersModel foundUser = users.get(0);
        if (encrypt.decryptAES256(foundUser.getPassword()).equals(user.getPassword())) {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            String token = JWT.create()
                    .withIssuer("chakray-api-prueba")
                    .withSubject(foundUser.getTax_id())
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationTime))
                    .sign(algorithm);
            Map<String, Object> responsObject = new HashMap<>();
            responsObject.put("message", "login successful");
            responsObject.put("token", token);
            responsObject.put("user", foundUser);
            return responsObject;
        } else {
            return "{\"message\": \"invalid credentials\"}";
        }
    }
}