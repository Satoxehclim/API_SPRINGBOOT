package com.chakray.prueba.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chakray.prueba.models.AddressModel;
import com.chakray.prueba.repositories.AddressRepository;


// Este servicio lo implemente para observar que estuvieran recibiendose los datos de la direccion desde postman, aunque no viene incluidor en la prueba tecnica fue util al estar programando
@Service
public class AddressService {
    @Autowired
    AddressRepository adressRepository;
    
    public ArrayList<AddressModel> getAddresses() {
        return (ArrayList<AddressModel>) adressRepository.findAll();
    }

    public AddressModel saveAddress(AddressModel adress){
        return adressRepository.save(adress);
    }
}