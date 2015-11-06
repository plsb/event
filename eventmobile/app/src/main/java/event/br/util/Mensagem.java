package event.br.util;

import android.app.Activity;
import android.app.AlertDialog;

public class Mensagem {

    private Activity _act;

    private static Mensagem msg;


    private void setAcvitivy(Activity act){
        this._act = act;
    }

    public static void exibeMessagem(Activity act, String Titulo,String Texto)
    {
        verificaInstacia();
        msg.setAcvitivy(act);
        AlertDialog.Builder mensagem = new AlertDialog.Builder(act);
        mensagem.setTitle(Titulo);
        mensagem.setMessage(Texto);
        mensagem.setNeutralButton("OK",null);
        mensagem.show();
    }

    private static void verificaInstacia(){
        if(msg==null){
            msg = new Mensagem();
        }
    }

}