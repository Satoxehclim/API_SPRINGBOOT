package com.chakray.prueba.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chakray.prueba.models.UsersModel;

@Repository
public interface  UsersRepository extends CrudRepository<UsersModel, Long> {

}