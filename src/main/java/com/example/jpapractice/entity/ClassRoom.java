package com.example.jpapractice.entity;

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
    // @JsonManagedReference // 순환 참조 해결 방법 1: 정방향 참조를 JSON 직렬화에 포함
    // @JsonIgnore // 순환 참조 해결 방법 2: 해당 필드를 JSON 직렬화에서 완전히 제외
    private List<Student> students = new ArrayList<>();
} 