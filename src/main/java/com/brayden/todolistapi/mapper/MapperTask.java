package com.brayden.todolistapi.mapper;

import com.brayden.todolistapi.dto.TaskRequestDTO;
import com.brayden.todolistapi.dto.TaskResponseDTO;
import com.brayden.todolistapi.model.Task;
import org.modelmapper.ModelMapper;


public class MapperTask {
    private static final ModelMapper mapper = new ModelMapper();
    public static Task dtoRequestToModel(TaskRequestDTO dto){
        return mapper.map(dto, Task.class);
    }
    public static TaskResponseDTO modelToResponseDto(Task task) {
        return mapper.map(task, TaskResponseDTO.class);
    }
}
