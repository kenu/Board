package project.board.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import project.board.config.custom.CustomAuthenticationFailureHandler;
import project.board.config.oauth.PrincipalOauthDetailsService;

// Configure global AuthenticationManagerBuilder
// Global AuthenticationManager configured with AuthenticationProvider bean with name customAuthenticationProvider
// Global AuthenticationManager configured with an AuthenticationProvider bean. UserDetailsService beans will not be used for username/password login.
// Consider removing the AuthenticationProvider bean. Alternatively, consider using the UserDetailsService in a manually instantiated DaoAuthenticationProvider.
@Service
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomAuthenticationFailureHandler authenticationFailureHandler;
	private final PrincipalOauthDetailsService principalOauthDetailsService;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		RequestCache nullRequestCache = new NullRequestCache();

		http.cors(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests(
				auth -> auth
			.requestMatchers(
				new AntPathRequestMatcher("/js/**"),			// static 하위
				new AntPathRequestMatcher("/img/**"),			// static 하위
				new AntPathRequestMatcher("/css/**"),			// static 하위
				new AntPathRequestMatcher("/auth/login"),		// 로그인 폼
				new AntPathRequestMatcher("/auth/signup"),	// 회원가입 폼
				new AntPathRequestMatcher("/login"),			// 로그인 POST
				new AntPathRequestMatcher("/signup"))			// 회원가입 POST
			.permitAll().anyRequest().authenticated())
			// 폼 로그인 기반 설정
			.formLogin(formLogin -> formLogin.loginPage("/auth/login")
						.loginProcessingUrl("/login")
						.defaultSuccessUrl("/board/posts")
						.failureHandler(authenticationFailureHandler)	// 로그인 실패 핸들러
						.permitAll())
			// 로그아웃 설정
			.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/auth/login")
			.invalidateHttpSession(true).deleteCookies("JSESSIONID"))
			// 소셜 로그인 설정
			.oauth2Login(oauth -> oauth.userInfoEndpoint(
				oauthService -> oauthService.userService(principalOauthDetailsService))
			.defaultSuccessUrl("/board"))
			.requestCache((cache) -> cache.requestCache(nullRequestCache));
		// 세션 설정(
		http.sessionManagement(session -> session.sessionFixation().changeSessionId()
				.maximumSessions(1).maxSessionsPreventsLogin(false).sessionRegistry(sessionRegistry())
				.expiredUrl("/duplicated-login"));
		return http.build();
	}
}
