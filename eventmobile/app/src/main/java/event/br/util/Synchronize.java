package event.br.util;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import event.br.dao.Database;
import event.br.eventmobile.MainActivity;


/**
 * Created by Pedro Saraiva on 05/10/2015.
 */
public class Synchronize {

    private static final Handler handler=new Handler();

    private static ProgressDialog dialog;
    private static String nameSpace = "http://ws.edu.br/";
    private static String url = "http://pedrosaraiva.acessotemporario.net/event/webservice/eventWS?wsdl";

    public static void synchronizeEvento(final String codevento, final Context act){
        dialog = ProgressDialog.show(act, "event", "Aguarde Sincronizando...", true);
        //sincroniza a cidade
        new Thread(new Runnable() {
            @Override
            public void run() {
                SoapObject soap = new SoapObject(nameSpace, "listaEventos");
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soap.addProperty("arg0", codevento);

                envelope.setOutputSoapObject(soap);

                HttpTransportSE httpTransport = new HttpTransportSE(url);
                Object msg = null;
                try {
                    httpTransport.call(nameSpace+"listaEventos", envelope);
                    msg = envelope.getResponse();

                } catch (Exception e) {
//                    dialog.dismiss();
                    Log.i("PEDRO", e.getMessage());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(act, "Verifique sua conexão!", Toast.LENGTH_LONG).show();
                        }
                    });

                    System.out.println(e.toString());
                }

                if (msg == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(act, "Evento não encontrado!", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Database db = new Database(act);
                    db.open();
                    db.execSql("delete from evento");
                    SoapObject response = (SoapObject) envelope.bodyIn;
                    for (int i = 0; i < response.getPropertyCount(); i++) {
                        SoapObject soapObject = (SoapObject) response.getProperty(i);
                        SoapObject soDono = (SoapObject) soapObject.getProperty("dono");
                        SoapObject soLocal = (SoapObject) soapObject.getProperty("local");

                        ContentValues cv = new ContentValues();
                        cv.put("_ID", Integer.parseInt(soapObject.getProperty("id").toString()));
                        cv.put("descricao", soapObject.getProperty("descricao").toString());
                        cv.put("dataInicio", soapObject.getProperty("dataInicio").toString());
                        cv.put("dataFim", soapObject.getProperty("dataFim").toString());
                        cv.put("local", soLocal.getProperty("descricao").toString());
                        cv.put("dono", soDono.getProperty("name").toString());
                        db.insert("evento", cv);

                    }
                    //fim do preenche cidade
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(act, "Evento Sincronizado!", Toast.LENGTH_LONG).show();
                        }
                    });

                }
                synchronizeUSuario(codevento,act);
            }


        }).start();
    }

    public static void synchronizeUSuario(final String codevento, final Context act){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SoapObject soap = new SoapObject(nameSpace, "listaUsuariosEvento");
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soap.addProperty("arg0", codevento);

                envelope.setOutputSoapObject(soap);

                HttpTransportSE httpTransport = new HttpTransportSE(url);
                Object msg = null;
                try {
                    httpTransport.call(nameSpace+"listaUsuariosEvento", envelope);
                    msg = envelope.getResponse();

                } catch (Exception e) {
//                    dialog.dismiss();
                    Log.i("PEDRO", e.getMessage());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(act, "Verifique sua conexão!", Toast.LENGTH_LONG).show();
                        }
                    });

                    System.out.println(e.toString());
                }

                if (msg == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(act, "Usuários não encontrado!", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Database db = new Database(act);
                    db.open();
                    db.execSql("delete from usuario");
                    SoapObject response = (SoapObject) envelope.bodyIn;
                    for (int i = 0; i < response.getPropertyCount(); i++) {
                        SoapObject soapObject = (SoapObject) response.getProperty(i);

                        ContentValues cv = new ContentValues();
                        cv.put("_ID", Integer.parseInt(soapObject.getProperty("id").toString()));
                        cv.put("nome", soapObject.getProperty("nome").toString());
                        cv.put("login", soapObject.getProperty("login").toString());
                        cv.put("senha", soapObject.getProperty("senha").toString());

                        db.insert("usuario", cv);

                    }
                    //fim do preenche cidade
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(act, "Usuários Sincronizados!", Toast.LENGTH_LONG).show();
                        }
                    });

                }
                synchronizeItens(codevento, act);
            }

        }).start();
    }

    public static void synchronizeItens(final String codevento, final Context act){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SoapObject soap = new SoapObject(nameSpace, "listaEventosItens");
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soap.addProperty("arg0", codevento);

                envelope.setOutputSoapObject(soap);

                HttpTransportSE httpTransport = new HttpTransportSE(url);
                Object msg = null;
                try {
                    httpTransport.call(nameSpace+"listaEventosItens", envelope);
                    msg = envelope.getResponse();

                } catch (Exception e) {
//                    dialog.dismiss();
                    Log.i("PEDRO", e.getMessage());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(act, "Verifique sua conexão!", Toast.LENGTH_LONG).show();
                        }
                    });

                    System.out.println(e.toString());
                }

                if (msg == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(act, "Itens não encontrado!", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Database db = new Database(act);
                    db.open();
                    db.execSql("delete from itensevento");
                    SoapObject response = (SoapObject) envelope.bodyIn;
                    for (int i = 0; i < response.getPropertyCount(); i++) {
                        SoapObject soapObject = (SoapObject) response.getProperty(i);

                        ContentValues cv = new ContentValues();
                        cv.put("_ID", Integer.parseInt(soapObject.getProperty("id").toString()));
                        cv.put("descricao", soapObject.getProperty("descricao").toString());
                        cv.put("data", soapObject.getProperty("data").toString());
                        cv.put("hora", soapObject.getProperty("hora").toString());

                        db.insert("itensevento", cv);

                    }
                    //fim do preenche cidade
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(act, "Itens de Evento Sincronizados!", Toast.LENGTH_LONG).show();
                        }
                    });

                }
                synchronizeInscricoes(codevento, act);
            }

        }).start();
    }

    public static void synchronizeInscricoes(final String codevento, final Context act){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SoapObject soap = new SoapObject(nameSpace, "listaEventoInscricaoItens");
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                soap.addProperty("arg0", codevento);

                envelope.setOutputSoapObject(soap);

                HttpTransportSE httpTransport = new HttpTransportSE(url);
                Object msg = null;
                try {
                    httpTransport.call(nameSpace+"listaEventoInscricaoItens", envelope);
                    msg = envelope.getResponse();

                } catch (Exception e) {
//                    dialog.dismiss();
                    Log.i("PEDRO", e.getMessage());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(act, "Verifique sua conexão!", Toast.LENGTH_LONG).show();
                        }
                    });

                    System.out.println(e.toString());
                }

                if (msg == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(act, "Inscrições não encontradadas!", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Database db = new Database(act);
                    db.open();
                    db.execSql("delete from inscricoes");
                    SoapObject response = (SoapObject) envelope.bodyIn;
                    for (int i = 0; i < response.getPropertyCount(); i++) {
                        SoapObject soapObject = (SoapObject) response.getProperty(i);
                        SoapObject soItemevento = (SoapObject) soapObject.getProperty("eventoItens");
                        SoapObject soItemInscricao = (SoapObject) soapObject.getProperty("eventoInscricao");
                        SoapObject soEvento = (SoapObject) soItemInscricao.getProperty("evento");
                        SoapObject soUsuario = (SoapObject) soItemInscricao.getProperty("usuario");


                        ContentValues cv = new ContentValues();
                        cv.put("_ID", Integer.parseInt(soapObject.getProperty("id").toString()));
                        cv.put("idInscricao", soItemInscricao.getProperty("id").toString());
                        cv.put("validada", soapObject.getProperty("validada").toString());
                        cv.put("idItemEvento", soItemevento.getProperty("id").toString());
                        cv.put("descricaoItemEvento", soItemevento.getProperty("descricao").toString());
                        cv.put("nomeEvento", soEvento.getProperty("descricao").toString());
                        cv.put("nomeUsuario", soUsuario.getProperty("name").toString());
                        cv.put("cpfUsuario", soUsuario.getProperty("cpf").toString());

                        db.insert("inscricoes", cv);

                    }
                    //fim do preenche cidade
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(act, "Inscrições Sincronizados!", Toast.LENGTH_LONG).show();
                        }
                    });

                }
                synchronizeCheckin(codevento,act);
            }

        }).start();
    }

    public static void synchronizeCheckin(final String codevento, final Context act){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Database db = new Database(act);
                db.open();
                Cursor cursor = db.consult("checkin", new String[]{"*"},
                        null, null, null, null,
                        null, null);

                if(cursor.getCount()>0){
                    cursor.moveToFirst();
                    SoapObject soap = new SoapObject(nameSpace, "insereCheckin");
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);

                    while (cursor.isAfterLast() == false) {
                        soap.addProperty("iditeminscricao", cursor.getString(cursor.getColumnIndex("idinscricaoitem")).toString());
                        soap.addProperty("data", cursor.getString(cursor.getColumnIndex("data")).toString());
                        soap.addProperty("hora", cursor.getString(cursor.getColumnIndex("hora")).toString());

//                        soap.addProperty("arg0", codevento);

                        envelope.setOutputSoapObject(soap);

                        HttpTransportSE httpTransport = new HttpTransportSE(url);
                        Object msg = null;
                        try {
                            httpTransport.call(nameSpace+"insereCheckin", envelope);
                            msg = envelope.getResponse();


                        } catch (Exception e) {
//                    dialog.dismiss();
                            Log.i("PEDRO", e.getMessage());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(act, "Verifique sua conexão!", Toast.LENGTH_LONG).show();
                                }
                            });

                            System.out.println(e.toString());
                            dialog.dismiss();
                            Intent i = new Intent(act, MainActivity.class);
                            act.startActivity(i);
                            return ;
                        }
                        cursor.moveToNext();
                    }
                    db.execSql("delete from checkin");
                }
                   //fim do preenche cidade
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(act, "Checkin Sincronizados!", Toast.LENGTH_LONG).show();
                        }
                    });


                dialog.dismiss();
                Intent i = new Intent(act, MainActivity.class);
                act.startActivity(i);
            }

        }).start();
    }


}
