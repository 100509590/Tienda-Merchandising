package es.uc3m.tienda.repositories;

import es.uc3m.tienda.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface CarritoRepository extends JpaRepository<CarritoItem, Integer> {
    
    List<CarritoItem> findByUser(User user);
    void deleteById(Integer id);
}