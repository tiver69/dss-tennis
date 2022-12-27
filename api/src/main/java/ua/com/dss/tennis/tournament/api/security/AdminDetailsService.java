package ua.com.dss.tennis.tournament.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.exception.ErrorConstants;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@PropertySource("classpath:error.properties")
public class AdminDetailsService implements UserDetailsService {

    private static final String ADMIN_NUMBER_PROPERTY = "admin.number";
    private static final String ADMIN_USERNAME_TEMPLATE_PROPERTY = "admin[%o].username";
    private static final String ADMIN_PASSWORD_TEMPLATE_PROPERTY = "admin[%o].password";
    private static final long ALLOWED_TIME_RESET_COUNTER = 900; //15 min

    private Map<String, Admin> adminsMap = new HashMap<>();

    @Autowired
    protected Environment environment;

    @PostConstruct
    public void loadAdminProperties() {
        int size = Integer.parseInt(environment.getProperty(ADMIN_NUMBER_PROPERTY));

        for (byte i = 0; i < size; i++) {
            String username = environment.getProperty(String.format(ADMIN_USERNAME_TEMPLATE_PROPERTY, i));
            String password = environment.getProperty(String.format(ADMIN_PASSWORD_TEMPLATE_PROPERTY, i));
            adminsMap.put(username, new Admin(username, password));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminsMap.get(username);
        if (admin == null) throw new DetailedException(ErrorConstants.ErrorKey.AUTHENTICATION_FAILED);
        return admin;
    }

    public void validateUnsuccessfulCounter(String username) {
        Admin admin = adminsMap.get(username);
        if (admin != null && admin.getAttempts() == 5) {
            if (Duration.between(admin.getLastUnsuccessfulAttempt(), LocalDateTime.now())
                    .getSeconds() >= ALLOWED_TIME_RESET_COUNTER) {
                resetUnsuccessfulCounter(username);
            } else {
                throw new DetailedException(ErrorConstants.ErrorKey.ACCOUNT_LOCKED);
            }
        }
    }

    public void updateRefreshToken(String username, LocalDateTime expirationTime) {
        Admin admin = adminsMap.get(username);
        admin.setRefreshToken(UUID.randomUUID().toString());
        admin.setRefreshTokenExpirationTime(expirationTime);
    }

    public void increaseUnsuccessfulCounter(String username) {
        Admin admin = adminsMap.get(username);
        admin.setAttempts((byte) (admin.getAttempts() + 1));
        admin.setLastUnsuccessfulAttempt(LocalDateTime.now());
    }

    public void resetUnsuccessfulCounter(String username) {
        Admin admin = adminsMap.get(username);
        admin.setAttempts((byte) 0);
        admin.setLastUnsuccessfulAttempt(null);
    }
}
