package ru.nutscoon.mapsproject.Models;

import java.util.ArrayList;
import java.util.List;

import ru.nutscoon.mapsproject.R;

public class OrganizationsOnMapData{

    public OrganizationsOnMapData(double lat, double lon, int img) {
        this.lat = lat;
        this.lon = lon;
        this.img = img;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    private double lat;
    private double lon;
    private int img;


    public static List<OrganizationsOnMapData> getOrganizationsOnMapDatas(){
        List<OrganizationsOnMapData> res = new ArrayList<>();
        res.add(new OrganizationsOnMapData(54.203946, 37.621329, R.drawable.point_red));
        res.add(new OrganizationsOnMapData(54.217367, 37.622325, R.drawable.point_green));
        res.add(new OrganizationsOnMapData(54.193946, 37.621429, R.drawable.point_red));
        res.add(new OrganizationsOnMapData(54.203946, 37.651329, R.drawable.point_green));
        res.add(new OrganizationsOnMapData(54.213946, 37.591329, R.drawable.point_red));
        res.add(new OrganizationsOnMapData(54.219946, 37.631329, R.drawable.point_green));
        res.add(new OrganizationsOnMapData(54.203946, 37.641329, R.drawable.point_red));

        return res;
    }
}
