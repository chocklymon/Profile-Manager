/*
 * Copyright (C) 2012 Curtis
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

import com.chockly.pm.games.Game;
import com.chockly.pm.games.GameFactory;

/**
 *
 * @author Curtis
 */
public class BuildIni {
    
    public static void buildDefaultIni(){
        try {
            RegistryReader reg = new RegistryReader();
            
            byte[] gameIds = GameFactory.getAllGameIds();
            
            for(int x=0; x<gameIds.length; x++){
                
                String key;
                
                switch(gameIds[x]){
                    case GameFactory.MORROWIND_ID: key = "Morrowind"; break;
                    case GameFactory.OBLIVION_ID: key = "Oblivion"; break;
                    case GameFactory.SKYRIM_ID: key = "Skyrim"; break;
                    case GameFactory.FALLOUT_3_ID: key = "Fallout3"; break;
                    case GameFactory.FALLOUT_NV_ID: key = "FalloutNV"; break;
                    default: System.err.println("Invalid Game ID"); continue;
                }
                
                String keyValue = reg.getKeyValue("Bethesda Softworks\\" + key, "Installed Path");
            
                Game g = GameFactory.getGameFromID(gameIds[x]);

                if( keyValue != null && !keyValue.isEmpty()){
                    
                    String exe = g.getExePath();
                    exe = exe.substring(exe.lastIndexOf('\\')+1);
                    
                    // Make sure that the keyValue has a trailing backslash
                    if(keyValue.charAt(keyValue.length()-1) != '\\')
                        keyValue += '\\';
                    
                    Config.set(g.getExeConfigKey(), keyValue + exe);
                    
                    if(g.usesExternalProfileDir())
                        Config.set(g.getDataDirKey(), keyValue);
                }

            }
        } catch(Exception ex){
            System.err.println("Exception occured while trying to build the ini");
            ex.printStackTrace(System.err);
        }
    }
    
}
