package com.example.jpapractice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDto {
    private Long id;
    private String name;
    private Integer age;
    private Long classRoomId;
    private String classRoomName;
} 