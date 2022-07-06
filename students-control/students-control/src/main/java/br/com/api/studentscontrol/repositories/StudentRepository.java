package br.com.api.studentscontrol.repositories;

import br.com.api.studentscontrol.models.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<StudentModel, UUID> {
}
