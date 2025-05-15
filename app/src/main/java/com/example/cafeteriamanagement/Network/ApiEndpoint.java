package com.example.cafeteriamanagement.Network;

public class ApiEndpoint {
    // Base URL
    public static final String BASE_URL = "http://192.168.1.37/CafeteriaManagement/";


    public static final String LOG_IN = BASE_URL + "login.php";
    public static final String GET_MENU_ITEMS = BASE_URL + "getMenuItems.php";
    public static final String UPDATE_MENU_ITEM = BASE_URL + "updateMenuItem.php";
    public static final String ADD_MENU_ITEM = BASE_URL + "addMenuItem.php";
    public static final String GET_ORDERS = BASE_URL + "getOrders.php";
    public static final String GET_TABLES = BASE_URL + "getTables.php";
    public static final String ADD_NEW_ORDER = BASE_URL + "addNewOrder.php";
    public static final String DELETE_ORDER = BASE_URL + "deleteOrder.php";


}