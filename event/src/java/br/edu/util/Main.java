
package br.edu.util;

import br.edu.usuario.Usuario;
import br.edu.usuario.UsuarioDAO;
import java.util.ArrayList;

public class Main {
    
    public static void main(String[] args) {
        
        Usuario u = new Usuario();
        UsuarioDAO dao = new UsuarioDAO();
        u.setCpf("21121");
        u.setActive(true);
        u.setEmail("2343242");
        u.setLogin("a");
        u.setName("r3r23");
        u.setPassword("21");
        u.setPermission(new ArrayList<String>());
        u.getPermission().add("EEF");
        dao.add(u);
                
        
    }
    
}
