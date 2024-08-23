package project.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import project.board.config.auth.PrincipalDetailsService;
import project.board.config.oauth.PrincipalOauthDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final PrincipalDetailsService principalDetailsService;
	private final PrincipalOauthDetailsService principalOauthDetailsService;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(principalDetailsService);
		authProvider.setPasswordEncoder(bCryptPasswordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.authenticationProvider(daoAuthenticationProvider())
				.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
		requestCache.setMatchingRequestParameterName("null");

		http.cors(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests(
				auth -> auth
			.requestMatchers(
				new AntPathRequestMatcher("/js/**"),
				new AntPathRequestMatcher("/img/**"),
				new AntPathRequestMatcher("/css/**"),
				new AntPathRequestMatcher("/loginForm"),
				new AntPathRequestMatcher("/signupForm"),
				new AntPathRequestMatcher("/login"),
				new AntPathRequestMatcher("/signup"),
				new AntPathRequestMatcher("/"))
			.permitAll().anyRequest().authenticated())
			.userDetailsService(principalDetailsService)
			.formLogin(
				formLogin -> formLogin
			.loginPage("/loginForm")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/board"))
			.logout(
				logout -> logout
			.logoutSuccessUrl("/board"))
			.oauth2Login(
				oauth -> oauth.userInfoEndpoint(
				oauthService -> oauthService.userService(principalOauthDetailsService))
			.defaultSuccessUrl("/board"));
		return http.build();
	}
}
