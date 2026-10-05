package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ListadoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ManagerUserSession managerUserSession;

    private boolean comprobarAdmin() {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return false;
        }
        UsuarioData usuario = usuarioService.findById(idUsuarioLogeado);
        if (usuario == null || !usuario.getEsAdmin()) {
            return false;
        }
        return true;
    }

    @GetMapping("/registrados")
    public String listado(Model model) {
        if (!comprobarAdmin())
            return "forbidden";
        List<UsuarioData> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "listadoRegistrados";
    }

    @GetMapping("/registrados/{id}")
    public String descripcion(@PathVariable(value = "id") Long idUsuario, Model model) {
        if (!comprobarAdmin())
            return "forbidden";
        UsuarioData usuario = usuarioService.findById(idUsuario);
        model.addAttribute("usuario", usuario);
        return "descripcionUsuario";
    }

    @PostMapping("/registrados/{id}/bloquear")
    public String cambiarBloqueo(@PathVariable(value = "id") Long idUsuario, Model model) {
        if (!comprobarAdmin())
            return "forbidden";
        usuarioService.cambiarEstadoBloqueo(idUsuario);
        return "redirect:/registrados";
    }
}
