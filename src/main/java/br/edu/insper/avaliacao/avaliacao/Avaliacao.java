package br.edu.insper.avaliacao.avaliacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String emailAvaliador;

    private String emailAvaliado;

    @Column(length = 2000)
    private String comentarios;

    // Double aceita tanto 8 quanto 8.5 no JSON
    private Double nota;

    private LocalDateTime dataAvaliacao;
}
