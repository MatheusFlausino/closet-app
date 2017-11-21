package com.matheusflausino.closetapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.matheusflausino.closetapp.db.DatabaseHelper;
import com.matheusflausino.closetapp.model.Clothes;
import com.matheusflausino.closetapp.model.Type;
import com.matheusflausino.closetapp.repo.ClothesDAO;
import com.matheusflausino.closetapp.repo.TypeDAO;
import com.matheusflausino.closetapp.util.GridViewAdapter;
import com.matheusflausino.closetapp.util.UtilString;

import java.util.ArrayList;
import java.util.List;

import static com.matheusflausino.closetapp.util.UtilString.ALTERAR;
import static com.matheusflausino.closetapp.util.UtilString.ID;
import static com.matheusflausino.closetapp.util.UtilString.MODO;
import static com.matheusflausino.closetapp.util.UtilString.NOVO;

public class MainActivity extends AppCompatActivity {

    private ClothesDAO dbClothes;
    private TypeDAO dbTypes;

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    private TextView text;

    private Spinner textType;

    private ArrayList<Clothes> listItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Iniciação
        dbClothes = new ClothesDAO(this);
        dbTypes = new TypeDAO(this);

        text = (TextView) findViewById(R.id.textNoData);

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_items, listItems);
        gridView.setAdapter(gridAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(multiChoice);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Clothes c = (Clothes) parent.getItemAtPosition(position);
                editClothes(c);
            }
        });

        // Spinner TIPO
        textType = findViewById(R.id.spinnerType);

        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.all_list));
        for (Type l : dbTypes.findAll()){
            list.add(l.getType());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        textType.setAdapter(adapter);
        textType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String t = parent.getItemAtPosition(position).toString();
                if(t == getString(R.string.all_list)){
                    feedList(null);
                }else {
                    Type obj = dbTypes.findByName(t).get(0);
                    feedList(obj);
                }
                gridAdapter.notifyDataSetChanged();
                viewList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_clothes, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch(item.getItemId()){
            case R.id.infoClothesApp:
                intent = new Intent(this, AboutActivity.class);
                intent.putExtra(MODO, NOVO);
                startActivityForResult(intent, NOVO);

                return true;
            case R.id.addClothes:

                intent = new Intent(this, CaptureActivity.class);
                intent.putExtra(MODO, NOVO);
                startActivityForResult(intent, NOVO);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        feedList(null);
        gridAdapter.notifyDataSetChanged();
        viewList();
    }

    private AbsListView.MultiChoiceModeListener multiChoice = new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                boolean selecionado = gridView.isItemChecked(position);

                View view = gridView.getChildAt(position);

                if (selecionado) {
                    view.setBackgroundColor(Color.DKGRAY);
                    view.setPadding(8,8,8,8);
                } else {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    view.setPadding(0,0,0,0);
                }

                int totalSelecionados = gridView.getCheckedItemCount();

                if (totalSelecionados > 0) {

                    mode.setTitle(getResources().getQuantityString(R.plurals.selecionado,
                            totalSelecionados,
                            totalSelecionados));
                }

                mode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_multi_select, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                if (gridView.getCheckedItemCount() > 1) {
                    menu.getItem(0).setVisible(false);
                } else {
                    menu.getItem(0).setVisible(true);
                }

                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menuItemAlterar:
                        for (int posicao = gridView.getChildCount(); posicao >= 0; posicao--){
                            if (gridView.isItemChecked(posicao)){
                                editClothes(listItems.get(posicao));
                                break;
                            }
                        }
                        mode.finish();
                        return true;

                    case R.id.menuItemExcluir:
                        deleteClothes(mode);
                        return true;

                    case R.id.menuItemFavorite:

                        for (int posicao = gridView.getChildCount(); posicao >= 0; posicao--){
                            if (gridView.isItemChecked(posicao)){
                                setFavorite(posicao);
                                break;
                            }
                        }
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                for (int posicao = 0; posicao < gridView.getChildCount(); posicao++){
                    View view = gridView.getChildAt(posicao);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
            }

    };

    private void feedList(Object t){
        List<Clothes> lista;
        if(t == null) {
            textType.setSelection(0);
            lista = dbClothes.findAll();
        }else {
            lista = dbClothes.findByType(t);
        }
        listItems.clear();
        listItems.addAll(lista);
    }

    private void viewList(){
        if(listItems.isEmpty()) {
            gridView.setVisibility(View.INVISIBLE);
            text.setVisibility(View.VISIBLE);
        }else{
            text.setVisibility(View.INVISIBLE);
            gridView.setVisibility(View.VISIBLE);
        }
    }

    private void editClothes(Clothes c){

        //Clothes c = listItems.get(i);

        Intent intent = new Intent(this, CaptureActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID, c.getId());

        startActivityForResult(intent, ALTERAR);
    }

    private void deleteClothes(final ActionMode mode){
        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                for (int pos = gridView.getChildCount(); pos >= 0; pos--){
                                    if (gridView.isItemChecked(pos)){
                                        dbClothes.delete(listItems.get(pos));
                                        listItems.remove(pos);
                                    }
                                }

                                gridAdapter.notifyDataSetChanged();
                                viewList();

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }

                        mode.finish();
                    }
                };

        UtilString.alertDialog(this, getString(R.string.message_alert), listener);
    }

    private void setFavorite(int i){
        Clothes c = listItems.get(i);
        c.setFavorite(1);
        dbClothes.update(c);
    }

}
