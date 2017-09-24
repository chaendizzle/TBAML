package club.nocs.compiler.tokens;

import java.util.HashMap;

import club.nocs.compiler.Compiler;

public class ExitToken extends DeclarationToken
{
	public HashMap<String, String> exits = new HashMap<String, String>();
	
	public ExitToken(String expression, int depth, Compiler compiler)
	{
		super("exit", depth, compiler);
		type = TokenType.EXIT;
		statement = StatementType.DECL;
		
		// if condition ends with a {
		if (expression.trim().length() > 0)
		{
			if (expression.trim().charAt(expression.trim().length() - 1) == '{')
			{
				// remove the {, we found it
				start = true;
			}
		}
	}
	
	@Override
	public boolean collect(String line)
	{
		String trimmed = line.trim();
		String[] split = trimmed.split(" ");
		// check for start
		if (trimmed.charAt(0) == '{')
		{
			start = true;
		}
		// check for end
		if (trimmed.charAt(trimmed.length() - 1) == '}')
		{
			end = true;
			return false;
		}
		// only valid exit if 2 items, exit name and destination identifier
		if (split.length == 2)
		{
			exits.put(split[0], split[1]);
		}
		return collect();
	}
}
