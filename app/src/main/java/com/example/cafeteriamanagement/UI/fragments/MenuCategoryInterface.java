package com.example.cafeteriamanagement.UI.fragments;

import com.example.cafeteriamanagement.model.MenuItem;

import java.util.List;

public interface MenuCategoryInterface {
    void setMenuItems(List<MenuItem> menuItems);
    void updateCategoryMenuItem(MenuItem menuItem);
}