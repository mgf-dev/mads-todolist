# Práctica 3

Marcos González Fernández - 03224142W

# Versión 1.2.0 de ToDoList

Esta práctica contiene la versión 1.2.0 de ToDoList y documenta los cambios realizados para dar soporte a la gestión de **equipos**. La aplicación, desarrollada con Java y Spring Boot siguiendo una arquitectura por capas (Controlador, Servicio, Repositorio), se ha ampliado con la posibilidad de crear equipos, listar los existentes, unirse/salir de ellos y consultar los miembros de cada equipo.

Los cambios corresponden a las historias de usuario **009** (Gestión de equipos) y **010** (Relación usuarios-equipos).

## 1. Rutas (endpoints) definidas

Para la funcionalidad de equipos se han añadido los siguientes endpoints en `EquipoController`:

| Método | Ruta                   | Acción                                                                                                        | Requiere sesión |
| :----- | :--------------------- | :------------------------------------------------------------------------------------------------------------ | :-------------: |
| `GET`  | `/equipos`             | Listado de todos los equipos. Muestra también los equipos a los que pertenece el usuario logeado (si lo hay). |    Opcional     |
| `GET`  | `/equipos/{id}`        | Detalle de un equipo: listado de usuarios que lo componen.                                                    |    Opcional     |
| `POST` | `/equipos/{id}/unirse` | Añade el usuario logeado al equipo indicado.                                                                  |       Sí        |
| `POST` | `/equipos/{id}/salir`  | Elimina el usuario logeado del equipo indicado.                                                               |       Sí        |
| `POST` | `/equipos/nuevo`       | Crea un nuevo equipo con el nombre recibido.                                                                  |    Opcional     |

### Clases y métodos

- **Modelo** (`src/main/java/madstodolist/model/Equipo.java`): Entidad JPA con `@Table(name = "equipos")`. Define `id`, `nombre`, la relación bidireccional `@ManyToMany(mappedBy = "equipos")` con `Usuario` y métodos auxiliares `addUsuario(...)` / `borrarUsuario(...)` para mantener la consistencia de ambos lados de la relación. También implementa `equals`/`hashCode` basados en el `id` cuando existe, o en el `nombre` en caso contrario.
- **Modelo** (`src/main/java/madstodolist/model/Usuario.java`): Se ha ampliado con `@ManyToMany` hacia `Equipo` mediante `@JoinTable(name = "equipos_usuarios", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "equipo_id"))` y el getter/setter correspondiente para el `Set<Equipo>`.
- **Repositorio** (`src/main/java/madstodolist/repository/EquipoRepository.java`): Extiende `CrudRepository<Equipo, Long>`. Se apoya en los métodos heredados (`findAll()`, `findById(...)`, `save(...)`) para las operaciones necesarias.
- **Servicio** (`src/main/java/madstodolist/service/EquipoService.java`): Capa de negocio que utiliza `ModelMapper` para convertir entre entidades `Equipo`/`Usuario` y DTOs `EquipoData`/`UsuarioData`. Implementa `obtenerEquipos()`, `crearEquipo(String)`, `recuperarEquipo(Long)`, `añadirUsuarioAEquipo(Long, Long)`, `borrarUsuarioDeEquipo(Long, Long)`, `usuariosEquipo(Long)` y `equiposUsuario(Long)`. Lanza `EquipoServiceException` cuando el equipo o el usuario no existen, o cuando se intenta eliminar un usuario que no pertenece al equipo.
- **DTO** (`src/main/java/madstodolist/dto/EquipoData.java`): Objeto de transferencia para exponer la información del equipo a las vistas, con `equals`/`hashCode` consistentes con la entidad.
- **Controlador** (`src/main/java/madstodolist/controller/EquipoController.java`): Gestiona las rutas anteriores. Usa `ManagerUserSession` para obtener el `idUsuario` logeado en las acciones de unirse/salir y para añadir `equiposUsuario` al modelo en `/equipos`.

### Plantillas Thymeleaf

- **`equipos.html`**: Muestra una tabla con `id` y `nombre` del equipo (con enlace a `/equipos/{id}`). Según pertenezca o no el usuario al equipo, se muestra el botón `Unirse` o `Salir`, ambos con formularios `POST` a las rutas correspondientes. Además incluye un formulario para crear un nuevo equipo (`POST` a `/equipos/nuevo`).
- **`detallesEquipo.html`**: Muestra el título "Usuarios del equipo" y un listado con el `nombre` de cada usuario perteneciente al equipo.

### Tests

Se han añadido tests tanto a nivel de repositorio como de servicio para garantizar el correcto funcionamiento de la relaciones y de la lógica de negocio:

- **Repositorio** (`src/test/java/madstodolist/repository/EquipoTest.java`): Comprueba la creación y persistencia de `Equipo`, la igualdad entre equipos, la relación bidireccional muchos-a-muchos entre `Usuario` y `Equipo` (actualización en BD y navegación desde ambos lados) y `findAll()`.
- **Servicio** (`src/test/java/madstodolist/service/EquipoServiceTest.java`): Verifica `crearRecuperarEquipo()`, `añadirUsuarioAEquipo()` (el usuario aparece en `usuariosEquipo`), `borrarUsuarioDeEquipo()` (queda vacío tras la operación), `recuperarEquiposDeUsuario()` (un usuario pertenece a varios equipos) y `comprobarExcepciones()` (se lanzan excepciones para entidades inexistentes o para casos no válidos).

## 2. Explicación de algunos fragmentos de código fuente interesantes

### Mantenimiento bidireccional de la relación

En `Equipo` se han añadido métodos para asegurar la sincronización entre ambos extremos de la relación:

```java
public void addUsuario(Usuario usuario) {
    if (this.usuarios == null) {
        this.usuarios = new HashSet<>();
    }
    this.usuarios.add(usuario);
    usuario.getEquipos().add(this);
}

public void borrarUsuario(Usuario usuario) {
    this.usuarios.remove(usuario);
    usuario.getEquipos().remove(this);
}
```

Este enfoque evita inconsistencias entre la caché de Hibernate y el grafo de objetos, algo especialmente importante en tests transaccionales y al operar desde ambos lados de la relación.

### Lógica de pertenencia en la vista

En `equipos.html`, la visibilidad de los botones se decide comparando el equipo con la lista de equipos del usuario:

```html
<form
  th:if="${equiposUsuario == null or !equiposUsuario.contains(equipo)}"
  th:action="@{/equipos/{id}/unirse(id=${equipo.id})}"
  method="post"
  style="display:inline;"
>
  <button type="submit" class="btn btn-sm" th:text="Unirse"></button>
</form>

<form
  th:if="${equiposUsuario != null and equiposUsuario.contains(equipo)}"
  th:action="@{/equipos/{id}/salir(id=${equipo.id})}"
  method="post"
  style="display:inline;"
>
  <button type="submit" class="btn btn-sm" th:text="Salir"></button>
</form>
```

Gracias a la implementación correcta de `equals`/`hashCode` en `EquipoData`, la operación `contains()` funciona correctamente aunque se comparen instancias distintas (DTO vs entidad mapeada).

### Inicialización de datos

En `InitDbService` se ha incorporado la creación de un equipo de ejemplo y su asociación con el usuario de prueba, lo que permite verificar la funcionalidad desde el primer arranque de la aplicación:

```java
Equipo equipo1 = new Equipo("Equipo 1");
equipo1.addUsuario(usuario);
equipoRepository.save(equipo1);
```

## 3. Conclusión

Con la incorporación de las entidades `Equipo`, la relación con `Usuario`, el servicio con sus validaciones, el controlador con las rutas REST para unirse/salir/crear y las vistas correspondientes, se han cubierto las historias de usuario 009 y 010. Los tests añadidos validan tanto la persistencia de la relación muchos-a-muchos como la lógica de negocio, garantizando el correcto comportamiento de la gestión de equipos en la versión 1.2.0.
