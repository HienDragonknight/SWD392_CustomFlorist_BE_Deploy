package edu.fpt.customflorist.filters;
import edu.fpt.customflorist.components.JwtTokenUtils;
import edu.fpt.customflorist.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter{
    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService;

    private final JwtTokenUtils jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull  HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws IOException {
        try {

            if(isBypassToken(request)) {
                filterChain.doFilter(request, response); //enable bypass
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);
            final String username = jwtTokenUtil.extractUserName(token);
            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(username);

                if(jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response); //enable bypass
        }catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }

    }

    private boolean isBypassToken(@NonNull  HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/swagger-ui.*", "GET"),
                Pair.of("/v3/api-docs.*", "GET"),
                Pair.of("/swagger-resources.*", "GET"),
                Pair.of("/webjars.*", "GET"),

                Pair.of(String.format("%s/api/v1/auth/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/api/v1/auth/**", apiPrefix), "OPTIONS"),
                Pair.of(String.format("/auth/**"), "GET"),
                Pair.of(String.format("/auth/**"), "OPTIONS"),

                //Login account
                Pair.of(String.format("%s/api/v1/users/signup", apiPrefix), "POST"),
                Pair.of(String.format("%s/api/v1/users/signup", apiPrefix), "OPTIONS"),
                Pair.of(String.format("%s/api/v1/users/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/api/v1/users/login", apiPrefix), "OPTIONS"),
                Pair.of(String.format("%s/api/v1/users/reset-password/**", apiPrefix), "POST"),
                Pair.of(String.format("%s/api/v1/users/reset-password/**", apiPrefix), "OPTIONS"),
                Pair.of(String.format("%s/api/v1/users/verify", apiPrefix), "GET"),
                Pair.of(String.format("%s/api/v1/users/verify", apiPrefix), "OPTIONS"),

                //Category
                Pair.of(String.format("%s/api/v1/categories/active", apiPrefix), "GET"),
                Pair.of(String.format("%s/api/v1/categories/active", apiPrefix), "OPTIONS"),

                //Feedback
                Pair.of(String.format("%s/api/v1/feedbacks/bouquet/**/active", apiPrefix), "GET"),
                Pair.of(String.format("%s/api/v1/feedbacks/bouquet/**/active", apiPrefix), "OPTIONS"),

                //Payment
                Pair.of(String.format("%s/api/v1/payment/vn-pay-callback", apiPrefix), "GET"),
                Pair.of(String.format("%s/api/v1/payment/vn-pay-callback", apiPrefix), "OPTIONS"),
                Pair.of(String.format("%s/api/v1/payment/vn-pay", apiPrefix), "POST"),
                Pair.of(String.format("%s/api/v1/payment/vn-pay", apiPrefix), "OPTIONS"),

                //Flower
                Pair.of(String.format("%s/api/v1/flowers/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/api/v1/flowers/**", apiPrefix), "OPTIONS"),

                //Image
                Pair.of(String.format("%s/api/v1/images/upload", apiPrefix), "POST"),
                Pair.of(String.format("%s/api/v1/images/upload", apiPrefix), "OPTIONS"),

                Pair.of(String.format("/payment-success.html", apiPrefix), "GET"),
                Pair.of(String.format("/payment-success.html", apiPrefix), "OPTIONS"),
                Pair.of(String.format("/payment-fail.html", apiPrefix), "GET"),
                Pair.of(String.format("/payment-fail.html", apiPrefix), "OPTIONS"),
                Pair.of(String.format("/error.html", apiPrefix), "GET"),
                Pair.of(String.format("/error.html", apiPrefix), "OPTIONS"),
                Pair.of(String.format("/verify-success.html", apiPrefix), "GET"),
                Pair.of(String.format("/verify-success.html", apiPrefix), "OPTIONS"),
                Pair.of(String.format("/reset-password-success.html", apiPrefix), "GET"),
                Pair.of(String.format("/reset-password-success.html", apiPrefix), "OPTIONS"),

                //bouquet
                Pair.of(String.format("%s/api/v1/bouquets/active", apiPrefix), "GET"),
                Pair.of(String.format("%s/api/v1/bouquets/active", apiPrefix), "OPTIONS"),

                //promotion
                Pair.of(String.format("%s/api/v1/promotions/active", apiPrefix), "GET"),
                Pair.of(String.format("%s/api/v1/promotions/active", apiPrefix), "OPTIONS")

                //chatbot
//                Pair.of(String.format("%s/api/v1/chatbot/message", apiPrefix), "POST"),
//                Pair.of(String.format("%s/api/v1/chatbot/message", apiPrefix), "OPTIONS"),
//                Pair.of(String.format("%s/api/v1/chatbot/history/**", apiPrefix), "GET"),
//                Pair.of(String.format("%s/api/v1/chatbot/history/**", apiPrefix), "OPTIONS")

        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        for (Pair<String, String> token : bypassTokens) {
            String path = token.getFirst();
            String method = token.getSecond();
            // Check if the request path and method match any pair in the bypassTokens list
            if (requestPath.matches(path.replace("**", ".*"))
                    && requestMethod.equalsIgnoreCase(method)) {
                return true;
            }
        }
        return false;
    }
}
