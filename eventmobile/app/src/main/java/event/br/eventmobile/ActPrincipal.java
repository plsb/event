package event.br.eventmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import event.br.util.Ativo;
import event.br.util.Synchronize;

/**
 * Created by Pedro Saraiva on 24/10/2015.
 */
public class ActPrincipal extends Activity implements View.OnClickListener{

    private TextView tvUsuario, tvEvento;
    private Button btCheckin, btSincronizar, btSair;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        tvUsuario = (TextView) findViewById(R.id.tvUsuario);
        tvEvento = (TextView) findViewById(R.id.tvEvento);
        btCheckin = (Button) findViewById(R.id.MenuBtn_checkin);
        btCheckin.setOnClickListener(this);
        btSair = (Button) findViewById(R.id.MenuBtn_close);
        btSair.setOnClickListener(this);
        btSincronizar = (Button) findViewById(R.id.MenuBtn_sincronizar);
        btSincronizar.setOnClickListener(this);

        tvUsuario.setText("Usuário: " + Ativo.getUsuario().getNome());
        tvEvento.setText("Evento: " + Ativo.getUsuario().getDescEvento());

    }

    @Override
    public void onClick(View v) {
        if(v==btCheckin){
            Intent i = new Intent(ActPrincipal.this, ActCheckin.class);
            startActivity(i);
        } else if(v==btSair){
            Intent i = new Intent(ActPrincipal.this, MainActivity.class);
            finish();
            startActivity(i);
        } else if(v==btSincronizar){
            String id = Ativo.getUsuario().getIdEvento();
            Synchronize.synchronizeEvento(String.valueOf(id), ActPrincipal.this);
        }
    }

}
