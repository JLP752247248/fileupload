package javase;

import java.util.Stack;
import java.util.regex.Pattern;

public class CalculateTest {
	public static void main(String []args){
		
		/*System.out.println(Pattern.matches(Calculator.numRex, "1"));
		System.out.println(Pattern.matches(Calculator.numRex, "2"));
		System.out.println(Pattern.matches(Calculator.numRex, "."));*/
		System.out.println(2.0*2.0-2);
		System.out.println(Calculator.Calculate("2*2.0-2"));
	}
}
class Calculator{
	static final String numRex="[0-9]|\\.";
	
	public static Double Calculate(String expression){
		//expression=expression+"=";
		Stack<Double> numStack=new Stack<Double>();
		Stack<Character> optStack=new Stack<Character>();
		Stack<Double> _numStack=new Stack<Double>();
		Stack<Character> _optStack=new Stack<Character>();
		char[] expArray=expression.toCharArray();
		int index=0;
		while(index<expArray.length){
			Character item=expArray[index];
			if(Pattern.matches(Calculator.numRex, item.toString()))
			{
				if(index==expArray.length-1){
					numStack.push(Double.parseDouble(item.toString()));
				}
				else{
					String appendNum=item.toString();
					while(index<expArray.length-1){
						if(Pattern.matches(Calculator.numRex,expArray[index+1]+""))
						{
							appendNum+=expArray[index+1]+"";
							index++;
						}
						else{
							break;
						}
					}
					numStack.push(Double.parseDouble(appendNum));
				}
				index++;
				continue;
			}else{
				switch(item){
				case '+':
				case '-':
				case '*':
				case '/':{
					optStack.push(item);
					index++;
					break;
				}
				case '(':
					String childExp="";
					Stack<Character> expStack=new Stack<Character>();
					while(index<expArray.length)
					{
						Character childItem=expArray[index];
						index++;
						if(childItem=='('){
							expStack.push(childItem);
						}
						else if(childItem==')'){
							expStack.pop();
							if(expStack.isEmpty())
							{
								break;
							}
						}else{
							childExp+=childItem;
						}
					}
					if(!childExp.isEmpty()){
						numStack.push(Calculate(childExp));
					}
					break;
				default:{
					index++;
					continue;
				}
				}
			}
			
		}
		//9*3-8+7
		//[9,3,8,7]
		//[* - +]
		while(!numStack.isEmpty()){
			Double num=numStack.peek();
			if(!optStack.isEmpty())
			{Character opt=optStack.pop();
			if('+'==opt||'-'==opt){
				numStack.pop();
				_numStack.push(num);
				_optStack.push(opt);
			}
			else if('*'==opt){
				numStack.pop();
				num=num*numStack.pop();
				//_numStack.push(num);
				numStack.push(num);
			}
			else if('/'==opt){
				num=numStack.pop()/num;
				_numStack.push(num);
			}}
			else{
				_numStack.push(num);
				numStack.pop();
			}
			
			continue;
		}
		Double result=_numStack.pop();
		while(!_numStack.isEmpty())
		{
			Double _num=_numStack.pop();
			if(!_optStack.isEmpty())
			{Character _opt=_optStack.pop();
			
			if('+'==_opt){
				result+=_num;
			}
			else{
				result-=_num;
			}}
			
		}
		return result;
	}
	
}