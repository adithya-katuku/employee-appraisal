package com.beehyv.backend.repositories;

import com.beehyv.backend.models.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface AttributeRepo extends JpaRepository<Attribute, Integer> {
    Attribute findByAttribute(String attribute);
    default Attribute saveOrFind(Attribute attribute){
        Attribute res = findByAttribute(attribute.getAttribute());
        if(res == null){
            res = save(attribute);
        }

        return res;
    }
    default List<Attribute> saveOrFindAll(List<Attribute> attributes){
        List<Attribute> res = new ArrayList<>();
        for(Attribute attribute: attributes){
            res.add(saveOrFind(attribute));

        }

        return res;
    }
}
