package club.nocs.compiler.tokens;

import java.util.Map;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.Token;

public class PrintToken extends Token
{
	String s;
	
	public PrintToken(String s, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.PRINT;
		this.s = s;
		// no lexing needed
	}
	
	@Override
	public boolean collect()
	{
		// print is single-line
		return false;
	}
	
	@Override
	public boolean collect(String line)
	{
		return false;
	}
	
	@Override
	public void parse()
	{
		// no parsing needed
	}
	
	@Override
	public Object evaluate(Interpreter interpreter)
	{
		String p = s.replaceAll("desc", interpreter.currentRoom.desc);
		String exits = "There is a ";
		int i = 0;
		for (Map.Entry<String, String> entry : interpreter.currentRoom.exits.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			if (i != 0)
			{
				exits += ", ";
			}
			if (i > 2 && i >= interpreter.currentRoom.exits.size() - 1)
			{
				exits += "and a ";
			}
			exits += interpreter.rooms.get(value).name + " to the " + key;
		}
		p = p.replaceAll("exits", exits);
		System.out.println(p);
		return null;
	}
}
