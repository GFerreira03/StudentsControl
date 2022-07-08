package br.com.api.studentscontrol.controllers;

import br.com.api.studentscontrol.dtos.StudentDto;
import br.com.api.studentscontrol.models.StudentModel;
import br.com.api.studentscontrol.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping
    @Operation(summary = "Saves one student")
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
    @Operation(summary = "Returns all students")
    public ResponseEntity<Page<StudentModel>> getAllStudents(@PageableDefault(page = 0, size = 10) Pageable pageable){
        Page<StudentModel> studentModelPage = studentService.findAll(pageable);
        for (StudentModel studentModel : studentModelPage){
            UUID id = studentModel.getId();
            studentModel.add(linkTo(methodOn(StudentController.class).getOneStudent(id)).withSelfRel());
        }
        return ResponseEntity.status(HttpStatus.OK).body(studentModelPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns one specific student")
    public ResponseEntity<Object> getOneStudent(@PathVariable("id") UUID id){
        Optional<StudentModel> studentModel = studentService.findById(id);
        if (!studentModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
        }
        studentModel.get().add(Link.of("http://localhost:8080/students").withRel("Students list"));
        return ResponseEntity.status(HttpStatus.OK).body(studentModel.get());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes one student")
    public ResponseEntity<Object> deleteOneStudent(@PathVariable("id") UUID id){
        Optional<StudentModel> studentModel = studentService.findById(id);
        if (!studentModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
        }
        studentService.delete(studentModel.get());
        return ResponseEntity.status(HttpStatus.OK).body("Student deleted.");

    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates one student")
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
        studentModel.setBirthDate(studentDto.getBirthDate());

        return ResponseEntity.status(HttpStatus.OK).body(studentService.save(studentModel));
    }



}
