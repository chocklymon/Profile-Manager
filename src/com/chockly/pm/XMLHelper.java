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

import com.chockly.pm.games.CustomGame;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Contains methods to help convert Profile Manager objects into XML documents,
 * and to read this objects from XML files.
 * 
 * @author Curtis Oakley
 */
public class XMLHelper {
    
    /**
     * Outputs the provided games to an XML file.
     * @param games The games to save into the XML file.
     * @param fileName The name of the xml file to store the Games into.
     */
    public static void GamesToXML(CustomGame[] games, String fileName){
        try {
            Document doc = getDocument();
            
            Element root = doc.createElement("profile_manager_games");
            doc.appendChild(root);
            
            for(int i=0; i<games.length; i++){
                Element game = doc.createElement(GAME_TAG);
                game.setAttribute(ID_ATR, Integer.toString(games[i].getId()));
                
                root.appendChild(game);
                
                appendTextNode(DIR_TAG, doc, game, games[i].getDir());
                appendTextNode(EXE_TAG, doc, game, games[i].getExe());
                appendTextNode(SAVE_DIR_TAG, doc, game, games[i].getGameSaveDir());
                appendTextNode(NAME_TAG, doc, game, games[i].getName());
                appendTextNode(PROFILE_DIR_TAG, doc, game, games[i].getSave());
                appendTextNode(IMG_TAG, doc, game, games[i].getIconPath());
            }
            
            store(fileName, doc);
            
        } catch (Exception ex) {
            Main.handleException("Exception occured while saving games to XML file.",
                    ex, Main.WARN_LEVEL);
        }
    }
    
    /**
     * Loads up custom games from an XML file.
     * @param fileName The name of the xml file to load the games from.
     * @return An array of CustomGames retrieved from the file, or <tt>null</tt>
     * if no games where loaded.
     */
    public static CustomGame[] GamesFromXML(String fileName){
        
        CustomGame[] games;
        
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(new File(fileName));
            
            Element root = doc.getDocumentElement();
            root.normalize();
            
            NodeList gNodes = root.getElementsByTagName(GAME_TAG);
            
            games = new CustomGame[gNodes.getLength()];
            
            String name = null, gameDir = null, exe = null, icon = null,
                    profileDir = null, saveDir = null;
            
            for(int i=0, nodeIndex=0; i<games.length; i++, nodeIndex++){
                Node n = gNodes.item(nodeIndex);
                
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    Element game = (Element) n;
                    NodeList gData = game.getChildNodes();

                    for(int j=0; j<gData.getLength(); j++){

                        n = gData.item(j);

                        if(n.getNodeType() == Node.ELEMENT_NODE){
                            Element e = (Element) n;

                            if(e.getTagName().equals(DIR_TAG))
                                gameDir = getTagValue(e);
                            else if(e.getTagName().equals(EXE_TAG))
                                exe = getTagValue(e);
                            else if(e.getTagName().equals(IMG_TAG))
                                icon = getTagValue(e);
                            else if(e.getTagName().equals(NAME_TAG))
                                name = getTagValue(e);
                            else if(e.getTagName().equals(PROFILE_DIR_TAG))
                                profileDir = getTagValue(e);
                            else if(e.getTagName().equals(SAVE_DIR_TAG))
                                saveDir = getTagValue(e);
                        }
                    }
                    
                    if(name == null
                            || gameDir == null
                            || exe == null
                            || profileDir == null
                            || saveDir == null)
                    {
                        // Invalid remove from the profile array
                        games = ArrayHelper.splice(games, i);
                        i--;
                    } else {
                        games[i] = new CustomGame(Byte.parseByte(game.getAttribute(ID_ATR)),
                            name, gameDir, exe, icon, profileDir, saveDir);
                    }

                    // Reset the variables
                    name = null; gameDir = null; exe = null; icon = null;
                    profileDir = null; saveDir = null;
                    
                } else {
                    i--;
                }
            }
            
        } catch (Exception ex) {
            Main.handleException("Exception occured while loading the custom game(s).",
                    ex, Main.WARN_LEVEL);
            games = null;
        }
        
        return games;
    }
    
    /**
     * Saves games to an XML file.
     * @param games The games to save.
     * @param fileName The name of the XML file to save the games to.
     */
    public static void ProfilesToXML(Profile[] profiles, String fileName){
        try {
            Document doc = getDocument();
            
            Element root = doc.createElement("profile_manager_profiles");
            doc.appendChild(root);
            
            for(int i=0; i<profiles.length; i++){
                Element profile = doc.createElement(PROFILE_TAG);
                profile.setAttribute(ID_ATR, Integer.toString(profiles[i].getID()));
                profile.setAttribute(GAME_ID_ATR, Integer.toString(profiles[i].getGameID()));
                
                root.appendChild(profile);
                
                appendTextNode(NAME_TAG, doc, profile, profiles[i].getName());
                appendTextNode(IMG_TAG, doc, profile, profiles[i].getImage());
                appendTextNode(DIR_TAG, doc, profile, profiles[i].getSaveDir());
            }
            
            store(fileName, doc);
            
            
        } catch (Exception ex) {
            Main.handleException("Exception occured while saving profiles to XML file.",
                    ex, Main.WARN_LEVEL);
        }
    }
    
    /**
     * Loads games from an XML file.
     * @param fileName The name of the XML file to get the games from.
     * @return An array of Profiles retrieved from the XML file, or <tt>null</tt>
     * if no games where found.
     */
    public static Profile[] ProfilesFromXML(String fileName){
        
        Profile[] profiles;
        
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(new File(fileName));
            
            Element root = doc.getDocumentElement();
            root.normalize();
            
            NodeList pNodes = root.getElementsByTagName(PROFILE_TAG);
            
            profiles = new Profile[pNodes.getLength()];
            
            String name = null, dir = null, img = null;
            
            for(int i=0, nodeIndex=0; i<profiles.length; i++, nodeIndex++){
                Node n = pNodes.item(nodeIndex);
                
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    Element profile = (Element) n;
                    NodeList pData = profile.getChildNodes();

                    for(int j=0; j<pData.getLength(); j++){

                        n = pData.item(j);

                        if(n.getNodeType() == Node.ELEMENT_NODE){
                            Element e = (Element) n;

                            if(e.getTagName().equals(DIR_TAG))
                                dir = getTagValue(e);
                            else if(e.getTagName().equals(NAME_TAG))
                                name = getTagValue(e);
                            else if(e.getTagName().equals(IMG_TAG))
                                img = getTagValue(e);
                        }
                    }
                    
                    if(name == null || dir == null){
                        // Invalid remove from the profile array
                        Profile[] temp = new Profile[profiles.length-1];
                        if(temp.length > 0){
                            System.arraycopy(profiles, 0, temp, 0, i);
                            System.arraycopy(profiles, i+1, temp, i, temp.length-i);
                        }
                        profiles = temp;
                        
                        i--;
                    } else {
                        profiles[i] = new Profile(name, dir, 
                                Byte.parseByte(profile.getAttribute(GAME_ID_ATR)),
                                Integer.parseInt(profile.getAttribute(ID_ATR)));
                        profiles[i].setImage(img);
                    }

                    // Reset the variables
                    name = null; dir = null; img = null;
                } else {
                    i--;
                }
            }
            
        } catch (Exception ex) {
            Main.handleException("Exception occured while loading the profile(s).",
                    ex, Main.WARN_LEVEL);
            profiles = null;
        }
        
        return profiles;
    }
    
    /**
     * Adds a text node to the provided element.
     * @param nodeName The name of the node.
     * @param doc The XML Document.
     * @param parent The Element node to attach the text node to.
     * @param text The text to place in the node.
     */
    private static void appendTextNode(String nodeName, Document doc, Element parent, String text){
        if(text != null){
            Element el = doc.createElement(nodeName);
            el.appendChild(doc.createTextNode(text));
            parent.appendChild(el);
        }
    }
    
    /**
     * Builds and XML document.
     * @return The XML document.
     * @throws ParserConfigurationException 
     */
    private static Document getDocument() throws ParserConfigurationException{
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return docBuilder.newDocument();
    }
    
    /**
     * Gets the text value from the specified element.
     * @param e The element to get the text content from.
     * @return The text value of the element, or null if the tag is empty.
     */
    private static String getTagValue(Element e) {
        Node n = (Node) e.getChildNodes().item(0);
        if(n == null)
            return null;
                    
        return n.getNodeValue();
    }
    
    /**
     * Saves the XML file to disk.
     * @param fileName The name of the file.
     * @param doc The XML Document to save to disk.
     * @throws TransformerConfigurationException
     * @throws TransformerException 
     */
    private static void store(String fileName, Document doc)
            throws TransformerConfigurationException, TransformerException
    {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        
        transformer.transform( new DOMSource(doc),
                new StreamResult(new File(fileName)) );
    }
    
    private static final String DIR_TAG = "dir";
    private static final String EXE_TAG = "exe";
    private static final String GAME_TAG = "game";
    private static final String IMG_TAG = "img";
    private static final String NAME_TAG = "name";
    private static final String PROFILE_DIR_TAG = "profile_dir";
    private static final String PROFILE_TAG = "profile";
    private static final String SAVE_DIR_TAG = "save_dir";
    
    private static final String ID_ATR = "id";
    private static final String GAME_ID_ATR = "gameid";
    
}
