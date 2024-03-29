package ua.com.dss.tennis.tournament.api.model.definitions.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import static ua.com.dss.tennis.tournament.api.security.JwtAuthenticationFilter.TOKEN_PREFIX;
import static ua.com.dss.tennis.tournament.api.security.JwtTokenProvider.EXPIRATION_TIME_SECONDS;

@Data
@NoArgsConstructor
public class AuthenticationResponse {
    String accessToken;
    long expireIn = EXPIRATION_TIME_SECONDS;
    String refreshToken;
    String tokenType = TOKEN_PREFIX;

    public AuthenticationResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
