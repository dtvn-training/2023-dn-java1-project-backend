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
@Table(name = "campaigns")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_campaign")
    private Long id;
    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "bid_amount")
    private Double bidAmount;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user_update;

    @Column(name = "delete_flag", nullable = false)
    private boolean deleteFlag;

    @Column(name = "delete_at", nullable = true, updatable = false)
    private LocalDateTime deleteAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "usage_rate", columnDefinition = "FLOAT DEFAULT 0.0")
    private Double usageRate;

    @Column(name = "used_amount", columnDefinition = "INT DEFAULT 0")
    private Double usedAmount;
}
