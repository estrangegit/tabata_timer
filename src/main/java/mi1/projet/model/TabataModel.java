package mi1.projet.model;

import java.util.ArrayList;

import mi1.projet.data.Tabata;

/**
 * Created by etien on 03/10/2016.
 */

public class TabataModel {

    //VARIABLES MEMBRES
    private Tabata tabata;

    //CONSTRUCTEUR
    public TabataModel(){
        this.tabata = new Tabata();
    }

    //GETTERS ET SETTERS
    public Tabata getTabata() {
        return tabata;
    }

    public void setTabata(Tabata tabata) {
        this.tabata = tabata;
    }

    //METHODES MEMBRES
    public void add_sec_prepare(){
        if(this.getTabata().getPrepare() < 599)
            this.getTabata().setPrepare(this.getTabata().getPrepare() + 1);
    }

    //On autorise le retrait de secondes uniquement pour des valeurs positives
    public void substract_sec_prepare(){
        if(this.getTabata().getPrepare() > 0)
            this.getTabata().setPrepare(this.getTabata().getPrepare() - 1);
    }

    public void add_sec_work(){
        if(this.getTabata().getWork() < 599)
            this.getTabata().setWork(this.getTabata().getWork() + 1);
    }

    //On autorise le retrait de secondes uniquement pour des valeurs positives
    public void substract_sec_work(){
        if(this.getTabata().getWork() > 1)
            this.getTabata().setWork(this.getTabata().getWork() - 1);
    }

    public void add_sec_rest(){
        if(this.getTabata().getRest() < 599)
            this.getTabata().setRest(this.getTabata().getRest() + 1);
    }

    //On autorise le retrait de secondes uniquement pour des valeurs positives
    public void substract_sec_rest(){
        if(this.getTabata().getRest() > 0)
            this.getTabata().setRest(this.getTabata().getRest() - 1);
    }

    public void add_nb_cycle(){
        if(this.getTabata().getCycles() < 100)
            this.getTabata().setCycles(this.getTabata().getCycles() + 1);
    }

    //On autorise le retrait de secondes uniquement pour des valeurs positives
    public void substract_nb_cycle(){
        if(this.getTabata().getCycles() > 1)
            this.getTabata().setCycles(this.getTabata().getCycles() - 1);
    }

    public void add_nb_tabata(){
        if(this.getTabata().getNbTabata() < 100)
            this.getTabata().setNbTabata(this.getTabata().getNbTabata() + 1);
    }

    //On autorise le retrait de secondes uniquement pour des valeurs positives
    public void substract_nb_tabata(){
        if(this.getTabata().getNbTabata() > 1)
            this.getTabata().setNbTabata(this.getTabata().getNbTabata() - 1);
    }

    public void add_sec_rest_tabata(){
        if(this.getTabata().getRestTabata() < 599)
            this.getTabata().setRestTabata(this.getTabata().getRestTabata() + 1);
    }

    //On autorise le retrait de secondes uniquement pour des valeurs positives
    public void substract_sec_rest_tabata(){
        if(this.getTabata().getRestTabata() > 0)
            this.getTabata().setRestTabata(this.getTabata().getRestTabata() - 1);
    }

    public void add_sec_cool_down(){
        if(this.getTabata().getCoolDown() < 599)
            this.getTabata().setCoolDown(this.getTabata().getCoolDown() + 1);
    }

    //On autorise le retrait de secondes uniquement pour des valeurs positives
    public void substract_sec_cool_down(){
        if(this.getTabata().getCoolDown() > 0)
            this.getTabata().setCoolDown(this.getTabata().getCoolDown() - 1);
    }

    public ArrayList<ScenarioStep> getScenario(){

        ArrayList<ScenarioStep> scenario = new ArrayList<>();

        if(this.getTabata().getPrepare() > 0){
            ScenarioStep prepareStep = new ScenarioStep("Prepare", this.getTabata().getPrepare());
            scenario.add(prepareStep);
        }

        for(int j = 0; j < this.getTabata().getNbTabata() - 1; j++) {

            for (int i = 0; i < this.getTabata().getCycles() - 1; i++) {
                if (this.getTabata().getWork() > 0) {
                    ScenarioStep workStep = new ScenarioStep("Work", this.getTabata().getWork());
                    scenario.add(workStep);
                }

                if (this.getTabata().getRest() > 0) {
                    ScenarioStep restStep = new ScenarioStep("Rest", this.getTabata().getRest());
                    scenario.add(restStep);
                }
            }

            if (this.getTabata().getWork() > 0) {
                ScenarioStep workStep = new ScenarioStep("Work", this.getTabata().getWork());
                scenario.add(workStep);
            }

            if(this.getTabata().getRestTabata() > 0) {
                ScenarioStep restTabata = new ScenarioStep("RestTabata", this.getTabata().getRestTabata());
                scenario.add(restTabata);
            }
        }

        for (int i = 0; i < this.getTabata().getCycles() - 1; i++) {
            if (this.getTabata().getWork() > 0) {
                ScenarioStep workStep = new ScenarioStep("Work", this.getTabata().getWork());
                scenario.add(workStep);
            }

            if (this.getTabata().getRest() > 0) {
                ScenarioStep restStep = new ScenarioStep("Rest", this.getTabata().getRest());
                scenario.add(restStep);
            }
        }

        if (this.getTabata().getWork() > 0) {
            ScenarioStep workStep = new ScenarioStep("Work", this.getTabata().getWork());
            scenario.add(workStep);
        }

        if(this.getTabata().getCoolDown() > 0)
        {
            ScenarioStep coolDownStep = new ScenarioStep("CoolDown", this.getTabata().getCoolDown());
            scenario.add(coolDownStep);
        }

        return scenario;
    }

}
