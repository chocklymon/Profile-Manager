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
package com.chockly.pm.games;

import com.chockly.pm.Config;
import java.io.File;
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
    private static final String OBLIVION_DATA_DIR = "oblivion_data_dir";
    /**
     * Key used to retrieve/store the value indicating the name of the folder in
     * the OBLIVION_DATA_DIR where saves are stored.<br/>
     * The default value for the key is "Saves".
     */
    private static final String OBLIVION_SAVE_DIR = "oblivion_save_dir";
    /**
     * Key used to retrieve/store the value indicating the executable to run
     * when launching Oblivion.
     */
    private static final String OBLIVION_EXE = "oblivion_exe";

    @Override
    public void autoSetupProfiles() {
        String[] validExtensions = {".ess",".bak"};
        
        autoSetupProfiles(validExtensions, 0x2bL, 0, " - ", " - ", false);
    }
    
    @Override
    public String getExe(){
        return Config.get(OBLIVION_EXE,
                "C:\\Program Files\\Bethesda Softworks\\Oblivion\\Oblivion.exe");
    }
    
    @Override
    public Icon getIcon(){
        return new ImageIcon(
                getClass().getResource("/com/chockly/pm/resources/oblivion_icon.png"));
    }
    
    @Override
    public byte getId(){
        return GameFactory.OBLIVION_ID;
    }
    
    @Override
    public String getIni(){
        return "Oblivion.ini";
    }
    
    @Override
    public String getName() {
        return "Oblivion";
    }

    @Override
    public String getFullName() {
        return "The Elder Scrolls IV: Oblivion";
    }
	
    /**
     * Gets the user's save and data directory for Oblivion.
     * @return The Oblivion user data directory with trailing separator.
     */
    @Override
    public String getDir() {
        String path = Config.get(OBLIVION_DATA_DIR);
        if(path == null)
            path = Config.get(Config.Key.user_directoy.toString()) + "My Games"
                    + File.separator + "Oblivion" + File.separator;
        
        return path;
    }

    @Override
    public String getDirConfigKey() {
        return OBLIVION_DATA_DIR;
    }

    @Override
    public String getExeConfigKey() {
        return OBLIVION_EXE;
    }

    @Override
    public String getSave() {
        return Config.get(OBLIVION_SAVE_DIR, SAVES_FOLDER);
    }
}
