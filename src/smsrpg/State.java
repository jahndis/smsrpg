package smsrpg;

import java.util.Stack;


public class State<E extends Enum<E>> {
	private Stack<E> stack;
	
	public State() {
		stack = new Stack<E>();
	}
	
	public E getCurrent() {
		return stack.peek();
	}
	
	public E pop() {
		return stack.pop();
	}
	
	public E push(E newState) {
		return stack.push(newState);
	}
	
	public E change(E newState) {
		stack.pop();
		return stack.push(newState);
	}
	
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	public int size() {
		return stack.size();
	}
	
	public void parseString(String stateText, Class<E> stateType) {	
		stack = new Stack<E>();
		String[] states = stateText.split(">");
		
		for (String state : states) {
			stack.push(Enum.valueOf(stateType, state));
		}
	}
	
	public String toString() {
		Stack<E> newStack = new Stack<E>();
		newStack.addAll(stack);
		
		StringBuilder stateText = new StringBuilder();
		
		while (!newStack.isEmpty()) {
			stateText.insert(0, ">");
			stateText.insert(0, newStack.pop().toString());
		}
		//Remove the last '>'
		stateText.deleteCharAt(stateText.length() - 1);

		return stateText.toString();
	}
	
}
