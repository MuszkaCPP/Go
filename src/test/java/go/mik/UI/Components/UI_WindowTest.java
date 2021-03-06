package go.mik.UI.Components;

import static org.junit.Assert.*;
import org.junit.Test;

public class UI_WindowTest {

    private ABS_UIWindow _testUIWindow  = new UIWindow(null,"test");


    @Test
    public void checkIfGameFieldWasCreated(){
        assertNotNull(_testUIWindow.getUI_Field());
    }

    @Test
    public void getMessageForChat(){

        _testUIWindow.getMessageForChat("test");

        assertEquals("test",_testUIWindow.get_chatBox().getText());
    }

}
