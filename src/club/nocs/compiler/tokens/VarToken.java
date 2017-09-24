package club.nocs.compiler.tokens;

import java.util.HashMap;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.TBAMLRuntimeError;
import club.nocs.compiler.Token;

public class VarToken extends Token
{
	String var;
	
	public VarToken(String var, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.VAR;
		statement = StatementType.VALUE;
		this.var = var;
		// no lexing needed
	}
	
	@Override
	public boolean collect()
	{
		// var is single-line
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
		if (interpreter.rooms.containsKey(interpreter.getCurrentRoom().identifier))
		{
			HashMap<String, String> vars = interpreter.rooms.get(interpreter.getCurrentRoom().identifier).variables;
			if (vars.containsKey(var))
			{
				return vars.get(var);
			}
		}
		throw new TBAMLRuntimeError("Local variable '" + var + "' was not initialized.");
	}
	
	// assigns a local variable to the room
	public void assign(Interpreter interpreter, String value)
	{
		// if room doesn't exist
		if (!interpreter.rooms.containsKey(interpreter.getCurrentRoom().identifier))
		{
			throw new TBAMLRuntimeError("The current room '" + interpreter.getCurrentRoom().identifier
					+ "' doesn't exist... Something went terribly wrong.");
		}
		// if all good, set the variable
		interpreter.rooms.get(interpreter.getCurrentRoom().identifier).variables.put(var, value);
	}
}
