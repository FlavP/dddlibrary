package com.flp.ddd.dddlibrary.lending.infrastructure.persistence.user;

import com.flp.ddd.dddlibrary.lending.domain.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "user_uuid"))
    private UserId userId;
    private String username;
    private String email;
}
