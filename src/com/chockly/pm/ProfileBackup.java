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
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.SwingWorker;

/**
 * Stores a profiles saved games into an archive.
 * @author Curtis Oakley
 */
public class ProfileBackup extends SwingWorker<Void, Void> {
    
    private final Profile p;
    private final ProfileManager pm;
    
    /**
     * Creates a new ProfileBackup.
     * @param p The Profile to backup.
     * @param pm The ProfileManager GUI instance. This is used to trigger that
     * the backup has completed. Can be <tt>null</tt>.
     */
    public ProfileBackup(Profile p, ProfileManager pm){
        this.p = p;
        this.pm = pm;
    }
    
    /** Stores a profile's saved games into a compressed archive. */
    public void backupProfile(){
        Game g = GameFactory.getGameFromID(p.getGameID());
        
        File dir = IOUtils.getProfileDir(g, p);
        
        if( Config.get(Config.Key.archive_format)
                .equals(Config.SEVEN_ZIP_FORMAT) )
        {
            // Use 7zip
            StringBuilder cmd = new StringBuilder(128);
                    
            cmd.append('"').append(Config.get(Config.Key.seven_zip_exe));
            cmd.append("\" a \"").append(dir.getAbsolutePath());
            cmd.append(".7z\" \"").append(g.getDir()).append(g.getSave()).append(File.separator).append(p.getSaveDir()).append('"');

            setProgress(100);

            try {
                Process process = Runtime.getRuntime().exec(cmd.toString());
                
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                
                String line;
            
                while((line = in.readLine()) != null){
                    System.out.println(line);
                }

                process.waitFor();

            } catch(Exception e){
                e.printStackTrace(System.err);
            }
        }
        else
        {
            // Use zip
            File[] files = dir.listFiles();
            double numFiles = files.length-1;
            
            int buffer = 2024;

            byte data[] = new byte[buffer];

            BufferedInputStream in = null;
            ZipOutputStream out = null;

            try {
                out = new ZipOutputStream(
                        new BufferedOutputStream(
                        new FileOutputStream(g.getDir() + g.getSave()
                        + File.separator + p.getSaveDir() + ".zip")));

                for(int i=0; i <= numFiles; i++){
                    in = new BufferedInputStream(new FileInputStream(files[i]));

                    ZipEntry entry = new ZipEntry(files[i].getName());

                    out.putNextEntry(entry);

                    int count;

                    while((count = in.read(data, 0, buffer)) != -1){
                        out.write(data, 0, count);
                    }

                    in.close();
                    out.closeEntry();
                    
                    setProgress((int) Math.floor( ((double) i / numFiles) * 100));
                }

            } catch(FileNotFoundException fnfe){
                Main.handleException("Unable to find a file while attempting to archive the profile.",
                        fnfe, Main.WARN_LEVEL);
            } catch(IOException ioe){
                Main.handleException("An exception occured while attempting to archive the profile.",
                        ioe, Main.WARN_LEVEL);
            } finally {
                try {
                    if(out != null)
                        out.close();
                    if(in != null)
                        in.close();
                } catch(IOException ioe){
                    Main.handleException("Unable to close the archiving files.",
                            ioe, Main.LOG_LEVEL);
                }
            }
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        backupProfile();
        return null;
    }
    
    @Override
    public void done(){
        if(pm != null)
            pm.finishBackupProfile();
    }
}
