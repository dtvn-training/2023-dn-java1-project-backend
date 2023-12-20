package com.example.project.repository;

import com.example.project.model.Campaign;
import com.example.project.model.Creatives;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ICreativeRepository extends JpaRepository<Creatives, Integer> {
    boolean existsByTitleAndDeleteFlagIsFalse(String title);

    @Query("SELECT c FROM Creatives c WHERE c.title = :title AND c.deleteFlag = false")
    Optional<Creatives> findByName(String title);

    Optional<Creatives> findByCampaignIdAndDeleteFlagIsFalse(Optional<Campaign> campaign);

    Creatives findByCampaignId(Campaign campaign);


}
