package ar.utn.da.dsi.frontend.controllers;


import ar.utn.da.dsi.frontend.client.MetaMapaApiClient;
import org.apache.coyote.Request;
import org.apache.coyote.Response;

public class LoginController {
  public void post(Request request, Response response) {
    // 1. Obtener email y pass del formulario
    String email = request.queryParams("email");
    String password = request.queryParams("password");

    // 2. Llamar al backend con el cliente API
    MetaMapaApiClient apiClient = new MetaMapaApiClient();
    Usuario usuarioLogueado = apiClient.login(email, password); // Este método interno usa Retrofit

    // 3. Si el login es exitoso...
    if (usuarioLogueado != null) {
      // ...guardar datos en la sesión
      request.session(true); // Crear una sesión si no existe
      request.session().attribute("usuario", usuarioLogueado);
      request.session().attribute("rol", usuarioLogueado.getRol());

      // Redirigir al panel de control correspondiente
      if (usuarioLogueado.getRol().equals("ADMINISTRADOR")) {
        response.redirect("/panel-admin");
      } else {
        response.redirect("/panel-contribuyente");
      }
    } else {
      // ...si no, mostrar un mensaje de error y volver al login
      response.redirect("/login?error=1");
    }
  }
}