package com.example.todo.application;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAnyRole;
import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAuthority;
import static org.springframework.security.authorization.AuthorizationManagers.anyOf;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000; // 15 min
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 jours


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .formLogin(f -> f.disable())
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(req -> req
                .requestMatchers("/h2-console/**", "/login", "/", "/oauth2/authorization/google").permitAll()
                .requestMatchers(antMatcher(HttpMethod.POST, "/users/**")).permitAll()
                .anyRequest().access(anyOf(hasAnyRole("USER", "ADMIN"), hasAuthority("OIDC_USER"))))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                    try {
                        jwt.decoder(jwtDecoder());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }))
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/users/home"))
                .sessionManagement(session -> session
                .maximumSessions(2))
                .logout(logout -> logout
                .deleteCookies("JSESSIONID"))
                .headers(headers -> headers.frameOptions().disable());
        ;
        return http.build();
    }
    @Bean
    static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
            .role("ADMIN").implies("USER")
            .build();
    }

    @Bean
    JwtDecoder jwtDecoder() throws Exception {
        return NimbusJwtDecoder.withPublicKey(publicKey()).build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private RSAPublicKey publicKey() throws Exception {
        return (RSAPublicKey) loadPublicKey("src/main/resources/keys/public_key.pem");
    }

    private static PrivateKey loadPrivateKey() throws Exception {

        
        // Lire le contenu du fichier public.pem
        // String key = System.getProperty("PRIVATE_KEY_PEM") != null ? System.getProperty("PRIVATE_KEY_PEM") : new String(Files.readAllBytes(Paths.get("src/main/resources/keys/private_key.pem")));
        String key = System.getProperty("PRIVATE_KEY_PEM");
        if (key == null) {
            Dotenv dotenv = Dotenv.configure().load();
            key = dotenv.get("PRIVATE_KEY_PEM");
        }

        // Supprimer les en-têtes et pieds de la clé
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        // Décoder la clé en Base64
        byte[] keyBytes = Base64.getDecoder().decode(key);

        // Construire une clé RSA publique
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey loadPublicKey(String filePath) throws Exception {
        // Lire le contenu du fichier public.pem
        String key = new String(Files.readAllBytes(Paths.get(filePath)));

        // Supprimer les en-têtes et pieds de la clé
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", ""); // Supprimer les espaces et sauts de ligne

        // Décoder la clé en Base64
        byte[] keyBytes = Base64.getDecoder().decode(key);

        // Construire une clé RSA publique
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    public static Map<String, String> generateTokens(String username, List<String> authorities) throws Exception {
        long now = System.currentTimeMillis();
        PrivateKey privateKey = loadPrivateKey();
        Map<String, List<String>> roles = new HashMap<>();
        roles.put("roles", authorities);
        String accessToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setClaims(roles)
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRATION))
                .signWith(privateKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRATION))
                .signWith(privateKey)
                .compact();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

}