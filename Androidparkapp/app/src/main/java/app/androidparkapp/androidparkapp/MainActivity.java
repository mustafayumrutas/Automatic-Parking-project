package app.androidparkapp.androidparkapp;

import android.content.DialogInterface;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";

    ListView listView;
    EditText kartNoEdt;
    Button sorgulaBtn;

    ArrayList<OtoparkUcretler> arrayList;
    String id;

    DatabaseReference myRef;
    DatabaseReference myRef1;
    DatabaseReference updateRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.listView);
        kartNoEdt=(EditText)findViewById(R.id.edtKartNo);
        sorgulaBtn=(Button)findViewById(R.id.btnSorgula);


        sorgulaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listele();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int indis, long l) {

                final OtoparkUcretler otoparkUcret=arrayList.get(indis);
                final String girisX=otoparkUcret.getGiris();
                final String cikisY=otoparkUcret.getCikis();



                Toast.makeText(getApplicationContext(),indis+". eleman",Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Borç Ödeme");
                builder.setMessage(otoparkUcret.getBorc()+" TL olan borcunuzu ödemek için 'Öde' butonuna basınız."+otoparkUcret.getUcretId());
                builder.setCancelable(false);
                builder.setPositiveButton("Öde", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //          ödeme işlemleri YAPILACAK. indis listview den seçilen item nosu
                        //indis değerine göre seçilen elemanı silecek. indis 0 dan başlıyor.

                        updateDurum(otoparkUcret.getUcretId(),false,girisX,cikisY);
                        Listele();

                    }
                });
                builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();


                return false;
            }
        });




    }

    public void Listele()
    {
        myRef=FirebaseDatabase.getInstance().getReference("logs");

        arrayList=new ArrayList<>();

        myRef1=myRef.child(kartNoEdt.getText().toString());
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {
                    TimeUnit.SECONDS.sleep(3);
                    arrayList.clear();
                    for (DataSnapshot otoparkSnapshot : dataSnapshot.getChildren()) {
                        OtoparkUcretler otoparkUcretler = otoparkSnapshot.getValue(OtoparkUcretler.class);
                        id = otoparkSnapshot.getKey();
                        otoparkUcretler.setUcretId(id);
                        arrayList.add(otoparkUcretler);
                    }
                    OtoparkList adapter = new OtoparkList(MainActivity.this, arrayList);
                    listView.setAdapter(adapter);


                }catch(Exception e){

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }

    public boolean updateDurum(String id, boolean Durum, String giris,String cikis){
        updateRef=myRef1.child(id).child("Durum");



        updateRef.setValue(Durum);
        Toast.makeText(getApplicationContext(),"Borcunuz ödenmiştir!",Toast.LENGTH_SHORT).show();

        return true;
    }





}
