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
import com.chockly.pm.gui.ProfileManager;
import java.io.PrintWriter;

/**
 * Profile Manger main class.<br/>
 * <br/>
 * The profile manager uses the following files and structure (if in jar format).<br/>
 * <pre>
 * [Root]
 *   Profile Manager.jar
 *   profile_manager.ini
 *   error.log
 *  [lib]
 *    games.xml
 *    HelpSystem.jar
 *    LICENSE.TXT
 *    Uninstaller.jar
 *   [help_docs]
 *      contents.xml
 *      + The various help documentation HTML files.
 *  [Profiles]
 *    .profile_manager
 *    default.png
 *    profiles.obj
 * </pre>
 * 
 * @author Curtis Oakley
 * @version 1.4.01b
 */
public class Main {
    
    /** The current version number for the Profile Manager. */
    public static final String VERSION_NUM = "1.4.01b";

    /**
     * This exception level indicates that a fatal error has occurred. This will 
     * perform the same actions as {@link Main#WARN_LEVEL}, and then terminate 
     * the program.
     */
    public static final byte FATAL_LEVEL = 4;
    /**
     * This exception level indicates that an error has occurred. This error will
     * be logged and the user informed of its occurrence.
     */
    public static final byte WARN_LEVEL = 2;
    /**
     * This exception level indicates that a minor error has occurred. This error
     * will be logged, but the user will not be informed.
     */
    public static final byte LOG_LEVEL = 0;

    private static byte gameID = 0;
    private static int profileID = 0;
    
    private static PrintWriter errFile = null;

    /**
     * Starts up the profile manager.
     * @param args The command line arguments.<br/>
     * The following arguments are accepted:
     * <table>
     * <thead>
     * <tr><th>Argument</th><th>Effect</th></tr>
     * </thead>
     * <tbody>
     * <tr><td>-l</td><td>Outputs a list of all the profiles.</td></tr>
     * <tr><td>-m</td><td>Starts the GUI in The Elder Scrolls III: Morrowind tab.</td></tr>
     * <tr><td>-o</td><td>Starts the GUI in The Elder Scrolls IV: Oblivion tab.</td></tr>
     * <tr><td>-s</td><td>Starts the GUI in The Elder Scrolls V: Skyrim tab.</td></tr>
     * <tr><td>-f</td><td>Starts the GUI in the Fallout 3 tab.</td></tr>
     * <tr><td>-n</td><td>Starts the GUI in the Fallout: New Vegas tab.</td></tr>
     * <tr><td>-p [Profile ID]</td><td>Activates the provided profile and then launches its respective game.</td></tr>
     * <tr><td>-h or -help or help</td><td>Outputs the help.</td></tr>
     * </tbody>
     * </table>
     * Some of the arguments may be combined. For example the argument '-lo'
     * would output a list off all the profiles and then launch the GUI into
     * the Oblivion tab.
     */
    public static void main(String[] args) {
        
        // Parse the command line arguments
        try {
            for(int x=0; x<args.length; x++){

                if(args[x].charAt(0) == '-'){
                    // Flag operator - parse it
                    String arg = args[x].substring(1);
                    arg = arg.toLowerCase();

                    for(int y=0; y<arg.length(); y++){
                        switch(arg.charAt(y)){
                            case 'l': listGameProfiles(); break;
                            case 'm': setGameID(GameFactory.MORROWIND_ID); break;
                            case 'o': setGameID(GameFactory.OBLIVION_ID); break;
                            case 's': setGameID(GameFactory.SKYRIM_ID); break;
                            case 'f': setGameID(GameFactory.FALLOUT_3_ID); break;
                            case 'n': setGameID(GameFactory.FALLOUT_NV_ID); break;
                            case 'p': if(profileID == 0){
                                          x++;
                                          profileID = Integer.parseInt(args[x]);
                                      } else {
                                          System.out.println("Only one profile can be specified at a time. Using the first specified profile.");
                                      }
                                      break;
                            case 'h': printHelpAndExit(); break;
                            default: System.err.println("Invalid flag argument provided.");
                        }
                    }

                } else if(args[x].equalsIgnoreCase("help")){
                    // Help requested, print it out then exit the program.
                    printHelpAndExit();
                } else {
                    // Unkown argument.
                    System.err.println("Invalid command line argument provided.\nUse the argument 'help' to print out a list of valid arguments.");
                    break;
                }
            }
        } catch(Exception e){
            System.err.println("An exception occured while attempting to parse the command line arguments.\nUse the argument 'help' to print out a list of valid arguments.\nException: "
                    + e.toString());
        }
        
        // Load up the config
        Config.loadConfig();

        // Set the default user directoy
        if(Config.get(Config.Key.user_directory) == null){
            Config.set(Config.Key.user_directory,
                    javax.swing.filechooser.FileSystemView.getFileSystemView()
                    .getDefaultDirectory().toString() + java.io.File.separatorChar);
        }

        // TEST CODE
        // END TEST CODE

        if(profileID > 0){
            // Profile set change to that profile and launch the game
            ProfileFactory pf = ProfileFactory.getInstance();
            Profile profile = pf.getProfile(profileID);
            Game game = GameFactory.getGameFromID(profile.getGameID());
            
            if(game.activateProfile(profile)){

                pf.saveProfiles();

                try {
                    IOUtils.startProgram( game.getExe() );
                } catch(java.io.FileNotFoundException fnfe){
                    handleException(null, fnfe, WARN_LEVEL);
                }
            }
        } else {
            
            // Try to use the default system look.
            try {
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                handleException("Couldn't use the system look and feel",
                        ex, LOG_LEVEL);
            }
            
            // Find which tab to start in
            final byte startTabNum = (gameID == 0)
                    ? Byte.parseByte( Config.get(Config.Key.start_tab.toString(),
                            Byte.toString(GameFactory.SKYRIM_ID)) )
                    : gameID;
            
            // Start the GUI
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ProfileManager(startTabNum).setVisible(true);
                    //new com.chockly.pm.gui.ConfigGUI(null).setVisible(true);
                }
            });
        }

        /* TODO
         * Version 0.5  - Complete skyrim folder swap, launch game, and auto profile.                                                                 Status: COMPLETED
         * Version 0.55 - Complete skyrim create shortcut.                                                                        Due: 01/13/2012     Status: COMPLETED
         * Version 0.58 - Organize all the ini settings into a single documentation.                                              Due: 01/04/2012     Status: COMPLETED                
         * Version 0.8  - Complete for Oblivion, Fallout 3, Fallout NV the save folder ini change, launch game, and auto profile. Due: 01/20/2012     Status: COMPLETED
         * Version 0.82 - Change the skyrim to change the INI instead of renaming the folder.                                                         Status: COMPLETED
         * Version 0.83 - Redo the skyrim auto-profile to detect pre built profile folders.                                                           Status: COMPLETED
         * Version 0.85 - Add the ability to list all the profiles via a command prompt (ie. -l):                                 Due: 01/09/2012     Status: COMPLETED
         * Version 0.9  - Add the ability to remove save games when removing a profile.                                           Due: 02/13/2012     Status: COMPLETED
         * Version 1.0  - Add ability to change what program gets called for game launch in the config.                           Due: 02/13/2012     Status: COMPLETED
         * Version 1.1  - Add morrowind profile manager.                                                                          Due: 02/15/2012     Status: COMPLETED
         * Version 1.2  - Add the ability to set which games are active.                                                          Due: 02/14/2012     Status: COMPLETED
         * Version NA   - Complete/update documentation.                                                                          Due: --             Status: In-Process
         * - RELEASE -
         * Version 1.21 - Add right click to rename functionality to the profiles and double click to activate and launch.        Due: 02/18/2012     Status: COMPLETED
         * Version 1.25 - Add a selection of default profiles pictures using the Bethesdas free wallpapers.                       Due: 02/20/2012     Status: COMPLETED (Only one default was used).
         * Version 1.3  - Create an exe wrapper for the program.                                                                                      Status: COMPLETED
         * Version 1.31 - Add better handling for missing files (ini, exe, etc.), and option to scan saved games for profile.(1)  Due: 03/08/2012     Status: COMPLETED
         * Version 1.32 - Add the ability to deactivate all profiles.                                                             Due: 03/09/2012     Status: COMPLETED
         * Version 1.35 - Add auto-update profiles based on folders.(2)                                                           Due: 03/10/2012     Status: COMPLETED
         * Version 1.4  - Add backup saves functionality.                                                                         Due: 03/16/2012     Status: COMPLETED
         * Version 1.44 - Add installer                                                                                           Due: 03/23/2012     Status: COMPLETED
         * Version 1.45 - Add un-installer                                                                                        Due: 03/30/2012     Status: COMPLETED
         * Version 1.46 - Add ability to export/import profiles through xml.                                                      Due: 04/06/2012     Status:
         * Version 2.0  - Add active plugin swapper.                                                                              Due: 05/01/2012     Status:
         * Version 2.1  - Add ini swapper.                                                                                        
         * Version 2.2  - Add the create own game profile manager.(3)                                                                                 Status: In-coding
         * Version 3.0  - Add installed mesh and texture replacer swapper.(4)
         * Version ??   - Add the ability to create different profile manager profiles via command line argument.
         * Version 4.0  - Re-write into C++.
         *
         * 1 - This means that the user can specify that all saved games will be scanned for the character's name instead of reading from the file name.
         *     This will be controlled via ini only. Valid options: auto = current behavior, false = only get character name from the file name, true = always read the files contents for the chracter name.
         * 2 - Create new profile for new folder, delete profile when no folder, both of which will prompt the user for confirmation.
         * 3 - This will use the morrowind type system of renaming folders to manage profiles, along with an UI to specify the games info (ie. icon, name, full name, etc.). 
         * 4 - For example: you can have EXEMS female body replacer, but your little brother will have the vanilla bodies.
         *     This will need to interface with BAIN, OBMM, FOMM, and NMM + others to be able to detect mods as well as having it's own ability to understand mods.
         * 
         * 
         * Profile Manager, merge/split profiles.
         * 
         * Merge, take two profiles and move all their saves into one folder.
         * Split, open a prompt with two lists, move records between them to split out the two records.
         */
    }
    
    /**
     * Closes the PrintWriter used by
     * {@link com.chockly.pm.Main#handleException(java.lang.String, java.lang.Throwable, byte)}
     * to log errors to the error.log file on disk.
     */
    public static void closeErrorLog(){
        if(errFile != null)
            errFile.close();
    }

    /**
     * Handles exceptions for the Profile Manager program.<br/>
     * <br/>
     * All errors will be written to the file 'error.log' (assuming that it can
     * be created). If the error level is higher than LOG_LEVEL then the user is
     * informed of the error via a JOptionPane message box.<br/>
     * <br/>
     * This method is synchronized so as to be thread safe.
     * 
     * @param msg Any message about the error to display to the user or to log.
     * Can be null.
     * @param ex The exception to handle. Can be null.
     * @param level The exception level. The level effects how the error is 
     * handled and the user informed.<br/>
     * See each of the levels for a description of what they do: 
     * {@link Main#FATAL_LEVEL} {@link Main#WARN_LEVEL} {@link Main#LOG_LEVEL}.
     */
    public synchronized static void handleException(String msg, Throwable ex, byte level){

        // Create the error log writer as needed
        if(errFile == null){
            try {
                errFile = new PrintWriter(new java.io.FileWriter("error.log", true));
            } catch(java.io.IOException ioe){
                ioe.printStackTrace(System.err);
            }
        }
        
        // Write exceptions to disk
        if(errFile != null){
            
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            StringBuilder message = new StringBuilder(128);
            
            message.append("ERROR:\t[");
            message.append(new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(new java.util.Date()));
            message.append("]\t[");
            message.append(stack[(stack.length > 1 ? 2 : 1)].getMethodName());
            message.append("|");
            message.append(VERSION_NUM);
            message.append("]");
                    
            if(msg != null){
                message.append("\t");
                message.append(msg);
            }
            
            message.append(System.getProperty("line.separator"));

            errFile.append(message.toString());
            
            if(ex != null)
                ex.printStackTrace(errFile);

            errFile.flush();
        }
        
        if(msg == null)
            msg = "An exception has occured!";
        
        if(ex != null)
            msg += "\n\nError Message:\n" + ex.toString();

        // Inform the user of the error if the level indicates to do so.
        if(level > LOG_LEVEL)
            javax.swing.JOptionPane.showMessageDialog(null,
                    msg,
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
	
        // If the error is categorized as fatal, stop the program.
        if(level == FATAL_LEVEL){
            closeErrorLog();
            System.exit(1);
        }
    }

    /** Prints out all the Profiles to the system's standard out. */
    private static void listGameProfiles(){
        byte[] ids = GameFactory.getAllGameIds();
        Profile[] p;
        
        StringBuilder output = new StringBuilder(128);
        output.append("Game\tProfile ID\tProfile Name\tProfile Dir\n"
                + "=====================================================\n");
        
        ProfileFactory pf = ProfileFactory.getInstance();
        
        for(byte id : ids){
            p = pf.getProfiles(id);
            if(p.length > 0){
                output.append("- ");
                output.append(GameFactory.getNameFromID(id));
                output.append(" -\n");
                
                for(int x=0; x<p.length; x++){
                    output.append("\t");
                    output.append(p[x].getID());
                    output.append("\t\t");
                    
                    String name = p[x].getName();
                    output.append(name);
                    output.append("\t");
                    if(name.length() < 8)
                        output.append("\t");
                    
                    output.append(p[x].getSaveDir());
                    output.append("\n");
                }
            }
        }
        System.out.print(output.toString());
    }

    /** 
     * Prints out the command line arguments that this program accepts and then
     * exits the program.
     */
    private static void printHelpAndExit(){
        System.out.println("-Help for the pofile manager-\n" +
"The following are the command line arguments that the program accepts:\n" +
"Argument  Effect\n" +
"=================================================\n" +
"-l        Displays a list of all the profiles.\n" +
"-m        Starts the program in The Elder Scrolls III: Morrowind tab.\n" +
"-o        Starts the program in The Elder Scrolls IV: Oblivion tab.\n" +
"-s        Starts the program in The Elder Scrolls V: Skyrim tab.\n" +
"-f        Starts the program in the Fallout 3 tab.\n" +
"-n        Starts the program in the Fallout: New Vegas tab.\n" +
"-p [ID]   Activates the provided profile and then launches its respective game.\n" +
"help      Outputs this help documentation.\n"
                );
        System.exit(0);
    }

    /**
     * Sets the gameID from the one sent in through the command line argument.<br/>
     * <br/>
     * Only permits the gameID to be set once even if multiple game IDs are sent
     * in through the command line arguments.
     * @param id The game ID from the command line arguments.
     */
    private static void setGameID(byte id){
        if(gameID == 0)
            gameID = id;
        else
            System.out.println("Only one game can be specified at a time. Using the first specified game only.");
    }

}
