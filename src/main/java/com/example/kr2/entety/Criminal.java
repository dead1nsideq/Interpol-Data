package com.example.kr2.entety;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Criminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 25)
    private String firstName;
    @NotNull
    @NotBlank
    @Size(max = 25)
    private String lastName;
    @NotNull
    @NotBlank
    @Size(max = 25)
    private String nickName;

    @NotNull
    @Min(value = 130)
    @Max(value = 220)
    private Integer height;

    @NotNull
    @Enumerated(EnumType.STRING)
    private HairColor hairColor;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EyeColor eyeColor;

    @ManyToOne(fetch = FetchType.LAZY)
    private CriminalGroup criminalGroup;

    private LocalDate dateOfArrest;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    @NotBlank
    @Size(max = 35)
    private String country;
    @ManyToMany()
    @JoinTable(
            name = "criminal_languages",
            joinColumns = @JoinColumn(name = "criminal_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    private Set<Language> languages = new LinkedHashSet<>();
    @ManyToMany()
    @JoinTable(
            name = "criminal_records",
            joinColumns = @JoinColumn(name = "criminal_id"),
            inverseJoinColumns = @JoinColumn(name = "criminalRecord_id"))
    private Set<CriminalRecord> criminalRecords = new LinkedHashSet<>();


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dossier_id",referencedColumnName = "id")
    private Dossier dossier;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id",referencedColumnName = "id")
    private Image image;

    public void addLanguageToCriminal(Language language) {
            this.getLanguages().add(language);
            language.getCriminals().add(this);
    }
    public void addDossierToCriminal(Dossier dossier) {
        dossier.setCriminal(this);
        this.setDossier(dossier);
    }
    public void addImageToCriminal(Image image) {
        image.setCriminal(this);
        this.setImage(image);
    }

    public void addCriminalRecordToCriminal(CriminalRecord criminalRecord) {
        this.getCriminalRecords().add(criminalRecord);
        criminalRecord.getCriminals().add(this);
    }
}
