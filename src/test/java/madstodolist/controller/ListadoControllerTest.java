package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListadoController.class)
public class ListadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    public void listadoDevuelveVistaListadoRegistrados() throws Exception {
        when(usuarioService.findAll()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/registrados"))
                .andExpect(status().isOk())
                .andExpect(view().name("listadoRegistrados"));
    }

    @Test
    public void listadoMuestraListaUsuariosEnModelo() throws Exception {
        List<UsuarioData> usuarios = new ArrayList<>();
        UsuarioData u1 = new UsuarioData();
        u1.setId(1L);
        u1.setEmail("u1@ua");
        UsuarioData u2 = new UsuarioData();
        u2.setId(2L);
        u2.setEmail("u2@ua");
        usuarios.add(u1);
        usuarios.add(u2);

        when(usuarioService.findAll()).thenReturn(usuarios);

        this.mockMvc.perform(get("/registrados"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usuarios"))
                .andExpect(model().attribute("usuarios", hasSize(2)))
                .andExpect(model().attribute("usuarios", hasItem(hasProperty("email", is("u1@ua")))))
                .andExpect(model().attribute("usuarios", hasItem(hasProperty("email", is("u2@ua")))));
    }
}
