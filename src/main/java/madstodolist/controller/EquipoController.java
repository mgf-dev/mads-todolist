package madstodolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import madstodolist.service.EquipoService;
import madstodolist.dto.EquipoData;

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
}
