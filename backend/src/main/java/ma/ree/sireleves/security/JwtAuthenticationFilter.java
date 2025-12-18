package ma.ree.sireleves.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.util.JwtUtil;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Récupérer le header Authorization
        String authHeader = request.getHeader("Authorization");

        // Vérifier si le header contient un token Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extraire le token
            String token = authHeader.substring(7);

            // Extraire l'email du token
            String email = jwtUtil.extractUsername(token);

            // Si l'email existe et qu'il n'y a pas déjà d'authentification
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Valider le token
                if (jwtUtil.validateToken(token, email)) {

                    // Extraire le rôle
                    String role = jwtUtil.extractRole(token);

                    // Créer l'authentification
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Mettre l'authentification dans le contexte de sécurité
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception e) {
            // En cas d'erreur, logger et continuer sans authentification
            logger.error("Erreur lors de la validation du token JWT", e);
        }

        filterChain.doFilter(request, response);
    }
}