package es.uc3m.tienda.controllers;

import es.uc3m.tienda.model.*;
import es.uc3m.tienda.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(path = "/")
public class MainController {

    // ----------------------------------------------------------------
    // Dependencias
    // ----------------------------------------------------------------

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;


    /**
     * Página principal — Catálogo de productos
     * URL: GET /
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("productos", productRepository.findByActivoTrue());
        return "index";
    }

    /**
     * Detalle / configurador de vela
     * URL: GET /detalle
     */
    @GetMapping("/detalle/{id}")
    public String mostrarDetalle(@PathVariable Integer id, Model model) {
        Product p = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if(!p.getActivo()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND ,"Producto no disponible");
        }

        model.addAttribute("producto", p);
        return "detalle";
    }

    /**
     * Carrito de la compra
     * URL: GET /carrito
     */
    @GetMapping("/carrito")
    public String mostrarCarrito() {
        return "carrito";
    }

    /**
     * Formulario de inicio de sesión
     * URL: GET /login
     */
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    /**
     * Formulario de registro
     * URL: GET /signup
     */
    @GetMapping("/signup")
    public String mostrarSignup() {
        return "signup";
    }

    /**
     * Panel de administración
     * URL: GET /admin
     */
    /*
    @GetMapping("/admin")
    public String mostrarAdmin() {
        return "admin";
    }
    */

    /**
     * Procesa el formulario de registro de nuevo usuario.
     * URL: POST /signup
     *
     * Spring vincula automáticamente cada name="" del formulario HTML
     * con el @RequestParam correspondiente.
     *
     * Flujo:
     *   1. Verificar que las dos contraseñas coinciden.
     *   2. Verificar que el email no está ya registrado.
     *   3. Hashear la contraseña con BCrypt y guardar el usuario.
     *   4. Redirigir al login (patrón PRG: Post/Redirect/Get).
     */
    @PostMapping("/signup")
    public String procesarRegistro(
            @RequestParam("name")           String name,
            @RequestParam("email")          String email,
            @RequestParam("password")       String password,
            @RequestParam("passwordRepeat") String passwordRepeat,
            Model model) {

        // 1. Validar que las contraseñas coinciden
        if (!password.equals(passwordRepeat)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "signup";
        }

        // 2. Validar que el email no esté ya registrado
        if (userRepository.findByEmail(email) != null) {
            model.addAttribute("error", "Ya existe una cuenta con ese correo electrónico.");
            return "signup";
        }

        // 3. Crear usuario y hashear contraseña
        User nuevoUsuario = new User();
        nuevoUsuario.setName(name);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setRol(User.Rol.CLIENTE); // todo usuario nuevo es CLIENTE por defecto

        // 4. Persistir en base de datos
        userRepository.save(nuevoUsuario);

        // 5. Redirigir al login 
        return "redirect:/login?registered=true";
    }
}

