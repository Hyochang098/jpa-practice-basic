package com.example.jpapractice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "class_rooms")
public class ClassRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(name = "teacher_name")
    private String teacherName;

    @OneToMany(mappedBy = "classRoom")
    @JsonManagedReference
    private List<Student> students = new ArrayList<>();
} 