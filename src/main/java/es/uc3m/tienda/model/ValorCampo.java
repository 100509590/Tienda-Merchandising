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

    

    public Integer getId(){
        return id;
    } 

    public void setId(Integer id){
        this.id = id;
    }

    public String getValorTexto(){
        return valorTexto;
    } 

    public void setValorTexto(String valorTexto){
        this.valorTexto = valorTexto;
    }

    public CarritoItem getCarritoItem(){
        return carritoItem;
    } 

    public void setCarritoItem(CarritoItem carritoItem){
        this.carritoItem = carritoItem;
    }

    public CampoPersonalizacion getCampoPersonalizacion(){
        return campo;
    } 

    public void setCampoPersonalizacion(CampoPersonalizacion campo){
        this.campo = campo;
    }
}

