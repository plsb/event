package event.br.eventmobile;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import event.br.dao.Database;
import event.br.model.Usuario;
import event.br.util.Ativo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etLogin, etPassword;
    private Button btSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        verificarDadosSincronizados();

        etLogin = (EditText) findViewById(R.id.LoginEtUserName);
        etPassword = (EditText) findViewById(R.id.LoginEtPass);
        btSignIn = (Button) findViewById(R.id.LoginBtnSingIn);
        btSignIn.setOnClickListener(this);
    }

    public void verificarDadosSincronizados(){
        Database db = new Database(MainActivity.this);
        db.open();
        try{
            Cursor cursorEvento = db.consult("evento",
                    new String[] { "*" },
                    null, null, null, null,
                    null, null);
            Cursor cursorUsuarios = db.consult("usuario",
                    new String[] { "*" },
                    null, null, null, null,
                    null, null);
            if(cursorEvento.getCount()==0 || cursorUsuarios.getCount()==0){
                Intent i = new Intent(MainActivity.this,ActSynchronize.class);
                finish();
                startActivity(i);
            }
        } catch (Exception ex){
            Log.i("PEDRO",ex.getMessage());
        }

    }

    public boolean autentica(String login, String password){
        Database db = new Database(MainActivity.this);
        db.open();
        Cursor cursor = db.consult("usuario",new String[] { "*" },
                " login = ? AND senha = ?",
                new String[] { login, password }, null, null,
                null, null);
        if (cursor.getCount() != 0) {
            //preenche user
            cursor.moveToFirst();
            Usuario u = new Usuario();
            u.setId(cursor.getString(cursor.getColumnIndex("_ID")).toString());
            u.setLogin(cursor.getString(cursor.getColumnIndex("login")).toString());
            u.setNome(cursor.getString(cursor.getColumnIndex("nome")).toString());

            Cursor cursorEvento = db.consult("evento",new String[] { "*" },
                    null, null, null, null,
                    null, null);
            if(cursorEvento.getCount()!=0){
                cursorEvento.moveToFirst();
                u.setDescEvento(cursorEvento.getString(cursorEvento.getColumnIndex("descricao")).toString());
                u.setIdEvento(cursorEvento.getString(cursorEvento.getColumnIndex("_ID")).toString());
            }
            Ativo.setUsuario(u);


            return true;
        } else {
            Toast.makeText(MainActivity.this, "Dados Incorretos!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v==btSignIn){
            if(autentica(etLogin.getText().toString(),
                    etPassword.getText().toString())){
                Intent intent = new Intent(MainActivity.this,
                        ActPrincipal.class);//AdministradorOpcoes.class);
                finish();
                startActivity(intent);
            } else {
                Toast.makeText(this,"Incorreto. Tente novamente!",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
