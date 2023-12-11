package com.example.project.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "campaign")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_campaign")
    private Long id;
    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "used_mount")
    private Long usedMount;

    @Column(name = "usage_mount")
    private Double usageRate;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "bid_mount")
    private Double bidAmount;

    @Column(name = "start_date", nullable = false, updatable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user_update;

    @Column(name = "delete_flag", nullable = false)
    private boolean deleteFlag;

    @Column(name = "delete_at", nullable = true, updatable = false)
    private LocalDateTime deleteAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;
}
