package one.two.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import one.two.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
	User findByUsername(String username);

	User findByActivationCode(String code);
}
