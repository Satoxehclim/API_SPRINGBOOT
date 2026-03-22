package com.chakray.prueba.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chakray.prueba.models.AddressModel;

@Repository
public interface  AddressRepository extends CrudRepository<AddressModel, Long> {

}