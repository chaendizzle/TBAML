package club.nocs.compiler;

import java.util.ArrayList;
import java.util.HashMap;

import club.nocs.compiler.tokens.DeclarationToken;

/**
 * Represents a parsed room in-game. NOT a token.
 */
public class ParseRoom
{
	// room identifier
	public String identifier;
	
	// name and desc
	public String name;
	public String desc;
	
	// exits, mapping exit string to room identifier
	public HashMap<String, String> exits = new HashMap<String, String>();
	
	// local variables
	public HashMap<String, String> variables = new HashMap<String, String>();
	
	// global actions
	public ArrayList<DeclarationToken> actions = new ArrayList<DeclarationToken>();
	
	// room inventory
	public Inventory inventory = new Inventory();
	
	public ParseRoom(String identifier)
	{
		this.identifier = identifier;
	}
}