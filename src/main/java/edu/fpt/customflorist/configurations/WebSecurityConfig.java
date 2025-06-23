package edu.fpt.customflorist.configurations;

import edu.fpt.customflorist.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig implements WebMvcConfigurer {

    private final WebClient userInfoClient;
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(String.format("%s/api/v1/auth/**", apiPrefix))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/auth/**", apiPrefix)).permitAll()
                        .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/auth/**", apiPrefix)).authenticated())
                .oauth2ResourceServer(c -> c.opaqueToken(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/swagger-resources/**",
                                    "/swagger-ui.html",
                                    "/webjars/**"
                            )
                            .permitAll()

                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/auth/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/auth/**", apiPrefix)).authenticated()

                            .requestMatchers(HttpMethod.GET, String.format("/auth/**")).permitAll()

                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/users/signup", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/users/login", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/users/reset-password/**", apiPrefix)).permitAll()

                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/users", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/users/*", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.PUT, String.format("%s/api/v1/users/*", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.PUT, String.format("%s/api/v1/users/*/password", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.PATCH, String.format("%s/api/v1/users/**", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/users/verify", apiPrefix)).permitAll()

                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/payment/vn-pay-callback", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/payment", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/payment/vn-pay", apiPrefix)).permitAll()

                            .requestMatchers("/payment-fail.html", "/payment-success.html","/error.html", "/verify-success.html", "/reset-password-success.html")
                            .permitAll()

                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/categories", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/categories", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.PUT, String.format("%s/api/v1/categories/**", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/categories/*", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/categories/active", apiPrefix)).permitAll()

                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/feedbacks", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/feedbacks/*", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/feedbacks/bouquet/*", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/feedbacks/bouquet/*/active", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/feedbacks/user/*", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/feedbacks/user/*/active", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/feedbacks", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.PUT, String.format("%s/api/v1/feedbacks/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/api/v1/feedbacks/**", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")

                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/delivery-histories", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/delivery-histories/*", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/delivery-histories/active/user/*", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/delivery-histories/active/courier/*", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/delivery-histories/user/*", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/delivery-histories/courier/*", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/delivery-histories", apiPrefix)).hasAnyRole("ADMIN", "MANAGER", "SHIPPER")
                            .requestMatchers(HttpMethod.PUT, String.format("%s/api/v1/delivery-histories/*", apiPrefix)).hasAnyRole("ADMIN", "MANAGER", "SHIPPER")
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/api/v1/delivery-histories/**", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")

                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/flowers", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/flowers", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.PUT, String.format("%s/api/v1/flowers/**", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/flowers/*", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/flowers/active", apiPrefix)).permitAll()

                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/orders", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/orders/*", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/orders/user/*", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/orders/active/courier", apiPrefix)).hasAnyRole("SHIPPER")
                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/orders", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/api/v1/orders/**", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.PATCH, String.format("%s/api/v1/orders/**", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")

                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/images/upload", apiPrefix)).permitAll()


                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/bouquets", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/bouquets/active", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/bouquets/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/bouquets", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.PUT, String.format("%s/api/v1/bouquets/**", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/api/v1/bouquets/**", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")

                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/promotions", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/promotions/*", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/promotions/code/*", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/promotions/active", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/promotions", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.PUT, String.format("%s/api/v1/promotions/**", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/api/v1/promotions/**", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")

                            .requestMatchers(HttpMethod.POST, String.format("%s/api/v1/chatbot/message", apiPrefix)).hasAnyRole("CUSTOMER")
                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/chatbot/history", apiPrefix)).hasAnyRole("CUSTOMER")

                            .requestMatchers(HttpMethod.GET, String.format("%s/api/v1/statistic/**", apiPrefix)).hasAnyRole("ADMIN", "MANAGER")

                            .anyRequest().authenticated();

                })
                //.oauth2ResourceServer(c -> c.opaqueToken(Customizer.withDefaults()))
//                .securityMatcher(String.format("%s/api/v1/auth/**", apiPrefix))
//                .oauth2ResourceServer(c -> c.opaqueToken(Customizer.withDefaults()))

                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)

                .cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(List.of("*", "https://yourflorist.vercel.app","http://localhost:3000", "http://localhost:4300", "http://localhost:4200"));
                        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                        configuration.setAllowedHeaders(Arrays.asList(
                                "Authorization", "authorization", "content-type", "x-auth-token", "accept",
                                "access-control-request-method", "access-control-request-headers"
                        ));
                        configuration.setExposedHeaders(List.of("Authorization", "x-auth-token"));
                        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                        source.registerCorsConfiguration("/**", configuration);
                        httpSecurityCorsConfigurer.configurationSource(source);
                    }
                });

        return http.build();
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Allow all paths
//                .allowedOrigins("http://localhost:3000") // Allow only this origin
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD") // Allow these methods
//                .allowedHeaders("*") // Allow all headers
//                .allowCredentials(true); // Allow credentials (cookies, authorization headers, etc.)
//    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    public OpaqueTokenIntrospector introspector() {
        return new GoogleOpaqueTokenIntrospector(userInfoClient);
    }

}
