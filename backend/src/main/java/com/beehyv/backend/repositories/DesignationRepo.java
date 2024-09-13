package com.beehyv.backend.repositories;

import com.beehyv.backend.models.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationRepo extends JpaRepository<Designation, Integer> {
    Designation findByDesignation(String designation);
    default Designation saveOrFind(Designation designation){
        Designation res = findByDesignation(designation.getDesignation());
        if(res==null){
            res = save(designation);
        }
        return res;
    }
}
