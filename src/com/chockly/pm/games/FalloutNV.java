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
 * Fallout: New Vegas Game.
 * @author Curtis Oakley
 */
public class FalloutNV extends BethesdaGame {
    
    /**
     * Key used to retrieve/store the value indicating the folder where the
     * Fallout: New Vegas saved games are stored.<br/>
     * Like all other directory values, this one will have a trailing path
     * separator.
     */
    public static final String NEW_VEGAS_DATA_DIR = "new_vegas_data_dir";
    /**
     * Key used to retrieve/store the value indicating the name of the folder in
     * the NEW_VEGAS_DATA_DIR where saves are stored.<br/>
     * The default value for the key is "Saves".
     */
    public static final String NEW_VEGAS_SAVE_DIR = "new_vegas_save_dir";
    /**
     * Key used to retrieve/store the value indicating the executable to run
     * when launching Fallout: New Vegas.
     */
    public static final String NEW_VEGAS_EXE = "new_vegas_exe";

    @Override
    public void autoSetupProfiles() {
        String[] validExtensions = {".fos",".bak"};
        
        autoSetupProfiles(validExtensions, 0x67L, 2, "   ", "  ", true,
                GameFactory.FALLOUT_NV_ID);
    }
    
    @Override
    public String getExePath(){
        return Config.get(NEW_VEGAS_EXE, "C:\\Program Files\\Steam\\SteamApps\\common\\Fallout New Vegas\\FalloutNV.exe");
    }
    
    @Override
    public Icon getIcon(){
        return new ImageIcon(getClass().getResource("/com/chockly/pm/resources/fallout_new_vegas_icon.png"));
    }
    
    @Override
    public String getName() {
        return "New Vegas";
    }

    @Override
    public String getFullName() {
        return "Fallout: New Vegas";
    }

    @Override
    public boolean activateProfile(Profile profile) {
        return activateProfile("Fallout.ini", profile);
    }
	
    /**
     * Gets the user's save and data directory for Fallout: New Vegas.
     * @return The Fallout: New Vegas user data directory with trailing separator.
     */
    @Override
    public String getDir() {
        String path = Config.get(NEW_VEGAS_DATA_DIR);
        if(path == null)
            path = Config.get(Config.USER_DIRECTORY) + "My Games" + java.io.File.separator + "FalloutNV" + java.io.File.separator;
        
        return path;
    }

    @Override
    public String getDataDirKey() {
        return NEW_VEGAS_DATA_DIR;
    }

    @Override
    public String getExeConfigKey() {
        return NEW_VEGAS_EXE;
    }

    @Override
    public String getSave() {
        return Config.get(NEW_VEGAS_SAVE_DIR, "Saves");
    }

}
