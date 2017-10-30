import java.util.Scanner;
/**
 * Created by josephchiou on 6/27/17.
 */
public class ExpressionTree {
    private static String expression;
    private static Scanner sc = new Scanner(System.in);
    private static boolean calculationError; //use for identifying whether there's any error during calc

    public static void main(String[] args){
        String startMsg = "****White spaces between characters are automatically removed before the expression being processed."
                            + "\n****An expression contains characters other than digit and + - * / ( ) will be treated as an invalid expression."
                            + "\n****An invalid expression will not be processed";
        System.out.println(startMsg);
        while(!getExpression()){

        }
        sc.close();

        StringBuilder sbExpression = new StringBuilder(expression);
        //some testing inputs
        //StringBuilder sbExpression = new StringBuilder("1/0");
        //StringBuilder sbExpression = new StringBuilder("9-3+2+1"); // this test for right associativity
        //StringBuilder sbExpression = new StringBuilder("(4*((5+2)*9))+(7*(2+9))");
        //StringBuilder sbExpression = new StringBuilder("(2+(((((((((9+2))))+3))))))");

        try{
            Node root = processExpression(sbExpression);
            System.out.println("\n===============================Output starts below:=======================================");
            System.out.println("Original expression: "+ expression.toString());
            System.out.println("Expression has been process successfully.\nPrint out the original expression by Inorder traversal: ");
            printInorder(root);
            System.out.println("\nPrint preorder: ");
            printPreorder(root);
            int output = calculateTree(root);
            System.out.print("\nExpression output (in integer): ");
            String outputString = (calculationError==true)?"\tArithmetic error":"\t"+output;
            System.out.println(outputString);
        }catch (BnfException e){
            System.out.println("\n==================================================================");
            System.out.println("Error occurs when parsing the expression by given BNF.");
            System.out.println(e);
            System.out.println(e.errorMsg);
        }

    }

    private static boolean getExpression() {
        System.out.print("Please enter the expression: ");
        if (sc.hasNextLine()) {
            expression = sc.nextLine();
        }
        expression = expression.replaceAll("\\s", ""); // remove all white space
        //expression = expression.replaceAll("(\\d{1})([(])", "$1*$2"); // auto add * between digit and open parenthesis
        //expression = expression.replaceAll("([)])(\\d{1})", "$1*$2"); // auto add * between closing parenthesis and following digit
        //expression = expression.replaceAll("(\\d{1})(\\d{1})","$1*$2"); //auto add * between two digit since digit is 0-9. 10 -->1*0
        String temp = expression.replaceAll("[-+*/()0-9]","");
        if(!temp.equals("")) {
            System.out.println("invalid expression!");
            return false;
        }
        return true;
    }

    public static void printInorder(Node root){
        if(root != null) {
            printInorder(root.left);
            System.out.print(" "+root.data);
            printInorder(root.right);
        }
    }
    public static void printPreorder(Node root){
        if(root != null) {
            System.out.print(" "+root.data);
            printPreorder(root.left);
            printPreorder(root.right);
        }
    }

    public static Node processExpression(StringBuilder substring)throws BnfException{
        Node root = new Node();
        System.out.println("\nBNF <expression> evaluating: " + substring.toString());
        int tokenIndex = getTokenIndex(substring, '+', '-');
        if(tokenIndex>=0){ // <term> + or - <expression>
            root.data = substring.charAt(tokenIndex);
            StringBuilder leftSubstring = new StringBuilder(substring.substring(0,tokenIndex));
            StringBuilder rightSubstring = new StringBuilder(substring.substring(tokenIndex+1,substring.length()));
            System.out.print("\t==>\t<term> " + substring.charAt(tokenIndex) + " <expression>, ");
            System.out.println(" <term> :" + leftSubstring.toString() + "\t<expression>: "+ rightSubstring.toString());
            root.right = processExpression(rightSubstring);
            root.left = processTerm(leftSubstring);
            return root;
        }
        System.out.println("\t==>\t<term>: " + substring.toString());
        return processTerm(substring); //<term>
    }

    public static Node processTerm(StringBuilder substring)throws BnfException{
        Node node = new Node();
        System.out.println("\nBNF <term> evaluating: " + substring.toString());
        int tokenIndex = getTokenIndex(substring, '*', '/');
        if(tokenIndex>=0){ // <factor> * or / <term>
            node.data = substring.charAt(tokenIndex);
            StringBuilder leftSubstring = new StringBuilder(substring.substring(0,tokenIndex));
            StringBuilder rightSubstring = new StringBuilder(substring.substring(tokenIndex+1,substring.length()));
            System.out.print("\t==>\t<factor> " + substring.charAt(tokenIndex) + " <term>, ");
            System.out.println(" <factor> :" + leftSubstring.toString() + "\t<term>: "+ rightSubstring.toString());
            node.right = processTerm(rightSubstring);
            node.left = processFactor(leftSubstring);
            return node;
        }
        System.out.println("\t==>\t<factor>: " + substring.toString());
        return processFactor(substring); //<factor>
    }
    //if parenthesis is needed in the final output then:
        //1. take off parenthesis from the original expression
        //2. redirect it to <expression>
        //3. insertParenthesis under the current node and return the node
    //if parenthesis is not need then take off parenthesis and redirect it back to <expression>
    public static Node processFactor(StringBuilder substring)throws BnfException{
        Node node = new Node();
        System.out.println("\nBNF <factor> evaluating: " + substring.toString());
        if(isParenExpression(substring)){  //(<expression>)
            char openParen = '(';
            char closingParen = ')';
            //remove parenthesis
            StringBuilder newSubstring = new StringBuilder(substring.substring(1,substring.length()-1));
            System.out.println("\t==>\t(<expression>): " + newSubstring.toString());
            //node = processExpression(newSubstring);
            //make the node carries its parenthesis that came in with, so the tree contains the same info as the original expression
            //insertParenthesis(node, openParen);
            //insertParenthesis(node, closingParen);
            return processExpression(newSubstring);
        }
        System.out.println("\t==>\t<literal>: " + substring.toString());
        return processLiteral(substring); //<literal>
    }

    public static Node processLiteral(StringBuilder substring)throws BnfException{
        Node node = new Node();
        System.out.println("\nBNF <literal> evaluating: " + substring);
        if (substring.length()==1 && Character.getNumericValue(substring.charAt(0)) >=0){
            System.out.println("\tSuccess: " + substring.charAt(0));
            node.data = substring.charAt(0);
        }else{ // get triggered when there are more than 1 digit
            System.out.println("\tCan not continue processing");
            String msg = "Fail. The remaining expression: " + substring + " can not be processed by <literal>";
            throw new BnfException(msg);
        }
        return node;
    }

    //this method insert partial parenthesis to deepest left/right leaf.
    public static void insertParenthesis(Node node, char parenthesis){
        switch (parenthesis){
            case '(':
                if(node.left == null){
                    Node parenthesisNode = new Node();
                    parenthesisNode.data = '(';
                    node.left = parenthesisNode;
                }else{
                    //continue looking for leaf
                    insertParenthesis(node.left,'(');
                }
                break;
            case ')':
                if(node.right == null){
                    Node parenthesisNode = new Node();
                    parenthesisNode.data = ')';
                    node.right = parenthesisNode;
                }else{
                    insertParenthesis(node.right,')');
                }
                break;
        }
    }

    public static int calculateTree(Node node){
        int nodeSum = 0;
        if(node!=null){
            switch (node.data){ //process operator first
                case '+':
                    nodeSum = calculateTree(node.left) + calculateTree(node.right);
                    break;
                case '-':
                    nodeSum = calculateTree(node.left) - calculateTree(node.right);
                    break;
                case '*':
                    nodeSum = calculateTree(node.left) * calculateTree(node.right);
                    break;
                case '/':
                    //double NaN = 0.0000000001;
                    int left = calculateTree(node.left);
                    int right = calculateTree(node.right);
                    //double / 0.0 wont throws arithmetic error. Use how small the denominator is to determine whether it's 0
                    if(right <= 0 ){
                        System.out.println("\n\tArithmetic error: divide by 0");
                        System.out.println("\tWhen processing " + left + " / " + right);
                        //System.out.println("\tReturning -1");
                        calculationError = true;
                    }else{
                        nodeSum = left / right;
                    }
                    break;
            }
            //if a node is a digit with no child node --> return the digit
            if(node.left==null && node.right ==null && isNumeric(node.data)) {
                nodeSum = (int)Character.getNumericValue(node.data);
            }
            if(node.data=='(' || node.data==')'){ // if the node is a partial parenthesis
                //do nothing
            }
            if(isNumeric(node.data) && node.left!=null && node.left.data=='('){
                //if a node is a digit that has open parenthesis child node on the left
                return (int)Character.getNumericValue(node.data);
            }
            if(isNumeric(node.data) && node.right!=null && node.right.data==')'){
                //if a node is a digit that has closing parenthesis child node on the right
                return (int)Character.getNumericValue(node.data);
            }
        }
        return nodeSum;
    }

    //check if the data in current node is a digit  //for calculating tree.
    public static boolean isNumeric(char c){
        try{
            Integer.parseInt(Character.toString(c));
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    //get the index of + - or * /
    public static int getTokenIndex(StringBuilder expression, char token1, char token2){
        int openParenCount = 0;
        int closingParenCount = 0;
        for(int index = 0; index < expression.length(); index++){
            if(expression.charAt(index)=='('){
                openParenCount++;
            }
            if(expression.charAt(index)==')'){
                closingParenCount++;
            }
            if(expression.charAt(index)==token1 || expression.charAt(index) == token2){
                if(openParenCount == closingParenCount){
                    return index;
                }
            }
        }
        return -1; //for not found
    }

    //determine if a sub-expression is ( <expression> )
    public static boolean isParenExpression(StringBuilder expression){
        if(expression.charAt(0)=='(' && expression.charAt(expression.length()-1) == ')'){
            return true;
        }
        return false;
    }
}

class Node{
    Node left;
    Node right;
    char data;
}

class BnfException extends Exception{
    String errorMsg;
    public BnfException(String msg){
        this.errorMsg = msg;
    }

}