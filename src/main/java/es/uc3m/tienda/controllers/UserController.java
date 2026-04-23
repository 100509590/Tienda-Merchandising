package es.uc3m.tienda.controllers;

import es.uc3m.tienda.model.User;
import es.uc3m.tienda.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // BCrypt es el estándar para hashear contraseñas en Spring Security.
    // Factor de coste 10 es el valor recomendado: suficientemente seguro sin ser lento.
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    /**
     * POST /signup — Procesa el formulario de registro
     * 
     * Spring vincula automáticamente los campos del formulario HTML
     * (name="name", name="email", etc.) con los atributos del objeto User
     * gracias a @ModelAttribute.
     */
    @PostMapping("/signup")
    public String procesarRegistro(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("passwordRepeat") String passwordRepeat,
            Model model) {

        // 1. Validar que las contraseñas coinciden
        if (!password.equals(passwordRepeat)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "signup"; // Vuelve al formulario con el mensaje de error
        }

        // 2. Validar que el email no esté ya registrado
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Ya existe una cuenta con ese correo electrónico.");
            return "signup";
        }

        // 3. Crear el usuario y hashear la contraseña
        // NUNCA guardes contraseñas en texto plano. BCrypt genera un hash
        // irreversible que incluye el "salt" automáticamente.
        User nuevoUsuario = new User();
        nuevoUsuario.setName(name);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setRol(User.Rol.CLIENTE); // Por defecto, todo nuevo usuario es CLIENTE

        // 4. Guardar en base de datos
        userRepository.save(nuevoUsuario);

        // 5. Redirigir al login con un parámetro de éxito
        // El patrón "redirect:" evita que el navegador reenvíe el POST al recargar la página (PRG pattern)
        return "redirect:/login?registered=true";
    }
}

