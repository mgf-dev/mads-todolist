# Práctica 2

Marcos González Fernández - 03224142W

# Versión 1.1.0 de ToDoList

Esta práctica contiene la versión 1.1.0 de ToDoList, y contiene todos los cambios realizados documentados de manera técnica. El proyecto consiste en una aplicación web desarrollada con Java y Spring Boot (desplegada con Docker), que permite gestionar tareas. Incluye además un usuario administrador capaz de bloquear usuarios de la plataforma.

La práctica sigue una arquitectura por capas, pudiendo diferenciar claramente los controlladores (`Controller`), servicios (`Service`) y repositorios (`Repository`), así como el uso de Data Transfer Objects para el paso de información. En concreto, se ha desarrollado la versión 1.1.0 que incluye los cambios detallados a continuación:

# Cambios realizados

## Página 'Acerca de'

- Se incluyó la página `about.html`
- Desde el login, se puede acceder a dicha página desde un enlace
- Se creó la clase `HomeController`, la cual permite exponer el endpoint `/about`

## Barra de menú

- Se añadió un `navbar` en `fragments.html`, el cual es utilizado en casi todas las páginas de la práctica
- Se implementó el sistema de logout, aunque el endpoint ya estaba creado en la plantilla inicial

## Listado de usuarios

- Se incluyó la página `listadoRegistrados.html`, la cual es accesible desde el endpoint `/registrados`.
- Se ha creado la clase `ListadoController` para exponer el endpoint.

## Descripción de usuario

- Se incluyó la página `descripcionUsuario.html`, la cual es accesible desde el endpoint `/registrados/{id}` (siendo id el ID del usuario)
- `ListadoController` ahora expone el endpoint para acceder a la descripción del usuario
- La descripción de un usuario ahora es accesible mediante el listado

## Administrador

- Se ha añadido un checkbox en el formulario de registro para hacer al usuario administrador del sistema. Sólo puede haber uno, por lo que si el administrador ya existe, no se verá este checkbox.

## Protección del listado y descripciones

- Se ha actualizado `ListadoController` para bloquear accesos no autorizados al listado y descripción de los usuarios
- Se ha creado la página `forbidden.html`, que muestra un mensaje de no autorizado y permite volver a la página principal

## Bloqueo

- Ahora el administrador puede bloquear y desbloquear usuarios desde el listado.
- Se ha creado la página `blocked.html` con un mensaje de que la cuenta ha sido bloqueada y un botón para volver a la página principal.

# Tests

Se han añadido nuevos test, especialmente a los controladores y servicios relacionados con los usuarios, ya que al incluir las funcionalidades del administrador y el bloqueo, se ha modificado el modelo `Usuario`. A continuación, un fragmento de los tests de la capa de servicio:

```java
    @Test
    public void existeAdministradorDevuelveTrueSiHayAdmin() {
        addUsuarioBD();
        UsuarioData admin = new UsuarioData();
        admin.setEmail("admin@ua");
        admin.setNombre("Admin");
        admin.setPassword("123");
        admin.setEsAdmin(true);
        usuarioService.registrar(admin);

        assertThat(usuarioService.existeAdministrador()).isTrue();
    }

    @Test
    public void loginUsuarioBloqueadoDevuelveUserBlocked() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("bloqueado@ua");
        usuario.setNombre("Bloqueado");
        usuario.setPassword("123");
        usuario.setBloqueado(true);
        usuarioService.registrar(usuario);

        UsuarioService.LoginStatus status = usuarioService.login("bloqueado@ua", "123");
        assertThat(status).isEqualTo(UsuarioService.LoginStatus.USER_BLOCKED);
    }
```

Los test de la capa controlador aseguran especialmente la redirección a las páginas `forbidden.html` o `blocked.html`, así como la aparición o no de elementos dinámicos en pantalla como la checkbox de "Registrarse como administrador" en `formRegistro.html`.

# Otras notas importantes

1. Existía la alternativa de crear la carpeta `errors` dentro de `src/main/resources/templates` para crear páginas que se lanzasen dado un error concreto (por ejemplo, 403 No autorizado). Sin embargo, en este caso se ha optado por crear páginas normales dentro de `templates` y crear la redirección manualmente, dado que el alcance de la práctica es académico y no contiene muchos archivos. Sería algo a reconsiderar si el proyecto creciera.
2. Al no haberse modificado los repositorios, no se han creado tests para la capa `Repository`.
