package com.example.kr2.repository;

import com.example.kr2.entety.CriminalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriminalRecordRepository extends JpaRepository<CriminalRecord,Long> {
    CriminalRecord getCriminalRecordByCriminalRecord(String str);
}
