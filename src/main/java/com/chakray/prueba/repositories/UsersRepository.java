package com.chakray.prueba.repositories;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chakray.prueba.models.UsersModel;

@Repository
public interface  UsersRepository extends CrudRepository<UsersModel, Long> {

    // Nota para los desarrolladores: Separe los metodos para tener mejor legiibilidad
    // Estos repositoriios son para el endpoint equals
    public abstract ArrayList<UsersModel> findByEmail(String email);
    public abstract ArrayList<UsersModel> findByName(String name);
    public abstract ArrayList<UsersModel> findByPhone(String phone);
    public abstract ArrayList<UsersModel> findByTax_id(String tax_id);
    public abstract ArrayList<UsersModel> findByCreated_at(String created_at);

    // Estos repositorios son para el endpoint contains
    public abstract ArrayList<UsersModel> findByEmailContains(String email);
    public abstract ArrayList<UsersModel> findByNameContains(String name);
    public abstract ArrayList<UsersModel> findByPhoneContains(String phone);
    public abstract ArrayList<UsersModel> findByTax_idContains(String tax_id);
    public abstract ArrayList<UsersModel> findByCreated_atContains(String created_at);

    // Estos repositorios son para el endpoint starts with
    public abstract ArrayList<UsersModel> findByEmailStartsWith(String email);
    public abstract ArrayList<UsersModel> findByNameStartsWith(String name);
    public abstract ArrayList<UsersModel> findByPhoneStartsWith(String phone);
    public abstract ArrayList<UsersModel> findByTax_idStartsWith(String tax_id);
    public abstract ArrayList<UsersModel> findByCreated_atStartsWith(String created_at);

    // Estos repositorios son para el endpoint ends with
    public abstract ArrayList<UsersModel> findByEmailEndsWith(String email);
    public abstract ArrayList<UsersModel> findByNameEndsWith(String name);
    public abstract ArrayList<UsersModel> findByPhoneEndsWith(String phone);
    public abstract ArrayList<UsersModel> findByTax_idEndsWith(String tax_id);
    public abstract ArrayList<UsersModel> findByCreated_atEndsWith(String created_at);
}