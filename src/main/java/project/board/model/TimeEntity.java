package project.board.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeEntity {

	@Column(name = "created_date", nullable = false)
	@CreatedDate
	private LocalDateTime createdDate;

	@Column(name = "modified_date", nullable = false)
	@LastModifiedDate
	private LocalDateTime modifiedDate;

	// 추적 용이함
	// 해당 엔티티 저장 직전에 실행
	@PrePersist
	public void onPrePersist() {
		this.createdDate = LocalDateTime.now();
		this.modifiedDate = createdDate;
	}

	// 추적 용이함
	// 해당 엔티티 업데이트 전에 실행
	@PreUpdate
	public void onPreUpdate() {
		this.modifiedDate = LocalDateTime.now();
	}
}
