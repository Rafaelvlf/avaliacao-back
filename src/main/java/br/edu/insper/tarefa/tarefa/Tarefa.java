package br.edu.insper.tarefa.tarefa;

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
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;

    private String emailCriador;

    @Column(length = 2000)
    private String descricao;

    // Double aceita tanto 8 quanto 8.5 no JSON
    private Double prioridade;

    private LocalDateTime dataCriacao;
}
