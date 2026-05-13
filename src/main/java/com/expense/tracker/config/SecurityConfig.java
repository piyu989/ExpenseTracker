package com.expense.tracker.config;

import com.expense.tracker.repository.UserInfoRepository;
import com.expense.tracker.service.UserDetailServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@Data
public class SecurityConfig {
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private final UserDetailServiceImpl userDetailService;

//    @Bean
//    @Autowired
//    public UserDetailsService userDetailsService(UserInfoRepository userInfoRepository
//                                                 , BCryptPasswordEncoder bCryptPasswordEncoder){
//        return new UserDetailServiceImpl(userInfoRepository,bCryptPasswordEncoder);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,JwtAuthFilter jwtFilter) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                        .antMatchers("/api/v1/login",
                                "/api/v1/refreshToken",
                                "/api/v1/signup")
                                .permitAll()
                                .anyRequest().authenticated()
                                .and()


                .sessionManagement(sess-> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
