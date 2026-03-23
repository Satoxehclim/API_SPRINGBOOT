package com.chakray.prueba.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
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
    public abstract ArrayList<UsersModel> findByTaxId(String tax_id);
    public abstract ArrayList<UsersModel> findByCreatedAt(String created_at);

    // Estos repositorios son para el endpoint contains
    @Query("SELECT u FROM UsersModel u WHERE CAST(u.id AS string) LIKE %:id%")
    public abstract ArrayList<UsersModel> findByIdContains(String id);
    public abstract ArrayList<UsersModel> findByEmailContains(String email);
    public abstract ArrayList<UsersModel> findByNameContains(String name);
    public abstract ArrayList<UsersModel> findByPhoneContains(String phone);
    public abstract ArrayList<UsersModel> findByTaxIdContains(String tax_id);
    public abstract ArrayList<UsersModel> findByCreatedAtContains(String created_at);

    // Estos repositorios son para el endpoint starts with
    @Query("SELECT u FROM UsersModel u WHERE CAST(u.id AS string) LIKE :id%")
    public abstract ArrayList<UsersModel> findByIdStartsWith(String id);
    public abstract ArrayList<UsersModel> findByEmailStartsWith(String email);
    public abstract ArrayList<UsersModel> findByNameStartsWith(String name);
    public abstract ArrayList<UsersModel> findByPhoneStartsWith(String phone);
    public abstract ArrayList<UsersModel> findByTaxIdStartsWith(String tax_id);
    public abstract ArrayList<UsersModel> findByCreatedAtStartsWith(String created_at);

    // Estos repositorios son para el endpoint ends with
    @Query("SELECT u FROM UsersModel u WHERE CAST(u.id AS string) LIKE %:id")
    public abstract ArrayList<UsersModel> findByIdEndsWith(String id);
    public abstract ArrayList<UsersModel> findByEmailEndsWith(String email);
    public abstract ArrayList<UsersModel> findByNameEndsWith(String name);
    public abstract ArrayList<UsersModel> findByPhoneEndsWith(String phone);
    public abstract ArrayList<UsersModel> findByTaxIdEndsWith(String tax_id);
    public abstract ArrayList<UsersModel> findByCreatedAtEndsWith(String created_at);
}