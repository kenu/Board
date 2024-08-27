package project.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
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
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(principalDetailsService);
		authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		RequestCache nullRequestCache = new NullRequestCache();

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
			.formLogin(
				formLogin -> formLogin
			.loginPage("/loginForm")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/board/posts"))
			.logout(
				logout -> logout.logoutRequestMatcher(
						new AntPathRequestMatcher("/logout")
				)
			.logoutSuccessUrl("/board")
			.invalidateHttpSession(true))
			.oauth2Login(
				oauth -> oauth.userInfoEndpoint(
				oauthService -> oauthService.userService(principalOauthDetailsService))
			.defaultSuccessUrl("/board/posts"))
			.requestCache((cache) -> cache.requestCache(nullRequestCache));
		return http.build();
	}
}
