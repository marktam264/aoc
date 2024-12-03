import re

# Specify the file name
file_name = "3.txt"

# Read the code line from the file
try:
    with open(file_name, 'r') as file:
        code_line = file.read().strip()
except FileNotFoundError:
    print(f"Error: File '{file_name}' not found.")
    exit()

# Regular expression patterns
mul_pattern = r"mul\((\d{1,3}),(\d{1,3})\)"
directive_pattern = r"\b(do|don't)\(\)"

# Find all "do()" and "don't()" directives and their positions
directives = [(match.group(), match.start()) for match in re.finditer(directive_pattern, code_line)]

# Find all "mul(X,Y)" and their positions
mul_matches = [(match.groups(), match.start()) for match in re.finditer(mul_pattern, code_line)]

# Determine the sum of products
total_sum = 0
add_next = True  # Default behavior is to add

if mul_matches:
    print("Processing matches:")
    for (x, y), mul_position in mul_matches:
        # Check for the most recent directive before this "mul"
        for directive, directive_position in directives:
            if directive_position < mul_position:
                add_next = directive == "do()"
            else:
                break  # No need to check further once past the "mul"

        # Convert X, Y to integers and calculate the product
        x, y = int(x), int(y)
        product = x * y

        # Conditionally add to the total sum
        if add_next:
            total_sum += product
            print(f"Adding mul({x},{y}) -> {x} * {y} = {product}")
        else:
            print(f"Skipping mul({x},{y}) -> {x} * {y} = {product}")

    print(f"Total sum of products: {total_sum}")
else:
    print("No matches found.")
