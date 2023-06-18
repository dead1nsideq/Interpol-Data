package com.example.kr2.repository;

import com.example.kr2.entety.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language,Long> {
    Language getLanguageByCriminalLanguage(String language);
}
