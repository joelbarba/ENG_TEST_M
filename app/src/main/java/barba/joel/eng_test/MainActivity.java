package barba.joel.eng_test;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DBManager DB_ET = null;
    final Context context = this;
    private static final String LOGTAG = "MainActivity";
    private Handler mHandler = new Handler();

    private C_Word_Ask word_ask;




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
    protected void onDestroy() {
        super.onDestroy();
        DB_ET.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


    }

    private void carregarParaula() {

        word_ask = DB_ET.get_word();

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
                    p.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOK));
                    // mostrar_avis("OK, correcte");

                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            carregarParaula();
                        }
                    }, 300);

                } else {
                    p.setBackgroundColor(ContextCompat.getColor(context, R.color.colorKO));
                }

            }
        });

    }



    private void mostrar_avis(String text){
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
