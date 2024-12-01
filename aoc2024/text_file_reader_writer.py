# text_file_reader_writer.py

def read_file(file_path) -> list[str]:
    """
    Reads the contents of a text file.
    
    :param file_path: Path to the text file.
    :return: Contents of the file as a string.
    """
    try:
        """
        with open(filename, 'r', encoding='UTF-8') as file:
            while line := file.readline():
                print(line.rstrip())
        """
        with open(file_path) as file:
            lines = [line.rstrip() for line in file]
            return lines
    except FileNotFoundError:
        print(f"Error: The file at '{file_path}' does not exist.")
        return None
    except Exception as e:
        print(f"An error occurred while reading the file: {e}")
        return None


def write_file(file_path, content):
    """
    Writes content to a text file, overwriting any existing content.
    
    :param file_path: Path to the text file.
    :param content: Content to write to the file.
    """
    try:
        with open(file_path, 'w', encoding='utf-8') as file:
            file.write(content)
        print(f"Content successfully written to '{file_path}'.")
    except Exception as e:
        print(f"An error occurred while writing to the file: {e}")


def append_to_file(file_path, content):
    """
    Appends content to a text file.
    
    :param file_path: Path to the text file.
    :param content: Content to append to the file.
    """
    try:
        with open(file_path, 'a', encoding='utf-8') as file:
            file.write(content)
        print(f"Content successfully appended to '{file_path}'.")
    except Exception as e:
        print(f"An error occurred while appending to the file: {e}")


# Example usage
if __name__ == "__main__":
    file_path = "example.txt"
    
    # Write content to the file
    write_file(file_path, "Hello, World!\n")
    
    # Append content to the file
    append_to_file(file_path, "This is additional content.\n")
    append_to_file(file_path, "A little more content.\n")
    
    # Read and display the file content
    content = read_file(file_path)
    if content is not None:
        print("\nFile contents:")
        print(content)
