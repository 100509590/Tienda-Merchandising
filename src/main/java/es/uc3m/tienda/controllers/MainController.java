package es.uc3m.tienda.controllers;


import jakarta.persistence.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class MainController {

    /**
     * Carga la página principal (Catálogo)
     * URL: http://localhost:8080/
     */
    @GetMapping("/")
    public String index() {
        // Busca el archivo src/main/resources/templates/index.html
        return "index"; 
    }

    /**
     * Carga la vista de detalle para configurar la vela
     * URL: http://localhost:8080/detalle
     */
    @GetMapping("/detalle")
    public String mostrarDetalle() {
        // Busca el archivo src/main/resources/templates/detalle.html
        return "detalle";
    }

    /* * Nota: A medida que creemos el carrito, login y registro, 
     * iremos añadiendo aquí los nuevos métodos @GetMapping.
     */



    /**
     * Carga la vista del carrito de la compra
     * URL: http://localhost:8080/carrito
     */
    @GetMapping("/carrito")
    public String mostrarCarrito() {
        // Busca el archivo src/main/resources/templates/carrito.html
        return "carrito";
    }
    
    /**
     * Carga la vista de inicio de sesión
     * URL: http://localhost:8080/login
     */
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }
    
    /**
     * Carga la vista de registro
     * URL: http://localhost:8080/registro
     */
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }
    
    /**
     * Carga el panel de administración
     * URL: http://localhost:8080/admin
     */
    @GetMapping("/admin")
    public String mostrarAdmin() {
        return "admin";
    }
    
    /**
     * Carga la vista de registro (Signup)
     * URL: http://localhost:8080/signup
     */
    @GetMapping("/signup")
    public String mostrarSignup() {
        return "signup"; // Ahora busca signup.html
    }

}
