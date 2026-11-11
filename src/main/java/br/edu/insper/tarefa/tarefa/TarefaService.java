package br.edu.insper.tarefa.tarefa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public List<Tarefa> listar() {
        return tarefaRepository.findAll();
    }

    public Tarefa criar(Tarefa tarefa, String emailCriador) {
        tarefa.setId(null);
        tarefa.setEmailCriador(emailCriador);

        if (tarefa.getDataCriacao() == null) {
            tarefa.setDataCriacao(LocalDateTime.now());
        }

        validar(tarefa);
        return tarefaRepository.save(tarefa);
    }

    // ---------- EXTRAS (nao estao no simulado, mas podem cair) ----------

    public Tarefa buscarPorId(Integer id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tarefa nao encontrada"));
    }

    public Tarefa atualizar(Integer id, Tarefa dados) {
        Tarefa tarefa = buscarPorId(id);
        tarefa.setDescricao(dados.getDescricao());
        tarefa.setPrioridade(dados.getPrioridade());

        validar(tarefa);
        return tarefaRepository.save(tarefa);
    }

    public void deletar(Integer id) {
        Tarefa tarefa = buscarPorId(id);
        tarefaRepository.delete(tarefa);
    }

    // ---------- Validacoes ----------

    private void validar(Tarefa tarefa) {
        if (tarefa.getEmailCriador() == null || tarefa.getEmailCriador().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email do criador e obrigatorio");
        }

        if (tarefa.getTitulo() == null || tarefa.getTitulo().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Titulo da tarefa e obrigatorio");
        }

        if (tarefa.getDescricao() == null || tarefa.getDescricao().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Descricao da tarefa e obrigatoria");
        }

        if (tarefa.getPrioridade() == null || tarefa.getPrioridade() < 0 || tarefa.getPrioridade() > 10) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "A prioridade deve estar entre 0 e 10");
        }
    }
}
