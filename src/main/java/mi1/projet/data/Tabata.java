package mi1.projet.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by etien on 03/10/2016.
 */
public class Tabata extends SugarRecord implements Parcelable, Serializable {

    //VARIABLES MEMBRES
    private String name;
    private int prepare;
    private int work;
    private int rest;
    private int cycles;
    private int nbTabata;
    private int restTabata;
    private int coolDown;


    //CONSTRUCTEURS
    public Tabata(){};

    public Tabata(String name, int prepare, int work, int rest, int cycles, int nbTabata, int restTabata, int coolDown) {
        this.setName(name);
        this.setPrepare(prepare);
        this.setWork(work);
        this.setRest(rest);
        this.setCycles(cycles);
        this.setNbTabata(nbTabata);
        this.setRestTabata(restTabata);
        this.setCoolDown(coolDown);
    }


    //GETTERS ET SETTERS

    public int getCycles() {
        return cycles;
    }

    //On autorise les modifications uniquement pour un nombre de cycle >= 0
    public void setCycles(int cycles) {
        if(cycles >= 0)
            this.cycles = cycles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrepare() {
        return prepare;
    }

    //On autorise les modifications du temps de préparation uniquement pour des valeurs positives
    public void setPrepare(int prepare) {
        if(prepare >= 0)
            this.prepare = prepare;
    }

    public int getWork() {
        return work;
    }

    //On autorise les modifications du temps d'effort uniquement pour des valeurs positives
    public void setWork(int work) {
        if(work >= 0)
            this.work = work;
    }

    public int getRest() {
        return rest;
    }

    //On autorise les modifications du temps de repos uniquement pour des valeurs positives
    public void setRest(int rest) {
        if(rest >= 0)
            this.rest = rest;
    }

    public int getNbTabata(){
        return nbTabata;
    }

    public void setNbTabata(int nbTabata) {
        if(nbTabata > 0)
            this.nbTabata = nbTabata;
    }

    public int getRestTabata() {
        return restTabata;
    }

    public void setRestTabata(int restTabata){
        if(restTabata >= 0)
            this.restTabata = restTabata;
    }

    public int getCoolDown(){
        return this.coolDown;
    }

    public void setCoolDown(int coolDown){
        if(coolDown >= 0)
            this.coolDown = coolDown;
    }

    //METHODES MEMBRES
    public void add_sec_prepare(){
        if(this.getPrepare() < 599)
            this.setPrepare(this.getPrepare() + 1);
    }

    //On autorise le retrait de secondes uniquement pour des valeurs positives
    public void substract_sec_prepare(){
        if(this.getPrepare() > 0)
            this.setPrepare(this.getPrepare() - 1);
    }

    public void add_sec_work(){
        if(this.getWork() < 599)
            this.setWork(this.getWork() + 1);
    }

    //On autorise le retrait de secondes uniquement pour des valeurs positives
    public void substract_sec_work(){
        if(this.getWork() > 0)
            this.setWork(this.getWork() - 1);
    }

    public void add_sec_rest(){
        if(this.getRest() < 599)
            this.setRest(this.getRest() + 1);
    }

    //On autorise le retrait de secondes uniquement pour des valeurs positives
    public void substract_sec_rest(){
        if(this.getRest() > 0)
            this.setRest(this.getRest() - 1);
    }

    public void add_nb_cycle(){
        if(this.getCycles() < 100)
            this.setCycles(this.getCycles() + 1);
    }

    //On autorise le retrait de cycle uniquement pour des valeurs positives
    public void substract_nb_cycle(){
        if(this.getCycles() > 1)
            this.setCycles(this.getCycles() - 1);
    }

    //On autorise le retrait de nombre de tabatas uniquement pour des valeurs positives
    public void add_nb_tabata(){
        if(this.getNbTabata() < 100)
            this.setNbTabata(this.getNbTabata() + 1);
    }

    public void substract_nb_tabata(){
        if(this.getNbTabata() > 1)
            this.setNbTabata(this.getNbTabata() - 1);
    }

    public void add_sec_rest_tabata(){
        if(this.getRestTabata() < 599)
            this.setRestTabata(this.getRestTabata() + 1);
    }

    //On autorise le retrait de secondes uniquement pour des valeurs positives
    public void substract_sec_rest_tabata(){
        if(this.getRestTabata() > 0)
            this.setRestTabata(this.getRestTabata() - 1);
    }

    public void add_sec_cool_down(){
        if(this.getCoolDown() < 599)
            this.setCoolDown(this.getCoolDown() + 1);
    }

    //On autorise le retrait de secondes uniquement pour des valeurs positives
    public void substract_sec_cool_down(){
        if(this.getCoolDown() > 0)
            this.setCoolDown(this.getCoolDown() - 1);
    }

    //Parcelling part
    protected Tabata(Parcel in){
        name = in.readString();
        prepare = in.readInt();
        work = in.readInt();
        rest = in.readInt();
        cycles = in.readInt();
        nbTabata = in.readInt();
        restTabata = in.readInt();
        coolDown = in.readInt();
    }

    public static final Parcelable.Creator<Tabata> CREATOR
            = new Parcelable.Creator<Tabata>() {
        public Tabata createFromParcel(Parcel in) {
            return new Tabata(in);
        }

        public Tabata[] newArray(int size) {
            return new Tabata[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeInt(getPrepare());
        dest.writeInt(getWork());
        dest.writeInt(getRest());
        dest.writeInt(getCycles());
        dest.writeInt(getNbTabata());
        dest.writeInt(getRestTabata());
        dest.writeInt(getCoolDown());
    }
}
