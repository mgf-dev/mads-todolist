package madstodolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListadoController {
    @GetMapping("/registrados")
    public String listado() {
        return "listadoRegistrados";
    }
}
