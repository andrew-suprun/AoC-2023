from collections import deque

EAST_WEST = 0
NORTH_SOUTH = 1


def step(city_map, cursors, cursor, step_range, first_turn):
    max_y = len(city_map)
    max_x = len(city_map[0])
    y, x, dir = cursor
    block = city_map[y][x]
    new_dir = 1 - dir
    if dir == EAST_WEST:
        dy, dx = 0, 1
    else:
        dy, dx = 1, 0
    new_loss = block[dir]
    for i in step_range:
        new_y, new_x = y + dy * i, x + dx * i
        if new_y < 0 or new_y >= max_y or new_x < 0 or new_x >= max_x:
            break
        new_block = city_map[new_y][new_x]
        new_loss += new_block[2]
        if new_block[new_dir] == 0 or new_block[new_dir] > new_loss:
            if abs(i) >= first_turn:
                new_block[new_dir] = new_loss

                cursors.append((new_y, new_x, new_dir))


def run(range1, range2, first_turn):
    with open('input/day17.data') as f:
        city_map = [[[0, 0, int(loss)] for loss in line.strip()] for line in f.readlines()]

    cursors = deque()
    cursors.append((0, 0, EAST_WEST))
    cursors.append((0, 0, NORTH_SOUTH))
    while cursors:
        cursor = cursors.popleft()
        step(city_map, cursors, cursor, range1, first_turn)
        step(city_map, cursors, cursor, range2, first_turn)
    exit_block = city_map[-1][-1]
    print(min(exit_block[0], exit_block[1]))


run(range(1, 4), range(-1, -4, -1), 1)
run(range(1, 11), range(-1, -11, -1), 4)

# Part 1: 1001
# Part 2: 1197
