package mi1.projet.model;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Created by etien on 22/10/2016.
 */

public class DataTimer {

    private TabataModel tabataModel;
    private boolean pause;
    private int currentStep;
    private ArrayList<ScenarioStep> scenario;

    public DataTimer(ArrayList<ScenarioStep> scenario, TabataModel tabataModel, int currentStep, boolean pause){
        this.setScenario(scenario);
        this.setTabataModel(tabataModel);
        this.setCurrentStep(currentStep);
        this.setPause(pause);
    }

    public TabataModel getTabataModel() {
        return tabataModel;
    }

    public void setTabataModel(TabataModel tabataModel) {
        this.tabataModel = tabataModel;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public ArrayList<ScenarioStep> getScenario() {
        return scenario;
    }

    public void setScenario(ArrayList<ScenarioStep> scenario) {
        this.scenario = scenario;
    }
}
