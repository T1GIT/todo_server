package com.todo.app.data.service.impl;

import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.Task;
import com.todo.app.data.repo.CategoryRepository;
import com.todo.app.data.repo.TaskRepository;
import com.todo.app.data.service.TaskService;
import com.todo.app.data.util.base.AbstractModel;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<Task> getOf(long userId, long categoryId) {
        return categoryRepository.findById(categoryId).map(category ->
                category.getTasks().stream()
                        .map(c -> Hibernate.unproxy(c, Task.class))
                        .sorted(Comparator.comparingLong(AbstractModel::getId))
                        .collect(Collectors.toList())
        ).orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
    }

    @Override
    public Task add(long userId, long categoryId, Task task) {
        return taskRepository.saveAndFlush(
            categoryRepository.findByUserIdAndId(userId, categoryId).map(category ->
                    task.edit(t -> t.setCategory(category))
            ).orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId)));
    }

    @Override
    public void update(long userId, long taskId, Task newTask) {
        taskRepository.saveAndFlush(
                taskRepository.findById(taskId).map(task -> {
                    if (!categoryRepository.existsByUserIdAndId(userId, task.getCategory().getId()))
                        throw new ResourceNotFoundException(Category.class, "taskId", taskId);
                    return task.edit(t -> {
                        t.setTitle(newTask.getTitle());
                        t.setDescription(newTask.getDescription());
                    });
                }).orElseThrow(() -> new ResourceNotFoundException(Task.class, taskId))
        );
    }

    @Override
    public void setCompleted(long userId, long taskId, boolean isCompleted) throws ResourceNotFoundException {
        taskRepository.saveAndFlush(
                taskRepository.findById(taskId).map(task -> {
                    if (!categoryRepository.existsByUserIdAndId(userId, task.getCategory().getId()))
                        throw new ResourceNotFoundException(Category.class, "taskId", taskId);
                    return task.edit(t -> {
                        if (isCompleted) {
                            t.setCompleted(true);
                            t.setExecuteDate(new Date());
                        } else {
                            t.setCompleted(false);
                            t.setExecuteDate(null);
                        }
                    });
                }).orElseThrow(() -> new ResourceNotFoundException(Task.class, taskId))
        );
    }

    @Override
    public void delete(long userId, long taskId) {
        taskRepository.findById(taskId).ifPresent(task -> {
            if (!categoryRepository.existsByUserIdAndId(userId, task.getCategory().getId()))
                throw new ResourceNotFoundException(Category.class, "taskId", taskId);
            taskRepository.delete(task);
        });
    }
}
