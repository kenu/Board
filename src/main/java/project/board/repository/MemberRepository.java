package project.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.board.model.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);
}
