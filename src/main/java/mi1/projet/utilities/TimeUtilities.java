package mi1.projet.utilities;

import java.util.ArrayList;

import mi1.projet.model.ScenarioStep;

public class TimeUtilities {

    public static String displayTimeSec(int nbSec){
        if(nbSec >= 60){
            int nbMin, nbSecLeft;
            nbMin = (int)(Math.floor(nbSec/60));
            nbSecLeft = nbSec%60;
            if(nbSecLeft < 10)
                return nbMin + ":0" + nbSecLeft;
            else
                return nbMin + ":" + nbSecLeft;
        }
        else{
            return nbSec + " sec";
        }
    }

    public static String displayTime(int nbSec){

        if(nbSec >= 3600){
            int nbHour, nbMin, nbMinLeft, nbSecLeft;
            nbHour = (int)(Math.floor(nbSec/3600));
            nbSecLeft = nbSec%3600;

            nbMin = (int)(Math.floor(nbSecLeft/60));
            nbSecLeft = nbSecLeft % 60;

            if(nbSecLeft < 10 && nbMin < 10)
                return nbHour + ":0" + nbMin + ":0" + nbSecLeft;
            else if(nbSecLeft < 10 && nbMin >= 10)
                return nbHour + ":" + nbMin + ":0" + nbSecLeft;
            else if(nbSecLeft >= 10 && nbMin < 10)
                return nbHour + ":0" + nbMin + ":" + nbSecLeft;
            else
                return nbHour + ":" + nbMin + ":" + nbSecLeft;
        }
        else if(nbSec <3600 && nbSec >= 60){
            int nbMin, nbSecLeft;
            nbMin = (int)(Math.floor(nbSec/60));
            nbSecLeft = nbSec%60;
            if(nbSecLeft < 10)
                return nbMin + ":0" + nbSecLeft;
            else
                return nbMin + ":" + nbSecLeft;
        }
        else{
            return nbSec + "";
        }
    }

    public static int getTotalTime(ArrayList<ScenarioStep> scenario)
    {
        int totalTime = 0;

        for (ScenarioStep scenarioStep:scenario) {
            totalTime = totalTime + scenarioStep.getScenarioTime();
        }

        return totalTime;
    }

}
