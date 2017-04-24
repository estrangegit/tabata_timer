package mi1.projet;

import android.app.Dialog;
import android.content.Intent;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.sql.Time;

import mi1.projet.data.Tabata;
import mi1.projet.data.TabataDAO;
import mi1.projet.tabata.TabataListActivity;
import mi1.projet.tabata.TabataTimerActivity;
import mi1.projet.utilities.NumberPickerUtilities;
import mi1.projet.utilities.RepeatListener;
import mi1.projet.model.TabataModel;
import mi1.projet.utilities.TimeUtilities;

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {


    public static final String TABATA = "TABATA";

    private int nbSecPrep, nbSecRest, nbSecWork, nbCycles, nbTabata, nbSecRestTabata, nbSecCoolDown;
    private Tabata tabata;

    private TextView prepare_sec_tv, work_sec_tv, rest_sec_tv, cycles_nb_tv, nbTabata_nb_tv, rest_tabata_sec_tv, cool_down_sec_tv;
    private ImageButton prepare_minus_button, prepare_plus_button;
    private ImageButton work_minus_button, work_plus_button;
    private ImageButton rest_minus_button, rest_plus_button;
    private ImageButton cycles_minus_button, cycles_plus_button;
    private ImageButton nbTabata_minus_button, nbTabata_plus_button;
    private ImageButton rest_tabata_minus_button, rest_tabata_plus_button;
    private ImageButton cool_down_minus_button, cool_down_plus_button;

    private Button startButton;

    private TabataModel tabataModel;
    private TabataDAO tabataDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INITIALISATION DES COMPOSANTS
        prepare_sec_tv = (TextView) findViewById(R.id.act_main_prepare_tv_sec);
        work_sec_tv = (TextView) findViewById(R.id.act_main_work_tv_sec);
        rest_sec_tv = (TextView) findViewById(R.id.act_main_rest_tv_sec);
        cycles_nb_tv = (TextView) findViewById(R.id.act_main_cycles_tv_nb);
        nbTabata_nb_tv =(TextView) findViewById(R.id.act_main_nbTabata_tv_nb);
        rest_tabata_sec_tv = (TextView) findViewById(R.id.act_main_rest_tabata_tv_sec);
        cool_down_sec_tv = (TextView) findViewById(R.id.act_main_cool_down_tv_sec);

        prepare_minus_button = (ImageButton) findViewById(R.id.act_main_prepare_minus_button);
        prepare_plus_button = (ImageButton) findViewById(R.id.act_main_prepare_plus_button);

        work_minus_button = (ImageButton) findViewById(R.id.act_main_work_minus_button);
        work_plus_button = (ImageButton) findViewById(R.id.act_main_work_plus_button);

        rest_minus_button = (ImageButton) findViewById(R.id.act_main_rest_minus_button);
        rest_plus_button = (ImageButton) findViewById(R.id.act_main_rest_plus_button);

        cycles_minus_button = (ImageButton) findViewById(R.id.act_main_cycles_minus_button);
        cycles_plus_button = (ImageButton) findViewById(R.id.act_main_cycles_plus_button);

        nbTabata_minus_button = (ImageButton) findViewById(R.id.act_main_nbTabata_minus_button);
        nbTabata_plus_button = (ImageButton) findViewById(R.id.act_main_nbTabata_plus_button);

        rest_tabata_minus_button = (ImageButton) findViewById(R.id.act_main_rest_tabata_minus_button);
        rest_tabata_plus_button = (ImageButton) findViewById(R.id.act_main_rest_tabata_plus_button);

        cool_down_minus_button = (ImageButton) findViewById(R.id.act_main_cool_down_minus_button);
        cool_down_plus_button = (ImageButton) findViewById(R.id.act_main_cool_down_plus_button);

        startButton = (Button) findViewById(R.id.act_main_start_button);

        //INITIALISATION DU MODEL

        //Récupération de la couche d'accès aux données
        tabataDAO = new TabataDAO();

        //Test de Sugar ORM

//        tabataDAO.deleteDB();
//        tabataDAO.displayDB();

        //Récupération des données en cas de changement de configuration
        if(savedInstanceState !=null){
            tabataModel = new TabataModel();

            tabataModel.setTabata((Tabata)savedInstanceState.getParcelable(TABATA));
        }
        else{
            tabataModel = new TabataModel();

            Intent intent = getIntent();
            tabata = (Tabata)intent.getParcelableExtra(MainActivity.TABATA);

            //Dans le cas de l'ajout d'un nouveau Tabata l'objet passé en extra sera nul
            if(tabata == null)
                tabata = new Tabata("Tabata", 0, 1, 0, 1, 1, 0, 0);

            nbSecPrep = tabata.getPrepare();
            nbSecRest = tabata.getRest();
            nbSecWork = tabata.getWork();
            nbCycles = tabata.getCycles();
            nbTabata = tabata.getNbTabata();
            nbSecRestTabata = tabata.getRestTabata();
            nbSecCoolDown = tabata.getCoolDown();

            tabataModel.setTabata(tabata);
        }

        //INITIALISATION DE LA VUE
        prepare_sec_tv.setText(TimeUtilities.displayTimeSec(nbSecPrep));
        work_sec_tv.setText(TimeUtilities.displayTimeSec(nbSecWork));
        rest_sec_tv.setText(TimeUtilities.displayTimeSec(nbSecRest));
        cycles_nb_tv.setText(String.valueOf(nbCycles));
        nbTabata_nb_tv.setText(String.valueOf(nbTabata));
        rest_tabata_sec_tv.setText(TimeUtilities.displayTimeSec(nbSecRestTabata));
        cool_down_sec_tv.setText(TimeUtilities.displayTimeSec(nbSecCoolDown));



        //DEFINITION DES ACTIONS DES BOUTONS
        /*
        Une action sur le bouton plus ajoute une seconde à la valeur correspondante
        Une action sur le bouton moins sousrtrait une seconde à la valeur correspondante (à condition que le nombre reste positif)
         */

        prepare_plus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabataModel.add_sec_prepare();
                prepare_sec_tv.setText(TimeUtilities.displayTimeSec(tabataModel.getTabata().getPrepare()));
            }
        }));

        prepare_minus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.substract_sec_prepare();
                prepare_sec_tv.setText(TimeUtilities.displayTimeSec(tabataModel.getTabata().getPrepare()));
            }
        }));

        work_plus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.add_sec_work();
                work_sec_tv.setText(TimeUtilities.displayTimeSec(tabataModel.getTabata().getWork()));
            }
        }));

        work_minus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.substract_sec_work();
                work_sec_tv.setText(TimeUtilities.displayTimeSec(tabataModel.getTabata().getWork()));
            }
        }));

        rest_plus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.add_sec_rest();
                rest_sec_tv.setText(TimeUtilities.displayTimeSec(tabataModel.getTabata().getRest()));
            }
        }));

        rest_minus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.substract_sec_rest();
                rest_sec_tv.setText(TimeUtilities.displayTimeSec(tabataModel.getTabata().getRest()));
            }
        }));

        cycles_plus_button.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.add_nb_cycle();
                cycles_nb_tv.setText(tabataModel.getTabata().getCycles() + "");
            }
        }));

        cycles_minus_button.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.substract_nb_cycle();
                cycles_nb_tv.setText(tabataModel.getTabata().getCycles() + "");
            }
        }));

        nbTabata_plus_button.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.add_nb_tabata();
                nbTabata_nb_tv.setText(tabataModel.getTabata().getNbTabata() + "");
            }
        }));

        nbTabata_minus_button.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.substract_nb_tabata();
                nbTabata_nb_tv.setText(tabataModel.getTabata().getNbTabata() + "");
            }
        }));

        rest_tabata_plus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.add_sec_rest_tabata();
                rest_tabata_sec_tv.setText(TimeUtilities.displayTimeSec(tabataModel.getTabata().getRestTabata()));
            }
        }));

        rest_tabata_minus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.substract_sec_rest_tabata();
                rest_tabata_sec_tv.setText(TimeUtilities.displayTimeSec(tabataModel.getTabata().getRestTabata()));
            }
        }));

        cool_down_plus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.add_sec_cool_down();
                cool_down_sec_tv.setText(TimeUtilities.displayTimeSec(tabataModel.getTabata().getCoolDown()));
            }
        }));

        cool_down_minus_button.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabataModel.substract_sec_cool_down();
                cool_down_sec_tv.setText(TimeUtilities.displayTimeSec(tabataModel.getTabata().getCoolDown()));
            }
        }));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //création de l'intent
                Intent tabataActivity = new Intent(MainActivity.this, TabataTimerActivity.class);

                //définit les paramètres pour l'intent
                tabataActivity.putExtra(MainActivity.TABATA, (Parcelable) tabataModel.getTabata());

                //Création ou mise à jour de l'objet Tabata lancé
                tabataDAO.insertOrUpdateByName(tabataModel.getTabata());

                //Lancement de l'activité
                startActivity(tabataActivity);
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

    //Permet d'ajouter une option dans le menu de l'activité
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tabata_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tabata_list:
                Intent tabataListActivity = new Intent(MainActivity.this, TabataListActivity.class);
                tabataListActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tabataListActivity);
                break;
        }
        return true;
    }


    //Sauvegarde des données du modèle en cas de changement d'orientation de l'écran
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(TABATA, tabataModel.getTabata());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is", "" + newVal);
    }

    //IMPLEMENTATION DES ACTIONS A REALISER LORS DE L'UTILISATION DES NUMBERS PICKERS
    public void show(final TextView tv) {
        final Dialog d = new Dialog(MainActivity.this);
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
            np1.setValue(tabataModel.getTabata().getPrepare() / 60);
            np2.setValue(tabataModel.getTabata().getPrepare() % 60);
        } else if (tv == work_sec_tv) {

            np1.setValue(tabataModel.getTabata().getWork() / 60);
            np2.setValue(tabataModel.getTabata().getWork() % 60);

        } else if (tv == rest_sec_tv) {

            np1.setValue(tabataModel.getTabata().getRest() / 60);
            np2.setValue(tabataModel.getTabata().getRest() % 60);

        } else if(tv == rest_tabata_sec_tv) {

            np1.setValue(tabataModel.getTabata().getRestTabata() / 60);
            np2.setValue(tabataModel.getTabata().getRestTabata() % 60);

        }else if(tv == cool_down_sec_tv) {

            np1.setValue(tabataModel.getTabata().getCoolDown() / 60);
            np2.setValue(tabataModel.getTabata().getCoolDown() % 60);

        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nbSec = np2.getValue();
                int nbMin = np1.getValue();

                //Modification du modèle en fonction des données saisies par l'utilisateur
                if (tv == prepare_sec_tv) {
                    tabataModel.getTabata().setPrepare(nbMin * 60 + nbSec);
                    prepare_sec_tv.setText(TimeUtilities.displayTimeSec(nbMin * 60 + nbSec));
                } else if (tv == work_sec_tv) {
                    //On autorise pas la sélection d'un temps nul pour la période d'activité
                    if (nbMin == 0 && nbSec == 0) {
                        nbSec = 1;
                    }
                    tabataModel.getTabata().setWork(nbMin * 60 + nbSec);
                    work_sec_tv.setText(TimeUtilities.displayTimeSec(nbMin * 60 + nbSec));
                } else if (tv == rest_sec_tv) {

                    tabataModel.getTabata().setRest(nbMin * 60 + nbSec);
                    rest_sec_tv.setText(TimeUtilities.displayTimeSec(nbMin * 60 + nbSec));

                } else if(tv == rest_tabata_sec_tv){

                    tabataModel.getTabata().setRestTabata(nbMin * 60 + nbSec);
                    rest_tabata_sec_tv.setText(TimeUtilities.displayTimeSec(nbMin * 60 + nbSec));

                }else if(tv == cool_down_sec_tv) {

                    tabataModel.getTabata().setCoolDown(nbMin * 60 + nbSec);
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
