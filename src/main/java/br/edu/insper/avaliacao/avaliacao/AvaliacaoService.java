package br.edu.insper.avaliacao.avaliacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public List<Avaliacao> listar(String emailAvaliado) {
        if (emailAvaliado != null && !emailAvaliado.isBlank()) {
            return avaliacaoRepository.findByEmailAvaliado(emailAvaliado);
        }
        return avaliacaoRepository.findAll();
    }

    public Avaliacao criar(Avaliacao avaliacao, String emailAvaliador) {
        avaliacao.setId(null);
        avaliacao.setEmailAvaliador(emailAvaliador);

        if (avaliacao.getDataAvaliacao() == null) {
            avaliacao.setDataAvaliacao(LocalDateTime.now());
        }

        validar(avaliacao);
        return avaliacaoRepository.save(avaliacao);
    }

    // ---------- EXTRAS (nao estao no simulado, mas podem cair) ----------

    public Avaliacao buscarPorId(Integer id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Avaliacao nao encontrada"));
    }

    public Avaliacao atualizar(Integer id, Avaliacao dados) {
        Avaliacao avaliacao = buscarPorId(id);
        avaliacao.setEmailAvaliado(dados.getEmailAvaliado());
        avaliacao.setComentarios(dados.getComentarios());
        avaliacao.setNota(dados.getNota());

        validar(avaliacao);
        return avaliacaoRepository.save(avaliacao);
    }

    public void deletar(Integer id) {
        Avaliacao avaliacao = buscarPorId(id);
        avaliacaoRepository.delete(avaliacao);
    }

    // ---------- Validacoes ----------

    private void validar(Avaliacao avaliacao) {
        if (avaliacao.getEmailAvaliador() == null || avaliacao.getEmailAvaliador().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email do avaliador e obrigatorio");
        }

        if (avaliacao.getEmailAvaliado() == null || avaliacao.getEmailAvaliado().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email do avaliado e obrigatorio");
        }

        if (avaliacao.getNota() == null || avaliacao.getNota() < 0 || avaliacao.getNota() > 10) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "A nota deve estar entre 0 e 10");
        }
    }
}
