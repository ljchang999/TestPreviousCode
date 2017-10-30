1. type "javac ExpressionTree.java" to compile
2. type "java ExpressionTree" to run the program

This program asks the user to give it an expression. Once it receives an expression it uses regular expression
to perform a basic syntax check the following:
	1. check and remove extra spaces between each character
	2. check if there are characters/symbols other than 0~9, +, -, *, /, (, and ) exists in the expression.
		- if yes, ask user to enter another valid expression
		- if no, take the expression and start processing.
		(This process doesnt have any effect on the BNF, the main program itself still function the same
		without this preliminary syntax checking process. The main program will still be able to
		indentify any out-of-scope syntax)

The program parses the expression given by the user by using the given BNF rules and recursive descent. An
expression is analyzed by <expression> first and then descends down to <term>, <factor> and  <literal> to
identify whether it's a digit between 0 to 9.

The program use the token (+,-,*,and /) index in an expression to split the original expression into 2
sub-expressions while it's processing <expression> and <term>. While the program starts processing <factor> it
checks whether the expression is surrounded by a pair of parenthesis to determine whether it should take away the
parenthesis and return the expression back to <expression> or descend it down to <digit>.

The program shows/prints the process flow when it's processing the expression. It also builds a binary tree
using the parsing result.

For the output:
	- if everything is parsed successfully then the program prints out the tree that is being built during
	  the process by inorder and preorder traversal. The inorder traversal printout should be same as the
	  original expression.
	  The program may also prints out a math calculation of the expression depending on whether there's any
	  "divide by 0" situation or not in the expression.
	- if there's something unrecognizable in the expression then the program will throw a BnfException and
	  prints out the part of expression that cause the problem. (ex. 55 is unrecognizable since <literal> can
	  only process 0~9. If the program runs without the preliminary syntax check, those things that should
	  get caught by syntax check will be identifid and printet as the output. 
