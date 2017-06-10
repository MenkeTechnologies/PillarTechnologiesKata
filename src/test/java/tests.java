/**
 * Created by jacobmenke on 6/9/17.
 */

import BabysitterCalculator.BabySitter;
import BabysitterCalculator.BabysittingJob;
import junit.framework.*;
import org.junit.*;
import org.junit.Test;

import static org.junit.Assert.*;


public class tests {

    @Test
    public void validHours() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "8:00 pm", "10:00 pm", "1:00 am");

        assertEquals("yes",babySitter.proposeJob(babysittingJob));


    }

    @Test
    public void InvalidHours(){


        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "3:00 pm", "10:00 pm", "1:00 am");

        assertNotEquals("yes",babySitter.proposeJob(babysittingJob));

    }




}
