package club.nocs.compiler.tokens;

import java.util.ArrayList;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.Token;

public class DeclarationToken extends Token
{
	public ArrayList<DeclarationToken> declarations = new ArrayList<DeclarationToken>();
	public ArrayList<Token> code = new ArrayList<Token>();
	
	public String identifier;
	
	public Token current;
	
	// whether or not we've started the code block (passed the first open brace)
	boolean start = false;
	// whether or not we've seen the last close brace
	boolean end = false;
	
	public DeclarationToken(String identifier, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.NONE;
		statement = StatementType.DECL;
		
		// if condition ends with a {
		this.identifier = identifier;
		if (identifier.length() > 0)
		{
			if (identifier.trim().charAt(identifier.trim().length() - 1) == '{')
			{
				// remove the {, we found it
				this.identifier = identifier.trim().substring(identifier.trim().length() - 1);
				start = true;
			}
		}
	}
	
	// non-pattern declaration token, such as room or item
	public DeclarationToken(TokenType type, String identifier, int depth, Compiler compiler)
	{
		this(identifier, depth, compiler);
		this.type = type;
	}
	
	// pattern based declaration token, such as noun or verb
	// for example, a verb's identifier would be a regex pattern like
	// "move|go|run|walk"
	public DeclarationToken(TokenType type, String[] identifier, int depth, Compiler compiler)
	{
		// start with no identifier
		this("", depth, compiler);
		this.type = type;
		
		// if condition ends with a {
		String end = identifier[identifier.length - 1].trim();
		if (end.length() > 0)
		{
			if (end.charAt(end.length() - 1) == '{')
			{
				// remove the {, we found it
				identifier[identifier.length - 1] = end.substring(end.length() - 1);
				start = true;
			}
		}
		
		// construct the regex identifier
		String id = "";
		for (int i = 0; i < identifier.length; i++)
		{
			identifier[i] = identifier[i].trim();
			if (id != "")
			{
				id += "|";
			}
			id += identifier[i];
		}
		this.identifier = id;
	}
	
	@Override
	public boolean collect()
	{
		return !(start && end);
	}
	
	@Override
	public boolean collect(String line)
	{
		// if we just started, or the last one is finished
		if (current == null || !current.collect())
		{
			// start a new one
			current = Token.createToken(line, depth + 1, compiler);
			// if it's another declaration
			if (current.statement == StatementType.DECL)
			{
				declarations.add((DeclarationToken) current);
			}
			// if it's a code block
			else
			{
				code.add(current);
			}
			// check for start
			if (code.get(code.size() - 1).type == TokenType.OP_BRACKET)
			{
				start = true;
			}
			// check for end
			if (code.get(code.size() - 1).type == TokenType.CL_BRACKET)
			{
				end = true;
			}
		}
		// otherwise
		else
		{
			// continue collecting into the old one
			current.collect(line);
		}
		return collect();
	}
	
	@Override
	public void parse()
	{
		// parse code
		for (int i = 0; i < code.size(); i++)
		{
			code.get(i).parse();
		}
	}
	
	@Override
	public Object evaluate(Interpreter interpreter)
	{
		// run the code in the block
		for (int i = 0; i < code.size(); i++)
		{
			code.get(i).evaluate(interpreter);
		}
		return null;
	}
}
