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

import com.chockly.pm.games.Game;
import com.chockly.pm.games.GameFactory;
import com.chockly.pm.win86.JLnk;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;

/**
 * Contains static methods to help perform I/O operations.
 * @author Curtis Oakley
 */
public class IOHelper {

    /**
     * Checks the provided game's save directory for any folders that have been
     * added or removed, and then adds or deletes profiles as appropriate.
     * @param gameID The ID of the game to check.
     * @return <tt>True</tt> if any profiles where added or removed, <tt>false</tt>
     * otherwise.
     */
    public static boolean checkForProfileDirChanges(byte gameID){
        Game g = GameFactory.getGameFromID(gameID);
        boolean changes = false;
        File saveDir = new File(g.getDir(), g.getSave());
        
        if(saveDir.exists()){
            File[] folders = saveDir.listFiles();
            if(folders == null)
                return false;
            
            Profile[] profiles = ProfileFactory.getProfiles(gameID);
            
            UpdateProfilesChecker check = new UpdateProfilesChecker();

            // Check for new files
            for(int i=0; i<folders.length; i++){
                if(folders[i].isDirectory()){
                    // See if this directory is already used by a profile
                    String name = folders[i].getName();
                    if( !dirInUse(name, profiles)){
                        // Folder not used, add a new profile
                        if(check.createProfile(name)){
                            ProfileFactory.addProfile(name, name, gameID);
                            changes = true;
                        }
                    }
                }
            }

            // Check for removed profile folders
            for(int i=0; i<profiles.length; i++){
                if( !new File(saveDir, profiles[i].getSaveDir()).exists() ){
                    // File removed
                    
                    if(profiles[i].isActive() && !g.usesIni())
                        // Profile is active for folder swapping game, ignore
                        continue;
                    
                    if(check.deleteProfile(profiles[i].getName())){
                        // Delete the profile
                        ProfileFactory.removeProfile(profiles[i]);
                        changes = true;
                    }
                }
            }
        }
        
        return changes;
    }
    
    /**
     * Attempts to create a directory and it's parent directory.<br/>
     * Informs the user if the folder isn't created.
     * @param folder The directory to create.
     */
    public static void createFolder(File folder) {
        if( ! folder.exists()){
            if( ! folder.mkdirs() ){
                Main.handleException(
                        "Unable to create the folder '" + folder.getPath() + "'",
                        null, Main.WARN_LEVEL);
            }
        }
    }
    
    /**
     * Creates a shortcut on the user's desktop that activates a profile and 
     * launches its game when clicked.
     * @param profile The Profile to create the shortcut for.
     * @throws IOException
     * @throws InterruptedException 
     */
    public static void createShortcut(Profile profile) throws IOException, InterruptedException{
        Game game = GameFactory.getGameFromID(profile.getGameID());
        
        JLnk link = new JLnk();

        link.setFolder(System.getProperty("user.home") + "\\Desktop");
        link.setName(profile.getName() + " " + game.getName());
        
        // Get the jar's name and location
        try {
            link.setPath(new File(
                    IOHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .getAbsolutePath());
            
        } catch (URISyntaxException ex) {
            throw new IOException(ex.getMessage(), ex);
        }
        
        link.setArguments("-p " + profile.getID());
        link.setIconLocation(game.getExe());
        link.setDescription("Start " + game.getName() + " as " + profile.getName());

        link.save();
    }
    
    /**
     * Deletes the Profile's save game directory and all of it's contents.
     * @param p The Profile to delete the directory of.
     */
    public static void deleteProfileDir(Profile p){
        
        File dir = getProfileDir(GameFactory.getGameFromID(p.getGameID()), p);
        
        if( !deleteFile(dir) )
            Main.handleException("Unable to delete all the files associated with the profile \""
                    + p.getName() + "\"",
                    null, Main.WARN_LEVEL);
    }
    
    /**
     * Detects if the provided directory name is in use by a profile.
     * @param dir The directory name to check.
     * @param profiles The profiles to check against.
     * @return True if the directory is used by one of the profiles, false
     * otherwise.
     */
    private static boolean dirInUse(String dir, Profile[] profiles){
        for(int i=0; i<profiles.length; i++){
            if(profiles[i].getSaveDir().equals(dir))
                return true;
        }
        return false;
    }
    
    /**
     * Deletes the provided file or directory.<br/>
     * <br/>
     * If the provided File is a directory then it's contents will be deleted
     * first.
     * @param f The file to delete.
     * @return True if the file was successfully deleted, false otherwise.
     */
    private static boolean deleteFile(File f){
        boolean success = true;
        
        if(f.exists()){
            if(f.isDirectory()){
                File[] files = f.listFiles();
                for(int i=0; i<files.length; i++){
                    if( !deleteFile(files[i]) )
                        success = false;
                }
            }
            if( !f.delete() )
                success = false;
        }
        
        return success;
    }
    
    /**
     * Gets the file extension of the provided file.
     * @param f The file to get the extension of.
     * @return The file's extension or <tt>null</tt> if it has no extension.<br/>
     * The returned string will be in lower case, and not have the extension's period.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    /**
     * Gets the directory of Profile.
     * @param g The Profile's game.
     * @param p The Profile to get the directory of.
     * @return The File indicating the Profiles directory.
     */
    public static File getProfileDir(Game g, Profile p){
        if( !g.usesIni() && p.isActive())
            return new File(g.getDir() + g.getGameSaveDir());
        else
            return new File(g.getDir() + g.getSave() + File.separator + p.getSaveDir());
    }
    
    /**
     * Moves a file to another folder.
     * @param source The file to move. This <b>must</b> be a file, and not a
     * directory.
     * @param dest The path and file name to move the source to.
     */
    public static void moveFile(File source, File dest){

        StringBuilder debugMessage = new StringBuilder(64);
        debugMessage.append("Unable to move:\n");
        debugMessage.append(source.getPath());
        debugMessage.append("\nto:\n");
        debugMessage.append(dest.getPath());
        debugMessage.append("\n\n");

        // Make the parent folder
        File destParent = dest.getParentFile();

        if(destParent != null && ! destParent.exists() ){
            if( ! destParent.mkdirs() )
                debugMessage.append("Parent directory failed to create.\n");
        }

        // Try to rename the file
        if( ! source.renameTo(dest) ){

            // Rename failed, attempt to manually copy
            try {
                if( ! dest.exists() )
                    dest.createNewFile();

                FileChannel sourceChan = new FileInputStream(source).getChannel();
                FileChannel destination = new FileOutputStream(dest).getChannel();

                long count =0;
                long size = sourceChan.size();
                while( count < size ){
                    count += destination.transferFrom(sourceChan, 0, size-count);
                }

                sourceChan.close();
                destination.close();

                if( size == count)
                    source.delete();
                else
                    Main.handleException(
                            debugMessage.append("All of the file failed to copy for an unkown reason.\nOrginal File Size: ")
                            .append(size).append("  New File Size: ").append(count).toString(),
                            null, Main.WARN_LEVEL);

            } catch(IOException ioe){
                Main.handleException(debugMessage
                        .append("Rename and copy failed.").toString(),
                        ioe, Main.WARN_LEVEL);
            }
        }
    }
    
    /**
     * Changes the value of the provided key in the given .ini type file, or 
     * adds the key and value if it doesn't exist.<br/>
     * <br/>
     * This will only work on MS INI formated files that use the standard 
     * key=value arrangement. Attempting to use this method on other file 
     * types/formats may have unexpected results including and quite likely
     * corrupting the provided file.
     * 
     * @param fileName The name of the file to edit.
     * @param key The key to search for. Do not include the equals sign.
     * @param value The value to replace the keys current value with.
     * @param section The section of the ini that the key value pair should be
     * placed in if the key doesn't currently exist. If the section doesn't exist
     * it will be appended to the end of the file. Sections names should contain
     * the opening and closing square brackets. For example: "[General]".
     * 
     * @throws FileNotFoundException If the file denoted by fileName doesn't exist.
     */
    public static void setINIValue(String fileName, String key, String value, String section)
            throws FileNotFoundException
    {
        try{
            // Read in the game ini
            BufferedReader in = new BufferedReader(
                    new FileReader(fileName));

            String line, newLine = System.getProperty("line.separator");
            StringBuilder file = new StringBuilder(1024);

            while( (line = in.readLine()) != null ){
                file.append(line);
                file.append(newLine);
            }
            in.close();

            // Find the key's value start location
            int start = file.indexOf(key);
            
            // Check if the key was found
            if(start == -1){
                // Key not found, add it.
                start = file.indexOf(section);
                
                String keyValue = key + "=" + value + newLine;
                
                if(start == -1){
                    // Section not found append it.
                    file.append(newLine);
                    file.append(section);
                    file.append(newLine);
                    
                    file.append(keyValue);
                    
                } else {
                    // Section found, append the line after it
                    start += section.length() + newLine.length();
                    file.insert(start, keyValue);
                }
                
            } else {
                // Key found, update it
                start += key.length() + 1;

                // Replace the current value with the new value
                file.replace(start,
                        file.indexOf(newLine, start),
                        value);
            }

            // Save the file information back to disk
            try{
                BufferedWriter out = new BufferedWriter(new
                        FileWriter(fileName));

                out.write(file.toString());

                out.flush();
                out.close();

            } catch(IOException ioe){
                Main.handleException(
                        "An IO Exception has occured while saving the game's INI file to disk.",
                        ioe, Main.WARN_LEVEL);
            }
        } catch(FileNotFoundException fnfe){
            throw new FileNotFoundException(fnfe.getMessage());
        } catch(IOException ioe){
            Main.handleException(
                    "An IO Exception has occured while attempting to read the file '" + fileName + "'",
                    ioe, Main.WARN_LEVEL);
        }
    }

    /**
     * Starts the provided executable file in a separate process.
     * @param exe The name and location of an executable file to start.
     * @throws FileNotFoundException If the provided executable doesn't exist.
     */
    public static void startProgram(String exe) throws FileNotFoundException {
        
        if( ! new File(exe).exists())
            throw new FileNotFoundException("Unable to find the specified executable \""
                    + exe + '"');
        
        try
        {
            // See if the exe has a path
            if(exe.contains(File.separator)){
                // Extract the path and then change the working directory to the path
                String path = exe.substring(0, exe.lastIndexOf(File.separator));

                Runtime.getRuntime().exec("cmd /c pushd \"" + path
                        + "\" & cmd /c \"" + exe + '"');
            
            } else {
                // Attempt to run the file
                Runtime.getRuntime().exec("cmd /c \"" + exe + '"');
            }
        }
        catch (IOException ioe)
        {
            Main.handleException("Unable to start " + exe + '.',
                    ioe, Main.WARN_LEVEL);
        }
    }
}
