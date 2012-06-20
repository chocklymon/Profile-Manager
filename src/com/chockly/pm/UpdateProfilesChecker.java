/* Profile Manager
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

import javax.swing.JOptionPane;

/**
 * Checks how the Profile Manger should deal with changes to a games profile
 * directory, prompting the user for input as needed.
 * @author Curtis Oakley
 */
public class UpdateProfilesChecker {
    
    private static final byte NOT_SET = -1;
    private static final byte TRUE = 0;
    private static final byte FALSE = 1;
    
    private static final String[] options = {"Yes to all, Always",
        "Yes to all",
        "Yes",
        "No",
        "No to all",
        "No to all, Always"};
    
    private byte sessionCreate = NOT_SET;
    private byte sessionDelete = NOT_SET;
    
    /**
     * Checks if a change should occur, prompts the user as needed.
     * @param text The text of the prompt should it be needed.
     * @param creates <tt>true</tt> if checking that a profile should be created,
     * <tt>false</tt> if checking that a profile should be deleted.
     * @return <tt>True</tt> if a change should be made, <tt>false</tt> otherwise.
     */
    private boolean check(String name, boolean creates){
        
        Config.Key configKey = creates ? Config.Key.auto_create
                : Config.Key.auto_delete;
        String prompt = Config.get(configKey);
        byte session = creates ? sessionCreate : sessionDelete;
        
        if(session == TRUE || Boolean.parseBoolean(prompt)){
            return true;
        } else if(session == FALSE || prompt.equalsIgnoreCase("false")){
            return false;
        } else {

            int response = JOptionPane.showOptionDialog(null,
                    creates ? "A new saved game folder was detected.\nName: "
                               + name
                               + "\n\nWould you like to create a new profile for this folder?"
                        : "The saved game folder for "
                               + name
                               + " has been deleted.\n\nWould you like to delete this profile also?",
                    creates ? "Create New Profile" : "Delete Profile",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if(response < 3){
                if(response == 0){
                    Config.set(configKey, Boolean.toString(true));
                } else if(response == 1){
                    if(creates)
                        sessionCreate = TRUE;
                    else
                        sessionDelete = TRUE;
                }

                return true;
            } else {
                if(response == 4){
                    Config.set(configKey, Boolean.toString(false));
                } else if(response == 5){
                    if(creates)
                        sessionCreate = TRUE;
                    else
                        sessionDelete = TRUE;
                }

                return false;
            }
        }
    }
    
    /**
     * Checks if the Profile Manager should create a new profile in response to
     * a new folder in the Game's profiles folder.
     * @param name The name of the folder.
     * @return <tt>True</tt> if a new profile should be created, <tt>false</tt>
     * otherwise.
     */
    public boolean createProfile(String name){
        return check(name, true);
    }
    
    /**
     * Checks if the Profile Manager should delete a new profile in response to
     * a new folder in the Game's profiles folder.
     * @param name The name of the folder.
     * @return <tt>True</tt> if a new profile should be deleted, <tt>false</tt>
     * otherwise.
     */
    public boolean deleteProfile(String name){
        return check(name, false);
    }
}
