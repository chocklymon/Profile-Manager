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
import com.chockly.pm.XMLUtils;
import java.io.File;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 *
 * @author Curtis Oakley
 */
final class CustomGameFactory {
    
    private static final CustomGameFactory instance = new CustomGameFactory();
    
    private static final String CUSTOM_GAME_FILE = "lib/games.xml";
    
    private CustomGame[] customGames = null;
    
    // TODO idea: this could be renamed GameLoader and then load/handle xml games and .class games.
    
    private CustomGameFactory(){
        // Load custom games
        if(new File(CUSTOM_GAME_FILE).exists())
            customGames = XMLUtils.GamesFromXML(CUSTOM_GAME_FILE);
        
        if(customGames == null)
            // Create an empty custom game
            customGames = new CustomGame[0];
        else
            // check for id conflicts
            checkForConflicts();
    }
    
    static CustomGameFactory getInstance(){
        return instance;
    }

    void addCustomGame(String name,
            String gameDir,
            String exe,
            String icon,
            String profileDir,
            String saveDir)
    {
        byte id = getNextAvailableId();
        
        // Increase the size of the array and add the new custom game.
        customGames = Arrays.copyOf(customGames, customGames.length + 1);
        customGames[customGames.length - 1] = new CustomGame(id,
                name, gameDir, exe, icon, profileDir, saveDir);
        
        // Save the changes
        saveCustomGames();
        
        // Put the custom game into the active games configuration.
        String activeTabs = Config.get(Config.Key.active_tabs);
        
        if(activeTabs != null)
            Config.set(Config.Key.active_tabs, activeTabs + ',' + id);
    }

    void addCustomGames(CustomGame[] games){
        int start = customGames.length;
        byte newId = getNextAvailableId();
        String activeTabs = Config.get(Config.Key.active_tabs);

        customGames = Arrays.copyOf(customGames, start + games.length);
        for(int i=0; i<games.length; i++){
            customGames[start + i] = games[i].clone(newId);

            if(activeTabs != null){
                if( !activeTabs.contains(Byte.toString(newId)))
                    activeTabs += "," + newId;
            }

            newId++;
        }
        
        saveCustomGames();

        if(activeTabs != null)
            Config.set(Config.Key.active_tabs, activeTabs);
    }
    
    /** Checks if any of the game IDs are the same (conflicting). */
    private void checkForConflicts(){
        // Check for conflicts with the built in ids
        boolean changed = checkForConflicts(GameFactory.getAllBuiltInGameIds());
        
        // Check for conflicts with the custom games
        byte[] ids = new byte[customGames.length];
        for(int i=0; i<customGames.length; i++){
            ids[i] = customGames[i].getId();
        }
        changed = checkForConflicts(ids) ? true : changed;
        
        // Save the profile id changes if needed
        if(changed)
            saveCustomGames();
    }
    
    /**
     * Checks if any of the custom games have the same ID number.
     * @param checkMe The byte array to check against.
     * @return True if a game had a conflict.
     */
    private boolean checkForConflicts(byte[] checkMe){
        boolean changed = false;
        for(int i=0; i<customGames.length; i++){
            for(int j=0; j<checkMe.length; j++){
                if(customGames[i].getId() == checkMe[j]){
                    if(i == j && checkMe[j] > GameFactory.FALLOUT_NV_ID)
                        continue;// Same game skip
                    
                    changed = true;
                    
                    String[] options = new String[] {"Update Id","Ignore"};
                    
                    if( JOptionPane.showOptionDialog(null,
                            "The game '" + customGames[i].getName() + "' has the same ID number as another game.\n\nYou can change this game's ID number, or ignore this game and not load it.",
                            "Game ID Conflict",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]) == JOptionPane.YES_OPTION )
                    {
                        // Change the games id
                        customGames[i] = customGames[i].clone(getNextAvailableId());
                        checkMe[i] = customGames[i].getId();
                    }
                    else
                    {
                        // Remove the CustomGame
                        splice(i);
                    }
                }
            }
        }
        return changed;
    }
    
    Game getGameFromId(byte gameID){
        for(int i=0; i<customGames.length; i++){
            if(customGames[i].getId() == gameID)
                return customGames[i];
        }
        return null;
    }
    
    /**
     * Returns all game IDs.
     * @return All the game IDs in a byte array.
     */
    byte[] getIds(){
        byte[] allIds = new byte[customGames.length];
        
        // Populate the ids array
        for(int i=0; i<allIds.length; i++){
            allIds[i] = customGames[i].getId();
        }

        return allIds;
    }
    
    /**
     * Gets the next open ID number.
     * @return The next open ID number.
     */
    private byte getNextAvailableId(){
        byte max = GameFactory.FALLOUT_NV_ID+1;
        
        for(int i=0; i<customGames.length; i++){
            if(customGames[i].getId() >= max){
                max = customGames[i].getId();
                max++;
            }
        }
        
        return max;
    }
    
    /**
     * Removes the custom game from the list of custom games.
     * @param id The id number of the game to remove.
     */
   void removeCustomGame(byte id){
        for(int i=0; i<customGames.length; i++){
            if(id == customGames[i].getId()){
                // Remove the custom Game from the array and save
                splice(i);
                saveCustomGames();
                
                // Remove from the active tabs
                String activeTabs = Config.get(Config.Key.active_tabs);
                if(activeTabs != null){
                    if(activeTabs.contains("," + id + ",")){
                        // Game id in the middle of the tabs
                        activeTabs = activeTabs.replace("," + id + ",", ",");
                    } else if(activeTabs.endsWith("," + id)){
                        // Game id at the end
                        activeTabs = activeTabs.substring(0, activeTabs.lastIndexOf(","));
                    } else if(activeTabs.startsWith(id + ",")){
                        // Game id at the start
                        activeTabs = activeTabs.substring(activeTabs.indexOf(",")+1);
                    }

                    Config.set(Config.Key.active_tabs, activeTabs);
                }
                
                break;
            }
        }
    }
   
    /**
     * Removes the specified index from the custom game array. Returning an
     * array with a length one shorter.
     * @param array The CustomGame array to splice.
     * @param index The index of the CustomGame to remove.
     * @return The array with with the specified index removed.
     */
    private void splice(int index){
        // TODO, try to remove uses of this method (ie, make the custom game list function like an ArrayList)s
        CustomGame[] temp = new CustomGame[customGames.length-1];
        if(temp.length > 0){
            System.arraycopy(customGames, 0, temp, 0, index);
            System.arraycopy(customGames, index+1, temp, index, temp.length-index);
        }
        customGames = temp;
    }
    
    /** Saves the custom games to disk. */
    void saveCustomGames(){
        if(customGames != null)
            XMLUtils.GamesToXML(customGames, CUSTOM_GAME_FILE);
    }

    /**
     * Updates the provided custom game with the new information provided.
     * @param game The custom game to update.
     * @param name The new name of the game.
     * @param gameDir The new game directory.
     * @param exe The new executable.
     * @param icon The new icon.
     * @param profileDir The new profile directory name.
     * @param saveDir The new save directory name.
     */
    void updateCustomGame(CustomGame game,
            String name,
            String gameDir,
            String exe,
            String icon,
            String profileDir,
            String saveDir)
    {
        for(int i=0; i<customGames.length; i++){
            if(customGames[i].equals(game)){
                customGames[i] = new CustomGame(game.getId(),
                        name, gameDir, exe, icon, profileDir, saveDir);
                saveCustomGames();
            }
        }
    }
    
    // TODO look into custom class loading (see http://www.exampledepot.com/egs/java.lang/LoadClass.html)
    
}
