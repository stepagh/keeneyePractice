package dev.keeneye.services;

import dev.keeneye.dto.TokenRefreshRequest;
import dev.keeneye.dto.TokenRefreshResponse;
import dev.keeneye.entities.RefreshToken;
import dev.keeneye.entities.User;
import dev.keeneye.exceptions.RefreshTokenExpiredException;
import dev.keeneye.exceptions.ResourceNotFoundException;
import dev.keeneye.repositories.RefreshTokenRepository;
import dev.keeneye.repositories.UserRepository;
import dev.keeneye.security.jwt.JwtCore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtCore jwtCore;


    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
    @Transactional
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.refreshToken();
        RefreshToken refreshToken = this.findByToken(requestRefreshToken);
        this.verifyExpiration(refreshToken);
        User user = refreshToken.getUser();
        String newJwtToken = jwtCore.generateToken(user.getUsername());
        RefreshToken newRefreshToken = this.createRefreshToken(user.getId());
        return new TokenRefreshResponse(newJwtToken, newRefreshToken.getToken());
    }
}