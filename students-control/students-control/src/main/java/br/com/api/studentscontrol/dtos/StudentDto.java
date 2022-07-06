package br.com.api.studentscontrol.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class StudentDto {

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 11, max = 11)
    private String cpf;

    @NotBlank
    private String course;

}
