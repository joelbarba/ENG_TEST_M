package barba.joel.eng_test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DB_ENG_TEST";
    private static final int DATABASE_VERSION = 28;
    private C_Load_Data Carregador = new C_Load_Data();


    String sent_create_all_words = "create table ALL_WORDS (" +
            "id_word      integer primary key autoincrement," +
            "id_list      integer," +
            "word         text," +
            "kind         text," +
            "translation  text," +
            "example      text," +
            "attempt      integer," +
            "ok           integer," +
            "lok          integer," +
            "skip         integer," +
            "sco          integer)";

    String sent_create_index_word = "create index INDEX_WORD on ALL_WORDS (id_word, id_list)";

    public DataBaseSQLiteHelper(Context contexto) {
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sent_create_all_words);
        db.execSQL(sent_create_index_word);
        iniciar_DB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("drop table if exists ALL_WORDS");
        db.execSQL(sent_create_all_words);
        db.execSQL(sent_create_index_word);
        iniciar_DB(db);
    }

    public void iniciar_DB(SQLiteDatabase db) {
        db.execSQL("delete from all_words");
        Carregador.load_db1(db);
        Carregador.load_db2(db);
        Carregador.load_db3(db);
    }
}