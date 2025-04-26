package com.example.cafeteriamanagement.Network;

public class ApiEndpoint {
    // Base URL
    public static final String BASE_URL = "http://192.168.43.219/CafeteriaManagement/";

    // Define  endpoints

    public static final String LOG_IN = BASE_URL + "login.php";
    public static final String GET_MENU_ITEMS = BASE_URL + "getMenuItems.php";
    public static final String UPDATE_MENU_ITEM = BASE_URL + "updateMenuItem.php";
    public static final String ADD_MENU_ITEM = BASE_URL + "addMenuItem.php";
    public static final String DELETE_MENU_ITEM = BASE_URL + "deleteMenuItem.php";


}