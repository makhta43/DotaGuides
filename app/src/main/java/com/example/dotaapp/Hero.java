package com.example.dotaapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Hero implements Parcelable {
    private static final AtomicInteger atomic;
    private int id;
    private int atomicId;
    private String name;
    private String localized_name;
    private String primary_attr;
    private String attack_type;
    private ArrayList<String> roles;
    private int legs;

    public Hero(int id, String name, String localized_name, String primary_attr, String attack_type, ArrayList<String> roles, int legs){
        this.id = id;
        this.name = name;
        this.localized_name = localized_name;
        this.primary_attr = primary_attr;
        this.attack_type = attack_type;
        this.roles = roles;
        this.legs = legs;
        this.atomicId = Hero.atomic.incrementAndGet();
    }
    public Hero(){this.atomicId = Hero.atomic.incrementAndGet();}

    //atomicInteger used as an internal ID due to the inconsistencies of the API ID
    static {
        atomic = new AtomicInteger(0);
    }

    protected Hero(Parcel in) {
        id = in.readInt();
        name = in.readString();
        localized_name = in.readString();
        primary_attr = in.readString();
        attack_type = in.readString();
        roles = in.createStringArrayList();
        legs = in.readInt();
    }

    public static final Creator<Hero> CREATOR = new Creator<Hero>() {
        @Override
        public Hero createFromParcel(Parcel in) {
            return new Hero(in);
        }

        @Override
        public Hero[] newArray(int size) {
            return new Hero[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAtomicId() { return atomicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalized_name() {
        return localized_name;
    }

    public void setLocalized_name(String localized_name) {
        this.localized_name = localized_name;
    }

    //When this method is called, short hand (str, int and agi) isn't returned
    //Instead it'll return full length words (Strength, Intelligence, Agility)
    public String getPrimary_attr() {
        String fullString=null;
        if(primary_attr.equals("str")){
            fullString = "Strength";
        }else if(primary_attr.equals("int")){
            fullString = "Intelligence";
        }else if(primary_attr.equals("agi")){
            fullString = "Agility";
        }
        return fullString;
    }

    public void setPrimary_attr(String primary_attr) { this.primary_attr = primary_attr; }

    public String getAttack_type() { return attack_type; }

    public void setAttack_type(String attack_type) {
        this.attack_type = attack_type;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    public int getLegs() {
        return legs;
    }

    public void setLegs(int legs) {
        this.legs = legs;
    }

    //Since the API has no image URL's, a substring of the 'name' variable must be substituted into a URL template to get the image of each character
    public String getFullImg(){
        String lrgImgTempl = "https://cdn.dota2.com/apps/dota2/images/heroes/%s_lg.png";
        String name = this.getName().substring(14);
        String URL = String.format(lrgImgTempl, name);
        return URL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(localized_name);
        dest.writeString(primary_attr);
        dest.writeString(attack_type);
        dest.writeStringList(roles);
        dest.writeInt(legs);
    }
    public void resetAtomic(){
        atomic.set(0);
    }
}
