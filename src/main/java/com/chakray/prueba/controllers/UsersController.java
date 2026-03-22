package com.chakray.prueba.controllers;

import java.util.ArrayList;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chakray.prueba.models.UsersModel;
import com.chakray.prueba.services.UsersService;


@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    UsersService usersService;

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
    public UsersModel saveUser(@RequestBody UsersModel user){
        return this.usersService.saveUser(user);
    }

}