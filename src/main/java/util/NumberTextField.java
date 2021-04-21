package util;

import javafx.scene.control.TextField;

/**
 *
 */
public class NumberTextField extends TextField {

    /**
     *
     */
    public NumberTextField() {
        this.setPromptText("Enter only numbers");
    }

    /**
     * @param i
     * @param i1
     * @param string
     */
    @Override
    public void replaceText (int i, int i1, String string) {
        if(string.matches("[0-9]") || string.isEmpty()) {
            super.replaceText(i,i1,string);
        }
    }

    /**
     * @param string
     */
    @Override
    public void replaceSelection(String string) {
        super.replaceSelection(string);
    }

}