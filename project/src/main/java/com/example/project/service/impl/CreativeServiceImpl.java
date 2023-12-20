package com.example.project.service.impl;

import com.example.project.model.Campaign;
import com.example.project.model.Creatives;
import com.example.project.repository.ICreativeRepository;
import com.example.project.service.ICreativeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CreativeServiceImpl implements ICreativeService {
    private final ICreativeRepository iCreativeRepository;
    @Override
    public boolean existsByTitleAndDeleteFlagIsFalse(String title) {
        return iCreativeRepository.existsByTitleAndDeleteFlagIsFalse(title);
    }

    @Override
    public Optional<Creatives> findByName(String title) {
        return iCreativeRepository.findByName(title);
    }

    @Override
    public Optional<Creatives> findByCampaignIdAndDeleteFlagIsFalse(Optional<Campaign> campaign) {
        return iCreativeRepository.findByCampaignIdAndDeleteFlagIsFalse(campaign);
    }

    @Override
    public Creatives findByCampaignId(Campaign campaign) {
        return iCreativeRepository.findByCampaignId(campaign);
    }
}
