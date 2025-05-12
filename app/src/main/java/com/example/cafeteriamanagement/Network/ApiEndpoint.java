package com.example.cafeteriamanagement.Network;

public class ApiEndpoint {
    // Base URL
    public static final String BASE_URL = "http://192.168.1.40/CafeteriaManagement/";


    public static final String LOG_IN = BASE_URL + "login.php";
    public static final String GET_MENU_ITEMS = BASE_URL + "getMenuItems.php";
    public static final String UPDATE_MENU_ITEM = BASE_URL + "updateMenuItem.php";
    public static final String ADD_MENU_ITEM = BASE_URL + "addMenuItem.php";
    public static final String GET_ORDERS = BASE_URL + "getOrders.php";



}