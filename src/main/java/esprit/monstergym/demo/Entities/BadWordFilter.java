package esprit.monstergym.demo.Entities;

import java.util.Arrays;
import java.util.List;

public class BadWordFilter {

    private static final List<String> badWords = Arrays.asList("badword1", "badword2", "badword3","ugly", "dumb"); // Add your list of bad words here

    public static String filterBadWords(String text) {
        // Split the text into individual words
        String[] words = text.split("\\s+");

        // Iterate through each word
        for (int i = 0; i < words.length; i++) {
            // Check if the word is a bad word
            if (badWords.contains(words[i].toLowerCase())) {
                // Replace the bad word with a placeholder or censor character
                words[i] = censorWord(words[i]);
            }
        }

        // Join the words back into a single string
        return String.join(" ", words);
    }

    private static String censorWord(String word) {
        // Replace each character in the word with asterisks (*) of the same length
        return "*".repeat(word.length());
    }

    public static void main(String[] args) {
        String text = "This is a sample text containing a badword1 and a badword2.";
        System.out.println("Original Text: " + text);
        String filteredText = filterBadWords(text);
        System.out.println("Filtered Text: " + filteredText);
    }
}

