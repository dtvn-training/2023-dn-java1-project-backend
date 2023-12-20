package com.example.project.repository;

import com.example.project.model.Campaign;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface ICampaignRepository extends JpaRepository<Campaign, Long>  {
        @Query("SELECT c FROM Campaign c WHERE c.deleteFlag = false " +
                "AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
                "AND ((:startDate IS NULL AND :endDate IS NULL) OR " +
                "(:startDate IS NOT NULL AND :endDate IS NULL AND c.startDate >= :startDate) OR " +
                "(:startDate IS NULL AND :endDate IS NOT NULL AND c.startDate <= :endDate) OR " +
                "(c.startDate BETWEEN :startDate AND :endDate)) " +
                "ORDER BY c.status DESC")
        Page<Campaign> getCampaigns(String name,LocalDateTime startDate,LocalDateTime endDate,Pageable pageable);

        @Query("SELECT c FROM Campaign c WHERE c.deleteFlag = false AND c.id = :id")
        Optional<Campaign> findByIdAndDeleteFlagIsFalse(Long id);

        boolean existsByNameAndDeleteFlagIsFalse(String name);

        @Query("SELECT c FROM Campaign c " +
                "WHERE c.status = true AND c.deleteFlag = false AND c.bidAmount != 0 AND c.bidAmount <= (c.budget - c.usedAmount) " +
                "GROUP BY c.id, c.status, c.bidAmount, c.deleteFlag " +
                "ORDER BY c.bidAmount DESC")
        List<Campaign> findTopCampaigns(Pageable pageable);
}
