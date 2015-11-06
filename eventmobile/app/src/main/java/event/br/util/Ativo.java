package event.br.util;

import event.br.model.Usuario;

/**
 * Created by Pedro Saraiva on 24/10/2015.
 */
public class Ativo {

    private static Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Ativo.usuario = usuario;
    }
}
