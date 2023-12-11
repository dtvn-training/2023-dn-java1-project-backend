package com.example.project.repository;

import com.example.project.model.Campaign;
import com.example.project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ICampaignRepository extends JpaRepository<Campaign, Integer>  {
    @Query("SELECT c FROM Campaign c WHERE c.deleteFlag = false " +
            "AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Campaign> findByName(@Param("name") String name, Pageable pageable);
    @Query("SELECT c FROM Campaign c WHERE c.deleteFlag = false AND c.id = :id")
    Optional<Campaign> findByIdAndDeleteFlagIsFalse(@Param("id") Long id);
    @Query("Select a From Campaign a Where a.deleteFlag = false")
    Page<Campaign> getAllCampaign(Pageable pageable);

    boolean existsByNameAndDeleteFlagIsFalse(String name);
}
