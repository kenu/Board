package project.board.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import project.board.config.auth.CustomAuthenticationFailureHandler;
import project.board.config.auth.CustomAuthenticationProvider;
import project.board.config.oauth.PrincipalOauthDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomAuthenticationProvider customAuthenticationProvider;
	private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	private final PrincipalOauthDetailsService principalOauthDetailsService;

	public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider,
						  CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
						  PrincipalOauthDetailsService principalOauthDetailsService) {
		this.customAuthenticationProvider = customAuthenticationProvider;
		this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
		this.principalOauthDetailsService = principalOauthDetailsService;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authenticationProvider(customAuthenticationProvider)
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								new AntPathRequestMatcher("/js/**"),
								new AntPathRequestMatcher("/img/**"),
								new AntPathRequestMatcher("/css/**"),
								new AntPathRequestMatcher("/auth/login"),
								new AntPathRequestMatcher("/auth/signup"),
								new AntPathRequestMatcher("/login"),
								new AntPathRequestMatcher("/signup")
						).permitAll()
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage("/auth/login")
						.loginProcessingUrl("/login")
						.defaultSuccessUrl("/board/posts")
						.failureHandler(customAuthenticationFailureHandler)
						.permitAll()
				)
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/auth/login")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
				)
				.oauth2Login(oauth -> oauth
						.userInfoEndpoint(userInfo -> userInfo
								.userService(principalOauthDetailsService)
						)
						.defaultSuccessUrl("/board")
				);

		return http.build();
	}
}