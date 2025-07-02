package kh.com.blog.entity;

import jakarta.persistence.*;
import kh.com.blog.common.enumeration.AccountLevel;
import kh.com.blog.common.enumeration.Gender;
import kh.com.blog.common.enumeration.Role;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = UserEntity.TABLE_NAME)
@FieldNameConstants
@Data
public class UserEntity {
	public static final String TABLE_NAME = "users";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // or UUID depending on your preference
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role = Role.USER;

	@Column(nullable = false)
	private String password;

	private LocalDateTime lastLogin = LocalDateTime.now();

	private boolean isVerified = false;

	@Enumerated(EnumType.STRING)
	private AccountLevel accountLevel = AccountLevel.BRONZE;

	private String profilePicture = "";

	private String coverImage = "";

	private String bio;

	private String location;

	private boolean emailNotifications = true;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@ManyToMany
	@JoinTable(
			name = "users_profile_viewers",
			joinColumns = @JoinColumn(name = "users_id"),
			inverseJoinColumns = @JoinColumn(name = "viewer_id")
	)
	private List<UserEntity> profileViewers;

	@ManyToMany
	@JoinTable(
			name = "users_followers",
			joinColumns = @JoinColumn(name = "users_id"),
			inverseJoinColumns = @JoinColumn(name = "follower_id")
	)
	private List<UserEntity> followers;

	@ManyToMany
	@JoinTable(
			name = "users_following",
			joinColumns = @JoinColumn(name = "users_id"),
			inverseJoinColumns = @JoinColumn(name = "following_id")
	)
	private List<UserEntity> following;

	@ManyToMany
	@JoinTable(
			name = "users_blocked",
			joinColumns = @JoinColumn(name = "users_id"),
			inverseJoinColumns = @JoinColumn(name = "blocked_id")
	)
	private List<UserEntity> blockedUsers;

	@ElementCollection
	@Column(name = "posts_id")
	private List<String> posts;

	@ElementCollection
	@Column(name = "posts_id")
	private List<String> likedPosts;

	private String passwordResetToken;

	private LocalDateTime passwordResetExpires;

	private String accountVerificationToken;

	private LocalDateTime accountVerificationExpires;

	@Column(updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	private LocalDateTime updatedAt = LocalDateTime.now();

}
