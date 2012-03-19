Profile Manager
============================
Version: 1.3.14
Author: Curtis Oakley (Chocklymon)
Website: http://pmanager.chocklydigital.com

If you encounter any bugs or have any enhancements you would like to suggest,
either Private Message me or leave a comment. Including the error.log file when
reporting bugs would be greatly appreciated.


============================
A more complete readme and documentation can be found in the Profile Manager.
After downloading and installing, either run the profile manager and click help
from the menu, or open the lib/help_docs folder (located where ever you installed
the program at) and open the index.html file.
Or, the help docs can be read online at http://pmanager.chocklydigital.com/help/


Description:
============================
The Profile Manager lets you create 'profiles' to manage and separate out your
saved games. Once your profiles are set up you can quickly switch between
characters and/or players. Never worry about your brother accidentally saving
over your latest save.


Requirements:
============================
Java 6 or higher


Installation:
============================
Just download and unzip the file where you want.


How to run:
============================
Double click "Profile Manager.exe"


First Time Setup:
============================
If your games are not installed in the default location be sure to open the
settings window and update where the files are located.

If you already have saved games, open the file menu and click "Auto Create Profiles"
the Profile Manager will then sort your saved games into profiles, one for each
character. If you don't already have saved games open the file menu and add a new
profile.

Your ready to go.


Un-installation:
============================
Simply delete the folder where you unzipped the program to.


FAQ:
============================
Q: Does this work with profile/save game manager x?
A: Yes, the Profile Manager will generally work with most other profile/save game
managers. In most situations all you will have to do is run auto create profiles
and the Profile Manager will correctly detect any existing profiles.
This is know to work for Wrye Bash and Fallout 3 Savegame Manager (and ones based
off of these, such as TES V Savegame Manager).

Q: I have a non-English version of the game, and the Profile Manager is not
working correctly, what is wrong?
A: The Profile Manager is probably looking for your saved games in the wrong
location, check in the settings window to make sure that it has the right location.
You may need to add skyrim_save_dir= + the name of the folder that contains your
saved games to profile_manager.ini.
For example with an English version of the game it would read skyrim_save_dir=Saves
See http://pmanager.chocklydigital.com/help/config-advanced.php' for a full list
of ini settings.

Q: Will this damage my saved games?
A: Short Answer: No.
Long Answer: No. The Profile Manager only touches your saved games when you run
auto create profiles, and then it only moves the saved games into different
folders using the same  mechanism windows does to move files (so it is as safe
as windows is). If it detects any anomaly when moving the file it will stop,
leaving the original file intact.
(Note: The Morrowind portion of the profile manager will move your saved games 
whenever the profile is changed.)

Q: The auto create profiles doesn't correctly sort my saved games, what should I
do?
A: This is generally caused by the Profile Manager not being able to correctly
extract the character name from the saved game file's name.
There are two things you can do to resolve this problem. One, add this line to
the profile_manager.ini file [font='Courier New']deep_scan=true[/font] this will
cause the Profile Manager to read the character's name from the saved game itself,
instead of trying to get it from the file's name. Two, you can manually sort the
saved games into sub-folders in the saves folder, and then run auto create profiles.
If you are experiencing this problem, please either post a comment or PM me and
include the exact name of one of your saved games so that I can try to resolve
this issue.


Changelog:
============================
Version 1.3.02
- Added ability to modify how the auto create profiles scans files.
- Fixed a bug with building the ini file.
- Fixed a bug where manually typing in files in the settings window would not save the changes.

Version 1.3.01
- Added better error handling for missing files.
- Fixed Morrowind's auto create profiles to correctly extract character names.

Version 1.3.00
- Fixed a bug where the desktop shortcuts wouldn't create on some systems.
- Enhancements to the main interface.
- Added an executable file to start the program.
- Fixes to error logging.
- Fixed bug with non-English version of the game.

Version 1.2.07
- Fixed bug where starting certain programs would fail (for example SKSE). Thanks to matys89 for pointing me in the right direction.
- Profile Manager will now automatically select the active profile.
- Re-compressed some images for smaller download size.

Version 1.2.06
- Update to the documentation and about window.
- Changed where the documentation was stored.

Version 1.2.05
- Fixed bug where the shortcuts would not create.

Version 1.2.04
- Initial Release.


Legal Stuff:
============================
Profile Manager
Copyright (C) 2012 Curtis Oakley

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Uses:
Fugue Icons by Yusuke Kamiyamane http://p.yusukekamiyamane.com/
 Copyright (C) 2012 Yusuke Kamiyamane.
 All rights reserved. These icons are licensed under a Creative Commons Attribution 3.0 License.
 