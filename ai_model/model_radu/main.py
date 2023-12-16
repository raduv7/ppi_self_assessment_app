import shutil
import sys


def copy_file(source, destination):
    try:
        shutil.copy(source, destination)
        print(f"File copied successfully from {source} to {destination}")
    except IOError as e:
        print(f"Unable to copy file. {e}")
    except:
        print("Unexpected error:", sys.exc_info())


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python script.py <source_file_path> <destination_file_path>")
    else:
        source_path = sys.argv[1]
        destination_path = sys.argv[2]
        copy_file(source_path, destination_path)
