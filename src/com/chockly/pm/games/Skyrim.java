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
 * The Elder Scrolls V: Skyrim's implementation of Game.
 * @author Curtis Oakley
 */
public class Skyrim extends BethesdaGame {
    
    /**
     * Key used to retrieve/store the value indicating the folder where the
     * Skyrim saved games are stored.<br/>
     * Like all other directory values, this one will have a trailing path
     * separator.
     */
    private static final String SKYRIM_DATA_DIR = "skyrim_data_dir";
    /**
     * Key used to retrieve/store the value indicating the name of the folder in
     * the SKYRIM_DATA_DIR where saves are stored.<br/>
     * The default value for the key is "Saves".
     */
    private static final String SKYRIM_SAVE_DIR = "skyrim_save_dir";
    /**
     * Key used to retrieve/store the value indicating The executable to run
     * when launching Skyrim.
     */
    private static final String SKYRIM_EXE = "skyrim_exe";

    @Override
    public void autoSetupProfiles() {
        String[] validExtensions = {".ess",".bak"};
        
        autoSetupProfiles(validExtensions, 0x1bL, 1, " - ", "  ", true);
    }

    @Override
    public String getExe(){
        return Config.get(SKYRIM_EXE,
                "C:\\Program Files\\Steam\\SteamApps\\common\\skyrim\\TESV.exe");
    }
    
    @Override
    public Icon getIcon(){
        return new ImageIcon(
                getClass().getResource("/com/chockly/pm/resources/skyrim_icon.png"));
    }
    
    @Override
    public String getIni(){
        return "Skyrim.ini";
    }
    
    @Override
    public byte getId(){
        return GameFactory.SKYRIM_ID;
    }
    
    @Override
    public String getName() {
        return "Skyrim";
    }

    @Override
    public String getFullName() {
        return "The Elder Scrolls V: Skyrim";
    }

    @Override
    public String getDir(){
        String dir = Config.get(SKYRIM_DATA_DIR);
        if(dir == null)
            dir = Config.get(Config.Key.user_directoy.toString()) + "My Games"
                    + File.separator + "Skyrim" + File.separator;

        return dir;
    }

    @Override
    public String getDirConfigKey() {
        return SKYRIM_DATA_DIR;
    }

    @Override
    public String getExeConfigKey() {
        return SKYRIM_EXE;
    }

    @Override
    public String getSave() {
        return Config.get(SKYRIM_SAVE_DIR, SAVES_FOLDER);
    }
}
