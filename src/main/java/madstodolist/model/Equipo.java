package madstodolist.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
}
