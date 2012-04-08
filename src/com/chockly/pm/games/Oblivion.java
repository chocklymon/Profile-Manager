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
 * The Elder Scrolls: IV Oblivion's implementation of Game.
 * @author Curtis Oakley
 */
public class Oblivion extends BethesdaGame {
    
    /**
     * Key used to retrieve/store the value indicating the folder where the
     * Oblivion saved games are stored.<br/>
     * Like all other directory values, this one will have a trailing path
     * separator.
     */
    public static final String OBLIVION_DATA_DIR = "oblivion_data_dir";
    /**
     * Key used to retrieve/store the value indicating the name of the folder in
     * the OBLIVION_DATA_DIR where saves are stored.<br/>
     * The default value for the key is "Saves".
     */
    public static final String OBLIVION_SAVE_DIR = "oblivion_save_dir";
    /**
     * Key used to retrieve/store the value indicating the executable to run
     * when launching Oblivion.
     */
    public static final String OBLIVION_EXE = "oblivion_exe";

    @Override
    public void autoSetupProfiles() {
        String[] validExtensions = {".ess",".bak"};
        
        autoSetupProfiles(validExtensions, 0x2bL, 0, " - ", " - ", false,
                GameFactory.OBLIVION_ID);
    }
    
    @Override
    public String getExePath(){
        return Config.get(OBLIVION_EXE,
                "C:\\Program Files\\Bethesda Softworks\\Oblivion\\Oblivion.exe");
    }
    
    @Override
    public Icon getIcon(){
        return new ImageIcon(
                getClass().getResource("/com/chockly/pm/resources/oblivion_icon.png"));
    }
    
    @Override
    public String getName() {
        return "Oblivion";
    }

    @Override
    public String getFullName() {
        return "The Elder Scrolls IV: Oblivion";
    }

    @Override
    public boolean activateProfile(Profile profile) {
        return activateProfile("Oblivion.ini", profile);
    }
	
    /**
     * Gets the user's save and data directory for Oblivion.
     * @return The Oblivion user data directory with trailing separator.
     */
    @Override
    public String getDir() {
        String path = Config.get(OBLIVION_DATA_DIR);
        if(path == null)
            path = Config.get(Config.USER_DIRECTORY) + "My Games"
                    + java.io.File.separator + "Oblivion" + java.io.File.separator;
        
        return path;
    }

    @Override
    public String getDataDirKey() {
        return OBLIVION_DATA_DIR;
    }

    @Override
    public String getExeConfigKey() {
        return OBLIVION_EXE;
    }

    @Override
    public String getSave() {
        return Config.get(OBLIVION_SAVE_DIR, "Saves");
    }

}
