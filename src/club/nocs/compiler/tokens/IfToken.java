package club.nocs.compiler.tokens;

import java.util.ArrayList;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.TBAMLCompileError;
import club.nocs.compiler.Token;

public class IfToken extends Token
{
	String rawCondition;
	
	Token condition;
	ArrayList<Token> code = new ArrayList<Token>();
	
	// whether or not we've started the code block (passed the first open brace)
	boolean start = false;
	// whether or not we've seen the last close brace
	boolean end = false;
	
	// else will set this if applicable
	public ElseToken elseToken;
	
	public IfToken(String expression, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.IF;
		compiler.ifTokens.put(depth, this);
		this.rawCondition = expression.trim();
		
		// if condition ends with a {
		if (rawCondition.charAt(rawCondition.length() - 1) == '{')
		{
			// remove the {, we found it
			rawCondition = rawCondition.substring(rawCondition.length() - 1);
			start = true;
		}
		
		// lex condition
		condition = Token.createToken(rawCondition, depth, compiler);
		// condition must be boolean
		if (condition.statement != StatementType.BOOLEAN)
		{
			throw new TBAMLCompileError(
					"Conditional statement of 'if' expression '" + rawCondition + "' is a '"
							+ condition.statement.name() + "' expression, not a boolean expression.");
		}
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
		// parse condition
		condition.parse();
		// parse code
		for (int i = 0; i < code.size(); i++)
		{
			code.get(i).parse();
		}
		// free string
		rawCondition = null;
	}
	
	@Override
	public Object evaluate(Interpreter interpreter)
	{
		// if condition is true
		if ((boolean) condition.evaluate(interpreter))
		{
			// run the code in the block
			for (int i = 0; i < code.size(); i++)
			{
				code.get(i).evaluate(interpreter);
			}
		}
		else if (elseToken != null)
		{
			elseToken.evaluateElse(interpreter);
		}
		return null;
	}
}
