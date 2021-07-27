import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class program {

    // temp IGNORE
    public static String[] wordsTemp = {"he", "went", "that", "came", "car", "to", "big", "want", 
    "it", "is", "so", "shopping", "with", "book", "university", "and", "got", "apple", "hot", "died", "cold", 
    "tasty", "delicious","warm", "rude", "mean", "go", "has", "am", "in", "need",  "at", "an", "i", "a"};

    // verbs
    public static String[] verbList = {"shout", "study", "shop"};
    public static String[] thirdPersonVerbList = {"shouts", "studies", "shops"};

    // adverbs
    public static String[] adverbList = {"carefully", "nicely", "cleverly"};

    // conjunction words
    public static String[] conjunctionWords = {"but", "and", "or"};

    // adjective words
    public static String[] adjectives = {"hot", "cold", "warm", "tasty", "delicious", "rude", "mean", "lovely","adorable","naughty"};

    // common words (common nouns)
    public static String[] commonWords = {"vegetables", "meat", "fruit", "vaccinated", "female"};

    // pronouns
    public static String[] personalPronounWords = {"he", "she", "it", "we", "i", "they"};
    public static String[] objectPronounWords = {"him", "her", "them"};
    public static String[] possessivePronounWords = {"hers", "theirs", "mine"};

    // vowels
    public static char[] vowels = {'A', 'E', 'I', 'O', 'U'};

    // music player (novelty)
    public static SimpleAudioPlayer simpleAudioPlayer;
    public static String[] songs = {"music.wav", "music2.wav", "music3.wav"}; // add more songs here
    public static int songID = 1; 

    // method that will idendentify if a full stop is present
    public static boolean checkForFullStop(String word) {
        char[] first  = word.toCharArray();
        if (first[first.length - 1] == '.')
            return true;
        return false;
    }  
    
    // method that will check if a pronoun exists directly after a common word or an adjective.
    public static boolean checkForPronoun(String left, String right) {
        boolean l = false;
        boolean r = false;

        // check if the word on the right is a personal pronoun.
        for (int i = 0; i < personalPronounWords.length; i++) {
            if (right.equals(personalPronounWords[i]))
                r = true;
        }

        // check if the word on the left is an adjective.
        for (int i = 0; i < adjectives.length; i++) {
            if (left.equals(adjectives[i]))
                l = true;
        }
        // check if the word on the left is a common word.
        for (int i = 0; i <  commonWords.length; i++) {
            if (left.equals(commonWords[i]))
                l = true;
        }

        if (l == true && r == true) {
            return true;
        } 
            return false;
    }


    // check if there are 2 adjectives back to back with no connectives 
    public static String adjCheck (String left, String right) {
        String firstAdj = "";
        String secondAdj = "";
        String firstCom = "";
        String secondCom = "";

        // check if they are adjectives
        for (String i : adjectives){
            if (i.equals(left)){
                firstAdj = left;
            }
            if (i.equals(right)){
                secondAdj = right;
            }
        }

        // check if they are common words
        for (String i : commonWords){
            if (i.equals(left)){
                firstCom = left;
            }
            if (i.equals(right)){
                secondCom = right;
            }
        }

        // if both words are "common nouns" then we will add a connective word between them.
        if (firstCom.equals(left) && secondCom.equals(right)){
            return left + " and";
        }

        // if both words are adjectives then we will add a connective word between them.
        if (firstAdj.equals(left) && secondAdj.equals(right)){
            return left + " and";
        }

        // if the words have a personal pronoun directly after an adjective or an common noun then we will add a comma.
        if (checkForPronoun(left, right))
            return left + ",";

        return left;
    }

    // function for autocorrecting
    public static int checkForSimilar(String wordInput, String comparedTo) {
        
        char[] first  = wordInput.toLowerCase().toCharArray(); // the word that needs auto correcting
        char[] second = comparedTo.toLowerCase().toCharArray(); // the word that it's being compared to

        int minLength = Math.min(first.length, second.length);
        int counter = 0;

        for(int i = 0; i < minLength; i++)
        {
                if (first[i] == second[i])
                {
                    counter++;                    
                } else if ( (i < minLength - 1) && (first[i] == second[i+1]) ) {
                    counter++;   
                }
        }
        if (first.length < second.length - 1) {
            counter = counter - (second.length - first.length);
        }
        return counter; // return simularity 
    }

    // check if the words has a capitalized first letter.
    public static boolean checkIfCapital(String word) {

        String l = word.toLowerCase();
        char[] lChar  = l.toCharArray();
        char[] nChar = word.toCharArray();

        if (nChar[0] == lChar[0])
            return false;
        
        return true;
    }

    // function that makes sure that there is a fullstop at the end of the string
    public static String expectFullStop(String word) {
        char[] lChar  = word.toCharArray();
        char expectfullStop = lChar[lChar.length - 1];

        String returnStr = word;
        if (expectfullStop == '.') {
            return returnStr;
        } else {
            returnStr += ".";
        }
        return returnStr; // return the string with a fullstop at the end.
    }

    // make the first letter of the word capital
    public static String makeCapital(String word) {
        String l = word.toLowerCase();
        char[] lChar  = l.toCharArray();
        lChar[0] = (char)(lChar[0] - 32);
        String returnStr = String.valueOf(lChar);  
        return returnStr;
    }

    // autocorrect words
    public static String autocorrect(String word, String[] list) {
        int index = 0; // index of the word dictonary that correlates with the word.
        int simularity = 0; // the simularity of the word.
        for (int i = 0; i < list.length; i++ ) { // for all defined words
            int sim = checkForSimilar(word, list[i]);
            if (sim >= simularity) { // if the word is more similar than the current similar word.
                simularity = sim;
                index = i;
            }
        }
        if (list[index].equals("i")) // if the word is "i"
            return makeCapital("i"); // capitalize it to I

        return list[index]; // return the best matched word.
    }

    // autocorrect the words that aren't capitalised
    public static String correctWord(String word, String[] list) { 
        if (checkIfCapital(word)) // the word is capital then don't auto correct it (assume uncommon noun like name)
            return word;

        // check if the word has a fullstop
        if(checkForFullStop(word)) 
            return autocorrect(word, list) + "."; // auto correct word and return the fullstop
        
        return autocorrect(word, list);
        
    }

    // check if two same words are used back to back
    public static boolean checkForRepeatedWord(String word1, String word2) {
        if (word1.equals(word2)) {
            return true;
        }
        return false;
    }
    
    // returns a String that contains all information for an ArrayList<String>
    public static String printList(ArrayList<String> list) { 
        String as  = "";
        for (String a : list)
            as = as + a + " ";
        return as;
    }
    
    // check if an "an" or "a" is present, if so then we will make sure that it is correctly used (if not then we will return the correct one)
    public static String checkForAnAndA(String left, String right) {
        if (left.toLowerCase().equals("an") || left.toLowerCase().equals("a")) {
            return fixAnAndA(right);
        } 
        return left;
    }

    // method that sorts an array based on an alphabetical order then based on length 
    public static String[] sortArray (String[] unsortedArray){
        String[] sortedArray = new String[unsortedArray.length];

        // SET TO LOWERCASE
        for (int i=0; i<unsortedArray.length ; i++){
            sortedArray[i] = unsortedArray[i].toLowerCase();
        }

        // SORT THE STRING ALPHABETICALLY
        Arrays.sort(sortedArray);

        // SORT BASED ON THE LENGTH OF EACH STRING
        for (int i = 1; i<sortedArray.length ; i++){
            String temp = sortedArray[i];
            int j = i - 1;
            while(j>=0 && temp.length() > sortedArray[j].length()){
                sortedArray[j+1] = sortedArray[j];
                j--;
            }
            sortedArray[j+1]= temp;
        }

        return sortedArray;
    }

    // based on the word on the right we will determine whether a or an is more suitable.
    public static String fixAnAndA(String rightWord) {
        
            char firstChar = rightWord.toCharArray()[0];
            for (int i = 0; i < vowels.length; i++) {
                if(firstChar == vowels[i] || firstChar == vowels[i] + 32) {
                    return "an";
                }
            }
            return "a";        
    }

    public static String answerr = ""; // string that will contain the corrected sentence that will be displayed onto the UI

    public program(String a) throws IOException { // constructor that fixes the sentence
        
        answerr = "";
        File file = new File("words"); // access the word bank
        Scanner fileScanner = new Scanner(file);
        String fileStr = ""; // string that will update the word bank by sorting
        String as = ""; // string that contains the word bank

        while(fileScanner.hasNextLine()){
            String rn = removeSpace(fileScanner.nextLine().toLowerCase()); // make sure there aren't any blank spaces
            as += rn + " "; // add a space between each word 
        }
        FileWriter writer = new FileWriter(file);
        String[] anda = as.split(" "); // turn the wordbank into an array
        String[] dddd = sortArray(anda); // sort the array
        wordsTemp = dddd; // add the words into the array that dictates what words are valid or not
        fileScanner.close(); 
        for(int i = 0; i < anda.length; i++)
        {
            fileStr += dddd[i] + "\n"; // have each on a different line
        }
        writer.write(fileStr); // update the word bank file (sort it)
        writer.close();
        // System.out.print(fileStr);
    
        String[] b = a.split(" "); // the word inputed by the user will be split
        ArrayList<String> c = new ArrayList<String>(); // create an arrayList
        for (int i = 0; i < b.length; i++) { // add all words into the arrayList
            c.add(b[i]);
        } 
        for (int i = 0; i < c.size(); i++ ) {
            c.set(i, correctWord(c.get(i), wordsTemp)); // correct the words
        }
        for (int i = 0; i < (c.size() - 1); i++ ) {
            if(checkForRepeatedWord(c.get(i), c.get(i+1))) { 
                c.remove(i); // remove repeated words
            }
        }
        for (int i = 0; i < (c.size() - 1); i++ ) {
            
                c.set(i, checkForAnAndA(c.get(i), c.get(i+1))); // make sure there is a correct use of an and a 
            
        }
        for (int i = 0; i < (c.size() - 1); i++ ) {
            if (checkForFullStop(c.get(i)) == true) {
                c.set(i+1, makeCapital(c.get(i+1))); // make sure that words that require capitalization are infact capitalized
                continue;
            }
            c.set(i, adjCheck(c.get(i), c.get(i + 1))); // add missing commas and connectives
        }
        c.set(0, makeCapital(c.get(0))); // make first letter of the sentence capital
        c.set(c.size() - 1, expectFullStop(c.get(c.size() - 1))); // make sure the last word has a full stop

        // print to console the correct statement
        System.out.print("Correct phrase: ");
        System.out.print(printList(c));
       
        for (String ans : c)
            answerr += ans + " "; // add the correct sentence to the string answerr
    }

    public String getAnswer() { // return the corrected sentence
        return answerr;
    }
    public static String removeSpace (String word) { // function that removes blank spaces
        String str = word.trim();
        return str;
     }

    // functions used to indicate what UI elements are opened or not
    public static boolean triedToOpen2 = false;
    public static boolean triedToOpen = false;

    public static void main(String args[]) throws IOException { // UI function
       // Desktop.getDesktop().open(new File("music.mp4"));
        //Creating the Frame
        try
        {
            simpleAudioPlayer = new SimpleAudioPlayer("music.wav");
            
        } 
          
        catch (Exception ex) 
        {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
          
        }
        
        JFrame frame = new JFrame("Auto Corrector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 400);

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("View Word Bank");
        JMenu m3 = new JMenu("Music Player");
        mb.add(m1);
        mb.add(m2);
        mb.add(m3);
        JMenuItem m11 = new JMenuItem("Open");
        JMenuItem m22 = new JMenuItem("Edit Word Bank");
        JMenuItem m33 = new JMenuItem("Play");
        JMenuItem m44 = new JMenuItem("Pause");
        JMenuItem m55 = new JMenuItem("Restart");
        JMenuItem m66 = new JMenuItem("Skip");
        m1.add(m11);
        m1.add(m22);
        m3.add(m33);
        m3.add(m44);
        m3.add(m55);
        m3.add(m66);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter Text");
        JTextField tf = new JTextField(24); // accepts upto 10 characters
        JButton send = new JButton("Send");
        JButton reset = new JButton("Reset");

        // Text Area at the Center
        JTextArea ta = new JTextArea();
        
        // implementations
        m33.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)  {
                System.out.println("work3");
                
                try
                    {
                        simpleAudioPlayer.play();
                    } 
                    
                    catch (Exception ex) 
                    {
                        System.out.println("Error with playing sound.");
                        ex.printStackTrace();
                    
                    }


            }
        });
        // implementations
        m44.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)  {
                System.out.println("work4");
                

                
                
                try
                    {
                        simpleAudioPlayer.pause();
                    } 
                    
                    catch (Exception ex) 
                    {
                        System.out.println("Error with playing sound.");
                        ex.printStackTrace();
                    
                    }

            }
        });
        m55.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)  {
                System.out.println("work5");
                

                
                try
                    {
                        simpleAudioPlayer.restart();
                    } 
                    
                    catch (Exception ex) 
                    {
                        System.out.println("Error with playing sound.");
                        ex.printStackTrace();
                    
                    }


            }
        });
        m66.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)  {
                System.out.println("work6");
                
                if (songID < songs.length) {
                    
                    try
                    {
                        simpleAudioPlayer.stop();
                        simpleAudioPlayer = new SimpleAudioPlayer(songs[songID]);
                        songID++;
                    } 
                    
                    catch (Exception ex) 
                    {
                        System.out.println("Error with playing sound.");
                        ex.printStackTrace();
                    
                    }
                    
                } else {
                    try
                    {   
                        songID = 0;
                        simpleAudioPlayer.stop();
                        simpleAudioPlayer = new SimpleAudioPlayer(songs[songID]);
                        
                    } 
                    
                    catch (Exception ex) 
                    {
                        System.out.println("Error with playing sound.");
                        ex.printStackTrace();
                    
                    }
                }
                
            }
        });
        // implementations
        m11.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)  {
                System.out.println("work1");
                

                
                
                if(triedToOpen == true) {
                    ta.setText("");
                    triedToOpen = false;
                    send.setForeground(Color.BLACK);
                    ta.setEditable(true);
                    send.setText("Send");
                } else {

                    ta.setText("INPUT NAME OF FILE TO OPEN (CANNOT BE WORDS) \n'waiting for input'");
                    triedToOpen = true;
                    triedToOpen2 = false;
                    send.setForeground(Color.BLUE);
                    ta.setEditable(false);
                    send.setText("Search");
                }
                


            }
        });
        m22.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)  {
                System.out.println("workkk2");
                

                if(triedToOpen == true) {
                    triedToOpen = false;
                    send.setText("Search");
                }
                
                if(triedToOpen2 == true) {
                    ta.setText("");
                    triedToOpen2 = false;
                    ta.setEditable(false);
                    send.setForeground(Color.BLACK);
                    send.setText("Send");
                } else {

                    ta.setText("ADDING NEW WORDS TO THE PROGRAM'S DICTONARY");
                    triedToOpen2 = true;
                    ta.setEditable(false);
                    
                    send.setForeground(Color.RED);
                    send.setText("Add Word");
                }
                


            }
        });
       
        m2.addMenuListener(new MenuListener()  {

                public void menuSelected(MenuEvent e) { 
                send.setText("Send");
                send.setForeground(Color.BLACK);
                    ta.setText("");
                    try {
                    File file = new File("words");
                    Scanner fileScanner = new Scanner(file);
                    String as = "";
                    int sizeOfText = 80;
                    int SIZEOFTEXT = 80;
                    while(fileScanner.hasNextLine()){
                        String rn = fileScanner.nextLine().toLowerCase();
                        if (rn.toCharArray().length > sizeOfText) {
                            as += "\n" + rn + " ";
                            sizeOfText = SIZEOFTEXT - rn.toCharArray().length - 1; 
                        } else {
                            as += rn + " ";
                            sizeOfText -= rn.toCharArray().length + 1;
                        }
                       
                    }
                    fileScanner.close();
                    ta.setText(as); 
                } catch (IOException eee) {


                }
                    System.out.println("menuSelected");
                    triedToOpen = false;
                    triedToOpen2 = false;
                    ta.setEditable(true);
                }
            
                public void menuDeselected(MenuEvent e) {
                    System.out.println("menuDeselected");
                    
                }
            
                public void menuCanceled(MenuEvent e) {
                    System.out.println("menuCanceled");
                }
        });
        

        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)  {
                send.setText("Send");
                send.setForeground(Color.BLACK);
                String c = "";
                boolean endLoop = false;
                if (triedToOpen2 == true) {
                    endLoop = true;
                    try {
                    triedToOpen2 = false;
                    ta.setEditable(true);
                    File file = new File("words");
                    Scanner fileScanner = new Scanner(file);
                    String fileStr = "";
                    String as = "";
            
                    while(fileScanner.hasNextLine()){
                        String rn = removeSpace(fileScanner.nextLine().toLowerCase());
                        as += rn + " ";
                    }
                    as += tf.getText();
                    FileWriter writer = new FileWriter(file);
                    String[] anda = as.split(" ");
                    String[] dddd = sortArray(anda);
                    fileScanner.close();
                    for(int i = 0; i < anda.length; i++)
                    {
                        fileStr += dddd[i] + "\n";
                    }
                    writer.write(fileStr);
                    writer.close();
                    ta.setText(ta.getText() + "\n The word " + tf.getText() + " has been added to the program's dictonary");
                    }catch(IOException eee) {
                        System.out.println("Error reading words file");
                    }
                }
                if(endLoop)
                    return;
                if (triedToOpen == false) {
                    triedToOpen2 = false;
                    ta.setEditable(true);
                try {
                    program prog = new program(tf.getText());
                    ta.setText(prog.getAnswer());
                  }
                  catch(Exception es) {
                    System.out.println("Error reading file 'words'");
                  }
                } else {
                    try {
                    triedToOpen2 = false;
                    File file = new File(tf.getText());
                    Scanner fileScanner = new Scanner(file);
                    c = "";
            
                    while(fileScanner.hasNextLine()){
                        String rn = removeSpace(fileScanner.nextLine());
                        c += rn + " ";
                    }
                    try {
                        program prog = new program(c);
                        int sizeOfText = 80;
                        int SIZEOFTEXT = 80;
                        String[] ans = prog.getAnswer().split(" ");
                        String finalAns = "";
                        for(String ca : ans) {
                            if (ca.toCharArray().length > sizeOfText) {
                                finalAns += "\n" + ca + " ";
                                sizeOfText = SIZEOFTEXT - ca.toCharArray().length - 1; 
                            } else {
                                finalAns += ca + " ";
                                sizeOfText -= ca.toCharArray().length + 1;
                            }
                        }
                        
                        ta.setText(finalAns);
                      }
                      catch(Exception es) {
                        System.out.println("Error reading file 'words'");
                      }
                    fileScanner.close();
                    tf.setText(c); 
                    } catch(Exception se) {}
                    triedToOpen = false;
                    ta.setEditable(true);
                }
            }
        });

        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ta.setText("");
                tf.setText("");
                send.setText("Send");
                send.setForeground(Color.BLACK);
                triedToOpen2 = false;
                ta.setEditable(true);
                if (triedToOpen == true) {
                    triedToOpen = false;
                    ta.setEditable(true);
                }
                
            }
        });

        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(send);
        panel.add(reset);

        

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);

        
    }
}
// the class below is used for the MusicPlayer functionality
class SimpleAudioPlayer 
{
  
    // to store current position
    Long currentFrame;
    Clip clip;
      
    // current status of clip
    String status;
      
    AudioInputStream audioInputStream;
    static String filePath;
  
    // constructor to initialize streams and clip
    public SimpleAudioPlayer(String songName)
        throws UnsupportedAudioFileException,
        IOException, LineUnavailableException 
    {
        
        filePath = songName;
        try
        {
            
              // create AudioInputStream object
            audioInputStream = 
            AudioSystem.getAudioInputStream(new File(filePath));
    
            // create clip reference
            clip = AudioSystem.getClip();
            
            // open audioInputStream to the clip
            clip.open(audioInputStream);
            
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            play();
            
        } 
          
        catch (Exception ex) 
        {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
          
          }
    }
      
    // Method to play the audio
    public void play() 
    {
        //start the clip
        clip.start();
          
        status = "play";
    }
      
    // Method to pause the audio
    public void pause() 
    {
        if (status.equals("paused")) {
            System.out.println("audio is already paused");
            return;
        }
        this.currentFrame = 
        this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }
      
    // Method to resume the audio
    public void resumeAudio() throws UnsupportedAudioFileException,
                                IOException, LineUnavailableException 
    {
        if (status.equals("play")) {
            System.out.println("Audio is already being played");
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }
      
    // Method to restart the audio
    public void restart() throws IOException, LineUnavailableException,
                                            UnsupportedAudioFileException 
    {
        clip.stop();
        clip.close();
        resetAudioStream();
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        this.play();
    }
      
    // Method to stop the audio
    public void stop() throws UnsupportedAudioFileException,
    IOException, LineUnavailableException 
    {
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }
      
    // Method to jump over a specific part
    public void jump(long c) throws UnsupportedAudioFileException, IOException,
                                                        LineUnavailableException 
    {
        if (c > 0 && c < clip.getMicrosecondLength()) 
        {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentFrame = c;
            clip.setMicrosecondPosition(c);
            this.play();
        }
    }
      
    // Method to reset audio stream
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
                                            LineUnavailableException 
    {
        audioInputStream = AudioSystem.getAudioInputStream(
        new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
  
}
