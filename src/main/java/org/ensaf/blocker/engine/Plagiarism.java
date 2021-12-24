package org.ensaf.blocker.engine;

public class Plagiarism {
    private int percentage;
    private String text;

    public Plagiarism() {
        this.text = "";
        this.percentage = 0;
    }
    public Plagiarism(String text, int percentage) {
        this.text = text;
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Plagiarism{" +
                "percentage=" + percentage +
                ", text='" + text + '\'' +
                '}';
    }
}
