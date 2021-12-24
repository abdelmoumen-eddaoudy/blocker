package org.ensaf.blocker.engine;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

// TODO Remove Word object class from the project
// TODO Choose a new name for ArrayWord Class

public class ArrayWord {
    private final ArrayList<Word> words;

    public ArrayWord() {
        words = new ArrayList<>();
    }

    public int length() {
        return words.size();
    }

    public ArrayList<Word> getWords() {
        return words;
    }


    public void add(Word word) {
        words.add(word);
    }

    /**
     * Parse {@code com.alpha.engine.Word} objects from {@code String} text
     */
    public void parse(String text) {
        if (text != null) {
            ArrayList<Integer> Index = new ArrayList<>();
            String separators = " \n,.?!()…" + ((char) 13);
            Index.add(-1);
            for (int i = 0; i < text.length(); i++) {
                if (separators.indexOf(text.charAt(i)) != -1) {
                    Index.add(i);
                }
            }
            // check if there's no ending character
            if (Index.get(Index.size() - 1) != text.length() - 1) {
                Index.add(text.length());
            }

            // adding words to the list
            for (int i = 0; i < Index.size() - 1; i++) {
                if (!text.substring(Index.get(i) + 1, Index.get(i + 1)).isEmpty()) {
                    Word word = new Word();
                    word.setContent(text.substring(Index.get(i) + 1, Index.get(i + 1)));
                    words.add(word);
                }
            }

        }
    }

    /**
     * @param search_word Example {@code Search(new com.alpha.engine.Word("Hello"))}
     * @return the index within the {@code WordArray} for the first occurrence of the specified {@code com.alpha.engine.Word}
     */
    int IndexOf(Word search_word) {
        int i = 0;
        boolean isFound = false;
        for (Word word : words) {
            if (Objects.equals(word.toString(), search_word.toString())) {
                isFound = true;
                break;
            }
            i++;
        }
        return (isFound) ? i : -1;
    }

    /**
     * <p>Compare a sequence of characters and return a {@code ArrayList<Integer>}
     * containing the matched index</p
     * <p><b>Example : </b></p>
     * <pre>
     *     String test_text = "Hello this is an example";
     *     String searched = "is an example";
     *     com.alpha.engine.ArrayWord aw = new com.alpha.engine.ArrayWord();
     *     aw.parse(test_text);
     *     aw.compareTo(searched, 2);</pre>
     */
    ArrayList<Integer> compareTo(@NotNull ArrayWord sequence, int index, int sequenceIndex) {
        ArrayList<Integer> indexArray = new ArrayList<>();
        for (int i = 0; i < sequence.words.size() - sequenceIndex; i++) {
            if (!(sequence.words.get(i + sequenceIndex).toString().equals(this.words.get(index + i).toString()))) {
                break;
            }
            indexArray.add(index + i);
        }
        return indexArray;
    }

    ArrayList<ArrayList<Integer>> searchByParagraph(@NotNull ArrayWord searched_word) {
        if (this.words.size() < searched_word.words.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        ArrayList<ArrayList<Integer>> array = new ArrayList<>();
        for (int i = 0; i < searched_word.length(); ) {
            int wIndex = this.IndexOf(searched_word.getWords().get(i));
            if (wIndex == -1) {
                i++;
                continue;
            }
            array.add(this.compareTo(searched_word, wIndex, i));
            i = i + array.get(array.size() - 1).size();
        }
        return array;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int i = 0;
        while (i < words.size() - 1) {
            str.append(words.get(i)).append(' ');
            i++;
        }
        str.append(words.get(i));
        return str.toString();
    }

}
