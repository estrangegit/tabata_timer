package mi1.projet.tabata;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mi1.projet.MainActivity;
import mi1.projet.R;
import mi1.projet.data.Tabata;
import mi1.projet.data.TabataDAO;
import mi1.projet.utilities.TimeUtilities;

public class TabataListActivity extends AppCompatActivity {

    public static final String TABATA = "TABATA";
    public static final String ADD = "ADD";
    public static final String UPDATE = "UPDATE";

    TabataDAO tabataDAO;

    private EditText tabataNameET;

    private ListView vue;

    private Button addButton, deleteButton, updateButton, selectButton;

    private ArrayList<Tabata> listAll;
    private String dbContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata_list);

        tabataNameET = (EditText) findViewById(R.id.act_tabata_list_name_et);

        addButton = (Button) findViewById(R.id.act_tabata_list_add_button);
        deleteButton = (Button) findViewById(R.id.act_tabata_list_delete_button);
        updateButton = (Button) findViewById(R.id.act_tabata_list_update_button);
        selectButton = (Button) findViewById(R.id.act_tabata_list_select_button);

        vue = (ListView) findViewById(R.id.act_tabata_list_listView);

        AlertDialog.Builder builder_name_not_stored = new AlertDialog.Builder(this);
        builder_name_not_stored.setMessage(R.string.dialog_name_not_stored)
                .setTitle(R.string.dialog_title_error);
        final AlertDialog dialog_name_not_stored = builder_name_not_stored.create();

        AlertDialog.Builder builder_name_empty = new AlertDialog.Builder(this);
        builder_name_empty.setMessage(R.string.dialog_name_empty)
                .setTitle(R.string.dialog_title_error);
        final AlertDialog dialog_name_empty = builder_name_empty.create();


        tabataDAO = new TabataDAO();

        updateContent();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //création de l'intent
                Intent tabataEditActivity = new Intent(TabataListActivity.this, TabataEditActivity.class);
                tabataEditActivity.putExtra(TabataListActivity.ADD, true);
                tabataEditActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //Lancement de l'activité
                startActivity(tabataEditActivity);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = tabataNameET.getText().toString();

                if (name != null && !name.trim().isEmpty()) {

                    Tabata tabataTest = tabataDAO.findFirstTabataByName(name);

                    if (tabataTest != null) {
                        //création de l'intent
                        Intent tabataEditActivity = new Intent(TabataListActivity.this, TabataEditActivity.class);

                        tabataEditActivity.putExtra(TabataListActivity.UPDATE, true);
                        tabataEditActivity.putExtra(TabataListActivity.TABATA, (Parcelable) tabataTest);

                        tabataEditActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        //Lancement de l'activité
                        startActivity(tabataEditActivity);
                    } else {
                        dialog_name_not_stored.show();
                    }
                }
                else
                {
                    dialog_name_empty.show();
                }
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tabataNameET.getText().toString();

                if (name != null && !name.trim().isEmpty()) {

                    Tabata tabataTest = tabataDAO.findFirstTabataByName(name);

                    if (tabataTest != null) {
                        //création de l'intent
                        Intent mainActivity = new Intent(TabataListActivity.this, MainActivity.class);

                        mainActivity.putExtra(TabataListActivity.TABATA, (Parcelable) tabataTest);

                        mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        //Lancement de l'activité
                        startActivity(mainActivity);
                    } else {
                        dialog_name_not_stored.show();
                    }
                }
                else
                {
                    dialog_name_empty.show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tabataNameET.getText().toString();

                if (name != null && !name.trim().isEmpty()) {


                    Tabata tabataTest = tabataDAO.findFirstTabataByName(name);

                    if (tabataTest != null) {
                        //suppression du tabata de nom name
                        tabataDAO.deleteByName(name);

                        //mise à jour de l'affichage
                        updateContent();
                    } else {
                        dialog_name_not_stored.show();
                    }
                }
                else
                {
                    dialog_name_empty.show();
                }
            }
        });

        vue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = (HashMap<String, String>) vue.getItemAtPosition(position);

                String tabataName = map.get("Name");

                Tabata tabata = tabataDAO.findFirstTabataByName(tabataName);

                //création de l'intent
                Intent mainActivity = new Intent(TabataListActivity.this, MainActivity.class);

                mainActivity.putExtra(TabataListActivity.TABATA, (Parcelable) tabata);

                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //Lancement de l'activité
                startActivity(mainActivity);
            }
        });
    }

    //FONCTION AFFICHANT LE CONTENU DE LA BASE DE DONNEES
    private void updateContent() {

        listAll = (ArrayList<Tabata>) Tabata.listAll(Tabata.class);

        List<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> element;

        for (Tabata tabata : listAll) {
            element = new HashMap<String, String>();
            element.put("Name", tabata.getName());
            element.put("Prepare", "Prepare : " + TimeUtilities.displayTimeSec(tabata.getPrepare()));
            element.put("Work", "Work : " + TimeUtilities.displayTimeSec(tabata.getWork()));
            element.put("Rest", "Rest : " + TimeUtilities.displayTimeSec(tabata.getRest()));
            element.put("Cycle", "Cycle(s) : " + String.valueOf(tabata.getCycles()));
            element.put("NbTabata", "Tabata(s) : " + String.valueOf(tabata.getNbTabata()));
            element.put("RestTabata", "Rest between tabatas: " + TimeUtilities.displayTimeSec(tabata.getRestTabata()));
            element.put("CoolDown", "Cool Down : " + TimeUtilities.displayTimeSec(tabata.getCoolDown()));

            liste.add(element);
        }

        SimpleAdapter adapter = new SimpleAdapter(this.getBaseContext(),
                liste,
                R.layout.display_item,
                new String[]{"Name", "Prepare", "Work", "Rest", "Cycle", "NbTabata", "RestTabata", "CoolDown"},
                new int[]{R.id.display_item_name, R.id.display_item_prepare, R.id.display_item_work,
                        R.id.display_item_rest, R.id.display_item_cycle, R.id.display_item_nb_tabata, R.id.display_item_rest_tabata, R.id.display_item_cool_down}
        );

        vue.setAdapter(adapter);
    }

}
