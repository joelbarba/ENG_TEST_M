package barba.joel.eng_test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DBManager {

    private DataBaseSQLiteHelper mHelper = null;
    private SQLiteDatabase db = null;

    public DBManager(final Context context) {
        this.mHelper = new DataBaseSQLiteHelper(context);
    }

    // Obre la base de dades i retorna la instància
    public DBManager open() {
        db = this.mHelper.getWritableDatabase();
        return this;
    }

    // Tanca la base de dades i allibera els recursos
    public void close() {
        if(db != null) { db.close(); }
    }

    public void ini_db(boolean reload) {
        // if (reload) iniciar_DB();
    }


    // Retorna paraula aleatoria.
    // 0 => word
    // 1 => kind
    // 2 => translation ok
    // 3-11 => options to choose
    public C_Word_Ask get_word() {

        C_Word_Ask word_ask = new C_Word_Ask();


        Cursor F_cursor = db.rawQuery(
                "select word, " +
                "       kind, " +
                "       substr(translation, 1, instr(translation || ',', ',') - 1) as first_trans, " +
                "       id_word, " +
                "       (select abs(random() % 9)) select_pos " +
                "  from (select abs(random() % 100000) as rand_index, t1.* from ALL_WORDS t1 where id_list = 2 " +
                "           and kind = (select case (abs(random() % 3)) when 0 then 'noun' when 1 then 'verb' when 2 then 'adjective' end)) " +
                " order by ifnull(sco, 0), rand_index limit 9 "
                , null);


        if (F_cursor.moveToFirst()) {
            int t = 0;
            word_ask.correct_pos_answer = F_cursor.getInt(4);
            do {
                word_ask.answers[t] = F_cursor.getString(2);

                if (word_ask.correct_pos_answer == t) {    // Resposta correcte
                    word_ask.word = F_cursor.getString(0);
                    word_ask.kind = F_cursor.getString(1);
                    word_ask.translation = F_cursor.getString(2);
                    word_ask.id_word = F_cursor.getInt(3);
                }
                t++;

            } while (F_cursor.moveToNext() && t <= 9);
        }

        F_cursor.close();

        return word_ask;
    }


    // Guarda la resposta a la paraula actual
    public void set_word_answ(C_Word_Ask curr_word, boolean answ_ok) {

        db.execSQL("update all_WORDS set attempt = ifnull(attempt,0) + 1  where id_list = 2 and id_word = " + String.valueOf(curr_word.id_word));
        if (answ_ok) {
            db.execSQL("update all_WORDS set ok = ifnull(ok,0) + 1  where id_list = 2 and id_word = " + String.valueOf(curr_word.id_word));
        }
        db.execSQL("update all_WORDS set sco = ifnull(attempt,0) + ifnull(ok,0)  where id_list = 2 and id_word = " + String.valueOf(curr_word.id_word));

    }
}