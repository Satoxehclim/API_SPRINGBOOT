package com.chakray.prueba.controllers;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chakray.prueba.Encrypt;
import com.chakray.prueba.models.UsersModel;
import com.chakray.prueba.services.UsersService;


@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    UsersService usersService;

    Encrypt encrypt = new Encrypt();
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

    //TODO: Implementar endpoint para filtrar usuarios con los parametros[email|id|name|phone|tax_id|created_at]+[co|eq|sw|ew]+[value], donde co es contains, eq es equals, sw es starts with y ew es ends with

    @PostMapping()
    public String saveUser(@RequestBody UsersModel user){

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy HH:mm");
        user.setCreated_at(now.format(formatter));
        
        user.setPassword(encrypt.encryptAES256(user.getPassword()));

        try {
            return this.usersService.saveUser(user).toString();
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

    //TODO: Implementar endpoint para actualizar usuario

}