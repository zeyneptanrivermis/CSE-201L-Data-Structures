import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.io.File;

public class MusicPlayer {

    private boolean isPlaying = false;
    private JButton playPauseButton;
    private Playlist playlist;
    private Player player;
    private JTextArea songDisplay;
    private Thread playbackThread;

    public MusicPlayer(String pathToPlaylist) {
        playlist = new Playlist(pathToPlaylist);

        JFrame frame = new JFrame("Music Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(new GridLayout(2, 5, 5, 5));
        JButton firstButton = new JButton("First");
        JButton lastButton = new JButton("Last");
        JButton prevButton = new JButton("<<");
        playPauseButton = new JButton("Play");
        JButton nextButton = new JButton(">>");

        firstButton.addActionListener(e -> playFirstSong());
        lastButton.addActionListener(e -> playLastSong());
        prevButton.addActionListener(e -> playPreviousSong());
        playPauseButton.addActionListener(e -> togglePlayPause());
        nextButton.addActionListener(e -> playNextSong());

        controlPanel.add(firstButton);
        controlPanel.add(prevButton);
        controlPanel.add(playPauseButton);
        controlPanel.add(nextButton);
        controlPanel.add(lastButton);


        JButton addBeginningButton = new JButton("Add to Beginning");
        JButton addEndButton = new JButton("Add to End");
        JButton removeBeginningButton = new JButton("Remove from Beginning");
        JButton removeEndButton = new JButton("Remove from Last");
        JButton removeCurrentButton = new JButton("Remove Current");

        addBeginningButton.addActionListener(e -> addSongToBeginning());
        addEndButton.addActionListener(e -> addSongToEnd());
        removeBeginningButton.addActionListener(e -> removeFirstSong());
        removeEndButton.addActionListener(e -> removeLastSong());
        removeCurrentButton.addActionListener(e -> removeCurrentSong());

        controlPanel.add(addBeginningButton);
        controlPanel.add(addEndButton);
        controlPanel.add(removeBeginningButton);
        controlPanel.add(removeEndButton);
        controlPanel.add(removeCurrentButton);


        songDisplay = new JTextArea(15, 40);
        songDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(songDisplay);



        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

        updateSongDisplay();
    }


    private void togglePlayPause() {
        if (isPlaying) {
            playPauseButton.setText("Play");
            stopPlaying();
        } else {
            playPauseButton.setText("Pause");
            playSelectedSong();
        }
        isPlaying = !isPlaying;
    }

    private void playSelectedSong() {
        Music currentSong = playlist.getCurrentSong();
        if (currentSong != null) {
            play(currentSong.getPath());
        } else {
            JOptionPane.showMessageDialog(null, "Please add a song to the playlist.");
        }
    }

    private void stopPlaying() {
        if (player != null && playbackThread != null && playbackThread.isAlive()) {
            player.close();
            playbackThread.interrupt();
            System.out.println("Stopped playback.");
        }
    }


    private void addSongToBeginning() {
        String songName = JOptionPane.showInputDialog("Enter the song name (without extension):");
        if (songName != null && !songName.trim().isEmpty()) {
            playlist.addSongAtBeginning(songName);
            updateSongDisplay();
            System.out.println("Added song to the beginning: " + songName);
        }
    }


    private void addSongToEnd() {
        String songName = JOptionPane.showInputDialog("Enter the song name (without extension):");
        if (songName != null && !songName.trim().isEmpty()) {
            playlist.addSongAtEnd(songName);
            updateSongDisplay();
            System.out.println("Added song to the end: " + songName);
        }
    }
    private void removeFirstSong() {
        try {
            playlist.removeFirstSong();
            updateSongDisplay();
            System.out.println("Removed first song.");
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "The playlist is empty.");
        }
    }


    private void removeLastSong() {
        try {
            playlist.removeLastSong();
            updateSongDisplay();
            System.out.println("Removed last song.");
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "The playlist is empty.");
        }
    }



    private void removeCurrentSong() {
        try {
            playlist.removeCurrentSong();
            updateSongDisplay();
            System.out.println("Removed current song.");
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "The playlist is empty.");
        }
    }
    private void playNextSong() {
        Music nextSong = playlist.playNext();
        play(nextSong.getPath());
    }


    private void playPreviousSong() {
        Music previousSong = playlist.playPrevious();
        play(previousSong.getPath());
    }


    private void playFirstSong() {
        Music firstSong = playlist.playFirst();
        play(firstSong.getPath());
    }


    private void playLastSong() {
        Music lastSong = playlist.playLast();
        play(lastSong.getPath());
    }
    public void play(String filePath) {
        stopPlaying(); 

        playbackThread = new Thread(() -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(filePath);
                player = new Player(fileInputStream);
                System.out.println("Playing: " + filePath);
                player.play(); 
            } catch (FileNotFoundException e) {
                System.out.println("MP3 file not found at path: " + filePath);
                e.printStackTrace();
            } catch (JavaLayerException e) {
                System.out.println("Error playing the MP3 file");
                e.printStackTrace();
            }
        });

        playbackThread.start();
    }


    private void updateSongDisplay() {
        StringBuilder displayText = new StringBuilder();
        for (int i = 0; i < playlist.size(); i++) {
            displayText.append(i + 1).append(". ").append(playlist.getSongAt(i).getName()).append("\n");
        }
        songDisplay.setText(displayText.toString());
    }

}



class Music {
    private String name;
    private String path;

    public Music(String path) {
        this.path = path;
        File file = new File(path);
        this.name = file.getName();
    }
    @Override
    public String toString() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}



class Playlist {
    private DoublyCircularLinkedList<Music> songs;
    private String pathToPlaylist;

    public Playlist(String path) {
        songs = new DoublyCircularLinkedList<>();
        pathToPlaylist = path;
    }

    public void addSongAtBeginning(String songName) {
        songs.addFirst(new Music(getSongPath(songName)));
    }

    public void addSongAtEnd(String songName) {
        songs.addLast(new Music(getSongPath(songName)));
    }

    public Music removeFirstSong() {
        return songs.removeFirst();
    }

    public Music removeLastSong() {
        return songs.removeLast();
    }

    public boolean removeCurrentSong() {
        return songs.remove(songs.getCurrent());
    }

    public Music playNext() {
        return songs.next();
    }

    public Music playPrevious() {
        return songs.previous();
    }

    public Music playFirst() {
        songs.first();
        return songs.getCurrent();
    }

    public Music playLast() {
        songs.last();
        return songs.getCurrent();
    }

    public Music getCurrentSong() {
        return songs.getCurrent();
    }

    public Music getSongAt(int index) {
        return songs.get(index);
    }

    public int size() {
        return songs.size();
    }

    private String getSongPath(String songName) {
        return this.pathToPlaylist + "/" + songName + ".mp3";
    }
}


