package com.chakray.prueba.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chakray.prueba.models.AddressModel;
import com.chakray.prueba.services.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    AddressService addressService;

    @GetMapping()
    public ArrayList<AddressModel> getAddress(){
        return (ArrayList<AddressModel>) addressService.getAddresses();
    }
}