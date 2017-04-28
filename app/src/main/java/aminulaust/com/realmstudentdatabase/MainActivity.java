package aminulaust.com.realmstudentdatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import aminulaust.com.realmstudentdatabase.model.Student;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    EditText sname, sid, sage;
    Button save, show;
    TextView show_detalis;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm=Realm.getDefaultInstance();

        sname=(EditText)findViewById(R.id.sname);
        sid=(EditText)findViewById(R.id.sid);
        sage=(EditText)findViewById(R.id.sage);
        save=(Button) findViewById(R.id.save);
        show=(Button)findViewById(R.id.show);
       show_detalis=(TextView) findViewById(R.id.showtxt);

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveToDatabase(sname.getText().toString().trim(),Integer.parseInt(sid.getText().toString().trim()),
                        Integer.parseInt(sage.getText().toString().trim()));
                Toast.makeText(getApplication(),"Data saved successfully ", Toast.LENGTH_SHORT).show();
            }

        });
        show.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                ShowDataBase();
                Toast.makeText(getApplication(),"Open listlive ", Toast.LENGTH_SHORT).show();

            }

        });

    }
    public void ShowDataBase(){
        RealmResults<Student> guests = realm.where(Student.class).findAll();
      //  String studet_details="";
        String s_name="";
        String s_id= "";
        String s_age= "";

        realm.beginTransaction();
        for (Student guest : guests) {
            s_name+=guest.getName();
            s_id+=guest.getId();
            s_age+=guest.getAge();
           // studet_details+=guest.toString();
        }
        //show_detalis.setText(studet_details);
        show_detalis.setText("Name:" +s_name+"ID:"+s_id+"Age:"+s_age);


    }
    public void saveToDatabase(final String sname, final int sid , final int sage){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Student user = bgRealm.createObject(Student.class);
                user.setName(sname);
                user.setId(sid);
                user.setAge(sage);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.v("Databse","Data Inserted");
                // Transaction was a success.
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("DataBase", error.getMessage());
            }
        });

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();

    }

}
