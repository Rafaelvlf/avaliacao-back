package br.edu.insper.avaliacao.avaliacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    // Mesmos nomes de claim que a Action do Auth0 coloca no token (ver guia)
    private static final String CLAIM_ROLES = "https://social-insper.com/roles";
    private static final String CLAIM_EMAIL = "https://social-insper.com/email";

    @Autowired
    private AvaliacaoService avaliacaoService;

    // GET /avaliacoes  -> ADMIN e USER (qualquer usuario logado)
    // GET /avaliacoes?emailAvaliado=fulano@x.com -> filtra pelo avaliado
    @GetMapping
    public List<Avaliacao> listar(@AuthenticationPrincipal Jwt jwt,
                                  @RequestParam(required = false) String emailAvaliado) {
        return avaliacaoService.listar(emailAvaliado);
    }

    // POST /avaliacoes -> somente ADMIN
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Avaliacao criar(@AuthenticationPrincipal Jwt jwt,
                           @RequestBody Avaliacao avaliacao) {
        verificarAdmin(jwt);

        // O avaliador e quem esta logado (email vem do token).
        // Se o token nao tiver o email, usa o que veio no corpo da requisicao.
        String emailAvaliador = jwt.getClaimAsString(CLAIM_EMAIL);
        if (emailAvaliador == null) {
            emailAvaliador = avaliacao.getEmailAvaliador();
        }

        return avaliacaoService.criar(avaliacao, emailAvaliador);
    }

    // ---------- EXTRAS (nao estao no simulado, mas podem cair) ----------

    @GetMapping("/{id}")
    public Avaliacao buscarPorId(@AuthenticationPrincipal Jwt jwt,
                                 @PathVariable Integer id) {
        return avaliacaoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Avaliacao atualizar(@AuthenticationPrincipal Jwt jwt,
                               @PathVariable Integer id,
                               @RequestBody Avaliacao avaliacao) {
        verificarAdmin(jwt);
        return avaliacaoService.atualizar(id, avaliacao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@AuthenticationPrincipal Jwt jwt,
                        @PathVariable Integer id) {
        verificarAdmin(jwt);
        avaliacaoService.deletar(id);
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
