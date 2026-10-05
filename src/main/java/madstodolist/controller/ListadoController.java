package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ListadoController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registrados")
    public String listado(Model model) {
        List<UsuarioData> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "listadoRegistrados";
    }

    @GetMapping("/registrados/{id}")
    public String descripcion(@PathVariable(value = "id") Long idUsuario, Model model) {
        UsuarioData usuario = usuarioService.findById(idUsuario);
        model.addAttribute("usuario", usuario);
        return "descripcionUsuario";
    }
}
