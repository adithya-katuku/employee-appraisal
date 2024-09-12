package com.beehyv.backend.repositories;

import com.telsukuntunna.practice.models.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepo extends JpaRepository<Attribute, Integer> {
}
