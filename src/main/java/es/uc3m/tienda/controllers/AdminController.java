package es.uc3m.tienda.controllers;

import es.uc3m.tienda.model.Product;
import es.uc3m.tienda.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    // GET /admin — muestra el panel con el inventario completo
    @GetMapping
    public String panel(Model model) {
        model.addAttribute("productos", productRepository.findAll());
        model.addAttribute("product", new Product()); // objeto vacío para el formulario
        return "admin";
    }

    // POST /admin/productos/nuevo — guarda un producto nuevo
    @PostMapping("/productos/nuevo")
    public String nuevoProducto(
            @RequestParam String name,
            @RequestParam java.math.BigDecimal precioBase,
            @RequestParam Product.Tipo tipo,
            @RequestParam(required = false) String description) {

        Product p = new Product();
        p.setName(name);
        p.setPrecioBase(precioBase);
        p.setTipo(tipo);
        p.setDescription(description);
        p.setActivo(false); // por defecto inactivo hasta que el admin lo publique
        productRepository.save(p);
        return "redirect:/admin";
    }

    // POST /admin/productos/{id}/activar — publica el producto (visible en catálogo)
    @PostMapping("/productos/{id}/activar")
    public String activar(@PathVariable Integer id) {
        productRepository.findById(id).ifPresent(p -> {
            p.setActivo(true);
            productRepository.save(p);
        });
        return "redirect:/admin";
    }

    // POST /admin/productos/{id}/desactivar — retira el producto del catálogo
    @PostMapping("/productos/{id}/desactivar")
    public String desactivar(@PathVariable Integer id) {
        productRepository.findById(id).ifPresent(p -> {
            p.setActivo(false);
            productRepository.save(p);
        });
        return "redirect:/admin";
    }

    // POST /admin/productos/{id}/borrar — elimina el producto
    @PostMapping("/productos/{id}/borrar")
    public String borrar(@PathVariable Integer id) {
        productRepository.deleteById(id);
        return "redirect:/admin";
    }
}

