package mi1.projet.model;

/**
 * Created by etien on 04/10/2016.
 */

public class ScenarioStep {

    // VARIABLES MEMBRES
    private String scenarioName;
    private int scenarioTime;

    // CONSTRUCTEUR
    public ScenarioStep(String scenarioName, int scenarioTime){
        this.setScenarioName(scenarioName);
        this.setScenarioTime(scenarioTime);
    }

    // GETTERS ET SETTERS
    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public int getScenarioTime() {
        return scenarioTime;
    }

    public void setScenarioTime(int scenarioTime) {
        this.scenarioTime = scenarioTime;
    }

    // METHODES MEMBRES
    public void decreaseTime(){
        if(this.getScenarioTime() >= 0)
            this.setScenarioTime(this.getScenarioTime() - 1);
    }
}
