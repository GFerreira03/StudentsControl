package br.com.api.studentscontrol.services;

import br.com.api.studentscontrol.models.StudentModel;
import br.com.api.studentscontrol.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Transactional
    public StudentModel save(StudentModel studentModel) {
        return studentRepository.save(studentModel);
    }

    public boolean existsByCpf(String cpf) {
        return studentRepository.existsByCpf(cpf);
    }

    public Page<StudentModel> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public Optional<StudentModel> findById(UUID id) {
        return studentRepository.findById(id);
    }

    @Transactional
    public void delete(StudentModel studentModel) {
        studentRepository.delete(studentModel);
    }
}
