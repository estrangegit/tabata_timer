package mi1.projet.tabata;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mi1.projet.MainActivity;
import mi1.projet.R;
import mi1.projet.data.Tabata;
import mi1.projet.model.DataTimer;
import mi1.projet.model.ScenarioStep;
import mi1.projet.model.TabataModel;
import mi1.projet.utilities.MyCountDownTimer;
import mi1.projet.utilities.TimeUtilities;

public class TabataTimerActivity extends AppCompatActivity {

    private TextView total_time_tv, period_time_tv, act_name_tv, cycle_num_tv, tabata_num_tv;
    private ImageView period_logo_iv;
    private ImageButton play_pause_button, back_button, next_button;
    private LinearLayout activityLayout;

    private TabataModel tabataModel;

    private DataTimer dataTimer;

    private int nbSecPrep, nbSecRest, nbSecWork, nbCycles, nbTabata, nbSecRestTabata, nbSecCoolDown;
    private int totalTimeScenario = 0;

    private ArrayList<ScenarioStep> scenario;
    private int currentStep;
    private int[] infosCycles;
    private int[] infosCurrentTabata;

    MyCountDownTimer mycounter;

    private boolean pause;
    private boolean changedOrientation = false;

    private int nbSec = -1;

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tabata_timer);

        //INITIALISATION DES COMPOSANTS
        total_time_tv = (TextView) findViewById(R.id.act_tabata_total_time_tv);
        period_time_tv = (TextView) findViewById(R.id.act_tabata_time_period_tv);
        act_name_tv = (TextView) findViewById(R.id.act_tabata_period_tv);
        cycle_num_tv = (TextView) findViewById(R.id.act_tabata_cycle_tv);
        tabata_num_tv = (TextView) findViewById(R.id.act_tabata_currentTabata_tv);

        period_logo_iv = (ImageView) findViewById(R.id.act_tabata_period_iv);

        play_pause_button = (ImageButton) findViewById(R.id.act_tabata_play_pause_button);
        back_button = (ImageButton) findViewById(R.id.act_tabata_back_button);
        next_button = (ImageButton) findViewById(R.id.act_tabata_next_button);

        activityLayout = (LinearLayout) findViewById(R.id.activity_tabata_timer);

        //INITIALISATION DU MODEL

        //Récupération des données en cas de changement de configuration

        dataTimer = (DataTimer) getLastCustomNonConfigurationInstance();

        if(dataTimer != null)
        {
            scenario = dataTimer.getScenario();
            tabataModel = dataTimer.getTabataModel();
            currentStep = dataTimer.getCurrentStep();
            pause = dataTimer.isPause();
            changedOrientation = true;
        }
        else{
            pause = false;
        }


        //Création d'un nouvel objet si il n'y a aucune donnée récupérée du contexte
        if (tabataModel == null) {
            tabataModel = new TabataModel();


            Intent intent = getIntent();

            //Récupération des paramètres de l'intent
//            tabataModel.setTabata((Tabata) intent.getSerializableExtra(MainActivity.TABATA));
            tabataModel.setTabata((Tabata) intent.getParcelableExtra(MainActivity.TABATA));
            scenario = tabataModel.getScenario();
            currentStep = 1;
        }

        nbSecPrep = tabataModel.getTabata().getPrepare();
        nbSecWork = tabataModel.getTabata().getWork();
        nbSecRest = tabataModel.getTabata().getRest();
        nbCycles = tabataModel.getTabata().getCycles();
        nbTabata = tabataModel.getTabata().getNbTabata();
        nbSecRestTabata = tabataModel.getTabata().getRestTabata();
        nbSecCoolDown = tabataModel.getTabata().getCoolDown();

        totalTimeScenario = TimeUtilities.getTotalTime(scenario);

        total_time_tv.setText(TimeUtilities.displayTime(totalTimeScenario));


        infosCycles = getInfoCycle(currentStep, scenario);
        infosCurrentTabata = getInfosCurrentTabata(currentStep, scenario);

        cycle_num_tv.setText("Cycle " + infosCycles[0] + "/" + infosCycles[1]);
        tabata_num_tv.setText("Tabata " + infosCurrentTabata[0] + "/" + infosCurrentTabata[1]);

        //INITIALISATION DE L'INTERFACE
        if(!pause)
            play_pause_button.setBackgroundResource(R.drawable.pause_white);
        else
            play_pause_button.setBackgroundResource(R.drawable.play_white);

        updateView();

        //LANCEMENT DU SCENARIO
        mycounter = new MyCountDownTimer(scenario.get(currentStep-1).getScenarioTime()*1000, 1000);
        RefreshTimer();

        if(!pause)
            mycounter.Start();

        play_pause_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pause = !pause;
                if (!pause) {
                    Log.v("startbutton", "startbutton");
                    mycounter.Start();
                    play_pause_button.setBackgroundResource(R.drawable.pause_white);
                } else {
                    play_pause_button.setBackgroundResource(R.drawable.play_white);
                    Log.v("stopbutton", "stopbutton");
                    mycounter.Stop();
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remise du temps adéquat à l'étape en cours
                if(scenario.get(currentStep-1).getScenarioName().compareTo("Prepare") == 0)
                {
                    scenario.get(currentStep-1).setScenarioTime(tabataModel.getTabata().getPrepare());
                }
                else if(scenario.get(currentStep-1).getScenarioName().compareTo("Work") == 0)
                {
                    scenario.get(currentStep-1).setScenarioTime(tabataModel.getTabata().getWork());
                }
                else if(scenario.get(currentStep-1).getScenarioName().compareTo("Rest") == 0)
                {
                    scenario.get(currentStep-1).setScenarioTime(tabataModel.getTabata().getRest());
                }
                else if(scenario.get(currentStep-1).getScenarioName().compareTo("RestTabata") == 0)
                {
                    scenario.get(currentStep-1).setScenarioTime(tabataModel.getTabata().getRestTabata());
                }
                else if(scenario.get(currentStep-1).getScenarioName().compareTo("CoolDown") == 0)
                {
                    scenario.get(currentStep-1).setScenarioTime(tabataModel.getTabata().getCoolDown());
                }

                if(currentStep > 1)
                {
                    currentStep--;
                    if(scenario.get(currentStep-1).getScenarioName().compareTo("Prepare") == 0)
                    {
                        scenario.get(currentStep-1).setScenarioTime(tabataModel.getTabata().getPrepare());
                    }
                    else if(scenario.get(currentStep-1).getScenarioName().compareTo("Work") == 0)
                    {
                        scenario.get(currentStep-1).setScenarioTime(tabataModel.getTabata().getWork());
                    }
                    else if(scenario.get(currentStep-1).getScenarioName().compareTo("Rest") == 0)
                    {
                        scenario.get(currentStep-1).setScenarioTime(tabataModel.getTabata().getRest());
                    }
                    else if(scenario.get(currentStep-1).getScenarioName().compareTo("RestTabata") == 0)
                    {
                        scenario.get(currentStep-1).setScenarioTime(tabataModel.getTabata().getRestTabata());
                    }
                    else if(scenario.get(currentStep-1).getScenarioName().compareTo("CoolDown") == 0)
                    {
                        scenario.get(currentStep-1).setScenarioTime(tabataModel.getTabata().getCoolDown());
                    }

                    updateView();
                }

                mycounter.kill();
                mycounter = new MyCountDownTimer(scenario.get(currentStep-1).getScenarioTime()*1000, 1000);
                play_pause_button.setBackgroundResource(R.drawable.pause_white);
                mycounter.Start();
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scenario.get(currentStep-1).setScenarioTime(0);
                if(currentStep < scenario.size()) {
                    currentStep++;

                    updateView();

                    mycounter.kill();
                    mycounter = new MyCountDownTimer(scenario.get(currentStep - 1).getScenarioTime() * 1000, 1000);
                    play_pause_button.setBackgroundResource(R.drawable.pause_white);
                    mycounter.Start();
                }
                else{
                    mycounter.kill();
                    mycounter = new MyCountDownTimer(0, 1000);
                    play_pause_button.setBackgroundResource(R.drawable.pause_white);
                    mycounter.Start();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        mycounter.kill();
        //réinitialiser le média player
        initMediaPlayer(mp);
        //annulation des étapes du scénario en cours
        for (ScenarioStep scenarioStep : scenario) {
            scenarioStep.setScenarioTime(0);
        }
        //fin de l'activité TabataTimerActivity
        this.finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mycounter != null)
            mycounter.kill();
    }


    //Sauvegarde des données du model en cas de changement d'orientation de l'écran
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        DataTimer dataTimer = new DataTimer(scenario, tabataModel, currentStep, pause);
        return (dataTimer);
    }


    //FONCTION DE MISE A JOUR DE L'INTERFACE EN FONCTION DU TIMER
    public void RefreshTimer()
    {
        final Handler handler = new Handler();
        final Runnable counter = new Runnable(){

            public void run(){
                handler.postDelayed(this, 0);
                //Mise à jour de l'affichage du timer

                //Conditionne la mise à jour seulement aux changements de secondes
                if((nbSec != (int)(mycounter.getCurrentTime()/1000))) {
                    nbSec = (int) (mycounter.getCurrentTime() / 1000);


                    if ((mycounter.getCurrentTime() / 1000) > 0) {
                        scenario.get(currentStep - 1).setScenarioTime((int) (mycounter.getCurrentTime() / 1000));

                        //Notification pour les débuts et les fins d'activité
                        if(((mycounter.getCurrentTime()/1000) == nbSecWork) && (scenario.get(currentStep - 1).getScenarioName().compareTo("Work") == 0))
                        {
                            if(!changedOrientation) {
                                initMediaPlayer(mp);
                                mp = MediaPlayer.create(getApplicationContext(), R.raw.work_start);
                                mp.start();
                            }
                            else
                                changedOrientation = !changedOrientation;
                        }

                        if(((mycounter.getCurrentTime()/1000) == nbSecRest) && (scenario.get(currentStep - 1).getScenarioName().compareTo("Rest") == 0))
                        {
                            if(!changedOrientation){
                                initMediaPlayer(mp);
                                mp = MediaPlayer.create(getApplicationContext(), R.raw.rest_start);
                                mp.start();
                            }
                            else
                                changedOrientation = !changedOrientation;
                        }

                        if(((mycounter.getCurrentTime()/1000) == nbSecRestTabata) && (scenario.get(currentStep - 1).getScenarioName().compareTo("RestTabata") == 0))
                        {
                            if(!changedOrientation){
                                initMediaPlayer(mp);
                                mp = MediaPlayer.create(getApplicationContext(), R.raw.rest_start);
                                mp.start();
                            }
                            else
                                changedOrientation = !changedOrientation;
                        }

                        if(((mycounter.getCurrentTime()/1000) == nbSecCoolDown) && (scenario.get(currentStep - 1).getScenarioName().compareTo("CoolDown") == 0))
                        {
                            if(!changedOrientation){
                                initMediaPlayer(mp);
                                mp = MediaPlayer.create(getApplicationContext(), R.raw.rest_start);
                                mp.start();
                            }
                            else
                                changedOrientation = !changedOrientation;
                        }

                        if (((mycounter.getCurrentTime() / 1000) == 3) || ((mycounter.getCurrentTime() / 1000) == 2) || ((mycounter.getCurrentTime() / 1000) == 1)) {
                            if(!changedOrientation){
                                initMediaPlayer(mp);
                                mp = MediaPlayer.create(getApplicationContext(), R.raw.sec_bip);
                                mp.start();
                            }
                            else
                                changedOrientation = !changedOrientation;
                        }

                        //Gestion de la taille de l'affichage en fonction du nombre de secondes et de l'orientation de l'écran
                        if(getRotation(TabataTimerActivity.this).compareTo("portrait") == 0 || getRotation(TabataTimerActivity.this).compareTo("reverse portrait") == 0) {
                            if (scenario.get(currentStep - 1).getScenarioTime() >= 60) {
                                period_time_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 170);
                                period_time_tv.setText(String.valueOf(TimeUtilities.displayTime(scenario.get(currentStep - 1).getScenarioTime())));
                            } else {
                                period_time_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 300);
                                period_time_tv.setText(String.valueOf(TimeUtilities.displayTime(scenario.get(currentStep - 1).getScenarioTime())));
                            }
                        }else if(getRotation(TabataTimerActivity.this).compareTo("landscape") == 0 || getRotation(TabataTimerActivity.this).compareTo("reverse landscape") == 0){
                                period_time_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
                                period_time_tv.setText(String.valueOf(TimeUtilities.displayTime(scenario.get(currentStep - 1).getScenarioTime())));
                        }

                        total_time_tv.setText(TimeUtilities.displayTime(TimeUtilities.getTotalTime(scenario)));
                    }

                    //Changement d'étape du scénario
                    if (((mycounter.getCurrentTime() / 1000) == 0) && (currentStep < scenario.size())) {

                        scenario.get(currentStep - 1).setScenarioTime((int) (mycounter.getCurrentTime() / 1000));
                        //avancée d'une étape dans le scénario
                        currentStep++;

                        //mise à jour de l'interface
                        updateView();

                        //lancement d'un nouveau compteur
                        mycounter = new MyCountDownTimer(scenario.get(currentStep - 1).getScenarioTime() * 1000, 1000);
                        mycounter.Start();
                    }

                    if (((mycounter.getCurrentTime() / 1000) == 0) && (currentStep == scenario.size())) {

                        //Notification pour la fin du tabata
                        if(!changedOrientation){
                            initMediaPlayer(mp);
                            mp = MediaPlayer.create(getApplicationContext(), R.raw.gong_end);
                            mp.start();
                        }
                        else
                            changedOrientation = !changedOrientation;

                        //Mise à jour des informations de fin de scénario

                        scenario.get(currentStep - 1).setScenarioTime((int) (mycounter.getCurrentTime() / 1000));

                        if(getRotation(TabataTimerActivity.this).compareTo("portrait") == 0 || getRotation(TabataTimerActivity.this).compareTo("reverse portrait") == 0) {
                            period_time_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 300);
                            period_time_tv.setText(String.valueOf(TimeUtilities.displayTime(scenario.get(currentStep - 1).getScenarioTime())));
                        }else if(getRotation(TabataTimerActivity.this).compareTo("landscape") == 0 || getRotation(TabataTimerActivity.this).compareTo("reverse landscape") == 0){
                            period_time_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
                            period_time_tv.setText(String.valueOf(TimeUtilities.displayTime(scenario.get(currentStep - 1).getScenarioTime())));
                        }

                        total_time_tv.setText(TimeUtilities.displayTime(TimeUtilities.getTotalTime(scenario)));
                        act_name_tv.setText("Finish");

                        activityLayout.setBackgroundColor(Color.rgb(45, 45, 134));

                        play_pause_button.setEnabled(false);
                        play_pause_button.setVisibility(View.INVISIBLE);

                        next_button.setEnabled(false);
                        next_button.setVisibility(View.GONE);

                        back_button.setEnabled(false);
                        back_button.setVisibility(View.GONE);

                        cycle_num_tv.setVisibility(View.INVISIBLE);
                        tabata_num_tv.setVisibility(View.INVISIBLE);

                        period_logo_iv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        };
        handler.postDelayed(counter, 0);
    }

    //FONCTION CALCULANT LES INFOS (NOMBRE DE CYCLE ET NUMERO DU CYCLE COURANT)
    private int[] getInfoCycle(int currentStep, ArrayList<ScenarioStep> scenario){

        int numCycle;
        int scenarioSize = scenario.size();

        int nbCycles = this.nbCycles;
        int nbTabata = this.nbTabata;

        int nbSecPrep = this.nbSecPrep;
        int nbSecRest = this.nbSecRest;
        int nbSecRestTabata = this.nbSecRestTabata;
        int nbSecCoolDown = this.nbSecCoolDown;

        if(nbSecPrep > 0)
            scenarioSize--;

        if(nbSecCoolDown > 0)
            scenarioSize--;

        int nbStepByTabata = (scenarioSize + 1)/nbTabata;

        numCycle = currentStep;

        if(nbSecPrep > 0)
            numCycle--;

        numCycle = numCycle % nbStepByTabata;

        if(nbSecRest > 0)
            numCycle = (int)Math.ceil((numCycle)/2.0);

        if((nbSecRestTabata == 0) && (numCycle == 0))
            numCycle = nbCycles;

        int infosCycles[] = {numCycle, nbCycles};
        
        return infosCycles;
    }

    private int[] getInfosCurrentTabata(int currentStep, ArrayList<ScenarioStep> scenario)
    {
        int numTabata;
        int scenarioSize = scenario.size();

        int nbTabata = this.nbTabata;

        int nbSecPrep = this.nbSecPrep;
        int nbSecCoolDown = this.nbSecCoolDown;

        if(nbSecPrep > 0)
            scenarioSize--;

        if(nbSecCoolDown > 0)
            scenarioSize--;

        int nbStepByTabata = (scenarioSize + 1)/nbTabata;

        numTabata = currentStep;

        if(nbSecPrep > 0)
            numTabata--;

        numTabata = (int)Math.ceil(((double)numTabata)/((double)nbStepByTabata));

        int infosCurrentTabata[] = {numTabata, nbTabata};
        return infosCurrentTabata;
    }

    //FONCTION PERMETTANT DE METTRE A JOUR TOUTES LES INFORMATIONS DE L'INTERFACE
    private void updateView(){
        //Modification de l'affichage du cycle courant
        infosCycles = getInfoCycle(currentStep, scenario);
        infosCurrentTabata = getInfosCurrentTabata(currentStep, scenario);

        //Modification des couleurs et titres en fonction de l'étape du scénario
        if(scenario.get(currentStep-1).getScenarioName().compareTo("Prepare") == 0)
        {
            period_logo_iv.setBackgroundResource(R.drawable.prepare_white);
            act_name_tv.setText("Prepare");
            cycle_num_tv.setText("");
            tabata_num_tv.setText("");
            activityLayout.setBackgroundColor(Color.rgb(0, 102, 0));
        }
        else if(scenario.get(currentStep-1).getScenarioName().compareTo("Work") == 0)
        {
            period_logo_iv.setBackgroundResource(R.drawable.work_white);
            act_name_tv.setText("Work");
            cycle_num_tv.setText("Cycle " + infosCycles[0] + "/" + infosCycles[1]);
            tabata_num_tv.setText("Tabata " + infosCurrentTabata[0] + "/" + infosCurrentTabata[1]);
            activityLayout.setBackgroundColor(Color.rgb(204, 51, 0));
        }
        else if(scenario.get(currentStep-1).getScenarioName().compareTo("Rest") == 0)
        {
            act_name_tv.setText("Rest");
            period_logo_iv.setBackgroundResource(R.drawable.rest_white);
            cycle_num_tv.setText("Cycle " + infosCycles[0] + "/" + infosCycles[1]);
            tabata_num_tv.setText("Tabata " + infosCurrentTabata[0] + "/" + infosCurrentTabata[1]);
            activityLayout.setBackgroundColor(Color.rgb(45, 45, 134));
        }
        else if(scenario.get(currentStep-1).getScenarioName().compareTo("RestTabata") == 0)
        {
            act_name_tv.setText("Rest between tabatas");
            period_logo_iv.setBackgroundResource(R.drawable.rest_tabata_white);
            cycle_num_tv.setText("");
            tabata_num_tv.setText("Tabata " + infosCurrentTabata[0] + "/" + infosCurrentTabata[1]);
            activityLayout.setBackgroundColor(Color.rgb(0, 179, 179));
        }
        else if(scenario.get(currentStep-1).getScenarioName().compareTo("CoolDown") == 0)
        {
            act_name_tv.setText("Cool Down");
            period_logo_iv.setBackgroundResource(R.drawable.cool_down_white);
            cycle_num_tv.setText("");
            tabata_num_tv.setText("");
            activityLayout.setBackgroundColor(Color.rgb(0, 153, 115));
        }
    }

    //FONCTION PERMETTANT DE REINITIALISER LE MEDIA PLAYER
    private void initMediaPlayer(MediaPlayer mp) {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            mp = null;
        }
    }

    public String getRotation(Context context){
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "portrait";
            case Surface.ROTATION_90:
                return "landscape";
            case Surface.ROTATION_180:
                return "reverse portrait";
            default:
                return "reverse landscape";
        }
    }

}

