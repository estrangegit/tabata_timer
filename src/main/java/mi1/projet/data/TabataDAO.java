package mi1.projet.data;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by etien on 09/10/2016.
 */

public class TabataDAO {

    public TabataDAO(){}

    public void insertOrUpdateByName(Tabata tabata){

        ArrayList<Tabata> listTabata = (ArrayList<Tabata>) Tabata.find(Tabata.class, "name = ?", tabata.getName());

        if(listTabata.size() == 1) {
            for (Tabata tabataLoop : listTabata) {
                tabataLoop.setPrepare(tabata.getPrepare());
                tabataLoop.setWork(tabata.getWork());
                tabataLoop.setRest(tabata.getRest());
                tabataLoop.setCycles(tabata.getCycles());
                tabataLoop.setNbTabata(tabata.getNbTabata());
                tabataLoop.setRestTabata(tabata.getRestTabata());
                tabataLoop.setCoolDown(tabata.getCoolDown());
                tabataLoop.save();
            }
        }
        else if(listTabata.size() > 1){
            System.out.println("ATTENTION : PROBLEME D'INTEGRITE DE LA BASE DE DONNEES...");
        }
        else
        {
            tabata.save();
        }
    }

    public void deleteByName(String name){

        ArrayList<Tabata> listTabata = (ArrayList<Tabata>) Tabata.find(Tabata.class, "name = ?", name);

        for (Tabata tabata:listTabata) {
            tabata.delete();
        }
    }

    public ArrayList<Tabata> findByName(String name){
        return (ArrayList<Tabata>) Tabata.find(Tabata.class, "name = ?", name);
    }

    public Tabata findFirstTabataByName(String name){
        ArrayList<Tabata> listByName = (ArrayList<Tabata>) Tabata.find(Tabata.class, "name = ?", name);
        if(listByName.size() > 0)
            return listByName.get(0);
        else
            return null;
    }

    public void deleteDB(){
        ArrayList<Tabata> listAll = (ArrayList<Tabata>) Tabata.listAll(Tabata.class);
        for (Tabata tabata:listAll) {
            tabata.delete();
        }
    }

    public void displayDB(){
        ArrayList<Tabata> listAll = (ArrayList<Tabata>) Tabata.listAll(Tabata.class);

        for (Tabata tabata: listAll) {
            Log.i("**********************", "***********************************");
            Log.i("id", String.valueOf(tabata.getId()));
            Log.i("name", String.valueOf(tabata.getName()));
            Log.i("prepare", String.valueOf(tabata.getPrepare()));
            Log.i("work", String.valueOf(tabata.getWork()));
            Log.i("rest", String.valueOf(tabata.getRest()));
            Log.i("cycles", String.valueOf(tabata.getCycles()));
            Log.i("nbTabata", String.valueOf(tabata.getNbTabata()));
            Log.i("rest between tabatas", String.valueOf(tabata.getRestTabata()));
            Log.i("cool down", String.valueOf(tabata.getCoolDown()));
            Log.i("**********************", "***********************************");
        }

    }


}
