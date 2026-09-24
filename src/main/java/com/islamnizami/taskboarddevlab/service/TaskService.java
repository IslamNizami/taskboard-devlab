package com.islamnizami.taskboarddevlab.service;

import com.islamnizami.taskboarddevlab.exception.ResourceNotFoundException;
import com.islamnizami.taskboarddevlab.filter.TaskSpecification;
import com.islamnizami.taskboarddevlab.model.mapper.TaskMapper;
import com.islamnizami.taskboarddevlab.model.dto.TaskRequestDTO;
import com.islamnizami.taskboarddevlab.model.dto.TaskResponseDTO;
import com.islamnizami.taskboarddevlab.model.entity.Label;
import com.islamnizami.taskboarddevlab.model.entity.Task;
import com.islamnizami.taskboarddevlab.model.entity.User;
import com.islamnizami.taskboarddevlab.model.enums.TaskStatus;
import com.islamnizami.taskboarddevlab.repository.LabelRepository;
import com.islamnizami.taskboarddevlab.repository.TaskRepository;
import com.islamnizami.taskboarddevlab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public TaskResponseDTO createTasK(TaskRequestDTO requestDTO, String email) {
        User user = getUserByEmail(email);

        Task task = taskMapper.toEntity(requestDTO);
        task.setUser(user);

        if (requestDTO.getStatus() != null) {
            task.setStatus(requestDTO.getStatus());
        }

        if (requestDTO.getLabelIds() != null && !requestDTO.getLabelIds().isEmpty()) {
            task.setLabels(resolveLabels(requestDTO.getLabelIds(), user.getId()));
        }

        Task saved = taskRepository.save(task);
        return taskMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(Long id, String email) {
        User user = getUserByEmail(email);

        Task task = taskRepository.findByIdAndUserIdAndIsDeletedFalse(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or access denied."));

        return taskMapper.toResponseDTO(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> searchTasks(
            TaskStatus status,
            String keyword,
            Long labelId,
            LocalDateTime deadline,
            Pageable pageable,
            String email
    ) {
        User user = getUserByEmail(email);

        Specification<Task> spec = TaskSpecification.filterTasks(user.getId(), status, keyword, labelId, deadline);

        return taskRepository.findAll(spec, pageable).map(taskMapper::toResponseDTO);
    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO requestDTO, String email) {
        User user = getUserByEmail(email);

        Task task = taskRepository.findByIdAndUserIdAndIsDeletedFalse(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or access denied."));

        task.setTitle(requestDTO.getTitle());
        task.setDescription(requestDTO.getDescription());
        task.setDeadline(requestDTO.getDeadline());

        if (requestDTO.getStatus() != null) {
            task.setStatus(requestDTO.getStatus());
        }

        if (requestDTO.getLabelIds() != null) {
            task.setLabels(resolveLabels(requestDTO.getLabelIds(), user.getId()));
        }

        Task updateTask = taskRepository.saveAndFlush(task);
        return taskMapper.toResponseDTO(updateTask);
    }

    @Transactional
    public void deleteTask(Long id, String email) {
        User user = getUserByEmail(email);
        Task task = taskRepository.findByIdAndUserIdAndIsDeletedFalse(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or access denied"));

        task.setDeleted(true);
        taskRepository.save(task);
    }

    private Set<Label> resolveLabels(Set<Long> labelIds, Long userId) {
        Set<Label> userLabels = new HashSet<>();
        for (Long labelId : labelIds) {
            Label label = labelRepository.findByIdAndUserIdAndIsDeletedFalse(labelId, userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Label with id " + labelId + " not found or access denied"));
            userLabels.add(label);
        }
        return userLabels;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }


    @Transactional(readOnly = true)
    public byte[] exportCompletedTasksToCsv(String email) {
        User user = getUserByEmail(email);
        List<Task> completedTasks = taskRepository.findByUserIdAndStatusAndIsDeletedFalse(user.getId(), TaskStatus.DONE);

        StringBuilder csvBuilder = new StringBuilder();

        csvBuilder.append("ID,Title,Description,Status,Deadline,Labels,CompletedAt\n");

        for (Task task : completedTasks) {
            String labelNames = task.getLabels().stream()
                    .map(Label::getName)
                    .reduce((a, b) -> a + ";" + b)
                    .orElse("");

            String title = escapeCsvField(task.getTitle());
            String description = escapeCsvField(task.getDescription());
            String deadline = task.getDeadline() != null ? task.getDeadline().toString() : "";
            String updatedAt = task.getUpdatedAt() != null ? task.getUpdatedAt().toString() : "";


            csvBuilder.append(task.getId()).append(",")
                    .append(title).append(",")
                    .append(description).append(",")
                    .append(task.getStatus()).append(",")
                    .append(deadline).append(",")
                    .append("\"").append(labelNames).append("\"").append(",")
                    .append(updatedAt).append("\n");

        }
        return csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeCsvField(String field) {
        if (field == null) {
            return "\"\"";
        }
        return "\"" + field.replace("\"", "\"\"") + "\"";
    }
}