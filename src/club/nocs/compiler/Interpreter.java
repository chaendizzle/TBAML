package club.nocs.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import club.nocs.compiler.tokens.DeclarationToken;

public class Interpreter
{
	// starting room's identifier
	public String start;
	
	// global vars
	public HashMap<String, String> global = new HashMap<String, String>();
	
	// global actions
	public ArrayList<DeclarationToken> actions = new ArrayList<DeclarationToken>();
	
	// player inventory
	public Inventory playerInv = new Inventory();
	
	// rooms
	public HashMap<String, ParseRoom> rooms = new HashMap<String, ParseRoom>();
	
	// items
	public HashMap<String, ParseItem> items = new HashMap<String, ParseItem>();
	
	// room the player's in
	public ParseRoom currentRoom;
	
	String verb;
	String noun;
	String prep;
	String adv;
	String adj;
	
	// take in user input
	@SuppressWarnings("unchecked")
	public void input(String verb, String noun, String prep, String adv, String adj)
	{
		// set local copies of input
		this.noun = noun;
		this.verb = verb;
		this.prep = prep;
		this.adv = adv;
		this.adj = adj;
		// add words to stack
		Stack<String> words = new Stack<String>();
		if (!adj.equals(""))
		{
			words.push(adj);
		}
		if (!adv.equals(""))
		{
			words.push(adv);
		}
		if (!prep.equals(""))
		{
			words.push(prep);
		}
		if (!noun.equals(""))
		{
			words.push(noun);
		}
		if (!verb.equals(""))
		{
			words.push(verb);
		}
		
		// find layers
		ArrayList<DeclarationToken> layers = new ArrayList<DeclarationToken>();
		findActionSet(currentRoom.actions, (Stack<String>) words.clone(), layers);
		// execute last layer
		if (layers.isEmpty())
		{
			findActionSet(actions, words, layers);
		}
		if (!layers.isEmpty())
		{
			layers.get(layers.size() - 1).evaluate(this);
		}
	}
	
	private ArrayList<DeclarationToken> findActionSet(ArrayList<DeclarationToken> current, Stack<String> words,
			ArrayList<DeclarationToken> declarations)
	{
		// stop if words is empty
		if (words.isEmpty())
		{
			return declarations;
		}
		// try each pattern to check for the word
		String word = words.pop();
		for (int i = 0; i < current.size(); i++)
		{
			String pattern = current.get(i).identifier;
			if (word.matches(pattern))
			{
				declarations.add(current.get(i));
				return findActionSet(current.get(i).declarations, words, declarations);
			}
		}
		return null;
	}
	
	public ParseRoom getCurrentRoom()
	{
		return currentRoom;
	}
	
	public void runCustomEvent(String eventKey)
	{
		switch (eventKey)
		{
			case "take":
				currentRoom.desc = currentRoom.desc.replaceAll(". There is a lighter on the ground", "");
				break;
			case "lit":
				currentRoom.desc = currentRoom.desc.replaceAll("unlit", "lit");
				break;
		}
	}
	
	public void movePlayer(String move)
	{
		move = move.replaceAll("noun", noun);
		// check if it's movable.
		if (currentRoom.exits.containsKey(move))
		{
			// move
			currentRoom = rooms.get(currentRoom.exits.get(move));
			System.out.println("You are in " + currentRoom.desc + ".");
		}
		else
		{
			System.out.println("I can't go " + move + " from here.");
		}
	}
}
