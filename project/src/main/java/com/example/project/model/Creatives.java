package com.example.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private Integer creativeId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "final_url")
    private String finalUrl;

    @ManyToOne
    @JoinColumn(name = "id_campaign")
    private Campaign campaignId;

    @CreationTimestamp
    @Column(name = "create_at")
    private Timestamp createAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private Timestamp updateAt;

    @Column(name = "delete_flag")
    private Boolean deleteFlag;
}
