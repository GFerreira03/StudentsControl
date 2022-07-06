package br.com.api.studentscontrol.controllers;

import br.com.api.studentscontrol.dtos.StudentDto;
import br.com.api.studentscontrol.models.StudentModel;
import br.com.api.studentscontrol.services.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping
    public ResponseEntity<Object> saveStudent(@RequestBody @Valid StudentDto studentDto){
        if(studentService.existsByCpf(studentDto.getCpf())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This CPF is already in use.");
        }

        StudentModel studentModel = new StudentModel();
        BeanUtils.copyProperties(studentDto, studentModel);
        studentModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.save(studentModel));
    }

    @GetMapping
    public ResponseEntity<Page<StudentModel>> getAllStudents(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(studentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneStudent(@PathVariable("id") UUID id){
        Optional<StudentModel> studentModel = studentService.findById(id);
        if (!studentModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(studentModel.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOneStudent(@PathVariable("id") UUID id){
        Optional<StudentModel> studentModel = studentService.findById(id);
        if (!studentModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
        }
        studentService.delete(studentModel.get());
        return ResponseEntity.status(HttpStatus.OK).body("Student deleted.");

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOneStudent(@PathVariable("id") UUID id,
                                                   @RequestBody @Valid StudentDto studentDto){
        Optional<StudentModel> studentModelOptional = studentService.findById(id);
        if (!studentModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
        }
        StudentModel studentModel = studentModelOptional.get();
        studentModel.setName(studentDto.getName());
        studentModel.setCpf(studentDto.getCpf());
        studentModel.setCourse(studentDto.getCourse());

        return ResponseEntity.status(HttpStatus.OK).body(studentService.save(studentModel));
    }



}
