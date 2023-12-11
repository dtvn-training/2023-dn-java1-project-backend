package com.example.project.repository;

import com.example.project.model.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ICampaignRepository {
    @Query("SELECT c FROM Campaign c WHERE c.deleteFlag = false " +
            "AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Campaign> findByName(@Param("name") String name, Pageable pageable);
    @Query("SELECT c FROM Campaign c WHERE c.deleteFlag = false AND c.id = :id")
    Optional<Campaign> findByIdAndDeleteFlagIsFalse(@Param("id") Integer id);
    @Query("Select a From Campaign a Where a.deleteFlag = false")
    Page<Campaign> getAllCampaign(Pageable pageable);

    boolean existsByNameAndDeleteFlagIsFalse(String name);
}
