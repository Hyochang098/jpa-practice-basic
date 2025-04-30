package com.example.jpapractice;

import com.example.jpapractice.entity.ClassRoom;
import com.example.jpapractice.entity.Student;
import com.example.jpapractice.repository.ClassRoomRepository;
import com.example.jpapractice.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class JpaNPlusOneTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setup() {
        // 테스트 데이터 생성
        ClassRoom classRoom1 = new ClassRoom();
        classRoom1.setName("1반");
        classRoom1.setCapacity(30);
        classRoom1.setTeacherName("김선생");
        classRoomRepository.save(classRoom1);

        ClassRoom classRoom2 = new ClassRoom();
        classRoom2.setName("2반");
        classRoom2.setCapacity(30);
        classRoom2.setTeacherName("이선생");
        classRoomRepository.save(classRoom2);

        for (int i = 1; i <= 5; i++) {
            Student student = new Student();
            student.setName("학생" + i);
            student.setAge(15 + i);
            student.setClassRoom(classRoom1);
            studentRepository.save(student);
        }

        for (int i = 6; i <= 10; i++) {
            Student student = new Student();
            student.setName("학생" + i);
            student.setAge(15 + i);
            student.setClassRoom(classRoom2);
            studentRepository.save(student);
        }

        em.flush();
        em.clear();
    }

    /**
     * N+1 문제가 발생하는 경우
     * 1. 모든 반을 조회 (1번의 쿼리)
     * 2. 각 반의 학생들을 조회 (N번의 쿼리)
     * 총 N+1번의 쿼리가 실행됨
     */
    @Test
    @Transactional
    void testNPlusOneProblem() {
        System.out.println("=== N+1 문제 발생 케이스 ===");
        List<ClassRoom> classRooms = classRoomRepository.findAll();
        
        for (ClassRoom classRoom : classRooms) {
            System.out.println("반 이름: " + classRoom.getName());
            // 여기서 지연 로딩이 발생하여 N번의 추가 쿼리가 실행됨
            List<Student> students = studentRepository.findByClassRoomId(classRoom.getId());
            for (Student student : students) {
                System.out.println("  - 학생: " + student.getName());
            }
        }
    }

    /**
     * N+1 문제를 해결하는 경우
     * 1. 페치 조인을 사용하여 한 번의 쿼리로 모든 데이터를 조회
     */
    @Test
    @Transactional
    void testNPlusOneSolution() {
        System.out.println("=== N+1 문제 해결 케이스 (페치 조인) ===");
        // 페치 조인을 사용한 쿼리
        List<Student> students = studentRepository.findAllWithClassRoom();
        
        for (Student student : students) {
            System.out.println("학생: " + student.getName() + 
                             " (반: " + student.getClassRoom().getName() + ")");
        }
    }

    /**
     * N+1 문제를 해결하는 다른 방법
     * 1. 배치 사이즈 설정을 사용하여 N번의 쿼리를 1번의 쿼리로 최적화
     */
    @Test
    @Transactional
    void testNPlusOneSolutionWithBatchSize() {
        System.out.println("=== N+1 문제 해결 케이스 (배치 사이즈) ===");
        List<ClassRoom> classRooms = classRoomRepository.findAll();
        
        for (ClassRoom classRoom : classRooms) {
            System.out.println("반 이름: " + classRoom.getName());
            // 배치 사이즈 설정으로 인해 한 번의 쿼리로 모든 학생을 조회
            List<Student> students = studentRepository.findByClassRoomId(classRoom.getId());
            for (Student student : students) {
                System.out.println("  - 학생: " + student.getName());
            }
        }
    }
} 