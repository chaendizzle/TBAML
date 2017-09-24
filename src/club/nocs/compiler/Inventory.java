package club.nocs.compiler;

import java.util.HashMap;

public class Inventory
{
	HashMap<String, Integer> inv = new HashMap<String, Integer>();
	
	public void remove(String item)
	{
		inv.put(item, Math.max(0, (inv.containsKey(item) ? inv.get(item) : 0) - 1));
	}
	
	public void add(String item)
	{
		inv.put(item, (inv.containsKey(item) ? inv.get(item) : 0) + 1);
	}
	
	public boolean has(String item)
	{
		return inv.containsKey(item) && inv.get(item) > 0;
	}
}
