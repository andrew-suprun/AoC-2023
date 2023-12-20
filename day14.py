SPACE = 0
ROUND = 1
CUBE = 2


def encode(c: str) -> int:
    return SPACE if c == '.' else ROUND if c == 'O' else CUBE


def parse_input(lines: list[str]) -> list[list[int]]:
    return [[encode(c) for c in line.strip()] for line in lines]


def rotate(platform: list[list[int]]) -> list[list[int]]:
    max_x = len(platform[0])
    max_y = len(platform)
    return [[platform[max_x - x - 1][y] for x in range(max_x)] for y in range(max_y)]


def tilt(platform: list[list[int]]) -> list[list[int]]:
    tilted = clone(platform)
    for row in tilted:
        place = 0
        for (i, rock) in enumerate(row):
            if rock == ROUND:
                row[i] = SPACE
                row[place] = ROUND
                place += 1
            elif rock == CUBE:
                place = i + 1
    return tilted


def cycle(platform: list[list[int]]) -> list[list[int]]:
    for i in range(4):
        platform = tilt(platform)
        platform = rotate(platform)

    return platform


def load(platform: list[list[int]]) -> int:
    result = 0
    for row in platform:
        for (i, rock) in enumerate(row):
            if rock == ROUND:
                result += len(row) - i
    return result


def clone(platform: list[list[int]]) -> list[list[int]]:
    return [[platform[y][x] for x in range(len(platform[0]))] for y in range(len(platform))]


def platform_hash(platform: list[list[int]]) -> int:
    result = 0
    for row in platform:
        for rock in row:
            result = 3 * result + hash(rock)
    return result


def prepare_platform() -> list[list[int]]:
    with open('input/day14.data') as f:
        platform = parse_input(f.readlines())
    for _ in range(3):
        platform = rotate(platform)
    return platform


def part1():
    print(f'Part 1: {load(tilt(prepare_platform()))}')


def part2():
    platform = prepare_platform()
    idx = 0
    hashes = dict[int, int]()
    pl_hash = 0
    cycled = platform
    while idx < 1000:
        cycled = cycle(platform)
        idx += 1
        pl_hash = platform_hash(cycled)
        if pl_hash in hashes:
            break
        hashes[pl_hash] = idx
        platform = cycled

    diff = idx - hashes[pl_hash]

    for _ in range(2 * diff + 1000000000 % diff - idx):
        cycled = cycle(cycled)

    print(f'Part 2: {load(cycled)}')
    return load(cycled)


part1()
part2()

# Part 1: 110779
# Part 2: 86069