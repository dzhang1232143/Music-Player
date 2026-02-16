# My Personal Project: Music Player

## Phase 0
This project will be a simple music player, using imported wav files, this program will be able to play music. This application will allow users add and remove *songs* connected to a wav file and to play the wav file based on the connected *song*. 

### Proposed Features
I hope to implement the following features:
- add and remove songs
- connect songs to imported wav files
- label songs by naem, artist and other relevant information
- play wav files connected to songs
- creating and naming playlists of songs
- individually adding and removing songs to a playlist
- ability to query all songs with a common artist, genre or other classification
- ability to randomize playlist order
- queue songs or playlists
- loops songs or playlists
- a ui with buttons to access above functionality

### Proposed User/Personal Interest
I hope to be able to use this program as a music player to play music that is not available on streaming services such as spotify and avoid ads such streaming services have. 



## User Stories

As a user, I want to:
- add a song to my player
- add a playlist to my player
- remove a song from my player
- queue songs or playlists
- loops songs or playlists
- ability to randomize my playlist order
- play wav files connected to each song


## Phase 1 

### Reflection
I have realized that adding songs and removing songs from my player does not make sense in the way I want to implement it. The user is unable to import .wav files through the UI and would have to use file navigation to do so (I may look for a way to implement this later, however it does seem rather cumbersome for no benefit). For this reason I have not implemented adding or removing songs, instead all songs will be fetched from the sound files within the data folder. This means songs no longer qualify for an arbitrary amount of X within Y but instead playlists within the player can fullfil this requirement.

As for the user stories, I have implemented the following ones:
- add/remove a playlist to my player
- print/display all playlists that are in my player
- queue songs or playlists
- play wav files connected to each song
- pause songs at a specfic point and start playing them at that point again (While this was not one of my initial user stories, please check it out, I have spent too much time on this one)

### How to use this program
This player works by manipulating songs, playlists and a queue. All songs will be fetched from the data file and automatically named based on the file name. Playlists however have to be user created and are not saved. To actually play songs, we need to add them or playlists of songs to the queue then use the **Play/Pause** functionality in the main menu.

On the main menu you have the capability to:
- Play/Pause
- Add a new playlist
- Enter the viewing menu
- Clear the queue
- Exit the Program

While the others are self explanitory, the viewing menu allows us to view all of the songs, playlists and the queue. 

Within the songs portion of this menu we can:
- Queue songs
- Rename Songs

Within the playlists portion of the menu we can:
- View all songs within a Playlist (Here we can also add and remove songs from this playlist)
- Delete a playlist
- Rename a playlist
- Add a playlist to the queue

The queue viewer does not have further functionality than viewing the songs in the queue.