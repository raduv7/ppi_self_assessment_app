import sys
import json
import random

MAX_CONFIDENCE = 100
MIN_CONFIDENCE = 0
FEELINGS = ["happy", "sad", "angry", "fear", "disgust", "surprise", "neutral"]
TIME_LENGTH = 10


# def copy_file(source, destination):
#     try:
#         shutil.copy(source, destination)
#         print(f"File copied successfully from {source} to {destination}")
#     except IOError as e:
#         print(f"Unable to copy file. {e}")
#     except:
#         print("Unexpected error:", sys.exc_info())


def generate_confidence():
    return int(random.triangular(MIN_CONFIDENCE, MAX_CONFIDENCE, MIN_CONFIDENCE))


def gen_rand_result(feelings, max_time):
    result = []
    for time in range(max_time):
        feelings_confidence = [{"feeling": feeling, "confidence": generate_confidence()} for feeling in feelings]
        result.append({
            "time": time * 17,
            "feelings_confidence": feelings_confidence
        })
    return result


def parse_to_json(obj):
    return json.dumps(obj, indent=4)


def write_to_file(file_path, string):
    with open(file_path, "w") as f:
        f.write(string)


def log_args_exception():
    print("Usage: python <path/to/dir/>main.py <nr_of_inputs> <inputs_paths>.. <output_path>")
    exit(1)


if __name__ == "__main__":
    if len(sys.argv) < 3:
        log_args_exception()

    inputsCount = int(sys.argv[1])

    if len(sys.argv) != 3 + inputsCount:
        log_args_exception()

    destination_path = sys.argv[2 + inputsCount]
    data = gen_rand_result(FEELINGS, TIME_LENGTH)
    result_json = parse_to_json(data)
    write_to_file(destination_path, result_json)
