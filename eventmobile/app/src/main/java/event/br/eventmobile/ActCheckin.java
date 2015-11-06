package event.br.eventmobile;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import event.br.dao.Database;
import event.br.util.Mensagem;

/**
 * Created by Pedro Saraiva on 25/10/2015.
 */
public class ActCheckin extends Activity implements View.OnClickListener{

    private EditText edtNumInscricao;
    private Button btBuscar, btCheckin, btCancelar;
    private Spinner spnItemEvento;
    private TextView tvNome, tvCPF, tvSituacao;
    private String itemSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_checkin);
        edtNumInscricao = (EditText) findViewById(R.id.idInscricao);
        btBuscar = (Button) findViewById(R.id.btBuscar);
        btBuscar.setOnClickListener(this);
        btCheckin = (Button) findViewById(R.id.btCheckin);
        btCheckin.setOnClickListener(this);
        btCancelar = (Button) findViewById(R.id.BtnCancelar);
        btCancelar.setOnClickListener(this);
        spnItemEvento = (Spinner) findViewById(R.id.SpnItensEvento);
        tvCPF = (TextView) findViewById(R.id.tvCpf);
        tvNome = (TextView) findViewById(R.id.tvNome);
        tvSituacao = (TextView) findViewById(R.id.tvSituacao);
        preencheSpinner();
    }

    private Cursor cursor =null;

    public void mostraInformacoes(String numInscricao){
        Database db = new Database(ActCheckin.this);
        db.open();
        int posIni,posFim;

        posIni = itemSelecionado.indexOf("");
        posFim = itemSelecionado.lastIndexOf(" |");
        if(posFim>0){
            String idItemEvento = itemSelecionado.substring(posIni, posFim);
            cursor = db.consult("inscricoes",new String[] { "*" },
                " idItemEvento = ? and idInscricao=?",
                new String[] { idItemEvento,numInscricao }, null, null,
                null, null);
            if(cursor.getCount()>0){
                cursor.moveToFirst();

                tvNome.setText("Nome: "+cursor.getString(cursor.getColumnIndex("nomeUsuario")).toString());
                tvCPF.setText("CPF: "+cursor.getString(cursor.getColumnIndex("cpfUsuario")).toString());
                if(cursor.getString(cursor.getColumnIndex("validada")).toString().equals("true")){
                    tvSituacao.setText("Situação: Ativa");
                } else {
                    tvSituacao.setText("Situação: Inativa");
                }

            } else {
                Mensagem.exibeMessagem(ActCheckin.this,"event","Inscrição Não encontrada!");
                edtNumInscricao.setText("");
            }
        } else {
            Mensagem.exibeMessagem(ActCheckin.this, "event", "Selecione o Item!");
        }

    }

    public void preencheSpinner(){
        ArrayList<String> ItemsEvento = new ArrayList<String>();
        ItemsEvento.add("- Selecione -");
        Database db = new Database(ActCheckin.this);
        db.open();
        final Cursor cursor = db.consult("itensevento",new String[] { "*" },
                null,
                null, null, null,
                "data,hora", null);
        if (cursor.getCount() != 0) {
//            cursor.moveToFirst();
            while (cursor.moveToNext()){
                String item = cursor.getString(cursor.getColumnIndex("_ID")).toString()+" |"+
                    cursor.getString(cursor.getColumnIndex("descricao")).toString();
                ItemsEvento.add(item);
            }
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ItemsEvento);
        ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnItemEvento.setAdapter(spinnerArrayAdapter);

        //Método do Spinner para capturar o item selecionado
        spnItemEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                itemSelecionado = parent.getItemAtPosition(posicao).toString();
                tvSituacao.setText("");
                tvCPF.setText("");
                tvNome.setText("");
//                edtNumInscricao.setText("");
//                cursor = null;
//                visit.setTipoLarvicida(parent.getItemAtPosition(posicao).toString());
                //imprime um Toast na tela com o nome que foi selecionado
                //Toast.makeText(ExemploSpinner.this, "Nome Selecionado: " + nome, Toast.LENGTH_LONG).show();
            }


            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    public void onClick(View v) {
        if(v==btBuscar){
            Toast.makeText(ActCheckin.this,edtNumInscricao.getText().toString(), Toast.LENGTH_LONG);
            if(edtNumInscricao.getText().toString().equals("")){
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            } else{
                mostraInformacoes(edtNumInscricao.getText().toString());
            }
        } else if(v==btCheckin){
            if(cursor!=null){
                if(cursor.getCount()>0 && !tvNome.getText().toString().equals("")){
                    if(cursor.getString(cursor.getColumnIndex("validada")).toString().equals("true")){


                        ContentValues cv = new ContentValues();
                        cv.put("idinscricaoitem", cursor.getString(cursor.getColumnIndex("_ID")).toString());
                        SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
                        cv.put("data", data.format(new Date(System.currentTimeMillis())));
                        SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
                        cv.put("hora", hora.format(new Date(System.currentTimeMillis())));

                        Database db = new Database(ActCheckin.this);
                        db.open();
                        db.insert("checkin", cv);

                        Toast.makeText(ActCheckin.this, "Checkin realizado!",Toast.LENGTH_LONG).show();

                    } else {
                        Mensagem.exibeMessagem(ActCheckin.this, "event", "Inscrição Inativa");
                    }

                    tvSituacao.setText("");
                    tvCPF.setText("");
                    tvNome.setText("");
                    edtNumInscricao.setText("");
                    cursor = null;

                } else {
                    Mensagem.exibeMessagem(ActCheckin.this, "event", "Não há dados para checkin");
                }
            } else {
                Mensagem.exibeMessagem(ActCheckin.this, "event", "Não há dados para checkin");
            }
        }else if(v==btCancelar){
            Intent intent = new Intent(ActCheckin.this,
                    ActPrincipal.class);//AdministradorOpcoes.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                edtNumInscricao.setText(contents);
                mostraInformacoes(contents);
                Log.i("xZing", "contents: " + contents + " format: " + format); // Handle successful scan
            }
            else if(resultCode == RESULT_CANCELED){ // Handle cancel
                Log.i("xZing", "Cancelled");
            }
        }
    }
}
