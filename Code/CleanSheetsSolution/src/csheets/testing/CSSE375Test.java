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
import csheets.ui.ctrl.BaseAction;
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
	
	private class UIControllerTest extends UIController {

		@Override
		public UIExtension[] getExtensions() {
			return new UIExtension[] {};
		}

		public UIControllerTest(CleanSheets app) {
			super(app);
		}
		
	}
	
	/**
	 * To get this function to work, I used Subclass and Override Method on UIController.getExtensions()
	 */
	@Test
	public void testMenuBar() {
		CleanSheets app = new CleanSheets();
		UIController con = new UIControllerTest(app);
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
	
	/**
	 * For an explaination of how I got this to work, check the bottom of this file.
	 * 
	 * But don't peek - figure it out yourself!
	 */
	@Test
	public void testMenuBarFrench() {
		
        Locale france = new Locale("fr", "FR");
        Locale.setDefault(france);
		
		CleanSheets app = new CleanSheets();
		UIController con = new UIControllerTest(app);
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 
	 How My Solution Works
	 
	 My goal was to figure out a way to add internationalization to menus with minimum duplication.
	 
	 Looking at the code in MenuBar, all menu items seem to be constructed in a very similar way.
	 All of them seem to inherit from a class called "BaseAction" and their accessed with a key
	 that seems like a good candidate for my keyword to look up the menu items' tranlations.
	 Ideally, I'd like to put all my changes in BaseAction so that they are inherited by all 
	 the many subclasses.
	 
	 Only complication - the keyword seems to be set in Frame.registerAllActions - it's not
	 known to the BaseAction subclasses.  But I can fix that.
	 
	 I add a new line to Action Manager.registerAction:
	 
	 public void registerAction(String identifier, BaseAction action) {
		actions.put(identifier, action); 
		action.setInternationalizationKeyword(identifier); // my new line
		if (action.requiresModification())
			modificationActions.add(action);
		if (action.requiresFile())
			fileActions.add(action);
	 }
	 
	 I also add a corresponding method in BaseAction.
	 
	 Poking around a bit and testing, I discover that it's actually this line in the constructor
	 of BaseAction that sets the name:
	 
	 String name = getName();
	 putValue(NAME, name);
	 
	 In theory I could change the constructor of BaseAction to take the internationalization keyword
	 or something, but that would require modifying all the subclasses.  I wouldn't be unhappy with 
	 the solution but - I figure - why not just reset the value in setInternationalizationKeyword?
	 
	 public void setInternationalizationKeyword(String keyword) {
		this.actionKeyword = keyword;

		ResourceBundle messages = ResourceBundle.getBundle("CommandTextBundle");
        if(messages.containsKey(keyword)) {
        	String internationalized = messages.getString(keyword);
    		putValue(NAME, internationalized);
    		
    		putValue(SHORT_DESCRIPTION, internationalized);
    		putValue(ACTION_COMMAND_KEY, internationalized);
        }
	 }
	 
	 This works for all my menu items but not the menu title (Spreadsheet) itself.  I discover
	 this is being set in the function MenuBar.addMenu.  So I update that function in a similar way:
	 
	 private JMenu addMenu(String name, int mnemonic) {
		ResourceBundle messages = ResourceBundle.getBundle("CommandTextBundle");
		JMenu menu;
        if(messages.containsKey(name)) {
        	menu = new JMenu(messages.getString(name));
        } else {
        	menu = new JMenu(name);
        }
		menu.setMnemonic(mnemonic);
		return add(menu);
	 }
	 
	 Only further change I have to make is to capitalize Spreadsheet in my translation file.
	 
	 */
	
	
}
