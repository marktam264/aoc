import heapq

from collections import Counter

from text_file_reader_writer import read_file

"""
Read the file and store columns in 2 separate minheaps.
Then iterate through the heaps calculating the distances
and summing them up.

"""

def total_distance() -> int:
    location_data = read_file("1.txt")

    left = []
    right = []
    for row in location_data:
        splits = row.split()
        left.append(splits[0])
        right.append(splits[1])

    heapq.heapify(left)
    heapq.heapify(right)

    total_distance = 0
    for i in range(len(left)):
        total_distance += abs(int(heapq.heappop(left)) - int(heapq.heappop(right)))

    return total_distance

"""
Create a freq map for all numbers in right list.
Then iterate through left list, multiplying each number by its
freq in the right list and summing the resulting product for
each number.

"""
def similarity() -> int:
    location_data = read_file("1.txt")

    left = []
    right = []
    for row in location_data:
        splits = row.split()
        left.append(int(splits[0]))
        right.append(int(splits[1]))

    right_freqz = Counter(right)

    similarity = 0
    for n in left:
        similarity += n * right_freqz[n]

    return similarity

print(f"total_distance = {total_distance()}")
print(f"similarity = {similarity()}")