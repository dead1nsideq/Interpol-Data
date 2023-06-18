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
public class CriminalGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 30)
    private String criminalGroupName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CriminalGroupType criminalGroupType;

    @OneToMany(mappedBy = "criminalGroup")
    private Set<Criminal> criminals = new LinkedHashSet<>();
}
