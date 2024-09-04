package project.board.config.oauth.provider;

// OAuth2.0 제공자들마다 제공해주는 값이 상이하여 구현체에 의존하기보다 공통 인터페이스를 별도로 만들어 구현한다.
public interface OAuth2UserInfo {
	String getProviderId();
	String getProvider();
	String getEmail();
	String getName();
}
