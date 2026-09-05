A Java based music player app with a java.swing UI that can play audio files in the data file, ported from school work for UBC CPSC 210. Uses javax.sound to play .WAV files and multithreading to process when music ends. Songs can be added to playlists and both playlists and songs and be queued to be played, queue will autoplay the next song when the last one has finished. This project contains two tabs:

### Queue Tab
Displays all songs in the queue and has three buttons that do the following:
- __Play/Pause__: If there are any songs in the queue, will play the current song if currently paused and pause if playing
- __Clear Queue__: Clears all songs from the queue
- __Next Song__: Skips the current song and immediately plays the next song

### Playlist Tab
Displays all the playlists and songs within the playlists. All the playlists are shown as a file directory where each playlist/folder can be open and closed to show what songs are in them. To use a certain button you have to select the song or playlist by clicking it and use one of the following buttons:
- __Add to Queue__: Adds the selected song or playlist to the queue. All songs within a playlsit will be added in order.
- __Delete__: Selected playlist will be deleted and selected song will be removed from the playlist
- __Rename__: Rename the selected playlist or song through a pop up menu. Renamed songs will change the name of the audio file.
- __Create new Playlist__: Creates a new playlist and asks for a name from a popup
- __Add Song to Playlist__: Will create a popup with a dropdown menu to to select which song to add to the selected playlist

## Adding Audio Files
Files must be of the .WAV format. Place the wanted audio file within the src/data/songs folder. DO NOT remove or change audio files while the program is running. There is no check for whether files have changed names or been removed and thus will result in an IO error if a file that no longer exists is attempted to be played.