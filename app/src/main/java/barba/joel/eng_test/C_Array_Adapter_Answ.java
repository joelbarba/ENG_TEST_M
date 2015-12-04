package barba.joel.eng_test;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by barba on 4/12/15.
 */
public class C_Array_Adapter_Answ extends ArrayAdapter {

    Activity context;
    String[] llista_answers;

    C_Array_Adapter_Answ(Activity context, String[] llista) {
        super(context, R.layout.item_answer, llista);
        this.context = context;
        this.llista_answers = llista;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.item_answer, null);

        TextView paraula_resposta = (TextView) item.findViewById(R.id.paraula_resposta);
        paraula_resposta.setText(llista_answers[position]);

        return(item);
    }
}
