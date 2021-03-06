package ru.nikulin.test.todo.webapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.nikulin.test.todo.webapi.dao.ProjectRepository;
import ru.nikulin.test.todo.webapi.dto.ProjectDto;
import ru.nikulin.test.todo.webapi.dto.ProjectStatusDto;
import ru.nikulin.test.todo.webapi.dto.TaskDto;
import ru.nikulin.test.todo.webapi.exception.EntityDoesNotExistException;
import ru.nikulin.test.todo.webapi.model.Project;
import ru.nikulin.test.todo.webapi.service.ProjectService;
import ru.nikulin.test.todo.webapi.service.TaskService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskService taskService;
    private final ModelMapper mapper;

    @Override
    public List<ProjectDto> findAllProjects(Integer pageNo, Integer pageSize, String sortBy) {
        var pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        var result = projectRepository.findAll(pageable);

        if (result.isEmpty()) {
            return Collections.emptyList();
        }
        return result.get().map(s -> mapper.map(s, ProjectDto.class)).collect(Collectors.toList());
    }

    @Override
    public Map<ProjectStatusDto, ProjectDto> findAllProjectsGroupedByStatus() {
        return null;
    }

    @Override
    public Optional<ProjectDto> findProjectById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id cannot be null or <= 0");
        }
        if (!projectRepository.existsById(id)) {
            throw new EntityDoesNotExistException(String.format("Project with specified id %d does not exist!", id));
        }
        return projectRepository.findById(id).map(s -> mapper.map(s, ProjectDto.class));
    }

    @Override
    public ProjectDto addProject(ProjectDto projectDto) {
        projectDto.setId(null);

        if (projectDto.getTasks() == null || projectDto.getTasks().isEmpty()) {
            var newProject = projectRepository.save(mapper.map(projectDto, Project.class));
            return mapper.map(newProject, ProjectDto.class);
        }
        var tasks = projectDto.getTasks();
        projectDto.setTasks(null);
        var newProject = mapper.map(projectRepository.save(mapper.map(projectDto, Project.class)), ProjectDto.class);
        var newTasks = taskService.addTasksForProject(tasks.toArray(TaskDto[]::new), newProject.getId());
        newProject.setTasks(newTasks);
        return newProject;
    }

    @Override
    public List<ProjectDto> addProjects(ProjectDto[] projectDtos) {
        var newProject = projectRepository.saveAll(
                Arrays.stream(projectDtos)
                        .peek(projectDto -> projectDto.setTasks(projectDto.getTasks().stream().peek(taskDto -> taskDto.setId(null)).collect(Collectors.toList())))
                        .peek(s -> s.setId(null))
                        .map(s -> mapper.map(s, Project.class))
                        .collect(Collectors.toList()));

        return newProject.stream().map(s -> mapper.map(s, ProjectDto.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    @Override
    public ProjectDto updateProject(ProjectDto projectDto, Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityDoesNotExistException(String.format("Project with specified id %d does not exist!", projectId));
        }
        projectDto.setId(projectId);
        if (projectDto.getTasks() != null && !projectDto.getTasks().isEmpty()) {
            projectDto.setTasks(projectDto.getTasks().stream().peek(taskDto -> taskDto.setProjectId(projectId)).collect(Collectors.toList()));
        }
        var newProject = projectRepository.save(mapper.map(projectDto, Project.class));

        return mapper.map(newProject, ProjectDto.class);
    }
}
