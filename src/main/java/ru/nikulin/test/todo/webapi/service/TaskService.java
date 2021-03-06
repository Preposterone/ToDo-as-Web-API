package ru.nikulin.test.todo.webapi.service;

import ru.nikulin.test.todo.webapi.dto.TaskDto;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<TaskDto> getTasksByProjectId(Long projectId);

    TaskDto addTaskForProject(TaskDto taskDto, Long projectId);

    List<TaskDto> addTasksForProject(TaskDto[] taskDto, Long projectId);

    void deleteTaskById(Long taskId);

    TaskDto updateTask(TaskDto taskDto, Long taskId);

    Optional<TaskDto> findTaskById(Long id);
}
