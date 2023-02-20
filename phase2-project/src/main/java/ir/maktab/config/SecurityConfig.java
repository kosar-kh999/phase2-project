package ir.maktab.config;

import ir.maktab.data.repository.AdminRepository;
import ir.maktab.data.repository.CustomerRepository;
import ir.maktab.data.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomerRepository customerRepository;
    private final ExpertRepository expertRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    public SecurityConfig(CustomerRepository customerRepository, ExpertRepository expertRepository,
                          BCryptPasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        this.customerRepository = customerRepository;
        this.expertRepository = expertRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.
                csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/customer/add_customer").permitAll()
                .requestMatchers("/expert/add_expert").permitAll()
                .requestMatchers("/admin/add_admin").permitAll()
                .requestMatchers("/expert/**").hasRole("EXPERT")
                .requestMatchers("/customer/**").hasRole("CUSTOMER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated().and().httpBasic();
        return httpSecurity.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService((username -> customerRepository.findCustomerByEmail(username).
                        orElseThrow(() -> new UsernameNotFoundException(String.format("This %s notFound!", username))))).
                passwordEncoder(passwordEncoder).and()
                .userDetailsService(username -> expertRepository.findExpertByEmail(username).
                        orElseThrow(() -> new UsernameNotFoundException(String.format("This %s notFound!", username)))).
                passwordEncoder(passwordEncoder).and()
                .userDetailsService(username -> adminRepository.findByUsername(username).orElseThrow(() ->
                        new UsernameNotFoundException(String.format("This %s notFound!", username)))).
                passwordEncoder(passwordEncoder);
    }
}
