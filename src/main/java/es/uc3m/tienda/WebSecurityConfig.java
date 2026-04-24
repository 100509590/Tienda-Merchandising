/*package es.uc3m.tienda;


import jakarta.persistence.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/**").permitAll());
        return http.build();
    } */
package es.uc3m.tienda; // Asegúrate de que el package sea el correcto

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                // 1. PERMITIR TODO EL DISEÑO (Esto arreglará la barra y el CSS)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/webjars/**").permitAll()
                
                // 2. PERMITIR LAS PÁGINAS PÚBLICAS
                .requestMatchers("/", "/index", "/signup", "/detalle/**").permitAll() 
                
                // 3. PROTEGER EL CARRITO
                .requestMatchers("/carrito").authenticated()
                
                // 4. EL RESTO (Por seguridad, puedes dejarlo permitAll o authenticated)
                .anyRequest().permitAll()
        )
        .formLogin((form) -> form
                .loginPage("/login")
                .permitAll()
        )
        .logout((logout) -> logout.permitAll());
    
        return http.build();
    }
} 






