package es.uc3m.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "opcion_campo")
public class OpcionCampo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    @NotBlank
    private String etiqueta;

    @Column(name = "incremento_precio", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal incrementoPrecio = java.math.BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "campo_id", nullable = false)
    private CampoPersonalizacion campo;

    // getters y setters...
}

