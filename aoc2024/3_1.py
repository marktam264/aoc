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

# Regular expression pattern to match "mul(X,Y)" where X and Y are 1-3 digit numbers
pattern = r"mul\((\d{1,3}),(\d{1,3})\)"

# Find all matches
matches = re.findall(pattern, code_line)

# Calculate the sum of the products
total_sum = 0
if matches:
    print("Found matches:")
    for x, y in matches:
        x, y = int(x), int(y)  # Convert to integers
        product = x * y
        total_sum += product
        print(f"mul({x},{y}) -> {x} * {y} = {product}")
    print(f"Total sum of products: {total_sum}")
else:
    print("No matches found.")
