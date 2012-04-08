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

package com.chockly.pm.games;

import com.chockly.pm.Config;
import com.chockly.pm.Profile;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Fallout 3 Game
 * @author Curtis Oakley
 */
public class Fallout3 extends BethesdaGame {
    
    /**
     * Key used to retrieve/store the value indicating the folder where the 
     * Fallout 3 saved games are stored.<br/>
     * Like all other directory values, this one will have a trailing path
     * separator.
     */
    public static final String FALLOUT3_DATA_DIR = "fallout3_data_dir";
    /**
     * Key used to retrieve/store the value indicating the name of the folder in
     * the FALLOUT3_DATA_DIR where saves are stored.<br/>
     * The default value for the key is "Saves".
     */
    public static final String FALLOUT3_SAVE_DIR = "fallout3_save_dir";
    /**
     * Key used to retrieve the value indicating the executable to run when
     * launching Fallout 3.
     */
    public static final String FALLOUT3_EXE = "fallout3_exe";


    @Override
    public boolean activateProfile(Profile profile) {
        return activateProfile("FALLOUT.INI", profile);
    }

    @Override
    public void autoSetupProfiles() {
        String[] validExtensions = {".fos",".bak"};
        
        autoSetupProfiles(validExtensions, 0x26L, 2, " - ", ",", true,
                GameFactory.FALLOUT_3_ID);
    }

    @Override
    public String getDataDirKey() {
        return FALLOUT3_DATA_DIR;
    }

    /**
     * Gets the user's save and data directory for Fallout 3.
     * @return The Fallout 3 user data directory with trailing separator.
     */
    @Override
    public String getDir() {
        String path = Config.get(FALLOUT3_DATA_DIR);
        if(path == null)
            path = Config.get(Config.USER_DIRECTORY) + "My Games" + java.io.File.separator + "Fallout3" + java.io.File.separator;
        
        return path;
    }

    @Override
    public String getExeConfigKey() {
        return FALLOUT3_EXE;
    }

    @Override
    public String getExePath(){
        return Config.get(FALLOUT3_EXE, "C:\\Program Files\\Bethesda Softworks\\Fallout 3\\Fallout3.exe");
    }

    @Override
    public String getFullName() {
        return getName();
    }

    @Override
    public Icon getIcon(){
        return new ImageIcon(getClass().getResource("/com/chockly/pm/resources/fallout3_icon.png"));
    }

    @Override
    public String getName() {
        return "Fallout 3";
    }

    @Override
    public String getSave() {
        return Config.get(FALLOUT3_SAVE_DIR, "Saves");
    }

}
