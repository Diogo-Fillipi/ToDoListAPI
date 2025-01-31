package diogo.project.todolistapi.controller;

import diogo.project.todolistapi.domain.user.AuthenticationDTO;
import diogo.project.todolistapi.domain.user.LoginResponseDTO;
import diogo.project.todolistapi.domain.user.User;
import diogo.project.todolistapi.domain.user.UserDTO;
import diogo.project.todolistapi.repository.UserRepository;
import diogo.project.todolistapi.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDTO user){
        var usernamePassword = new UsernamePasswordAuthenticationToken(user.email(), user.passwordd());
        var authentication = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid AuthenticationDTO user){
        if(this.userRepository.findByEmail(user.email()) != null){
            return ResponseEntity.badRequest().body("Email already exists");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        User newUser = new User(user.email(), encryptedPassword, user.role());
        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }

}