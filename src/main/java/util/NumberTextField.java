package util;

import javafx.scene.control.TextField;

/**
 * Numeric textfield for JavaFX
 */
public class NumberTextField extends TextField {

    /**
     * Warning to only type in numeric values
     */
    public NumberTextField() {
        this.setPromptText("Enter only numbers");
    }

    /**
     * @param start start of selected text
     * @param end end of selected text
     * @param text numeric text to replace text
     */
    @Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            super.replaceText(start, end, text);
        }
    }

    /**
     * @param text numeric text to replace selection
     */
    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
    }

    /**
     *
     * @param text text to check
     * @return true if text is numeric
     */
    private boolean validate(String text)
    {
        if (text.matches("[0-9]") || text == "")
        {
            return true;
        }
        return false;
    }

}