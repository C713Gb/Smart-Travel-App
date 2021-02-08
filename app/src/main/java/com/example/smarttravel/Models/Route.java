package com.example.smarttravel.Models;

public class Route {

    private String destination;
    private String userId;
    private String mode;
    private String date;
    private Destination destinationLatLng;
    private String routeId;

    public Route() {
    }

    public Route(String destination, String userId, String mode, String date, Destination destinationLatLng, String routeId) {
        this.destination = destination;
        this.userId = userId;
        this.mode = mode;
        this.date = date;
        this.destinationLatLng = destinationLatLng;
        this.routeId = routeId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Destination getDestinationLatLng() {
        return destinationLatLng;
    }

    public void setDestinationLatLng(Destination destinationLatLng) {
        this.destinationLatLng = destinationLatLng;
    }
}
