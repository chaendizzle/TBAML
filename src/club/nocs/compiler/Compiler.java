package club.nocs.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import club.nocs.compiler.tokens.DeclarationToken;
import club.nocs.compiler.tokens.ExitToken;
import club.nocs.compiler.tokens.IfToken;
import club.nocs.compiler.tokens.ParseTree;

public class Compiler
{
	// if layers
	// used to match else to if
	public HashMap<Integer, IfToken> ifTokens = new HashMap<Integer, IfToken>();
	
	// the room to start in
	public String start;
	
	// the DeclarationToken that holds everything
	public ParseTree parseTree;
	
	// interpreter
	public Interpreter interpreter;
	
	/**
	 * Compiles the text file to an Interpreter object that runs the text based
	 * adventure.
	 * 
	 * @param file
	 *            The file to compile
	 */
	public Interpreter compile(File file)
	{
		// initialize
		if (!file.exists())
		{
			throw new TBAMLCompileError("Compilation error: File '" + file.getName() + "' does not exist.");
		}
		if (!file.canRead())
		{
			throw new TBAMLCompileError("Compilation error: File '" + file.getName() + "' can not be read.");
		}
		interpreter = new Interpreter();
		parseTree = new ParseTree(this);
		
		// compile the code
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				line = line.trim();
				if (!line.equals(""))
				{
					parseTree.collect(line);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new TBAMLCompileError("Compilation error: Failed to read file: " + file.getName());
		}
		interpreter.start = start;
		
		// take compiled code and dump to interpreter
		for (int i = 0; i < parseTree.declarations.size(); i++)
		{
			// only verb, room, and item declarations at top level
			DeclarationToken declaration = parseTree.declarations.get(i);
			switch (declaration.type)
			{
				case VERB:
					interpreter.actions.add(declaration);
					break;
				case ROOM:
					ParseRoom room = new ParseRoom(declaration.identifier);
					interpreter.rooms.put(declaration.identifier, room);
					interpreter.currentRoom = room;
					for (int j = 0; j < declaration.declarations.size(); j++)
					{
						// name, desc, exits, actions
						DeclarationToken roomdata = declaration.declarations.get(j);
						switch (roomdata.type)
						{
							case NAME:
								room.name = roomdata.identifier;
								break;
							case DESC:
								room.desc = roomdata.identifier;
								break;
							case EXIT:
								room.exits = ((ExitToken) roomdata).exits;
								break;
							case VERB:
								room.actions.add(roomdata);
								break;
							default:
								break;
						}
					}
					// evaluate room code
					declaration.evaluate(interpreter);
					break;
				case ITEM:
					ParseItem item = new ParseItem(declaration.identifier);
					for (int j = 0; j < declaration.declarations.size(); j++)
					{
						// name, desc
						DeclarationToken itemdata = declaration.declarations.get(j);
						switch (itemdata.type)
						{
							case NAME:
								item.name = itemdata.identifier;
								break;
							case DESC:
								item.desc = itemdata.identifier;
								break;
							default:
								break;
						}
					}
					// evaluate item code
					declaration.evaluate(interpreter);
					break;
				default:
					break;
			}
		}
		// set starting room
		interpreter.currentRoom = interpreter.rooms.get(interpreter.start);
		// eval global code
		parseTree.evaluate(interpreter);
		
		// done!
		return interpreter;
	}
}
