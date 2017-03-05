package com.mystery0.hitokoto;

public class SettingsClass
{
    private String text;
    private boolean isOpen;
    private boolean checkBoxAvailable;

    public SettingsClass(String text, boolean isOpen, boolean checkBoxAvailable)
    {
        this.text = text;
        this.isOpen = isOpen;
        this.checkBoxAvailable = checkBoxAvailable;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public boolean isOpen()
    {
        return isOpen;
    }

    public void setOpen(boolean open)
    {
        isOpen = open;
    }

    public boolean isCheckBoxAvailable()
    {
        return checkBoxAvailable;
    }

    public void setCheckBoxAvailable(boolean checkBoxAvailable)
    {
        this.checkBoxAvailable = checkBoxAvailable;
    }
}
