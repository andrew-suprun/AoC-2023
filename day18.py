from collections import namedtuple

DELTAS1 = {'R': (0, 1), 'D': (1, 0), 'L': (0, -1), 'U': (-1, 0)}
DELTAS2 = {'0': (0, 1), '1': (1, 0), '2': (0, -1), '3': (-1, 0)}

VLine = namedtuple('VLine', 'x, y1, y2')


def parse_line1(line: str) -> ((int, int), int):
    dir, steps, *_ = line.split()
    return DELTAS1[dir], int(steps)


def parse_line2(line: str) -> ((int, int), int):
    _, _, digits = line.split()
    return DELTAS2[digits[7]], int(digits[2:7], 16)


def run(parse_line) -> int:
    total = 0
    loc = 0, 0
    minx = 0
    v_lines = set[VLine]()
    ys = {0}
    with open('input/day18.data') as f:
        for line in f.readlines():
            delta, steps = parse_line(line)
            total += steps
            y = loc[0]
            ny = y + delta[0] * steps
            x = loc[1]
            nx = x + delta[1] * steps
            if delta[1] == 0:
                v_lines.add(VLine(x=x, y1=min(y, ny), y2=max(y, ny)))
            loc = ny, nx
            minx = min(minx, nx)
            ys.add(ny)

    ys_sorted = [y for y in ys]
    ys_sorted.sort()
    y_regions = [(y, 1) for y in ys_sorted]
    y_regions.extend((y1 + 1, y2 - y1 - 1) for (y1, y2) in zip(ys_sorted, ys_sorted[1:]))
    v_lines_sorted = [v_line for v_line in v_lines]
    v_lines_sorted.sort()

    for (y, m) in y_regions:
        above, below = False, False
        start = minx - 1
        for v_line in v_lines_sorted:
            if v_line[1] <= y <= v_line[2]:
                if y == v_line[1]:
                    below = not below
                elif y == v_line[2]:
                    above = not above
                else:
                    above = not above
                    below = not below

                if above and below:
                    start = v_line[0]
                elif start > minx - 1:
                    diff = v_line[0] - start - 1
                    total += diff * m
                    start = minx - 1

    return total


print(run(parse_line1))
print(run(parse_line2))

# Part 1: 40761
# Part 2: 106920098354636
