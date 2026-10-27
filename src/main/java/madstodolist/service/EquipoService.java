package madstodolist.service;

import madstodolist.dto.*;
import madstodolist.model.*;
import madstodolist.repository.*;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EquipoService {

    @Autowired
    EquipoRepository equipoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public List<EquipoData> obtenerEquipos() {
        List<Equipo> equipos = equipoRepository.findAll();
        return equipos.stream().map(equipo -> modelMapper.map(equipo, EquipoData.class)).collect(Collectors.toList());
    }

    @Transactional
    public EquipoData crearEquipo(String nombre) {
        Equipo equipo = new Equipo(nombre);
        equipoRepository.save(equipo);
        return modelMapper.map(equipo, EquipoData.class);
    }

    @Transactional(readOnly = true)
    public EquipoData recuperarEquipo(Long id) {
        Equipo e = equipoRepository.findById(id).orElse(null);
        if (e == null) {
            throw new EquipoServiceException("El equipo " + id + " no existe.");
        }
        return modelMapper.map(e, EquipoData.class);
    }

    @Transactional
    public void añadirUsuarioAEquipo(Long id, Long id1) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        Usuario usuario = usuarioRepository.findById(id1).orElse(null);
        if (equipo == null)
            throw new EquipoServiceException("El equipo " + id + "no existe.");
        if (usuario == null)
            throw new EquipoServiceException("El usuario " + id1 + "no existe.");
        equipo.addUsuario(usuario);
    }

    @Transactional
    public void borrarUsuarioDeEquipo(Long equipoId, Long usuarioId) {
        Equipo equipo = equipoRepository.findById(equipoId).orElse(null);
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (equipo == null)
            throw new EquipoServiceException("El equipo " + equipoId + "no existe.");
        if (usuario == null)
            throw new EquipoServiceException("El usuario " + usuarioId + "no existe.");
        if (!equipo.getUsuarios().contains(usuario))
            throw new EquipoServiceException("El usuario no está en el equipo.");
        equipo.borrarUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioData> usuariosEquipo(Long id) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        // Hacemos uso de Java Stream API para mapear la lista de entidades a DTOs.
        if (equipo == null)
            throw new EquipoServiceException("El equipo " + id + "no existe.");
        return equipo.getUsuarios().stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioData.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EquipoData> equiposUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null)
            throw new EquipoServiceException("El usuario " + usuarioId + "no existe.");
        return usuario.getEquipos().stream()
                .map(equipo -> modelMapper.map(equipo, EquipoData.class))
                .collect(Collectors.toList());
    }

}