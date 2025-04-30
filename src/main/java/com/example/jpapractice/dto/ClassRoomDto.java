package com.example.jpapractice.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ClassRoomDto {
    private Long id;
    private String name;
    private Integer capacity;
    private String teacherName;
    private List<StudentDto> students;
} 