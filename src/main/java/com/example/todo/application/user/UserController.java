package com.example.todo.application.user;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.todo.dto.ResponseValidationCaptcha;
import com.example.todo.dto.SignInReq;
import com.example.todo.dto.SignUpReq;
import com.example.todo.dto.SignedUser;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserServiceImpl service;

    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @PostMapping 
    public ResponseEntity<SignedUser> signin(@Valid @RequestBody SignInReq user) throws Exception {
        return status(HttpStatus.OK).body(service.login(user));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpReq user, HttpServletRequest request) {
        String ip = getClientIp(request);
        String secret = Dotenv.load().get("H_CAPTCHA_SECRET");
        if (!validateCaptcha(secret, ip, user.captcha())) {
            throw new RuntimeException("Captcha Non Valide");
        }
        UserDetails userDetails = 
        new org.springframework.security.core.userdetails.User(user.email(), user.password(), user.getAuthorities());
        
        service.createUser(userDetails);
        return status(HttpStatus.CREATED).build();
    }
    
    @GetMapping("/home")
    public ResponseEntity<SignedUser> home(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email");
        return status(HttpStatus.OK).body(service.createUserFromGoogleAccount(email));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handler(Exception e) {
        return status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    private boolean validateCaptcha(String secret, String client_ip, String response) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // CaptchaToValidate captchatToValidate = new CaptchaToValidate(secret, client_ip, response);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("secret", secret);
        formData.add("remoteip", client_ip);
        formData.add("response", response);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
        ResponseEntity<ResponseValidationCaptcha> responseValidationCaptcha = template.exchange("https://api.hcaptcha.com/siteverify", HttpMethod.POST, requestEntity, ResponseValidationCaptcha.class);
        return responseValidationCaptcha.getBody().isSuccess();
    }

    private String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
    } else {
        // "X-Forwarded-For" peut contenir plusieurs IPs (ex: "IP_CLIENT, proxy1, proxy2")
        ip = ip.split(",")[0].trim();
    }
    return ip;
}
    
}