/*
 * Copyright (C) 2012 Curtis Oakley
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.chockly.pm;

import java.io.IOException;
import java.util.Properties;

/**
 * Contains configuration settings for the Profile Manager saved in key value
 * pairs. Also contains methods to save and load these configuration settings
 * to disk as a properties file.
 * @author Curtis Oakley
 */
public class Config {

    /////////////////////////////////
    //                             //
    //          CONSTANTS          //
    //                             //
    /////////////////////////////////
    
    // ------- KEYS -------- \\
    //  PUBLIC  \\
    
    // GENERAL
    /** 
     * Key used to retrieve/store the user's "My documents" directoy value.<br/>
     * It is safe to assume that the value returned by this key will not be NULL
     * since the value should be set when the program first runs.<br/>
     * Like all other directory values, this one will have a trailing path separator.
     */
    public static final String USER_DIRECTORY = "user_directoy";
    /**
     * Key used to retrieve/store the boolean String value indicating if the
     * program should terminate when a game is launched. Defaults to true.
     */
    public static final String EXIT_ON_LAUNCH = "exit_on_launch";
    /**
     * Key used to retrieve/store the byte value indicating the default tab that
     * the program should start in.
     */
    public static final String START_TAB = "start_tab";
    /**
     * Key used to retrieve/store the boolean String value indicating if the
     * program should open with the last opened tab.<br/>
     * If true the START_TAB's value is the last tab that was opened, if false
     * the START_TAB's value indicates the preferred starting tab.
     */
    public static final String START_IN_LAST_TAB = "start_in_last_tab";
    /**
     * Key used to retrieve/store the active tabs. Active tabs are defined by a
     * comma separated list of game ID.
     */
    public static final String ACTIVE_TABS = "active_tabs";
    
    /**
     * Key used to retrieve/store the action to be taken when double clicking
     * the icons in the profile list.
     */
    public static final String DB_CLICK_ICON_KEY = "double_click_icon";
    /**
     * Key used to retrieve/store the action to be taken when double clicking
     * the profile's name in the profile list.
     */
    public static final String DB_CLICK_TEXT_KEY = "double_click_name";
    /** Indicates that the double click event should activate the profile. */
    public static final String DB_CLICK_ACTIVATE = "activate";
    /** Indicates that the double click event should start the game. */
    public static final String DB_CLICK_LAUNCH = "launch";
    /** Indicates that the double click event should rename the profile. */
    public static final String DB_CLICK_RENAME = "rename";
    
    /**
     * Key used to retrieve/store the deep scan flag for saved games.<br/>
     * <br/>
     * Valid values for the key: <tt>true, false, auto</tt><br/>
     * 
     * @see com.chockly.pm.Config#DEEP_SCAN_AUTO
     */
    public static final String DEEP_SCAN_KEY = "deep_scan";
    /** The key value indicating that deep scan is set to auto. */
    public static final String DEEP_SCAN_AUTO = "auto";
    
    
    //  PRIVATE  \\
    // GUI
    public static final String GUI_X_LOCATION = "gui_x";
    public static final String GUI_Y_LOCATION = "gui_y";
    public static final String GUI_HEIGHT = "gui_height";
    public static final String GUI_WIDTH = "gui_width";
    // ------- END KEYS -------- \\
    
    
    // ------- MISC CONSTANTS ------- \\
    /** The profile managers data storage folder. */
    public static final String PROFILE_DATA_DIR = "profiles" + java.io.File.separator;
    
    
    // Private
    private static final String CONFIG_FILE = "profile_manger.ini";
    private static final String PRIVATE_CONFIG_FILE = PROFILE_DATA_DIR + ".profile_manager";
    
    // Properties objects
    private static final Properties props = new Properties();
    private static final Properties privateProps = new Properties();
    // ------- END MISC CONSTANTS ------- \\

    
    
    /////////////////////////////////
    //                             //
    //          METHODS            //
    //                             //
    /////////////////////////////////
    
    
    /**
     * Loads the configuration files from disk.<br/>
     * In general this should be called only once per program run, as reloading
     * the files from disk will remove any configuration settings that were not
     * saved to disk.
     */
    public static void loadConfig(){
        try {
            props.load(new java.io.FileInputStream(CONFIG_FILE));
        } catch(java.io.FileNotFoundException fnfe){
            System.out.println(CONFIG_FILE
                    + " not found. Rebuilding.");
            BuildIni.buildDefaultIni();
        } catch(IOException ioe){
            System.err.println("An IO Exception occured while attempting to read "
                    + CONFIG_FILE + ". Using the default configuration.");
        }
        try {
            privateProps.load(new java.io.FileInputStream(PRIVATE_CONFIG_FILE));
        } catch(java.io.FileNotFoundException fnfe){
            System.out.println("Private config not found. Using the default configuration.");
        } catch(IOException ioe){
            System.err.println("An IO Exception occured while attempting to read the private configuration file. Using the default configuration.");
        }
    }

    /**
     * Saves the configuration settings to a file on the disk.<br/>
     * In general this should be called whenever any changes have been made.
     * If any changes have been made and the program exits without calling this
     * method any changes will be lost.
     */ 
    public static void saveConfig(){
        try {
            props.store(new java.io.FileOutputStream(CONFIG_FILE),
                    "Configuration settings for the profile manager.");
        } catch(IOException ioe){
            Main.handleException("Unable to save the configuration file.",
                    ioe, Main.WARN_LEVEL);
        }
        try {
            privateProps.store(new java.io.FileOutputStream(PRIVATE_CONFIG_FILE),
                    "WARNING! DO NOT EDIT! This file is used by the profile manager to keep track of internal variables and settings, editing this file can potentially damage your profiles and keep the profile manager from working.");
        } catch(IOException ioe){
            Main.handleException("Unable to save the private configuration file.",
                    ioe, Main.WARN_LEVEL);
        }
    }

    /**
     * Retrieves the value for the provided configuration setting key.<br/>
     * Null is returned if the key doesn't exist, or if the key's value equals
     * null.
     * @param configSettingName The key for the value to retrieve.
     * @return The value of the configuration key, or null if the key doesn't
     * exist.
     */
    public static String get(String configSettingName){
        return props.getProperty(configSettingName);
    }

    /**
     * Retrieves the value for the provided configuration setting key.
     * @param configSettingName The key for the value to retrieve.
     * @param defaultValue The value to return if the key doesn't exist.
     * @return The value of the configuration key, or the default value if the
     * key doesn't exist.
     */
    public static String get(String configSettingName, String defaultValue){
        return props.getProperty(configSettingName, defaultValue);
    }

    /**
     * Saves a configuration value to the provided configuration key.
     * @param configSettingName The key to save the value to.
     * @param value The value for the provided key.
     */
    public static void set(String configSettingName, String value){
        props.setProperty(configSettingName, value);
    }
    
    /**
     * Retrieves the value for the provided configuration setting key from the
     * private configuration.<br/>
     * Null is returned if the key doesn't exist, or if the key's value equals
     * null.
     * @param configSettingName The key for the value to retrieve.
     * @return The value of the configuration key, or null if the key doesn't
     * exist.
     */
    public static String getPrivateProperty(String configSettingName){
        return privateProps.getProperty(configSettingName);
    }

    /**
     * Retrieves the value for the provided configuration setting key from the
     * private configuration.
     * @param configSettingName The key for the value to retrieve.
     * @param defaultValue The value to return if the key doesn't exist.
     * @return The value of the configuration key, or the default value if the
     * key doesn't exist.
     */
    public static String getPrivateProperty(String configSettingName, String defaultValue){
        return privateProps.getProperty(configSettingName, defaultValue);
    }
    
    /**
     * Saves a configuration value to the provided private configuration key.
     * @param configSettingName The key to save the value to.
     * @param value The value for the provided key.
     */
    public static void setPrivateProperty(String configSettingName, String value){
        privateProps.setProperty(configSettingName, value);
    }

}