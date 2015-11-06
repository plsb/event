package event.br.eventmobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import event.br.util.Mensagem;
import event.br.util.Synchronize;

/**
 * Created by Pedro Saraiva on 24/10/2015.
 */
public class ActSynchronize extends Activity implements View.OnClickListener{

    private EditText id;
    private Button btSynchronize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_synchronize);

        id = (EditText) findViewById(R.id.idEvento);
        btSynchronize = (Button) findViewById(R.id.btSynchronize);

        btSynchronize.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btSynchronize){
            if(id.getText().toString().equals("")){
                Mensagem.exibeMessagem(ActSynchronize.this, "endemics", "Informe o Código do Evento!");
            } else {
                Synchronize.synchronizeEvento(id.getText().toString(), ActSynchronize.this);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
