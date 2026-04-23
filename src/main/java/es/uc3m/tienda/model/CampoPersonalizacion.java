package es.uc3m.tienda.model;

import jakarta.persistence.*;
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
    private String name;

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

    

    public Integer getId(){
        return id;
    } 

    public void setId(Integer id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TipoCampo getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(TipoCampo tipoCampo) {
        this.tipoCampo = tipoCampo   ;
    }

    public boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public String getRegexValidacion() {
        return regexValidacion;
    }

    public void setRegexValidacion(String regexValidacion) {
        this.regexValidacion = regexValidacion;
    }

    public Product getProduct(){
        return product;
    }

    public void setProduct(Product product){
        this.product = product;
    }

    public java.util.List<OpcionCampo> getOpciones(){
        return opciones;
    }

    public void setOpciones(java.util.List<OpcionCampo> opciones){
        this.opciones = opciones;
    }
}

