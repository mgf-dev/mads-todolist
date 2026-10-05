package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql")
public class ListadoWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void getListadoDevuelveTablaUsuarios() throws Exception {
        // GIVEN
        // Un usuario en la base de datos
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("usuario1@ua");
        usuario.setNombre("Usuario 1");
        usuario.setPassword("123");
        usuarioService.registrar(usuario);

        // WHEN, THEN
        // Se accede a la página de listado y debe mostrar el email del usuario
        this.mockMvc.perform(get("/registrados"))
                .andExpect(content().string(containsString("usuario1@ua")));
    }

    @Test
    public void getListadoMuestraCabecerasTabla() throws Exception {
        this.mockMvc.perform(get("/registrados"))
                .andExpect(content().string(containsString("Identificador")))
                .andExpect(content().string(containsString("Correo electrónico")));
    }
}
