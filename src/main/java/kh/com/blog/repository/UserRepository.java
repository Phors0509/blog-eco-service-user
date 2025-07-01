package kh.com.blog.repository;

import kh.com.blog.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity findByUsernameOrEmail(String username, String email);
	UserEntity findByUsername(String username);
}
