package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.SetsDTO;
import com.strongBeton.strongBeton.dao.SetsRepository;
import com.strongBeton.strongBeton.entity.Sets;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SetsServiceImpl implements SetsService {

    private final ModelMapper modelMapper;
    private final SetsRepository setsRepository;

    public SetsServiceImpl(ModelMapper modelMapper, SetsRepository setsRepository) {
        this.modelMapper = modelMapper;
        this.setsRepository = setsRepository;
    }

    @Override
    @Transactional
    public SetsDTO saveSet(Sets sets) {
        return modelMapper.map(setsRepository.save(sets), SetsDTO.class);
    }


    @Override
    public List<SetsDTO> findSetsByWorkoutId(int workoutId){
        return setsRepository.findSetsByWorkoutDetailsId(workoutId)
                .stream()
                .map(set -> {
                    SetsDTO setsDTO =modelMapper.map(set, SetsDTO.class);
                    return setsDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSet(int theId) {
        setsRepository.deleteById(theId);
    }


}
