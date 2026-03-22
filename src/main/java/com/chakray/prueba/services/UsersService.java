package com.chakray.prueba.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chakray.prueba.models.UsersModel;
import com.chakray.prueba.repositories.UsersRepository;

@Service
public class UsersService {
    @Autowired
    UsersRepository usersRepository;

    public ArrayList<UsersModel> getUsers() {
        return (ArrayList<UsersModel>) usersRepository.findAll();
    }

    public UsersModel saveUser(UsersModel user){
        return usersRepository.save(user);
    }

}