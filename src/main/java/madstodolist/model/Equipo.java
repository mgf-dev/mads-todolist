package madstodolist.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "equipos")
public class Equipo implements Serializable {

    @NotNull
    String nombre;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    public Equipo() {
        this.nombre = "";
    }

    public Equipo(String _nombre) {
        this.nombre = _nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || !(obj instanceof Equipo))
            return false;

        if (this.id != null && ((Equipo) obj).getId() != null)
            return this.id.hashCode() == ((Equipo) obj).getId().hashCode();
        return this.nombre.equals(((Equipo) obj).getNombre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nombre);
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "equipo_usuario", joinColumns = { @JoinColumn(name = "fk_equipo") }, inverseJoinColumns = {
            @JoinColumn(name = "fk_usuario") })
    Set<Usuario> usuarios = new HashSet<>();

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void addUsuario(Usuario usuario) {
        // Hay que actualiar ambas colecciones, porque
        // JPA/Hibernate no lo hace automáticamente
        this.getUsuarios().add(usuario);
        usuario.getEquipos().add(this);
    }

    public void borrarUsuario(Usuario usuario) {
        this.getUsuarios().remove(usuario);
        usuario.getEquipos().remove(this);
    }
}
