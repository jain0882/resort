package com.resorts.season.repository;

import com.resorts.season.model.Resort;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface   ResortRepository extends JpaRepository<Resort, Long> {

}
