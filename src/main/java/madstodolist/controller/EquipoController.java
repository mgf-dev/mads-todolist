package madstodolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import madstodolist.service.EquipoService;
import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.*;

@Controller
public class EquipoController {

    @Autowired
    EquipoService equipoService;

    @Autowired
    ManagerUserSession managerUserSession;

    @GetMapping("/equipos")
    public String equipos(Model model) {
        List<EquipoData> equipos = equipoService.obtenerEquipos();
        Long idUsuario = managerUserSession.usuarioLogeado();
        model.addAttribute("usuarioLogueado", idUsuario);
        model.addAttribute("equipos", equipos);
        if (idUsuario != null) {
            List<EquipoData> equiposUsuario = equipoService.equiposUsuario(idUsuario);
            model.addAttribute("equiposUsuario", equiposUsuario);
        }
        model.addAttribute("equipoData", new EquipoData());
        return "equipos";
    }

    @GetMapping("/equipos/{id}")
    public String detallesEquipo(@PathVariable(value = "id") Long idEquipo, Model model) {
        List<UsuarioData> usuariosEnEquipo = equipoService.usuariosEquipo(idEquipo);
        model.addAttribute("usuarios", usuariosEnEquipo);
        return "detallesEquipo";
    }

    @PostMapping("/equipos/{id}/unirse")
    public String unirseEquipo(@PathVariable(value = "id") Long idEquipo, Model model) {

        equipoService.añadirUsuarioAEquipo(idEquipo, managerUserSession.usuarioLogeado());
        return "redirect:/equipos";
    }

    @PostMapping("/equipos/{id}/salir")
    public String salirEquipo(@PathVariable(value = "id") Long idEquipo, Model model) {

        equipoService.borrarUsuarioDeEquipo(idEquipo, managerUserSession.usuarioLogeado());
        return "redirect:/equipos";
    }

    @PostMapping("/equipos/nuevo")
    public String nuevoEquipo(@ModelAttribute EquipoData equipoData, Model model) {
        equipoService.crearEquipo(equipoData.getNombre());
        return "redirect:/equipos";
    }
}
