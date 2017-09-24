package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.Inventory;
import club.nocs.compiler.TBAMLCompileError;
import club.nocs.compiler.Token;

public class InvHasToken extends Token
{
	String rawA;
	String rawB;
	
	Token A;
	Token B;
	
	public InvHasToken(String rawA, String rawB, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.INV_HAS;
		statement = StatementType.BOOLEAN;
		this.rawA = rawA.trim();
		this.rawB = rawB.trim();
		
		// lex left and right
		A = Token.createToken(this.rawA, depth, compiler);
		// convert item identifier to string
		B = Token.createToken("\"" + this.rawB + "\"", depth, compiler);
		// left is inventory, right is item name
		if (A.type != TokenType.INV)
		{
			throw new TBAMLCompileError(
					"left statement of 'has' expression '" + rawA + "' is a '" + A.type.name()
							+ "' expression, not an inventory expression.");
		}
		if (B.type != TokenType.STRING)
		{
			throw new TBAMLCompileError(
					"right statement of 'has' expression '" + rawB + "' is a '" + B.type.name()
							+ "' expression, not a string expression.");
		}
	}
	
	@Override
	public boolean collect()
	{
		// and is single-line
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
		// parse both
		A.parse();
		B.parse();
		// free strings
		rawA = null;
		rawB = null;
	}
	
	@Override
	public Object evaluate(Interpreter interpreter)
	{
		// check inv
		return ((Inventory) A.evaluate(interpreter)).has((String) B.evaluate(interpreter));
	}
}
