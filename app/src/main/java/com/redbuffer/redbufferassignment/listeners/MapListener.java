package com.redbuffer.redbufferassignment.listeners;

import com.redbuffer.redbufferassignment.model.User;

import java.util.ArrayList;

public interface MapListener {
      void getAllUserLocation(ArrayList<User> userArrayList);
      void showLocationOnMap(User user);
}
