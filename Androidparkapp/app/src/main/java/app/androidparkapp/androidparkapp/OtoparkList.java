package app.androidparkapp.androidparkapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Mustafaspc on 11-Mar-18.
 */

public class OtoparkList extends ArrayAdapter<OtoparkUcretler> {

    private Activity context;
    private List<OtoparkUcretler> otoparkList;

    public OtoparkList(Activity context, List<OtoparkUcretler> otoparkList){
        super(context, R.layout.list_view_layout, otoparkList);
        this.context=context;
        this.otoparkList=otoparkList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();

        View listViewItem=inflater.inflate(R.layout.list_view_layout,null, true);

        TextView textViewDurum=(TextView)listViewItem.findViewById(R.id.textViewDurum);
        TextView textViewGiris=(TextView)listViewItem.findViewById(R.id.textViewGiris);
        TextView textViewCikis=(TextView)listViewItem.findViewById(R.id.textViewCikis);
        TextView textViewBorc=(TextView)listViewItem.findViewById(R.id.textViewBorc);

        OtoparkUcretler otoparkUcretler=otoparkList.get(position);



        if(otoparkUcretler.isDurum()==true){
            textViewDurum.setText("Durum        : Ödenmedi!");
        }else{
            textViewDurum.setText("Durum        : Ödendi.");
        }

        textViewGiris.setText("Giriş Tarihi  :"+otoparkUcretler.getGiris());
        textViewCikis.setText("Çıkış Tarihi  :"+otoparkUcretler.getCikis());
        try{
            textViewBorc.setText("Borç           :"+otoparkUcretler.borcHesapla(otoparkUcretler.getGiris(),otoparkUcretler.getCikis())+" TL");
        }catch(ParseException e){

        }

        return listViewItem;
    }
}
