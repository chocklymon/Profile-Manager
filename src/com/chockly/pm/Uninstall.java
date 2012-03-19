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

import com.chockly.pm.games.Game;
import com.chockly.pm.games.GameFactory;
import com.chockly.uninstaller.GameUninstaller;
import com.chockly.uninstaller.Uninstaller;
import java.awt.EventQueue;
import java.io.File;
import javax.swing.JFrame;

/**
 *
 * @author Curtis Oakley
 */
public class Uninstall {
    
    public static void uninstall(byte[] gameIds, final JFrame returnTo){
        final GameUninstaller[] gus = new GameUninstaller[gameIds.length];
        Game g;
        Profile[] profiles;
        for(int i=0; i<gameIds.length; i++){
            g = GameFactory.getGameFromID(gameIds[i]);
            profiles = ProfileFactory.getProfiles(gameIds[i]);
            
            File dir = new File(g.getDir());
            File[] profileDir = new File[profiles.length];
            
            for(int j=0; j<profiles.length; j++){
                profileDir[j] = new File(dir, g.getSave() + File.separator
                        + profiles[j].getSaveDir());
            }
            
            if(g.usesIni())
                gus[i] = new GameUninstaller(new File(dir, g.getSave()),
                        profileDir,
                        g.getName(),
                        new File(dir, g.getIni()),
                        "SLocalSavePath",
                        g.getSave());
            else
                gus[i] = new GameUninstaller(new File(dir, g.getSave()),
                        profileDir,
                        g.getName());
        }
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Uninstaller(returnTo, gus).setVisible(true);
            }
        });
    }
    
}
