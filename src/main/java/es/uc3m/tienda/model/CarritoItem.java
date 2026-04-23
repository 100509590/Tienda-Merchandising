package es.uc3m.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "carrito_item")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private int cantidad = 1;

    @Column(name = "precio_calculado", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal precioCalculado;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "carritoItem", cascade = CascadeType.ALL)
    private java.util.List<ValorCampo> valores;

    // getters y setters...
}

