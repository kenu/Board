package project.board.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.board.config.auth.PrincipalDetails;
import project.board.config.oauth.provider.GoogleUserInfo;
import project.board.config.oauth.provider.KakaoUserInfo;
import project.board.config.oauth.provider.NaverUserInfo;
import project.board.config.oauth.provider.OAuth2UserInfo;
import project.board.model.Member;
import project.board.model.Role;
import project.board.repository.MemberRepository;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauthDetailsService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		return processOAuth2User(userRequest, oAuth2User);
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest request, OAuth2User oAuth2User) {
		OAuth2UserInfo oAuth2UserInfo = null;

		if (request.getClientRegistration().getRegistrationId().equalsIgnoreCase("google")) {
			log.info("구글 로그인 요청");
			log.info("OAuth2User.Google={}", oAuth2User.getAttributes());
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		} else if (request.getClientRegistration().getRegistrationId().equalsIgnoreCase("kakao")) {
			log.info("카카오 로그인 요청");
			log.info("OAuth2User.Kakao={}", oAuth2User.getAttributes());
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
		} else if (request.getClientRegistration().getRegistrationId().equalsIgnoreCase("naver")) {
			log.info("네이버 로그인 요청");
			log.info("OAuth2User.Naver={}", oAuth2User.getAttributes());
			oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
		}

		Optional<Member> findMember = memberRepository.findByEmail(oAuth2UserInfo.getEmail());
		Member member;

		// 소셜 로그인 시 계정 중복 여부를 검증
		if (findMember.isPresent()) {
			log.info("해당 이메일로 가입한 계정이 존재합니다.");
			member = findMember.get();
			update(member, oAuth2UserInfo);
		} else {
			log.info("해당 이메일로 가입한 계정이 존재하지 않습니다. 소셜 로그인과 동시에 회원가입이 자동으로 진행됩니다.");
			// OAuth 2.0 유저의 경우 패스워드가 없음
			member = Member.builder()
					.username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
					.email(oAuth2UserInfo.getEmail())
					.role(Role.ROLE_USER)
					.provider(oAuth2UserInfo.getProvider())
					.providerId(oAuth2UserInfo.getProviderId())
					.build();
		}
		memberRepository.save(member);
		return new PrincipalDetails(member.getEmail(), member.getRole(), oAuth2User.getAttributes());
	}

	// 소셜 로그인 중복 계정 가입 시 → username, provider, providerId, modified_date 정보만 변경
	// 이외의 데이터는 그대로 유지되도록
	private Member update(Member member, OAuth2UserInfo oAuth2UserInfo) {
		member.setUsername(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId());
		member.setProvider(oAuth2UserInfo.getProvider());
		member.setProviderId(oAuth2UserInfo.getProviderId());
		return memberRepository.save(member);
	}
}
