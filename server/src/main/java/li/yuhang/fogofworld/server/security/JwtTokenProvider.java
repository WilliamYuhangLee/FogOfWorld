package li.yuhang.fogofworld.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import li.yuhang.fogofworld.server.exception.InvalidJwtTokenException;
import li.yuhang.fogofworld.server.service.AccountService;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Autowired
    private SecuritySettings securitySettings;

    @Autowired
    private AccountService accountService;

    private Key secretKey;
    private long expireLength; // in milliseconds

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(Base64.decode(securitySettings.getSecretKey()));
        expireLength = securitySettings.getExpireLength();
    }

    public String createToken(String username) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + expireLength);
        Claims claims = Jwts.claims()
                            .setSubject(username)
                            .setIssuedAt(now)
                            .setExpiration(expire);
        return Jwts.builder().setClaims(claims).signWith(secretKey, SignatureAlgorithm.HS256).compact();
    }

    public boolean validateToken(String token) {
        try {
            // OK, we can trust this JWT
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Don't trust this JWT!
            return false;
        }
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = accountService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
