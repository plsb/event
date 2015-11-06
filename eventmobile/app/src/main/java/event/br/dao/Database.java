package event.br.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database {

    private static SQLiteDatabase bd;
    private static Context cx;
    private DatabaseHelper dbHelper;

    public Database(Context cx){
        this.cx = cx;
    }

    public Database open(){
        dbHelper = new DatabaseHelper(cx);
        bd = dbHelper.getWritableDatabase();
        return this;
    }

    public void fechaBanco(){
        dbHelper.close();
    }

    public long insert(String tabela, ContentValues valores) {
        return bd.insert(tabela, null, valores);

    }

    public long update(String tabela, ContentValues valores, String clausulaWhere) {
        return bd.update(tabela, valores, clausulaWhere, null);

    }

    public boolean execSql(String Sql){
        try{
            open();
            bd.execSQL(Sql);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public Cursor consult(String table, String[] columns, String where,
                          String[] argumentosWhere, String groupby, String having,
                          String orderby, String limit) {
        return bd.query(table, columns, where, argumentosWhere, groupby,
                having, orderby, limit);
    }

    //classe DatabaseHelper

    private class DatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 9;
        private static final String DATABASE_NAME = "event";

        private static final String TB_EVENTO = "CREATE TABLE if not exists evento " +
                "(_ID integer PRIMARY KEY autoincrement NOT NULL,"
                + "descricao text NOT NULL,dataInicio text NOT NULL,dataFim text NOT NULL," +
                "local text NOT NULL, dono TEXT);";

        private static final String TB_USUARIO = "CREATE TABLE if not exists usuario " +
                "(_ID integer PRIMARY KEY autoincrement NOT NULL,"
                + "nome text NOT NULL,login text NOT NULL,senha text NOT NULL);";

        private static final String TB_EVENTOS_ITENS = "CREATE TABLE if not exists itensevento " +
                "(_ID integer PRIMARY KEY autoincrement NOT NULL,"
                + "descricao text NOT NULL,data text NOT NULL,hora text NOT NULL);";

        private static final String TB_INSCRIOES = "CREATE TABLE if not exists inscricoes " +
                "(_ID string PRIMARY KEY NOT NULL,"
                + "validada text NOT NULL, idItemEvento integer,descricaoItemEvento text NOT NULL,nomeEvento text NOT NULL," +
                "nomeUsuario text NOT NULL, cpfUsuario text NOT NULL, idInscricao String);";

        private static final String TB_CHECKIN = "CREATE TABLE if not exists checkin " +
                "(_ID integer PRIMARY KEY autoincrement NOT NULL,"
                + "idinscricaoitem int NOT NULL, data text,hora text);";



        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TB_EVENTO);
            db.execSQL(TB_USUARIO);
            db.execSQL(TB_EVENTOS_ITENS);
            db.execSQL(TB_INSCRIOES);
            db.execSQL(TB_CHECKIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase bd, int versaoAntiga, int versaoNova) {
            bd.execSQL("DROP TABLE IF EXISTS evento");
            bd.execSQL("DROP TABLE IF EXISTS usuario");
            bd.execSQL("DROP TABLE IF EXISTS itensevento");
            bd.execSQL("DROP TABLE IF EXISTS inscricoes");
            bd.execSQL("DROP TABLE IF EXISTS checkin");
            onCreate(bd);
        }

    }
}
