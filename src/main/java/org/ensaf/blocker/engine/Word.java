package org.ensaf.blocker.engine;

/**
 * <p>Create a {@code com.alpha.engine.Word} object
 * <blockquote><pre>com.alpha.engine.Word word = new com.alpha.engine.Word();</pre></blockquote>
 * Usage of {@code setContent} method
 *  <blockquote><pre>word.setContent("com.alpha.engine.Word");</pre></blockquote></p>
 * <p>This object created to contain word only in lowercase (eg. hello, hey, have, ...).</p>
 * <p><b>Note: </b> <i>This class may get deprecated</i></p>
 */
public class Word {
    private String content;

    public Word() {
        this.content = "";
    }

    public Word(String content) {
        setContent(content);
    }

    public void setContent(String content) {
        this.content = content.toLowerCase().substring(0, content.length());
    }

    @Override
    public String toString() {
        return content;
    }
}


