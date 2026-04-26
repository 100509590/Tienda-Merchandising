package es.uc3m.tienda.repositories;

import es.uc3m.tienda.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Spring Data genera el SQL automáticamente a partir del nombre del método
    //Devuelve solo los productos activos
    List<Product> findByActivoTrue();
}
