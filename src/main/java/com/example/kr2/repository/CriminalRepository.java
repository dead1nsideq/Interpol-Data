package com.example.kr2.repository;

import com.example.kr2.dto.CriminalDTO;
import com.example.kr2.entety.Criminal;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;


public interface CriminalRepository extends JpaRepository<Criminal, Long>, QuerydslPredicateExecutor<Criminal> {

    List<Criminal> findAll(Predicate predicate, Sort sort);

    Criminal getCriminalById(Long id);
}
