package com.islamnizami.taskboarddevlab.service;

import com.islamnizami.taskboarddevlab.exception.BadRequestException;
import com.islamnizami.taskboarddevlab.exception.ResourceNotFoundException;
import com.islamnizami.taskboarddevlab.mapper.LabelMapper;
import com.islamnizami.taskboarddevlab.model.dto.LabelRequestDTO;
import com.islamnizami.taskboarddevlab.model.dto.LabelResponseDTO;
import com.islamnizami.taskboarddevlab.model.entity.Label;
import com.islamnizami.taskboarddevlab.model.entity.User;
import com.islamnizami.taskboarddevlab.repository.LabelRepository;
import com.islamnizami.taskboarddevlab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;
    private final UserRepository userRepository;
    private final LabelMapper labelMapper;

    @Transactional
    public LabelResponseDTO createLabel(LabelRequestDTO labelRequestDTO, String email) {
        User user = getUserByEmail(email);

        if (labelRepository.existsByNameIgnoreCaseAndUserIdAndIsDeletedFalse(labelRequestDTO.getName(), user.getId())) {
            throw new BadRequestException("Label with this name already exists for your account!");
        }

        Label label = labelMapper.toEntity(labelRequestDTO);
        label.setUser(user);

        Label saved = labelRepository.save(label);
        return labelMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<LabelResponseDTO> getAllLabels(String email) {
        User user = getUserByEmail(email);
        List<Label> labels = labelRepository.findByUserIdAndIsDeletedFalse(user.getId());
        return labelMapper.toResponseDTOList(labels);
    }

    @Transactional(readOnly = true)
    public LabelResponseDTO getLabelById(Long id, String email) {
        User user = getUserByEmail(email);
        Label label = labelRepository.findByIdAndUserIdAndIsDeletedFalse(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found or access denied"));
        return labelMapper.toResponseDTO(label);
    }

    @Transactional
    public LabelResponseDTO updateLabel(Long id, LabelRequestDTO requestDTO, String email) {
        User user = getUserByEmail(email);

        Label label = labelRepository.findByIdAndUserIdAndIsDeletedFalse(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found or access denied"));

        if (!label.getName().equalsIgnoreCase(requestDTO.getName()) &&
                labelRepository.existsByNameIgnoreCaseAndUserIdAndIsDeletedFalse(requestDTO.getName(), user.getId())) {
            throw new BadRequestException("Label with this name already exists for your account!");
        }

        labelMapper.updateEntityFromDto(requestDTO, label);
        return labelMapper.toResponseDTO(label);
    }

    @Transactional
    public void deleteLabel(Long id, String email) {
        User user = getUserByEmail(email);

        Label label = labelRepository.findByIdAndUserIdAndIsDeletedFalse(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found or access denied"));

        label.setDeleted(true);
        labelRepository.save(label);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}