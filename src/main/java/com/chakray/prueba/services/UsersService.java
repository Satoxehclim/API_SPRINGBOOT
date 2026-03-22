package com.chakray.prueba.services;

import java.util.ArrayList;
import java.util.Optional;

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

    public boolean deleteUser(Long id){
        try {
            usersRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

    public UsersModel getUserById(Long id){
        return usersRepository.findById(id).orElse(null);
    }

    // Contains
    public ArrayList<UsersModel> findUserContainsEmail(String email) {
        return usersRepository.findByEmailContains(email);
    }

    public ArrayList<UsersModel> findUserContainsId(String id) {
        return usersRepository.findByIdContains(id);
    }

    public ArrayList<UsersModel> findUserContainsName(String name) {
        return usersRepository.findByNameContains(name);
    }

    public ArrayList<UsersModel> findUserContainsPhone(String phone) {
        return usersRepository.findByPhoneContains(phone);
    }

    public ArrayList<UsersModel> findUserContainsTaxId(String tax_id) {
        return usersRepository.findByTaxIdContains(tax_id);
    }

    public ArrayList<UsersModel> findUserContainsCreatedAt(String created_at) {
        return usersRepository.findByCreatedAtContains(created_at);
    }

    // Equals
    public ArrayList<UsersModel> findUserEqualsEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public Optional<UsersModel> findUserEqualsId(Long id) {
        return  usersRepository.findById(id);
    }

    public ArrayList<UsersModel> findUserEqualsName(String name) {
        return usersRepository.findByName(name);
    }

    public ArrayList<UsersModel> findUserEqualsPhone(String phone) {
        return usersRepository.findByPhone(phone);
    }

    public ArrayList<UsersModel> findUserEqualsTaxId(String tax_id) {
        return usersRepository.findByTaxId(tax_id);
    }

    public ArrayList<UsersModel> findUserEqualsCreatedAt(String created_at) {
        return usersRepository.findByCreatedAt(created_at);
    }
    
    // start with
    public ArrayList<UsersModel> findUserStartsWithEmail(String email) {
        return usersRepository.findByEmailStartsWith(email);
    }

    public ArrayList<UsersModel> findUserStartsWithId(String id) {
        return usersRepository.findByIdStartsWith(id);
    }

    public ArrayList<UsersModel> findUserStartsWithName(String name) {
        return usersRepository.findByNameStartsWith(name);
    }

    public ArrayList<UsersModel> findUserStartsWithPhone(String phone) {
        return usersRepository.findByPhoneStartsWith(phone);
    }

    public ArrayList<UsersModel> findUserStartsWithTaxId(String tax_id) {
        return usersRepository.findByTaxIdStartsWith(tax_id);
    }

    public ArrayList<UsersModel> findUserStartsWithCreatedAt(String created_at) {
        return usersRepository.findByCreatedAtStartsWith(created_at);
    }

    // end with
    public ArrayList<UsersModel> findUserEndsWithEmail(String email) {
        return usersRepository.findByEmailEndsWith(email);
    }

    public ArrayList<UsersModel> findUserEndsWithId(String id) {
        return usersRepository.findByIdEndsWith(id);
    }

    public ArrayList<UsersModel> findUserEndsWithName(String name) {
        return usersRepository.findByNameEndsWith(name);
    }

    public ArrayList<UsersModel> findUserEndsWithPhone(String phone) {
        return usersRepository.findByPhoneEndsWith(phone);
    }

    public ArrayList<UsersModel> findUserEndsWithTaxId(String tax_id) {
        return usersRepository.findByTaxIdEndsWith(tax_id);
    }

    public ArrayList<UsersModel> findUserEndsWithCreatedAt(String created_at) {
        return usersRepository.findByCreatedAtEndsWith(created_at);
    }

}