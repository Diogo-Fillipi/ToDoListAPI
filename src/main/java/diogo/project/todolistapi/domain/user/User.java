package diogo.project.todolistapi.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Users")
public class User implements UserDetails {

    public User(){}

    public User(String email, String password, String role) {
        this.email = email;
        this.passwordd = password;
        this.rolee = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;


    @NotBlank
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "passwordd")
    private String passwordd;

    private String rolee;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
    @Override
    public String getPassword() {
        return passwordd;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordd(String passwordd) {
        this.passwordd = passwordd;
    }

    public String getRolee() {
        return rolee;
    }

    public void setRole(String role) {
        this.rolee = role;
    }
}
