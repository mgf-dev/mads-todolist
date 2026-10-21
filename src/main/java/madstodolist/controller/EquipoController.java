package madstodolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import madstodolist.service.EquipoService;
import madstodolist.dto.*;

@Controller
public class EquipoController {

    @Autowired
    EquipoService equipoService;

    @GetMapping("/equipos")
    public String equipos(Model model) {
        List<EquipoData> equipos = equipoService.obtenerEquipos();
        model.addAttribute("equipos", equipos);
        return "equipos";
    }

    @GetMapping("/equipos/{id}")
    public String detallesEquipo(@PathVariable(value = "id") Long idEquipo, Model model) {
        List<UsuarioData> usuariosEnEquipo = equipoService.usuariosEquipo(idEquipo);
        model.addAttribute("usuarios", usuariosEnEquipo);
        return "detallesEquipo";
    }
}
