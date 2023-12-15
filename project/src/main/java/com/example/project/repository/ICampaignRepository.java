package com.example.project.repository;

import com.example.project.model.Campaign;
import com.example.project.model.User;
import com.google.api.services.storage.Storage.BucketAccessControls.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface ICampaignRepository extends JpaRepository<Campaign, Long>  {
        @Query("SELECT c FROM Campaign c WHERE c.deleteFlag = false " +
            "AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND ((:startdate IS NULL AND :enddate IS NULL) OR (c.startDate BETWEEN :startdate AND :enddate))" +
            " ORDER BY c.status desc")
        Page<Campaign> getCampaign(
            @Param("name") String name,
            @Param("startdate") LocalDateTime startdate,
            @Param("enddate") LocalDateTime enddate,
            Pageable pageable);

        @Query("SELECT c FROM Campaign c WHERE c.deleteFlag = false AND c.id = :id")
        Optional<Campaign> findByIdAndDeleteFlagIsFalse(Long id);

        boolean existsByNameAndDeleteFlagIsFalse(String name);
}
