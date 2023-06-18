package com.example.kr2.entety;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CriminalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 25)
    @Column(name="criminal_record",unique = true)
    private String criminalRecord;

    @ManyToMany(mappedBy = "criminalRecords")
    private Set<Criminal> criminals = new LinkedHashSet<>();
}
