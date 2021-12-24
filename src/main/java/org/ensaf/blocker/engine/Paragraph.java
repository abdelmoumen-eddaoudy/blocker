package org.ensaf.blocker.engine;

import java.util.ArrayList;

public class Paragraph {
    private ArrayWord mText;

    public Paragraph(ArrayWord text) {
        this.mText = text;
    }

    public static Plagiarism Analyse(ArrayList<ArrayList<Integer>> result, ArrayWord text, int copy_length) {
        StringBuilder str_result = new StringBuilder();
        Plagiarism plagiarism = new Plagiarism();
        for (ArrayList<Integer> integers : result) {
            if (integers.size() > 2) {
                plagiarism.setPercentage((int) (integers.size() / (double) copy_length * 100));
                for (Integer index : integers) {
                    str_result.append(text.getWords().get(index)).append(" ");
                }
                plagiarism.setText(str_result.toString());
            }
        }

        return plagiarism;
    }


    public static ArrayList<Plagiarism> check(String original_text, String copy_text) {
        ArrayWord copy_paragraph = new ArrayWord();
        copy_paragraph.parse(original_text);
        ArrayList<Plagiarism> plagiarisms_result = new ArrayList<>();
        ArrayWord original_paragraph = new ArrayWord();
        original_paragraph.parse(copy_text);
        try {
            Paragraph temp = new Paragraph(copy_paragraph);
            var result = temp.extract(56);
            for (ArrayWord arrayWord : result) {
                var array = original_paragraph.searchByParagraph(arrayWord);
                plagiarisms_result.add(Analyse(array, original_paragraph, copy_paragraph.length()));
            }

        } catch (ArrayIndexOutOfBoundsException exception) {
            System.err.println("the input size is bigger than existing size");
        }
        return plagiarisms_result;
    }

    public ArrayList<ArrayWord> extract(int row_length) {
        ArrayList<ArrayWord> result = new ArrayList<>();
        ArrayWord row_temp;
        int total_length = mText.getWords().size();
        int column_length = total_length / row_length;
        for (int column = 0; column < column_length; column++) {
            row_temp = new ArrayWord();
            for (int row = 0; row < row_length; row++) {
                row_temp.add(mText.getWords().get(column * row_length + row));
            }
            result.add(row_temp);
        }

        int rest = total_length % row_length;
        if (rest != 0) {
            row_temp = new ArrayWord();
            for (int i = total_length - rest; i < total_length; i++) {
                row_temp.add(mText.getWords().get(i));
            }
            result.add(row_temp);
        }
        return result;
    }
}
