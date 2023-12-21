NORTH = 1
EAST = 2
SOUTH = 3
WEST = 4

guide = {
    ('.', NORTH): [NORTH],
    ('.', SOUTH): [SOUTH],
    ('.', EAST): [EAST],
    ('.', WEST): [WEST],

    ('-', NORTH): [EAST, WEST],
    ('-', SOUTH): [EAST, WEST],
    ('-', EAST): [EAST],
    ('-', WEST): [WEST],

    ('|', NORTH): [NORTH],
    ('|', SOUTH): [SOUTH],
    ('|', EAST): [NORTH, SOUTH],
    ('|', WEST): [NORTH, SOUTH],

    ('/', NORTH): [EAST],
    ('/', SOUTH): [WEST],
    ('/', EAST): [NORTH],
    ('/', WEST): [SOUTH],

    ('\\', NORTH): [WEST],
    ('\\', SOUTH): [EAST],
    ('\\', EAST): [SOUTH],
    ('\\', WEST): [NORTH],
}

deltas = {
    NORTH: (-1, 0),
    EAST: (0, 1),
    SOUTH: (1, 0),
    WEST: (0, -1),
}


def calc_energized(grid: list[str], start: (int, int, int)) -> int:
    max_x = len(grid[0]) - 1
    max_y = len(grid) - 1

    energized = set[(int, int, int)]()
    paths = [start]
    while len(paths) > 0:
        place = paths.pop()
        if place in energized:
            continue
        energized.add(place)
        mirror = grid[place[0]][place[1]]
        for direction in guide[(mirror, place[2])]:
            delta = deltas[direction]
            new_place = (place[0] + delta[0], place[1] + delta[1], direction)
            if 0 <= new_place[0] <= max_y and 0 <= new_place[1] <= max_x:
                paths.append(new_place)

    locations = set[(int, int)]()
    for loc in energized:
        locations.add((loc[0], loc[1]))
    return len(locations)


def run():
    with open('input/day16.data') as f:
        grid = [line.strip() for line in f.readlines()]

    print(f'Part 1: {calc_energized(grid, (0, 0, EAST))}')

    max_energized = 0
    for y in range(len(grid)):
        max_energized = max(max_energized, calc_energized(grid, (y, 0, EAST)))
        max_energized = max(max_energized, calc_energized(grid, (y, len(grid[0]) - 1, WEST)))
    for x in range(len(grid[0])):
        max_energized = max(max_energized, calc_energized(grid, (0, x, SOUTH)))
        max_energized = max(max_energized, calc_energized(grid, (len(grid) - 1, x, NORTH)))

    print(f'Part 2: {max_energized}')


run()

# Part 1: 7199
# Part 2: 7438
