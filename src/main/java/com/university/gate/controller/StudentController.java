package com.university.gate.controller;

import com.university.gate.model.Student;
import com.university.gate.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class StudentController {

    private final StudentRepository studentRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/students")
    public String listStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("baseUrl", baseUrl);
        return "students";
    }

    @PostMapping("/students")
    public String createStudent(@RequestParam("name") String name,
                                @RequestParam("registrationNumber") String registrationNumber,
                                @RequestParam("phoneNumber") String phoneNumber) {
        Student student = new Student(name, registrationNumber, phoneNumber);
        studentRepository.save(student);
        return "redirect:/students";
    }
}



