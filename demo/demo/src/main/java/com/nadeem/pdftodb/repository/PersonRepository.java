package com.nadeem.pdftodb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nadeem.pdftodb.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // JpaRepository provides all the basic CRUD operations, including saveAll.
}

