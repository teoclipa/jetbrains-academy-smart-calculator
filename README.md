# Smart Calculator (Java)

This project is an implementation of a command-line-based calculator application developed in Java. The calculator is designed to support standard arithmetic operations, including addition (`+`), subtraction (`-`), multiplication (`*`), and integer division (`/`). It also supports the use of parentheses for grouping expressions. 

One of the distinguishing features of this calculator is its ability to work with variables. Users can assign numerical values to variables, which can then be used in later calculations. Variables can also be assigned the value of other variables. The calculator checks for and handles invalid assignments, and notifies users of unknown variables.

This calculator uses the [Shunting Yard algorithm](https://en.wikipedia.org/wiki/Shunting-yard_algorithm) to convert infix mathematical expressions into postfix expressions. The postfix expression is then evaluated using a stack-based algorithm.

The calculator also handles multiple consecutive operators (`++`, `--`, `-+`, `+-`) by replacing them with the appropriate operator. This process is repeated until no more consecutive operators are present.

If an error occurs during calculation or assignment (for example, due to unbalanced parentheses, division by zero, or invalid expressions), the calculator will notify the user of the error.

The project also includes a `/help` command which outputs information about the available operations and how to use variables in expressions.

## Running the application

1. Compile the `Calculator.java` file.
2. Run the `Calculator` class. The calculator will accept input from the command line until the `/exit` command is entered.

## Commands

- `/exit` - Exit the calculator.
- `/help` - Print help information.
- `[variable] = [value]` - Assign a value to a variable. The value can be a number or another variable.
- Mathematical expressions - Evaluate the expression and print the result. 

Examples:
- `a = 5`
- `b = a`
- `a + b * 2`

Please refer to the source code for more detailed implementation information.

Feel free to fork and contribute to this project. All suggestions and improvements are welcome!
