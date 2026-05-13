package org.example.repository;

import org.example.entity.District;
import org.example.entity.SportField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SportFieldRepository extends JpaRepository<SportField, Long> {


    @Query("""
     select s from SportField s 
     where (cast(:search as string) is null or s.name ilike concat('%', cast(:search as string), '%')
     and s.deleted=false)
          """)
    Page<SportField> findByCriteria(@Param("search") String search, Pageable pageable);
}
