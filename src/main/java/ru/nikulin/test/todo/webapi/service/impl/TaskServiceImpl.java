package ru.nikulin.test.todo.webapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nikulin.test.todo.webapi.dao.ProjectRepository;
import ru.nikulin.test.todo.webapi.dao.TaskRepository;
import ru.nikulin.test.todo.webapi.dto.TaskDto;
import ru.nikulin.test.todo.webapi.model.Task;
import ru.nikulin.test.todo.webapi.service.TaskService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Override
    public List<Task> getTasksByProjectId(Long projectId) {
        return null;
    }

    @Override
    public boolean saveTaskForProject(TaskDto taskDto, Long projectId) {
        return false;
    }

    @Override
    public boolean deleteTaskById(Long taskId) {
        return false;
    }

    @Override
    public boolean updateTask(TaskDto taskDto) {
        return false;
    }
}