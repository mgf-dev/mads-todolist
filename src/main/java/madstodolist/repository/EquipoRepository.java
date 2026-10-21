package madstodolist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import madstodolist.model.Equipo;

public interface EquipoRepository extends CrudRepository<Equipo, Long> {
    public List<Equipo> findAll();
}
