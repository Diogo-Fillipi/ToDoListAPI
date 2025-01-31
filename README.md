# To-DoListAPI

I previously built a CLI To-Do List application using JDBC and MySQL. Since it was a simple CRUD application without user authentication, I decided to remake it using Spring Boot and implement security.

---
We have the AuthenticationController where we configure our endpoints to authenticate our user and receive the JWT Token or create a new one


```java
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

```

---

We have the SecurityConfiguration class where we configure our SecurityFilterChain, which is used for specific configurations of our application  
```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
```
---
Finally, we have our TokenService class, which is used for JWT Token creation
```java
@Service
public class TokenService {

    @Value("$[api.security.token.secret]")
    private String secret;

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withExpiresAt(genExpirationTime())
                    .sign(algorithm);
            return token;
        }catch (JWTCreationException e){
            throw new RuntimeException("Error while generating token",e);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTCreationException e){
            throw new RuntimeException("Error while validating token",e);
        }
    }

    private Instant genExpirationTime(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-3"));
    }
    
}
```

This project idea comes from Roadmap.sh, here's the link for it: https://roadmap.sh/projects/todo-list-api
