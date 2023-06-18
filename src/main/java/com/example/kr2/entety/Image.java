package com.example.kr2.entety;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String originalFileName;

    private String contentType;

    private Long size;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] bytes;

    @OneToOne(mappedBy = "image")
    private Criminal criminal;

}
