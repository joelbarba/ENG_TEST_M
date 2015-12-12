package barba.joel.eng_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    final Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // startActivity(new Intent(context, PlayActivity.class));


        Button button_list1 = (Button) findViewById(R.id.button_list1);
        Button button_list2 = (Button) findViewById(R.id.button_list2);
        Button button_list3 = (Button) findViewById(R.id.button_list3);


        button_list1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PlayActivity.class);
                Bundle b = new Bundle();
                b.putString("ID_LLISTA", "1");
                i.putExtras(b);
                startActivity(i);
            }
        });

        button_list2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PlayActivity.class);
                Bundle b = new Bundle();
                b.putString("ID_LLISTA", "2");
                i.putExtras(b);
                startActivity(i);
            }
        });

        button_list3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PlayActivity.class);
                Bundle b = new Bundle();
                b.putString("ID_LLISTA", "3");
                i.putExtras(b);
                startActivity(i);
            }
        });

    }

}
