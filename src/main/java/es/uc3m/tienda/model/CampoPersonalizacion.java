package es.uc3m.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "campo_personalizacion")
public class CampoPersonalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    @NotBlank
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_campo", nullable = false)
    private TipoCampo tipoCampo;

    public enum TipoCampo { SELECCION, TEXTO }

    @Column(nullable = false)
    private boolean obligatorio = true;

    @Column(name = "regex_validacion", length = 500)
    private String regexValidacion; // NULL si tipo = SELECCION

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "campo", cascade = CascadeType.ALL)
    private java.util.List<OpcionCampo> opciones;

    // getters y setters...
}

