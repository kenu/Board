package project.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import project.board.model.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);
	Optional<Member> findByProviderAndProviderId(@Param(value = "provider") String provider, @Param(value = "providerId") String providerId);
}
