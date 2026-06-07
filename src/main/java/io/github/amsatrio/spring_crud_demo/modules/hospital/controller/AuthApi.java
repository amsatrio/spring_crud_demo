package io.github.amsatrio.spring_crud_demo.modules.hospital.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MRole;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MUser;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TToken;
import io.github.amsatrio.spring_crud_demo.dto.request.LoginRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.RefreshTokenRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.RegisterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.VerificationRequest;
import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MRoleRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MUserRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.TTokenRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.UserDetailsImpl;
import io.github.amsatrio.spring_crud_demo.util.JwtUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthApi {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/public")
    public ResponseEntity<Response<String>> publicAccess() {
        Response<String> response = new Response<>();
        response.setStatus(HttpStatus.OK.value());
        response.setPath(httpServletRequest.getRequestURI());
        response.setDate(new Date());
        response.setMessage("Public Content.");

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/faskes")
    @PreAuthorize("hasRole('FASKES') or hasRole('ADMIN')")
    public ResponseEntity<Response<String>> faskesAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            log.info("username: " + userDetails.getUsername());
            log.info("id: " + userDetails.getId());
        }

        Response<String> response = new Response<>();
        response.setStatus(HttpStatus.OK.value());
        response.setPath(httpServletRequest.getRequestURI());
        response.setDate(new Date());
        response.setMessage("faskes, ");

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/dokter")
    @PreAuthorize("hasRole('DOKTER') or hasRole('ADMIN')")
    public ResponseEntity<Response<String>> dokterAccess() {
        Response<String> response = new Response<>();
        response.setStatus(HttpStatus.OK.value());
        response.setPath(httpServletRequest.getRequestURI());
        response.setDate(new Date());
        response.setMessage("Dokter Content.");

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/pasien")
    @PreAuthorize("hasRole('PASIEN') or hasRole('ADMIN')")
    public ResponseEntity<Response<String>> pasienAccess() {
        Response<String> response = new Response<>();
        response.setStatus(HttpStatus.OK.value());
        response.setPath(httpServletRequest.getRequestURI());
        response.setDate(new Date());
        response.setMessage("Pasien Content.");

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<String>> adminAccess() {
        Response<String> response = new Response<>();
        response.setStatus(HttpStatus.OK.value());
        response.setPath(httpServletRequest.getRequestURI());
        response.setDate(new Date());
        response.setMessage("Admin Content.");

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // ====
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtil jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        String mainToken = jwtUtils.generateToken(userPrincipal, true);
        String refreshToken = jwtUtils.generateToken(userPrincipal, false);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> map = new HashMap<>();
        map.put("mainToken", mainToken);
        map.put("refreshToken", refreshToken);
        map.put("expiredIn", jwtUtils.getExpiration());

        Response<Object> response = new Response<>();
        response.setStatus(HttpStatus.OK.value());
        response.setPath(httpServletRequest.getRequestURI());
        response.setMessage("Success");
        response.setData(new Date());
        response.setData(map);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<Object> refreshToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest) {

        String mainToken = refreshTokenRequest.getMainToken();
        String refreshToken = refreshTokenRequest.getRefreshToken();

        Map<String, Object> map = new HashMap<>();
        map.put("mainToken", mainToken);
        map.put("refreshToken", refreshToken);

        Response<Object> response = new Response<>();
        response.setStatus(HttpStatus.OK.value());
        response.setPath(httpServletRequest.getRequestURI());
        response.setDate(new Date());
        response.setMessage("success");
        response.setData(map);

        Boolean tokenValidated = jwtUtils.validateToken(mainToken);
        if (!tokenValidated) {
            log.error("main token is invalid");
            throw new RuntimeException("token is invalid");
        }

        if (!jwtUtils.isExpired()) {
            log.info("main token is not expired");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        tokenValidated = jwtUtils.validateToken(refreshToken);
        if (!tokenValidated) {
            log.error("refresh token is invalid");
            throw new RuntimeException("token is invalid");
        }

        if (jwtUtils.isExpired()) {
            log.info("refresh token is expired");
            throw new RuntimeException("refresh token is expired");
        }

        mainToken = jwtUtils.reGenerateToken();

        map.put("mainToken", mainToken);

        response.setData(map);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Autowired
    private MUserRepository mUserRepository;
    @Autowired
    private MRoleRepository mRoleRepository;
    @Autowired
    private TTokenRepository tTokenRepository;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest registerRequest) {
        Date date = new Date();

        Response<Object> response = new Response<>();
        response.setStatus(HttpStatus.OK.value());
        response.setPath(httpServletRequest.getRequestURI());
        response.setDate(date);
        response.setMessage("success");
        response.setData("");

        if (mUserRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("user is exist");
        }

        // get role
        Optional<MRole> optionalMRole = mRoleRepository.findByCodeAndIsDeleteFalse(registerRequest.getRole());
        if (!optionalMRole.isPresent()) {
            throw new RuntimeException("role not found");
        }

        // initialize new user
        MUser mUser = new MUser();
        mUser.setId(date.getTime());
        mUser.setEmail(registerRequest.getEmail());
        mUser.setPassword(registerRequest.getPassword());
        mUser.setIsLocked(true);
        mUser.setIsDelete(false);
        mUser.setLoginAttempt(0);
        mUser.setCreatedBy(0L);
        mUser.setCreatedOn(date);
        mUser.setRoleId(optionalMRole.get().getId());

        // init validation
        TToken tToken = new TToken();
        tToken.setUsedFor("registration");
        tToken.setEmail(mUser.getEmail());
        tToken.setCreatedBy(mUser.getCreatedBy());
        tToken.setCreatedOn(mUser.getCreatedOn());
        tToken.setId(mUser.getId());
        tToken.setUserId(mUser.getId());
        tToken.setIsDelete(false);
        tToken.setIsExpired(false);

        // set verification code
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        uuidAsString = uuidAsString.replaceAll("-", "");
        tToken.setToken(uuidAsString);

        // set expired date
        long time = new Date().getTime();
        Date dateTomorrow = new Date(time + (24 * 60 * 60 * 1000));
        tToken.setExpiredOn(dateTomorrow);

        // send verification to email
        sendVerificationEmail(tToken);

        // save
        mUserRepository.save(mUser);
        tTokenRepository.save(tToken);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Autowired
    private JavaMailSender mailSender;

    private void sendVerificationEmail(TToken tToken) {
        String verifyUrl = "http://localhost:8085/api/auth" + "/email_verification?code=" + tToken.getToken();

        try {
            String toAddress = tToken.getEmail();
            String fromAddress = "noreply@example.com";
            String senderName = "Your company name";
            String subject = "Please verify your registration";
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Expired on: [[EXPIRED_URL]]<br>"
                    + "Thank you,<br>"
                    + "Your company name.";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            content = content.replace("[[name]]", tToken.getEmail());
            content = content.replace("[[URL]]", verifyUrl);
            content = content.replace("[[EXPIRED_URL]]", simpleDateFormat.format(tToken.getExpiredOn()));

            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException exception) {
            log.error("messaging exception", exception);
            throw new RuntimeException(exception);
        } catch (UnsupportedEncodingException exception) {
            log.error("unsupported encoding exception", exception);
            throw new RuntimeException(exception);
        } catch (Exception exception) {
            log.error("exception", exception);
            throw new RuntimeException(exception);
        }
    }

    @GetMapping("/email_verification")
    public ResponseEntity<Object> verifyUser(@RequestParam String code, HttpServletRequest request) {
        Date date = new Date();
        Response<Object> response = new Response<>();
        response.setStatus(HttpStatus.OK.value());
        response.setPath(httpServletRequest.getRequestURI());
        response.setDate(date);
        response.setMessage("success");
        // response.setData("");

        Optional<TToken> optionalTToken = this.tTokenRepository.findByTokenAndIsDeleteFalse(code);
        if (!optionalTToken.isPresent()) {
            throw new RuntimeException("verification token is not found");
        }

        TToken tToken = optionalTToken.get();

        if (tToken.getIsExpired()) {
            throw new RuntimeException("verification token is expired");
        }

        // get user
        MUser mUser = tToken.getMUser();
        if (mUser == null) {
            throw new RuntimeException("user is not found");
        }

        mUser.setIsLocked(false);
        mUser.setModifiedOn(date);
        mUser.setModifiedBy(0L);
        mUserRepository.save(mUser);

        tToken.setIsExpired(true);
        tToken.setModifiedOn(date);
        tToken.setModifiedBy(0L);
        tTokenRepository.save(tToken);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/resend_verification")
    public ResponseEntity<Object> resendVerification(@Valid @RequestBody VerificationRequest verificationRequest,
            HttpServletRequest request) {
        Date date = new Date();
        Response<Object> response = new Response<>();
        response.setStatus(HttpStatus.OK.value());
        response.setPath(httpServletRequest.getRequestURI());
        response.setDate(date);
        response.setMessage("success");

        Optional<TToken> optionalTToken = tTokenRepository.findByTokenAndIsDeleteFalse(verificationRequest.getCode());
        if (!optionalTToken.isPresent()) {
            throw new RuntimeException("validation code is not found");
        }

        TToken tToken = optionalTToken.get();

        if (tToken.getIsExpired()) {
            // set verification code
            UUID uuid = UUID.randomUUID();
            String uuidAsString = uuid.toString();
            uuidAsString = uuidAsString.replaceAll("-", "");
            tToken.setToken(uuidAsString);

            // set expired date
            Date dateTomorrow = new Date(date.getTime() + (24 * 60 * 60 * 1000));
            tToken.setExpiredOn(dateTomorrow);

            tToken.setModifiedBy(0L);
            tToken.setModifiedOn(date);
            tToken.setIsExpired(false);

            tTokenRepository.save(tToken);
        }

        sendVerificationEmail(tToken);

        return ResponseEntity.status(response.getStatus()).body(response);

    }
}
