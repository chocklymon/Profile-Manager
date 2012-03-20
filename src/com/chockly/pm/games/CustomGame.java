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

import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * This class is used to create a custom directory swapping games.
 * @author Curtis Oakley
 */
public class CustomGame extends AbstractDirGame {
    
    private final byte id;
    private final String name;
    private final String gameDir;
    private final String exe;
    private final String icon;
    private final String profileDir;
    private final String saveDir;
    
    /**
     * Creates a new Custom Game.
     * @param id The games unique id.
     * @param name The name of the game.
     * @param gameDir The directory where the games profiles and saves are stored.
     * @param exe The executable to start when launching the game.
     * @param icon The path and icon to display for the game.<br/>
     * If this is null, or the File denoted by the path doesn't exist, then when
     * requesting the game's icon a default generic icon will display.
     * @param profileDir The name of the directory where the profiles should be
     * stored.
     * @param saveDir The name of the directory where the saved games are stored.
     */
    public CustomGame(byte id,
            String name,
            String gameDir,
            String exe,
            String icon,
            String profileDir,
            String saveDir)
    {
        this.id = id;
        this.name = name;
        this.exe = exe;
        this.gameDir = gameDir;
        this.icon = icon;
        this.profileDir = profileDir;
        this.saveDir = saveDir;
    }
    
    /**
     * Returns a copy of the game with a new ID.
     * @param newId The new unique ID for the game.
     * @return A copy of the CustomGame with the new ID.
     */
    public CustomGame clone(byte newId){
        return new CustomGame(newId,
                name,
                gameDir,
                exe,
                icon,
                profileDir,
                saveDir);
    }

    @Override
    public String getDir() {
        return gameDir;
    }

    @Override
    public String getDirConfigKey() {
        throw new UnsupportedOperationException("Not supported by custom game.");
    }

    @Override
    public String getExe() {
        return exe;
    }

    @Override
    public String getExeConfigKey() {
        throw new UnsupportedOperationException("Not supported by custom game.");
    }

    @Override
    public String getFullName() {
        return name;
    }

    @Override
    public String getGameSaveDir() {
        return saveDir;
    }

    @Override
    public Icon getIcon() {
        if(icon == null || !new File(icon).exists() )
            return new ImageIcon(getClass().getResource("/com/chockly/pm/resources/game.png"));
        else
            return new ImageIcon(icon);
    }
    
    public String getIconPath(){
        return icon;
    }

    @Override
    public byte getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSave() {
        return profileDir;
    }
}
