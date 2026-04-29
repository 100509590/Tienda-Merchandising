package es.uc3m.tienda.repositories;

import es.uc3m.tienda.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface OpcionCampoRepository extends JpaRepository<OpcionCampoRepository, Integer> {
    
}
