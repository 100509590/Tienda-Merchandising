package es.uc3m.tienda.repositories;

import es.uc3m.tienda.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Spring Data genera el SQL automáticamente a partir del nombre del método
    User findByEmail(String email);
}
