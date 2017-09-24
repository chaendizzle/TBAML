package club.nocs.compiler.tokens;

import java.util.ArrayList;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.Token;

public class ElseToken extends Token
{
	ArrayList<Token> code = new ArrayList<Token>();
	
	// whether or not we've started the code block (passed the first open brace)
	boolean start = false;
	// whether or not we've seen the last close brace
	boolean end = false;
	
	public ElseToken(String expression, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.ELSE;
		
		// if condition ends with a {
		if (expression.trim().length() > 0)
		{
			if (expression.trim().charAt(expression.trim().length() - 1) == '{')
			{
				// remove the {, we found it
				start = true;
			}
		}
		
		// find if counterpart and link to it
		compiler.ifTokens.get(depth).elseToken = this;
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
		if (code.size() == 0 || !code.get(code.size() - 1).collect())
		{
			// start a new one
			code.add(Token.createToken(line, depth + 1, compiler));
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
			code.get(code.size() - 1).collect(line);
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
	
	// run by if
	public Object evaluateElse(Interpreter interpreter)
	{
		// run the code in the block (run by if statement)
		for (int i = 0; i < code.size(); i++)
		{
			code.get(i).evaluate(interpreter);
		}
		return null;
	}
	
	// do nothing if not run by if
	@Override
	public Object evaluate(Interpreter interpreter)
	{
		return null;
	}
}
