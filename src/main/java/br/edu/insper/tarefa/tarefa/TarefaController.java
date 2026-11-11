package br.edu.insper.tarefa.tarefa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    // Mesmos nomes de claim que a Action do Auth0 coloca no token (ver guia)
    private static final String CLAIM_ROLES = "https://social-insper.com/roles";
    private static final String CLAIM_EMAIL = "https://social-insper.com/email";

    @Autowired
    private TarefaService tarefaService;

    // GET /tarefas  -> ADMIN e USER (qualquer usuario logado)
    // GET /tarefas?emailAvaliado=fulano@x.com -> filtra pelo avaliado
    @GetMapping
    public List<Tarefa> listar(@AuthenticationPrincipal Jwt jwt
                               ) {
        return tarefaService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tarefa criar(@AuthenticationPrincipal Jwt jwt,
                        @RequestBody Tarefa tarefa) {

        // O avaliador e quem esta logado (email vem do token).
        // Se o token nao tiver o email, usa o que veio no corpo da requisicao.
        String emailCriador = jwt.getClaimAsString(CLAIM_EMAIL);
        if (emailCriador == null) {
            emailCriador = tarefa.getEmailCriador();
        }

        return tarefaService.criar(tarefa, emailCriador);
    }

    // ---------- EXTRAS (nao estao no simulado, mas podem cair) ----------

    @GetMapping("/{id}")
    public Tarefa buscarPorId(@AuthenticationPrincipal Jwt jwt,
                              @PathVariable Integer id) {
        return tarefaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Tarefa atualizar(@AuthenticationPrincipal Jwt jwt,
                            @PathVariable Integer id,
                            @RequestBody Tarefa tarefa) {
        verificarAdmin(jwt);
        return tarefaService.atualizar(id, tarefa);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        tarefaService.deletar(id);
    }

    // ---------- Autorizacao (RBAC) ----------
    // Mesmo jeito do repo connections do professor: le as roles do token
    // e devolve 403 se o usuario nao for ADMIN.
    private void verificarAdmin(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList(CLAIM_ROLES);

        if (roles == null || !roles.contains("ADMIN")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Apenas ADMIN pode executar esta acao"
            );
        }
    }
}
