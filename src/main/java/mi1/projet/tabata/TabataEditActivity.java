package mi1.projet.tabata;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.Field;

import mi1.projet.MainActivity;
import mi1.projet.R;
import mi1.projet.data.Tabata;
import mi1.projet.data.TabataDAO;
import mi1.projet.model.TabataModel;
import mi1.projet.utilities.NumberPickerUtilities;
import mi1.projet.utilities.RepeatListener;
import mi1.projet.utilities.TimeUtilities;

public class TabataEditActivity extends AppCompatActivity {

    private EditText name_ed;

    private TextView prepare_sec_tv, work_sec_tv, rest_sec_tv, cycles_nb_tv, nbTabata_nb_tv, rest_tabata_sec_tv, cool_down_sec_tv;
    private ImageButton prepare_minus_button, prepare_plus_button;
    private ImageButton work_minus_button, work_plus_button;
    private ImageButton rest_minus_button, rest_plus_button;
    private ImageButton cycles_minus_button, cycles_plus_button;
    private ImageButton nbTabata_minus_button, nbTabata_plus_button;
    private ImageButton rest_tabata_minus_button, rest_tabata_plus_button;
    private ImageButton cool_down_minus_button, cool_down_plus_button;
    private Button saveButton;

    private Tabata tabata;
    private TabataDAO tabataDAO;

    private String name = "Tabata";

    private boolean add = false;
    private boolean update = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata_edit);

        //INITIALISATION DES COMPOSANTS
        name_ed = (EditText) findViewById(R.id.act_tabata_edit_name_ed);

        prepare_sec_tv = (TextView) findViewById(R.id.act_tabata_edit_prepare_tv_sec);
        work_sec_tv = (TextView) findViewById(R.id.act_tabata_edit_work_tv_sec);
        rest_sec_tv = (TextView) findViewById(R.id.act_tabata_edit_rest_tv_sec);
        cycles_nb_tv = (TextView) findViewById(R.id.act_tabata_edit_cycles_tv_nb);
        nbTabata_nb_tv =(TextView) findViewById(R.id.act_tabata_edit_nbTabata_tv_nb);
        rest_tabata_sec_tv = (TextView) findViewById(R.id.act_tabata_edit_rest_tabata_tv_sec);
        cool_down_sec_tv = (TextView) findViewById(R.id.act_tabata_edit_cool_down_tv_sec);

        prepare_minus_button = (ImageButton) findViewById(R.id.act_tabata_edit_prepare_minus_button);
        prepare_plus_button = (ImageButton) findViewById(R.id.act_tabata_edit_prepare_plus_button);

        work_minus_button = (ImageButton) findViewById(R.id.act_tabata_edit_work_minus_button);
        work_plus_button = (ImageButton) findViewById(R.id.act_tabata_edit_work_plus_button);

        rest_minus_button = (ImageButton) findViewById(R.id.act_tabata_edit_rest_minus_button);
        rest_plus_button = (ImageButton) findViewById(R.id.act_tabata_edit_rest_plus_button);

        cycles_minus_button = (ImageButton) findViewById(R.id.act_tabata_edit_cycles_minus_button);
        cycles_plus_button = (ImageButton) findViewById(R.id.act_tabata_edit_cycles_plus_button);

        nbTabata_minus_button = (ImageButton) findViewById(R.id.act_tabata_edit_nbTabata_minus_button);
        nbTabata_plus_button = (ImageButton) findViewById(R.id.act_tabata_edit_nbTabata_plus_button);

        rest_tabata_minus_button = (ImageButton) findViewById(R.id.act_tabata_edit_rest_tabata_minus_button);
        rest_tabata_plus_button = (ImageButton) findViewById(R.id.act_tabata_edit_rest_tabata_plus_button);

        cool_down_minus_button = (ImageButton) findViewById(R.id.act_tabata_edit_cool_down_minus_button);
        cool_down_plus_button = (ImageButton) findViewById(R.id.act_tabata_edit_cool_down_plus_button);

        saveButton = (Button) findViewById(R.id.act_tabata_edit_save_button);

        AlertDialog.Builder builder_manque_info = new AlertDialog.Builder(this);
        builder_manque_info.setMessage(R.string.dialog_name_already_used)
                .setTitle(R.string.dialog_title_error);
        final AlertDialog dialog_name_already_used = builder_manque_info.create();

        AlertDialog.Builder builder_incorrect_name = new AlertDialog.Builder(this);
        builder_incorrect_name.setMessage(R.string.dialog_incorrect_name)
                .setTitle(R.string.dialog_title_error);
        final AlertDialog dialog_incorrect_name = builder_incorrect_name.create();

        AlertDialog.Builder builder_name_empty = new AlertDialog.Builder(this);
        builder_name_empty.setMessage(R.string.dialog_name_empty)
                .setTitle(R.string.dialog_title_error);
        final AlertDialog dialog_name_empty = builder_name_empty.create();

        //INITIALISATION DU MODEL
        tabataDAO = new TabataDAO();

        //Récupération des données en cas de changement de configuration
        if(savedInstanceState != null)
        {
            tabata = (Tabata) savedInstanceState.getParcelable(TabataListActivity.TABATA);
            add = (boolean) savedInstanceState.getBoolean(TabataListActivity.ADD);
            update = (boolean) savedInstanceState.getBoolean(TabataListActivity.UPDATE);
        }
        else {
            Intent intent = getIntent();
            tabata = (Tabata) intent.getParcelableExtra(TabataListActivity.TABATA);

            //Dans le cas de l'ajout d'un nouveau Tabata l'objet passé en extra sera nul
            if (tabata == null)
                tabata = new Tabata("", 0, 1, 0, 1, 1, 0, 0);

            add = intent.getBooleanExtra(TabataListActivity.ADD, false);
            update = intent.getBooleanExtra(TabataListActivity.UPDATE, false);
        }

        //INITIALISATION DE LA VUE
        name_ed.setText(tabata.getName());
        prepare_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getPrepare()));
        work_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getWork()));
        rest_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getRest()));
        cycles_nb_tv.setText(String.valueOf(tabata.getCycles()));
        nbTabata_nb_tv.setText(String.valueOf(tabata.getNbTabata()));
        rest_tabata_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getRestTabata()));
        cool_down_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getCoolDown()));

        if(update)
            name_ed.setEnabled(false);

        //DEFINITION DES ACTIONS DES BOUTONS
        /*
        Une action sur le bouton plus ajoute une seconde à la valeur correspondante
        Une action sur le bouton moins sousrtrait une seconde à la valeur correspondante (à condition que le nombre reste positif)
         */

        prepare_plus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabata.add_sec_prepare();
                prepare_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getPrepare()));
            }
        }));

        prepare_minus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.substract_sec_prepare();
                prepare_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getPrepare()));
            }
        }));

        work_plus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.add_sec_work();
                work_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getWork()));
            }
        }));

        work_minus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.substract_sec_work();
                work_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getWork()));
            }
        }));

        rest_plus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.add_sec_rest();
                rest_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getRest()));
            }
        }));

        rest_minus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.substract_sec_rest();
                rest_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getRest()));
            }
        }));

        cycles_plus_button.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.add_nb_cycle();
                cycles_nb_tv.setText(tabata.getCycles() + "");
            }
        }));

        cycles_minus_button.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.substract_nb_cycle();
                cycles_nb_tv.setText(tabata.getCycles() + "");
            }
        }));

        nbTabata_plus_button.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.add_nb_tabata();
                nbTabata_nb_tv.setText(tabata.getNbTabata() + "");
            }
        }));

        nbTabata_minus_button.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.substract_nb_tabata();
                nbTabata_nb_tv.setText(tabata.getNbTabata() + "");
            }
        }));

        rest_tabata_plus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.add_sec_rest_tabata();
                rest_tabata_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getRestTabata()));
            }
        }));

        rest_tabata_minus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.substract_sec_rest_tabata();
                rest_tabata_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getRestTabata()));
            }
        }));

        cool_down_plus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.add_sec_cool_down();
                cool_down_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getCoolDown()));
            }
        }));

        cool_down_minus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabata.substract_sec_cool_down();
                cool_down_sec_tv.setText(TimeUtilities.displayTimeSec(tabata.getCoolDown()));
            }
        }));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name_ed.getText().toString() != null && !name_ed.getText().toString().trim().isEmpty()) {

                    if (add) {

                        //Initialisation de l'objet tabata à créer ou à mettre à jour
                        if (name_ed.getText().toString().matches("[a-zA-Z0-9]+")) {
                            name = name_ed.getText().toString();

                            if (tabataDAO.findByName(name).size() == 0) {
                                tabata.setName(name);

                                //Création ou mise à jour de l'objet Tabata lancé
                                tabataDAO.insertOrUpdateByName(tabata);

                                //création de l'intent
                                Intent tabataListActivity = new Intent(TabataEditActivity.this, TabataListActivity.class);
                                tabataListActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //Lancement de l'activité
                                startActivity(tabataListActivity);

                            } else {
                                dialog_name_already_used.show();
                            }
                        } else {
                            dialog_incorrect_name.show();
                        }

                    } else if (update) {
                        //création de l'intent
                        Intent tabataListActivity = new Intent(TabataEditActivity.this, TabataListActivity.class);
                        tabataListActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        //Création ou mise à jour de l'objet Tabata lancé
                        tabataDAO.insertOrUpdateByName(tabata);

                        //Lancement de l'activité
                        startActivity(tabataListActivity);
                    }
                }
                else
                {
                    dialog_name_empty.show();
                }
            }
        });

        //UN CLIC SUR LES ZONES D'AFFICHAGE DES TEMPS FAIT APPARAITRE UN NUMBER PICKER
        prepare_sec_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(prepare_sec_tv);
            }
        });

        work_sec_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(work_sec_tv);
            }
        });

        rest_sec_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(rest_sec_tv);
            }
        });

        rest_tabata_sec_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(rest_tabata_sec_tv);
            }
        });

        cool_down_sec_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(cool_down_sec_tv);
            }
        });
    }

    //Sauvegarde des données du modèle en cas de changement d'orientation de l'écran
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(TabataListActivity.TABATA, tabata);
        savedInstanceState.putBoolean(TabataListActivity.ADD, add);
        savedInstanceState.putBoolean(TabataListActivity.UPDATE, update);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    //IMPLEMENTATION DES ACTIONS A REALISER LORS DE L'UTILISATION DES NUMBERS PICKERS
    public void show(final TextView tv) {
        final Dialog d = new Dialog(TabataEditActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np1 = (NumberPicker) d.findViewById(R.id.numberPicker1);
        final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2);

        NumberPickerUtilities.setTextColor(np1, Color.BLUE);
        NumberPickerUtilities.setTextColor(np2, Color.BLUE);

        NumberPickerUtilities.setDividerColor(np1, Color.BLUE);
        NumberPickerUtilities.setDividerColor(np2, Color.BLUE);

        np1.setMaxValue(9);
        np1.setMinValue(0);

        np2.setMaxValue(59);
        np2.setMinValue(0);

//        np1.setOnValueChangedListener(this);
//        np2.setOnValueChangedListener(this);

        //Initialisation des valeurs des numbers pickers en fonction des valeurs du modèle
        if (tv == prepare_sec_tv) {
            np1.setValue(tabata.getPrepare() / 60);
            np2.setValue(tabata.getPrepare() % 60);
        } else if (tv == work_sec_tv) {

            np1.setValue(tabata.getWork() / 60);
            np2.setValue(tabata.getWork() % 60);

        } else if (tv == rest_sec_tv) {

            np1.setValue(tabata.getRest() / 60);
            np2.setValue(tabata.getRest() % 60);

        } else if (tv == rest_tabata_sec_tv) {

            np1.setValue(tabata.getRestTabata() / 60);
            np2.setValue(tabata.getRestTabata() % 60);

        } else if (tv == cool_down_sec_tv) {

            np1.setValue(tabata.getCoolDown() / 60);
            np2.setValue(tabata.getCoolDown() % 60);

        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nbSec = np2.getValue();
                int nbMin = np1.getValue();

                //Modification du modèle en fonction des données saisies par l'utilisateur
                if (tv == prepare_sec_tv) {
                    tabata.setPrepare(nbMin * 60 + nbSec);
                    prepare_sec_tv.setText(TimeUtilities.displayTimeSec(nbMin * 60 + nbSec));
                } else if (tv == work_sec_tv) {
                    //On autorise pas la sélection d'un temps nul pour la période d'activité
                    if (nbMin == 0 && nbSec == 0) {
                        nbSec = 1;
                    }
                    tabata.setWork(nbMin * 60 + nbSec);
                    work_sec_tv.setText(TimeUtilities.displayTimeSec(nbMin * 60 + nbSec));

                } else if (tv == rest_sec_tv) {

                    tabata.setRest(nbMin * 60 + nbSec);
                    rest_sec_tv.setText(TimeUtilities.displayTimeSec(nbMin * 60 + nbSec));

                } else if(tv == rest_tabata_sec_tv) {

                tabata.setRestTabata(nbMin * 60 + nbSec);
                rest_tabata_sec_tv.setText(TimeUtilities.displayTimeSec(nbMin * 60 + nbSec));

            }else if(tv == cool_down_sec_tv) {

                tabata.setCoolDown(nbMin * 60 + nbSec);
                cool_down_sec_tv.setText(TimeUtilities.displayTimeSec(nbMin * 60 + nbSec));
            }

                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();
    }
}
