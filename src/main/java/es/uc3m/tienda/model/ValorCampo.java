package es.uc3m.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "valor_campo")
public class ValorCampo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "valor_texto", nullable = false, length = 500)
    private String valorTexto;

    @ManyToOne
    @JoinColumn(name = "carrito_item_id", nullable = false)
    private CarritoItem carritoItem;

    @ManyToOne
    @JoinColumn(name = "campo_id", nullable = false)
    private CampoPersonalizacion campo;

    // getters y setters...
}

