package com.example.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "creatives")
public class Creatives {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "preview_image")
    private String preview_image;

    @Column(name = "fianl_url")
    private String final_url;

    @ManyToOne
    @JoinColumn(name = "id_campaign")
    private Campaign id_campaign;
}
