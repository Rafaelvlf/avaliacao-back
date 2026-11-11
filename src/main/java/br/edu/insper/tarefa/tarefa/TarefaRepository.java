package br.edu.insper.tarefa.tarefa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {

    // O Spring gera a consulta a partir do nome do metodo.
    // Outros exemplos uteis: findByEmailAvaliador, findByNotaGreaterThanEqual,
    // findByEmailAvaliadoStartingWith, findByEmailAvaliadoAndNota...
    List<Tarefa> findByEmailCriador(String emailCriador);
}
