package com.islamnizami.taskboarddevlab.scheduler;


import com.islamnizami.taskboarddevlab.model.entity.Task;
import com.islamnizami.taskboarddevlab.model.enums.TaskStatus;
import com.islamnizami.taskboarddevlab.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskReminderScheduler {

    private final TaskRepository taskRepository;

    @Scheduled(cron = "0 0 * * * *")

    //For test purposes
    //@Scheduled(fixedRate = 10000)
    @Transactional(readOnly = true)
    public void checkUpcomingTaskDeadlines(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24Hours = now.plusDays(24);

        List<Task> upcomingTasks = taskRepository.findByStatusNotAndDeadlineBetweenAndIsDeletedFalse(
                TaskStatus.DONE,
                now,
                next24Hours
        );

        if(upcomingTasks.isEmpty()){
            log.info("No upcoming tasks found due in the next 24 hours");
            return;
        }

        log.info("Found {} task(s) due within the next 24 hours:",upcomingTasks.size());
        for(Task task : upcomingTasks){
            log.warn("Task ID: {} | Title: '{}' | Due: {} | Assigned User: {}",
                    task.getId(),
                    task.getTitle(),
                    task.getDeadline(),
                    task.getUser().getEmail());
        }

    }
}
