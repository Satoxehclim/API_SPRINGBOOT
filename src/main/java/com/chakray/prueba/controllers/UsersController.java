package com.chakray.prueba.controllers;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

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

    @PostMapping()
    public String saveUser(@RequestBody UsersModel user){

        if (user.getEmail() == null || user.getName() == null || user.getPhone() == null || user.getPassword() == null || user.getTax_id() == null) {
            return "{\"message\": \"missing required fields\"}";
        }

        ZoneId zoneId = ZoneId.of("GMT+3");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy HH:mm");
        user.setCreated_at(now.format(formatter));
        
        user.setPassword(encrypt.encryptAES256(user.getPassword()));

        return this.usersService.saveUser(user).toString();
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

}