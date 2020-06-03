package ru.service.db.dao;

import ru.service.db.models.User;

import java.util.Optional;


public interface EmployeesDao extends CrudDao<User> {
    Optional<Integer> findByName(String firstName, String secondName);
}
