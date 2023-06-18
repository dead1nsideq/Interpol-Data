package com.example.kr2.repository;

import com.example.kr2.entety.CriminalGroup;
import com.example.kr2.entety.CriminalGroupType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriminalGroupRepository extends JpaRepository<CriminalGroup,Long> {
    CriminalGroup getCriminalGroupByCriminalGroupNameAndCriminalGroupType(String name, CriminalGroupType type);
}
