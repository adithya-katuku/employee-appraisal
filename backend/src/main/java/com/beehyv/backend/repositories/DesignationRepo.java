package com.beehyv.backend.repositories;

import com.telsukuntunna.practice.models.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationRepo extends JpaRepository<Designation, Integer> {
}
