package csheets.testing;
import static org.junit.Assert.*;

import java.awt.Component;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;

import org.junit.Before;
import org.junit.Test;

import csheets.CleanSheets;
import csheets.ui.Frame;
import csheets.ui.MenuBar;
import csheets.ui.ctrl.ActionManager;
import csheets.ui.ctrl.UIController;
import csheets.ui.ext.UIExtension;


public class CSSE375Test {

	@Before
	public void setup() {
		//for consistency's sake, let's insure our tests always
		//start in US mode
		Locale.setDefault(new Locale("en","US"));
	}
	
	@Test
	public void testJavaInternationalizationExample() {
		ResourceBundle messages;
        
        messages = ResourceBundle.getBundle("CommandTextBundle");
        assertEquals("Hello", messages.getString("greetings"));
        
        Locale france = new Locale("fr", "FR");
        Locale.setDefault(france);
        messages = ResourceBundle.getBundle("CommandTextBundle");
        assertEquals("Bonjour", messages.getString("greetings"));
	}
	
	private void assertMenuText(Component c, String expected) {
		JMenuItem menu = (JMenuItem) c;
		assertEquals(expected,menu.getText());
	}
	
	@Test
	public void testMenuBar() {
		CleanSheets app = null;
		UIController con = null;
		ActionManager actions = new ActionManager(app, con, null);
		Frame.registerAllActions(app, con, null, actions);
		MenuBar bar = new MenuBar(app, actions, con);
		Component[] menus = bar.getComponents();
		JMenu ss = (JMenu) menus[3];
		Component[] submenu = ss.getPopupMenu().getComponents();
		
		assertMenuText(ss,"Spreadsheet");
		assertMenuText(submenu[0],"Add");
		assertMenuText(submenu[1],"Remove");
		assertMenuText(submenu[2],"Rename");
		assertMenuText(submenu[4],"Insert Column");
		assertMenuText(submenu[5],"Remove Column");
		assertMenuText(submenu[6],"Insert Row");
		assertMenuText(submenu[7],"Remove Row");
		
	}
	
	@Test
	public void testMenuBarFrench() {
		
        Locale france = new Locale("fr", "FR");
        Locale.setDefault(france);
		
		CleanSheets app = null;
		UIController con = null;
		ActionManager actions = new ActionManager(app, con, null);
		Frame.registerAllActions(app, con, null, actions);
		MenuBar bar = new MenuBar(app, actions, con);
		Component[] menus = bar.getComponents();
		JMenu ss = (JMenu) menus[3];
		Component[] submenu = ss.getPopupMenu().getComponents();
		
		assertMenuText(ss,"Tableur");
		assertMenuText(submenu[0],"Ajouter");
		assertMenuText(submenu[1],"Supprimer");
		assertMenuText(submenu[2],"Renommez");
		assertMenuText(submenu[4],"Insérer Une Colonne");
		assertMenuText(submenu[5],"Supprimer Une Colonne");
		assertMenuText(submenu[6],"Insérer Une Ligne");
		assertMenuText(submenu[7],"Supprimer La Ligne");
		
	}

}
