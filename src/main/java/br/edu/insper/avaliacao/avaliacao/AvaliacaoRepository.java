package br.edu.insper.avaliacao.avaliacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {

    // O Spring gera a consulta a partir do nome do metodo.
    // Outros exemplos uteis: findByEmailAvaliador, findByNotaGreaterThanEqual,
    // findByEmailAvaliadoStartingWith, findByEmailAvaliadoAndNota...
    List<Avaliacao> findByEmailAvaliado(String emailAvaliado);
}
