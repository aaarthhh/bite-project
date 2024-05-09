package com.arth.calorytracker;

import com.arth.calorytracker.models.Weightmodel;

import java.util.Comparator;

public class DateI {

    Weightmodel date;
    /* Constructor */
    DateI(Weightmodel date)
    {
        /* This keyword is used to refer current object */
        this.date = date;
    }
}
class sortCompare implements Comparator<DateI>
{
    // Method of this class
    @Override
    public int compare(DateI a, DateI b)
    {
        /* Returns sorted data in ascending order */
        return a.date.getDate().compareTo(b.date.getDate());
    }
}
