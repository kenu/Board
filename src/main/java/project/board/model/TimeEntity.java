package project.board.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@Column(name = "created_date", nullable = false)
	@CreatedDate
	private LocalDateTime createdDate;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
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
