package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.TBAMLCompileError;
import club.nocs.compiler.Token;

public class NotToken extends Token
{
	String raw;
	Token token;
	
	public NotToken(String expression, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.NOT;
		this.raw = expression;
		// lex
		token = Token.createToken(raw, depth, compiler);
		if (token.statement != StatementType.BOOLEAN)
		{
			throw new TBAMLCompileError(
					"Cannot apply the 'not' operator to '" + raw + "', since it is a '" + token.statement.name()
							+ "' expression, not a boolean expression.");
		}
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
		// free string
		raw = null;
	}
	
	@Override
	public Object evaluate(Interpreter interpreter)
	{
		return !(boolean) token.evaluate(interpreter);
	}
	
}
