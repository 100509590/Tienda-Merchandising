package es.uc3m.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "product") 
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 64)
    @NotBlank
    @Size(max = 64)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal precioBase;
    
    @Column(nullable = false) 
    private boolean activo = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

    public enum Tipo {FIJO, PERSONALIZABLE}

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private java.util.List<CampoPersonalizacion> campos;
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.math.BigDecimal getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(java.math.BigDecimal precioBase) {
        this.precioBase = precioBase;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public java.util.List<CampoPersonalizacion> getCampos(){
        return campos;
    }
    
    public void setCampos(java.util.List<CampoPersonalizacion> campos){
        this.campos = campos;
    } 
}