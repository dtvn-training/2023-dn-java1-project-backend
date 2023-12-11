package com.example.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "creatives")
public class Creatives {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creative_id")
    private Integer creativeId;


    @ManyToOne
    @JoinColumn(name = "id_campaign")
    private Campaign id_campaign;

    @Column(name = "name", length = 100)
    private String name;


    @Column(name = "description")
    private String description;


    @Column(name = "fianl_url")
    private String final_url;
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "delete_flag")
    private Boolean deleteFlag;
}
