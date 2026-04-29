package es.uc3m.tienda.controllers;

import es.uc3m.tienda.model.CampoPersonalizacion;
import es.uc3m.tienda.model.OpcionCampo;
import es.uc3m.tienda.model.Product;
import es.uc3m.tienda.repositories.CampoPersonalizacionRepository;
import es.uc3m.tienda.repositories.OpcionCampoRepository;
import es.uc3m.tienda.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OpcionCampoRepository opcionRepository;

    @Autowired
    private CampoPersonalizacionRepository campoRepository;

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
            @RequestParam(required = false) String descripcion) {

        Product p = new Product();
        p.setName(name);
        p.setPrecioBase(precioBase);
        p.setTipo(tipo);
        p.setDescription(descripcion);
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

    @PostMapping("/productos/{id}/campos/nuevo")
    public String nuevoCampo(
           @PathVariable Integer id,
           @RequestParam String name,
           @RequestParam CampoPersonalizacion.TipoCampo tipoCampo,
           @RequestParam(defaultValue = "true") boolean obligatorio,
           @RequestParam(required = false) String regexValidacion) {
 
       Product producto = productRepository.findById(id)
           .orElseThrow(() -> new ResponseStatusException(
               HttpStatus.NOT_FOUND, "Producto no encontrado"));
 
       CampoPersonalizacion campo = new CampoPersonalizacion();
       campo.setName(name);
       campo.setTipoCampo(tipoCampo);
       campo.setObligatorio(obligatorio);
       campo.setRegexValidacion(
           (regexValidacion != null && regexValidacion.isBlank()) ? null : regexValidacion);
       campo.setProduct(producto);
       campoRepository.save(campo);
 
       return "redirect:/admin/productos/" + id + "/editar";
    }
 
   // POST /admin/campos/{campoId}/opciones/nueva — añade una opción a un campo SELECCION
   @PostMapping("/campos/{campoId}/opciones/nueva")
   public String nuevaOpcion(
           @PathVariable Integer campoId,
           @RequestParam String etiqueta,
           @RequestParam(defaultValue = "0") java.math.BigDecimal incrementoPrecio) {
 
       CampoPersonalizacion campo = campoRepository.findById(campoId)
           .orElseThrow(() -> new ResponseStatusException(
               HttpStatus.NOT_FOUND, "Campo no encontrado"));
 
       OpcionCampo opcion = new OpcionCampo();
       opcion.setEtiqueta(etiqueta);
       opcion.setIncrementoPrecio(incrementoPrecio);
       opcion.setCampoPersonalizacion(campo);
       opcionRepository.save(opcion);
 
       return "redirect:/admin/productos/" + campo.getProduct().getId() + "/editar";
   }
 
   // POST /admin/campos/{campoId}/borrar — elimina un campo y sus opciones
   @PostMapping("/campos/{campoId}/borrar")
   public String borrarCampo(@PathVariable Integer campoId) {
       CampoPersonalizacion campo = campoRepository.findById(campoId)
           .orElseThrow(() -> new ResponseStatusException(
               HttpStatus.NOT_FOUND, "Campo no encontrado"));
       Integer productoId = campo.getProduct().getId();
       campoRepository.deleteById(campoId);
       return "redirect:/admin/productos/" + productoId + "/editar";
   }
 
   // POST /admin/opciones/{opcionId}/borrar — elimina una opción
   @PostMapping("/opciones/{opcionId}/borrar")
   public String borrarOpcion(@PathVariable Integer opcionId) {
       OpcionCampo opcion = opcionRepository.findById(opcionId)
           .orElseThrow(() -> new ResponseStatusException(
               HttpStatus.NOT_FOUND, "Opción no encontrada"));
       Integer productoId = opcion.getCampoPersonalizacion().getProduct().getId();
       opcionRepository.deleteById(opcionId);
       return "redirect:/admin/productos/" + productoId + "/editar";
   }

}

