package es.uc3m.tienda.controllers;

import es.uc3m.tienda.model.*;
import es.uc3m.tienda.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired private CarritoRepository carritoRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private OpcionCampoRepository opcionRepository;
    @Autowired private CampoPersonalizacionRepository campoRepository;

    @GetMapping
    public String verCarrito(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName());
        var items = carritoRepository.findByUser(user);
        
        BigDecimal total = items.stream()
            .map(item -> item.getPrecioCalculado().multiply(new BigDecimal(item.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "carrito";
    }

    @PostMapping("/añadir")
    public String añadirAlCarrito(
            @RequestParam Integer productoId,
            @RequestParam int cantidad,
            @RequestParam Map<String, String> todasLasParams,
            Authentication auth) {

        User user = userRepository.findByEmail(auth.getName());
        Product producto = productRepository.findById(productoId).orElseThrow();

        CarritoItem item = new CarritoItem();
        item.setUser(user);
        item.setProduct(producto);
        item.setCantidad(cantidad);

        BigDecimal precioFinal = producto.getPrecioBase();
        item.setValores(new ArrayList<>());

        // Procesar personalizaciones (los nombres de los campos en el HTML serán "campo_ID")
        for (CampoPersonalizacion campo : producto.getCampos()) {
            String valorRecibido = todasLasParams.get("campo_" + campo.getId());
            if (valorRecibido != null && !valorRecibido.isEmpty()) {
                ValorCampo vc = new ValorCampo();
                vc.setCampoPersonalizacion(campo);
                vc.setCarritoItem(item);
                
                if (campo.getTipoCampo() == CampoPersonalizacion.TipoCampo.SELECCION) {
                    Integer opcionId = Integer.parseInt(valorRecibido);
                    OpcionCampo opcion = opcionRepository.findById(opcionId).get();
                    precioFinal = precioFinal.add(opcion.getIncrementoPrecio());
                    vc.setValorTexto(opcion.getEtiqueta());
                } else {
                    vc.setValorTexto(valorRecibido);
                }
                item.getValores().add(vc);
            }
        }

        item.setPrecioCalculado(precioFinal);
        carritoRepository.save(item);

        return "redirect:/carrito";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        carritoRepository.deleteById(id);
        return "redirect:/carrito";
    }
}


