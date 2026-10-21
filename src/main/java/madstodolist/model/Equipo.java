package madstodolist.model;

public class Equipo {
    String nombre;
    Long id;

    public Equipo() {
        this.nombre = "";
        this.id = -1L;
    }

    public Equipo(String _nombre) {
        this.nombre = _nombre;
        this.id = 1L;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getId() {
        return id;
    }
}
