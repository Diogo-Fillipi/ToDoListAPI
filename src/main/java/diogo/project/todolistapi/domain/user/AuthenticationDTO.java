package diogo.project.todolistapi.domain.user;

public record AuthenticationDTO(
    String email,
    String password,
    String role
) {
}
