package barba.joel.eng_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PlayActivity extends AppCompatActivity {

    private DBManager DB_ET = null;
    final Context context = this;
    private static final String LOGTAG = "MainActivity";
    private Handler mHandler = new Handler();
    private String id_llista = "";

    private C_Word_Ask word_ask;
    private boolean curr_answ = true;

    public class C_Array_Adapter_Answ2 extends ArrayAdapter {

        Activity context;
        String[] llista_answers;

        C_Array_Adapter_Answ2(Activity context) {
            super(context, R.layout.item_answer, word_ask.answers);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.item_answer, null);

            TextView paraula_resposta = (TextView) item.findViewById(R.id.paraula_resposta);
            paraula_resposta.setText(word_ask.answers[position]);

            return(item);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DB_ET.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        final Button button_mark = (Button) findViewById(R.id.button_mark);

        // Configurar menu superior
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    // Pantalla de configuració
                    case R.id.action_settings:
                        mostrar_avis("de moment no hi ha settings");
                        break;
                    case R.id.menu_fer_backup_db:
                        try {
                            backup_db_file();
                        } catch (Exception e) {
                        }
                        break;
                    case R.id.menu_mode_edit:
                        button_mark.setVisibility(View.VISIBLE);
                        break;
                }
                return true;
            }
        });

        Bundle b = this.getIntent().getExtras();
        this.id_llista = b.getString("ID_LLISTA");
        mostrar_avis("id llista = " + id_llista);


        // Iniciar DB
        DB_ET = new DBManager(getApplicationContext()); // Crear l'interface amb la DB
        DB_ET.open();

        DB_ET.ini_db(true);

        carregarParaula();



        // final Button button_next = (Button) findViewById(R.id.button_next);
        TextView button_next = (TextView) findViewById (R.id.label_word);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarParaula();
            }
        });


        button_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB_ET.set_word_mark(word_ask, id_llista);
                mostrar_avis("Paraula " + word_ask.word + " marcada (lok=1)");
                carregarParaula();

            }
        });

    }



    private void carregarParaula() {

        word_ask = DB_ET.get_word(id_llista);
        curr_answ = true;

        TextView label_word = (TextView) findViewById (R.id.label_word);
        // label_word.setText(String.valueOf(word_ask.correct_pos_answer) + ", " + word_ask.word);
        label_word.setText(word_ask.word);

        String show_kind = "";
        TextView text_kind = (TextView) findViewById (R.id.text_kind);
        show_kind = word_ask.kind;
        if (word_ask.kind == "N") show_kind = "Noun";
        if (word_ask.kind == "V") show_kind = "Verb";
        text_kind.setText(show_kind);

        C_Array_Adapter_Answ2 adp = new C_Array_Adapter_Answ2(this);
        final GridView grd1 = (GridView)findViewById(R.id.taula1);
        grd1.setAdapter(adp);
/*
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, word_ask.answers);
        final GridView grd1 = (GridView)findViewById(R.id.taula1);
        grd1.setAdapter(adapt);
*/
        grd1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView p = (TextView) view.findViewById(R.id.paraula_resposta);

                if (position == word_ask.correct_pos_answer) {
                    if (curr_answ) DB_ET.set_word_answ(word_ask, true, id_llista);
                    p.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOK));
                    // mostrar_avis("OK, correcte");

                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            carregarParaula();
                        }
                    }, 300);

                } else {
                    p.setBackgroundColor(ContextCompat.getColor(context, R.color.colorKO));
                    if (curr_answ) DB_ET.set_word_answ(word_ask, false, id_llista);
                    curr_answ = false;
                }

            }
        });

    }


    private void backup_db_file() {

        mostrar_avis("exportant DB");

        File f = new File("/data/data/barba.joel.eng_test/databases/DB_ENG_TEST");
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {

            fis = new FileInputStream(f);
            fos = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "db_dump_ENG_TEST.db");

            while (true) {
                int i = fis.read();
                if (i!=-1) { fos.write(i); }
                else { break; }
            }
            fos.flush();
            mostrar_avis("OK: " + Environment.getExternalStorageDirectory() + File.separator + "db_dump_ENG_TEST.db");

        } catch(Exception e) {
            e.printStackTrace();
            mostrar_avis("DB dump ERROR");

        } finally {
            try {
                fos.close();
                fis.close();
            } catch(IOException ioe) {}
        }

    }



    private void mostrar_avis(String text){
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
