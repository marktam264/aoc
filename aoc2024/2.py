from text_file_reader_writer import read_file


def is_safe(levels: list[int]) -> bool:
    is_increasing = -1
    for i in range(len(levels)):
        if is_increasing > 0:
            if levels[i] > levels[i - 1] and abs(levels[i] - levels[i - 1]) >= 1 and abs(levels[i] - levels[i - 1]) <= 3:
                continue
            else:
                return False
        elif is_increasing == 0:
            if levels[i] < levels[i - 1] and abs(levels[i] - levels[i - 1]) >= 1 and abs(levels[i] - levels[i - 1]) <= 3:
                continue
            else:
                return False
        else:
            if i == 0:
                continue
            else:
                if levels[i] > levels[i - 1] and abs(levels[i] - levels[i - 1]) >= 1 and abs(levels[i] - levels[i - 1]) <= 3:
                    is_increasing = 1
                elif levels[i] < levels[i - 1] and abs(levels[i] - levels[i - 1]) >= 1 and abs(levels[i] - levels[i - 1]) <= 3: 
                    is_increasing = 0
                else:
                    return False
    return True

"""
Iterate over each row.
Each row has to be all increasing or decreasing.
Consecutive numbers (levels) in each row must differ
by at least 1 and not more than 3.

"""
def count_safe() -> int:
    report_data = read_file("2.txt")

    safe = 0
    for row in report_data:
        levels_chars = row.split()
        levels = [int(c) for c in levels_chars]

        if is_safe(levels):
            safe += 1

    return safe

def count_safe_with_problem_dampener() -> int:
    report_data = read_file("2.txt")

    safe = 0
    for row in report_data:
        levels_chars = row.split()
        levels = [int(c) for c in levels_chars]

        if is_safe(levels):
            safe += 1
            continue

        for l in range(len(levels)):
            levels_dampened = levels.copy()
            del levels_dampened[l]

            if is_safe(levels_dampened):
                safe += 1
                break

    return safe

print(f"count_safe = {count_safe()}")
print(f"count_safe_with_problem_dampener = {count_safe_with_problem_dampener()}")
